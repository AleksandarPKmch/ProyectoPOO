package carnets.modelo;

import java.time.LocalDateTime;

/**
 * Una linea de la bitacora: cada intento de abordar, aceptado o rechazado.
 * Por privacidad guarda solo el carnet, el tipo de persona y el resultado, no el nombre.
 */
public class RegistroAbordaje {

    private final LocalDateTime fechaHora;
    private final String carnetLeido;
    private final TipoPersona tipo;
    private final boolean aceptado;
    private final String motivo;

    /** El tipo es null cuando el carnet no pertenece a nadie registrado. */
    public RegistroAbordaje(LocalDateTime fechaHora, String carnetLeido, TipoPersona tipo,
                            boolean aceptado, String motivo) {
        this.fechaHora = fechaHora;
        this.carnetLeido = carnetLeido;
        this.tipo = tipo;
        this.aceptado = aceptado;
        this.motivo = motivo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getCarnetLeido() {
        return carnetLeido;
    }

    public TipoPersona getTipo() {
        return tipo;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public String getMotivo() {
        return motivo;
    }
}
