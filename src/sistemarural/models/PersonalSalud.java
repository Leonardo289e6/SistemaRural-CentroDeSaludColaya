package sistemarural.models;

/**
 * PersonalSalud
 * --------------
 * Refleja los datos de la tabla 'personal_medico': usuario, nombres y
 * rol, más el id (necesario para registrar quién atendió). El hash de la
 * contraseña se maneja aparte en AuthService y nunca se guarda en este objeto).
 *
 * Como 'personal_medico' no tiene columnas dni/apellidos/fecha_nacimiento,
 * esos campos heredados de Persona quedan vacíos para este tipo: es una
 * decisión de diseño consciente, no un error.
 */
public class PersonalSalud extends Persona {

    private long id; // id en la tabla personal_medico (se guarda en historias_clinicas.atendido_por)
    private String usuario;
    private String rol; // ej. "Licenciada en Enfermería", "Médico General"

    public PersonalSalud(long id, String usuario, String nombres, String rol) {
        super("", nombres, "", "");
        this.id = id;
        this.usuario = usuario;
        this.rol = rol;
    }

    public long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    /**
     * POLIMORFISMO: a diferencia de Paciente, aquí el "rol" es el cargo
     * real que cumple la persona en el establecimiento.
     */
    @Override
    public String obtenerRol() {
        return rol;
    }

    @Override
    public String getInformacion() {
        return super.getInformacion() + " (usuario: " + usuario + ")";
    }
}