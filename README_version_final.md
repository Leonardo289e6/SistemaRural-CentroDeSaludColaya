# SistemaRural-PE

Sistema de gestión de historias clínicas para el **Centro de Salud Rural Santa Rosa**. Permite al personal de salud iniciar sesión, registrar pacientes, registrar atenciones médicas y consultar la historia clínica de cada paciente, con los datos almacenados en una base de datos PostgreSQL en la nube (Supabase).

Aplicación de escritorio en **Java** con interfaz gráfica **Swing**, arquitectura por capas y atención a la protección de datos personales (Ley N.° 29733).

---

## Tabla de contenidos

1. [Características](#características)
2. [Tecnologías](#tecnologías)
3. [Interfaz gráfica](#interfaz-gráfica)
4. [Arquitectura](#arquitectura)
5. [Estructura del proyecto](#estructura-del-proyecto)
6. [Modelo de datos](#modelo-de-datos)
7. [Seguridad y protección de datos](#seguridad-y-protección-de-datos)
8. [Validaciones y manejo de errores](#validaciones-y-manejo-de-errores)
9. [Instalación y ejecución](#instalación-y-ejecución)
10. [Datos de prueba](#datos-de-prueba)
11. [Guía de uso](#guía-de-uso)
12. [Conceptos de POO aplicados](#conceptos-de-poo-aplicados)
13. [Limitaciones conocidas y mejoras futuras](#limitaciones-conocidas-y-mejoras-futuras)

---

## Características

- **Login obligatorio** contra la tabla `personal_medico`, con máximo de 3 intentos.
- **Registro de pacientes** con validación de datos y **rechazo de DNI duplicados** (nunca se sobrescribe un paciente existente).
- **Búsqueda de pacientes por DNI**, mostrando el DNI enmascarado.
- **Registro de atenciones médicas**, guardando automáticamente **quién atendió** (el personal con sesión activa).
- **Historia clínica por paciente**, ordenada de la más reciente a la más antigua, con el nombre y el rol del profesional que atendió.
- Contraseñas almacenadas como **hash SHA-256**, nunca en texto plano.
- **Credenciales de la base de datos fuera del código**, leídas de variables de entorno.

---

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 17 o superior (usa `record`, `switch` con flechas y `String.isBlank`) |
| Interfaz gráfica | Swing (`javax.swing`), incluido en el JDK |
| Base de datos | PostgreSQL alojado en Supabase |
| Acceso a datos | JDBC con `PreparedStatement` |
| Driver | PostgreSQL JDBC (`org.postgresql.Driver`) |

---

## Interfaz gráfica

La GUI reemplaza el menú de consola original y conserva exactamente las mismas funciones. Está compuesta por **dos ventanas** en el paquete `sistemarural.views`, que no contienen lógica de negocio: solo capturan datos, llaman a los controladores y muestran el texto que estos devuelven.

### Flujo de la aplicación

```
 Main
  │  1. Conecta a la BD (falla rápido y avisa con un cuadro de diálogo)
  │  2. Arma repositorios → servicios → controladores
  ▼
 LoginView ──(login válido)──▶ DashboardView ──(Salir / cerrar ventana)──▶ cierra BD y termina
     │
     └──(3 intentos fallidos)──▶ cierra BD y termina
```

`Main` conecta las ventanas con **callbacks** (`Runnable`): `LoginView` no conoce a `DashboardView`; solo ejecuta `alIngresar` o `alAgotarIntentos`. Así cada ventana queda desacoplada y es fácil de probar o reemplazar.

### Ventana 1: `LoginView` (ingreso del personal)

```
┌────────────────────────────────────┐
│          SistemaRural-PE           │
│   Centro de Salud Rural Santa Rosa │
│                                    │
│   Usuario:     [________________]  │
│   Contraseña:  [****************]  │
│                                    │
│   Acceso denegado... (intento 1/3) │
│            [  Ingresar  ]          │
└────────────────────────────────────┘
```

- La contraseña se captura con `JPasswordField`, por lo que no se muestra en pantalla.
- **Enter** equivale a pulsar "Ingresar".
- En cada intento fallido se limpia la contraseña y se muestra el mensaje del controlador junto con el contador `intento X/3`.
- Al llegar al tercer intento fallido se muestra un aviso, se cierra la conexión a la base de datos y termina el programa.
- Si el login es válido, la ventana se cierra y se abre el panel principal.

### Ventana 2: `DashboardView` (panel principal)

Solo se abre con una sesión activa. La cabecera muestra la información de la sesión (`PersonalSalud.getInformacion()`) y el botón **Salir**. El cuerpo es un `JTabbedPane` con **una pestaña por cada opción del menú de consola**:

| Pestaña | Equivale a | Campos | Qué hace |
|---|---|---|---|
| **Registrar paciente** | Opción 1 | DNI, nombres, apellidos, fecha de nacimiento (`yyyy-MM-dd`) | Valida y guarda; avisa si el DNI ya existe |
| **Buscar paciente** | Opción 2 | DNI | Muestra los datos del paciente con el DNI enmascarado |
| **Registrar atención** | Opción 3 | DNI del paciente, fecha (`yyyy-MM-dd`), diagnóstico, tratamiento | Guarda la atención asociada al personal con sesión activa |
| **Historia clínica** | Opción 4 | DNI | Lista las atenciones, con "Atendido por: Nombre (Rol)" |

Comportamientos de usabilidad:

- Los resultados de las operaciones de registro se muestran en un **cuadro de diálogo**; las consultas muestran su resultado en un **área de texto de solo lectura** con scroll.
- En **Registrar paciente**, el formulario solo se limpia si el registro fue exitoso; ante un error (por ejemplo, DNI duplicado) conserva lo escrito para poder corregirlo.
- En las pestañas de consulta, **Enter** ejecuta la búsqueda.
- **Salir** y la **X de la ventana** piden confirmación (`¿Deseas cerrar la sesión y salir?`). Al aceptar se cierra la conexión a la BD y termina la aplicación.
- Si la historia clínica está vacía, se muestra "No hay atenciones registradas para este paciente."

### Diseño interno de la vista

Para evitar código repetido, `DashboardView` usa dos constructores de interfaz reutilizables:

- `formulario(boton, etiqueta1, campo1, ...)`: arma un formulario con `GridBagLayout` a partir de pares etiqueta/campo. Lo usan *Registrar paciente* y *Registrar atención*.
- `consulta(etiqueta, campo, boton, areaSalida)`: arma el panel de búsqueda con el resultado debajo. Lo usan *Buscar paciente* e *Historia clínica*.

Al arrancar, `Main` aplica el *Look and Feel* nativo del sistema operativo.

---

## Arquitectura

Arquitectura **por capas**. Cada capa solo conoce a la inmediatamente inferior, y las capas superiores dependen de **interfaces** (no de implementaciones concretas).

```
 views            LoginView, DashboardView            Interfaz gráfica (Swing)
   │
 controllers      AuthController, HistorialController Traducen excepciones a mensajes para la UI
   │
 services         AuthService, HistorialService       Reglas de negocio y validaciones
   │
 repositories     I*Repository  →  *RepositoryJdbc    Acceso a datos (JDBC)
   │
 config           CloudDbConnection, DatabaseConfig   Conexión a PostgreSQL / Supabase
```

Transversales: `models` (entidades), `exceptions` (errores de dominio) y `security` (hash, validación y enmascarado).

| Capa | Responsabilidad |
|---|---|
| **views** | Capturar datos y mostrar mensajes. Sin reglas de negocio. |
| **controllers** | Llamar al servicio y convertir excepciones en texto listo para mostrar. Mantienen la sesión activa (`AuthController`). |
| **services** | Validar datos, verificar reglas (paciente existente, DNI duplicado) y coordinar repositorios. |
| **repositories** | Ejecutar SQL con `PreparedStatement` y traducir `SQLException` a excepciones propias. |
| **config** | Singleton de conexión y lectura de configuración desde variables de entorno. |

---

## Estructura del proyecto

```
src/sistemarural/
├── Main.java                         Punto de entrada: conexión, dependencias y lanzamiento de la GUI
├── config/
│   ├── CloudDbConnection.java        Conexión única a la BD (Singleton)
│   └── DatabaseConfig.java           Lectura de DB_* desde variables de entorno
├── controllers/
│   ├── AuthController.java           Login y sesión activa
│   └── HistorialController.java      Pacientes, atenciones e historia clínica
├── exceptions/
│   ├── CredencialesInvalidasException.java
│   ├── DatoInvalidoException.java
│   ├── ErrorPersistenciaException.java
│   ├── PacienteDuplicadoException.java
│   └── PersonaNoEncontradaException.java
├── models/
│   ├── Persona.java                  Clase abstracta base
│   ├── Paciente.java                 Extiende Persona
│   ├── PersonalSalud.java            Extiende Persona (con id, usuario y rol)
│   └── Atencion.java                 Fila de historias_clinicas
├── repositories/
│   ├── IPacienteRepository.java / PacienteRepositoryJdbc.java
│   ├── IHistoriaClinicaRepository.java / HistoriaClinicaRepositoryJdbc.java
│   ├── IPersonalMedicoRepository.java / PersonalMedicoRepositoryJdbc.java
│   └── RegistroPersonalMedico.java   DTO (record) con el hash de la contraseña
├── security/
│   ├── GestorSeguridad.java          Hash SHA-256
│   ├── ValidadorDatos.java           Validaciones de DNI, fechas y textos
│   └── DataMasker.java               Enmascarado de DNI y teléfono
├── services/
│   ├── AuthService.java
│   └── HistorialService.java
└── views/
    ├── LoginView.java                Ventana de ingreso
    └── DashboardView.java            Panel principal con 4 pestañas
```

---

## Modelo de datos

Tres tablas en el esquema `public` de Supabase:

```
 personal_medico                 historias_clinicas                  pacientes
 ───────────────                 ──────────────────                  ─────────
 id  (PK, int4)  ◄──────────┐    id  (PK, int4)                      dni  (PK, varchar)
 usuario (UNIQUE, varchar)  └──  atendido_por (FK, int4)             nombres (varchar)
 password_hash (varchar)         paciente_dni (FK, varchar) ──────►  apellidos (varchar)
 nombres (varchar)               fecha_atencion (timestamp)          fecha_nacimiento (date)
 rol (varchar)                   diagnostico (text)
                                 tratamiento (text)
```

- Un paciente puede tener **muchas** atenciones; cada atención pertenece a **un** paciente y puede registrar al profesional que la atendió.
- `pacientes.dni` es la clave primaria, lo que garantiza a nivel de base de datos que no existan DNI repetidos.
- `personal_medico.rol` es descriptivo (por ejemplo: *Médico General*, *Obstetra*, *Licenciada en Enfermería*).

> El diagrama es de referencia. Si tus restricciones reales difieren (por ejemplo, el comportamiento de las claves foráneas al borrar), ajústalo a tu esquema.

---

## Seguridad y protección de datos

- **Contraseñas con hash.** `GestorSeguridad.generarHash` calcula SHA-256 en hexadecimal. En el login se hashea lo que escribe el usuario y se compara con `password_hash`; la contraseña nunca viaja ni se guarda en texto plano.
- **Mensajes de error genéricos.** Usuario inexistente y contraseña incorrecta devuelven el mismo mensaje, para no revelar qué usuarios existen.
- **Enmascarado de datos sensibles.** `DataMasker` oculta el DNI al mostrarlo (`47581234` → `******34`).
- **Prevención de inyección SQL.** Todas las consultas usan `PreparedStatement` con parámetros.
- **Credenciales fuera del código.** `DatabaseConfig` lee `DB_HOST`, `DB_USER` y `DB_PASSWORD` de variables de entorno y falla con un mensaje claro si falta alguna. El archivo `.env` está excluido de Git.
- **Aislamiento del hash.** El hash viaja solo entre el repositorio y `AuthService` mediante el DTO `RegistroPersonalMedico`; nunca llega a la interfaz.
- **Trazabilidad clínica.** Cada atención guarda el id del profesional que la registró (`atendido_por`).

---

## Validaciones y manejo de errores

Reglas de validación (`ValidadorDatos`):

| Dato | Regla |
|---|---|
| DNI | Exactamente 8 dígitos |
| Fechas | Formato `yyyy-MM-dd` |
| Nombres, apellidos, diagnóstico, tratamiento | No pueden estar vacíos |
| Atención | El paciente debe existir y debe haber un profesional asociado |
| Paciente nuevo | El DNI no puede estar registrado |

Excepciones de dominio, convertidas en mensajes por los controladores:

| Excepción | Cuándo ocurre | Mensaje en la GUI |
|---|---|---|
| `CredencialesInvalidasException` | Usuario o contraseña incorrectos | "Acceso denegado: …" |
| `DatoInvalidoException` | Dato con formato incorrecto o vacío | "Error de validación: …" |
| `PacienteDuplicadoException` | DNI ya registrado | "Aviso: Ya existe un paciente registrado con DNI: …" |
| `PersonaNoEncontradaException` | DNI sin paciente | "Aviso: …" / "No se puede registrar la atención: …" |
| `ErrorPersistenciaException` | Falla de la base de datos | "Error de base de datos: …" |

---

## Instalación y ejecución

### Requisitos

- **JDK 17 o superior**
- Driver **PostgreSQL JDBC** (`postgresql-42.x.x.jar`), agregado al classpath del proyecto
- Una base de datos PostgreSQL / Supabase con las tres tablas descritas arriba

### 1. Configurar las credenciales

Define estas variables de entorno (nunca las escribas en el código):

| Variable | Obligatoria | Valor por defecto |
|---|---|---|
| `DB_HOST` | Sí | — |
| `DB_USER` | Sí | — |
| `DB_PASSWORD` | Sí | — |
| `DB_PORT` | No | `6543` |
| `DB_NAME` | No | `postgres` |
| `DB_SSLMODE` | No | `require` |

Opción recomendada en VS Code: crear un archivo `.env` en la raíz del proyecto:

```
DB_HOST=tu-host.pooler.supabase.com
DB_USER=postgres.tu-proyecto
DB_PASSWORD=tu_contraseña
```

y cargarlo desde `.vscode/launch.json`:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "SistemaRural",
      "request": "launch",
      "mainClass": "sistemarural.Main",
      "envFile": "${workspaceFolder}/.env"
    }
  ]
}
```

Agrega al `.gitignore`:

```
.env
.vscode/launch.json
```

### 2. Ejecutar

- **VS Code:** abrir el proyecto, agregar el JAR del driver en *Referenced Libraries* y ejecutar `Main` con la configuración anterior.
- **Línea de comandos (ejemplo en Linux/macOS):**

```bash
javac -encoding UTF-8 -cp lib/postgresql-42.x.x.jar -d out $(find src -name "*.java")
java -cp "out:lib/postgresql-42.x.x.jar" sistemarural.Main
```

En Windows, separa el classpath con `;` en lugar de `:`.

---

## Datos de prueba

La carpeta `datos_prueba/` contiene archivos CSV listos para importar desde el panel de Supabase:

| Archivo | Contenido |
|---|---|
| `personal_medico.csv` | 4 usuarios: leonardo, andrea, ariana y daniel (contraseña como hash SHA-256) |
| `pacientes.csv` | 20 pacientes ficticios |
| `historias_clinicas.csv` | 20 atenciones, enlazadas a pacientes y a personal médico |

**Orden de importación** (por las claves foráneas): `personal_medico` → `pacientes` → `historias_clinicas`.

Los DNI, nombres y diagnósticos son **ficticios**. Las contraseñas en texto normal están en `credenciales_prueba.md`, que **no debe subirse al repositorio**.

---

## Guía de uso

1. **Ingresar:** escribe tu usuario y contraseña y pulsa *Ingresar* (o Enter). Tienes 3 intentos.
2. **Registrar un paciente:** pestaña *Registrar paciente*; completa DNI de 8 dígitos, nombres, apellidos y fecha de nacimiento (`yyyy-MM-dd`).
3. **Buscar un paciente:** pestaña *Buscar paciente*; escribe el DNI y pulsa *Buscar* (o Enter).
4. **Registrar una atención:** pestaña *Registrar atención*; indica el DNI de un paciente ya registrado, la fecha, el diagnóstico y el tratamiento. La atención queda asociada a tu usuario.
5. **Ver la historia clínica:** pestaña *Historia clínica*; escribe el DNI y pulsa *Ver historia*.
6. **Salir:** botón *Salir* o la X de la ventana, y confirma.

### Lista de verificación manual

- [ ] Login con credenciales válidas abre el panel principal
- [ ] Tres logins fallidos cierran el programa
- [ ] Registrar un DNI repetido muestra el aviso y no modifica el paciente original
- [ ] Registrar un DNI con menos de 8 dígitos muestra un error de validación
- [ ] Una atención registrada aparece en la historia con "Atendido por: …"
- [ ] Una atención a un DNI inexistente se rechaza
- [ ] Salir pide confirmación y cierra la conexión

---

## Conceptos de POO aplicados

- **Singleton:** `CloudDbConnection` mantiene una única conexión, pensada para un establecimiento rural con ancho de banda limitado.
- **Repository + interfaces:** los servicios dependen de `I*Repository`, no de JDBC, lo que facilita sustituir la persistencia.
- **Herencia y polimorfismo:** `Persona` es abstracta y define `obtenerRol()`; `Paciente` y `PersonalSalud` lo implementan de forma distinta.
- **Composición:** una `Atencion` no tiene sentido sin el paciente al que pertenece.
- **DTO inmutable:** `RegistroPersonalMedico` es un `record` de Java.
- **Programación funcional:** `HistorialService` usa *streams* (`filter`, `map`, `collect`) para buscar nombres por apellido y contar pacientes. Estas operaciones existen en el servicio, pero aún no están expuestas en la GUI.
- **Inyección de dependencias manual:** `Main` crea e inyecta repositorios, servicios y controladores.
- **Excepciones propias:** cada tipo de fallo tiene su excepción de dominio.

---

## Limitaciones conocidas y mejoras futuras

- **Hash de contraseñas:** SHA-256 sin sal es rápido de atacar por fuerza bruta. Se recomienda migrar a PBKDF2, BCrypt o Argon2 con sal aleatoria.
- **Hilo de la interfaz:** las consultas a la BD se ejecutan en el hilo de Swing; con una conexión lenta la ventana puede congelarse unos segundos. Mover esas llamadas a `SwingWorker` lo resolvería.
- **Fecha de atención:** se guarda solo la fecha (sin hora), aunque la columna es `timestamp`.
- **Cerrar sesión:** `AuthController.cerrarSesion()` existe, pero la GUI actual cierra la aplicación al salir en lugar de volver al login.
- **Funciones pendientes de exponer en la GUI:** búsqueda por apellido y conteo de pacientes (ya implementadas en `HistorialService`).
- **Control de acceso por rol:** el rol se muestra, pero aún no restringe funciones.
- **Pruebas automatizadas:** agregar pruebas unitarias de servicios con repositorios simulados.

---

## Autor

Proyecto académico desarrollado por Henry Alfaro, Ariana Pardo, Andrea Puma, Daniel Alaya — Centro de Salud Rural Colaya, Perú.
   registrar atención médica, ver historia clínica.
4. Cada acción del menú pasa por Controller → Service → Repository (JDBC) → PostgreSQL real.
