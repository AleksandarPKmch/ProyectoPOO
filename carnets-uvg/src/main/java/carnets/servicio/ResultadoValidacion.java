package carnets.servicio;

import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;

/** Lo que la pantalla necesita mostrar despues de validar un carnet. */
public class ResultadoValidacion {

    private final RegistroAbordaje registro;
    private final Persona persona;

    /** La persona es null cuando el carnet no esta registrado. */
    public ResultadoValidacion(RegistroAbordaje registro, Persona persona) {
        this.registro = registro;
        this.persona = persona;
    }

    public boolean isAceptado() {
        return registro.isAceptado();
    }

    public RegistroAbordaje getRegistro() {
        return registro;
    }

    public Persona getPersona() {
        return persona;
    }
}
