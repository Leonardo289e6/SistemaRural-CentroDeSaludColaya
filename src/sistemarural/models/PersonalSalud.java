package sistemarural.models;

/**
 * PersonalSalud
 * --------------
 * Refleja los datos de la tabla 'personal_medico': usuario, nombres y
 * rol (además del id y el hash de contraseña, que se manejan aparte
 * en AuthService por seguridad y nunca se guardan en este objeto).
 *
 * Como 'personal_medico' no tiene columnas dni/apellidos/fecha_nacimiento,
 * esos campos heredados de Persona quedan vacíos para este tipo: es una
 * decisión de diseño consciente, no un error.
 */
public class PersonalSalud extends Persona {

    private String usuario;
    private String rol; // ej. "Licenciada en Enfermería", "Médico General"

    public PersonalSalud(String usuario, String nombres, String rol) {
        super("", nombres, "", "");
        this.usuario = usuario;
        this.rol = rol;
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
