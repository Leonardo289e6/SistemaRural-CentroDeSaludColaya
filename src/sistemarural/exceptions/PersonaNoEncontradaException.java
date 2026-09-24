package sistemarural.exceptions;

/**
 * PersonaNoEncontradaException
 * ------------------------------
 * Excepción de negocio (checked exception) que se lanza cuando se busca
 * una persona (Paciente o PersonalSalud) por DNI y esta no existe en el
 * repositorio. Es genérica porque, gracias a la jerarquía Persona, el
 * mismo tipo de error aplica tanto a pacientes como a personal de salud.
 */
public class PersonaNoEncontradaException extends Exception {

    public PersonaNoEncontradaException(String dni) {
        super("No se encontró ninguna persona registrada con DNI: " + dni);
    }

    public PersonaNoEncontradaException(String dni, String tipoPersona) {
        super("No se encontró ningún/a " + tipoPersona + " registrado/a con DNI: " + dni);
    }
}
