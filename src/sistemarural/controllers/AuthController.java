package sistemarural.controllers;

import sistemarural.exceptions.CredencialesInvalidasException;
import sistemarural.exceptions.ErrorPersistenciaException;
import sistemarural.models.PersonalSalud;
import sistemarural.services.AuthService;

/**
 * AuthController
 * ---------------
 * Controla el estado de sesión de la aplicación. Ninguna otra pantalla
 * o servicio debería ejecutarse mientras sesionActual sea null: es el
 * "portón" de acceso al resto del programa.
 */
public class AuthController {

    private final AuthService authService;
    private PersonalSalud sesionActual;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Intenta iniciar sesión. Devuelve un mensaje listo para mostrar y,
     * si tiene éxito, deja registrada la sesión activa.
     */
    public String iniciarSesion(String usuario, String passwordPlano) {
        try {
            PersonalSalud personal = authService.login(usuario, passwordPlano);
            this.sesionActual = personal;
            return "Acceso concedido. Bienvenido/a " + personal.getNombreCompleto()
                    + " (" + personal.getRol() + ").";
        } catch (CredencialesInvalidasException e) {
            this.sesionActual = null;
            return "Acceso denegado: " + e.getMessage();
        } catch (ErrorPersistenciaException e) {
            this.sesionActual = null;
            return "No se pudo validar el acceso: " + e.getMessage();
        }
    }

    public boolean haySesionActiva() {
        return sesionActual != null;
    }

    public PersonalSalud getSesionActual() {
        return sesionActual;
    }

    public void cerrarSesion() {
        this.sesionActual = null;
    }
}
