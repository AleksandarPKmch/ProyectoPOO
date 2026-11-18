package carnets.servicio.reglas;

import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;
import carnets.repositorio.Repositorio;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * El mismo carnet no puede abordar dos veces en pocos minutos.
 * Si pasa, lo mas probable es que alguien este usando un carnet prestado.
 */
public class SinDobleAbordaje implements ReglaAbordaje {

    private final int minutos;

    public SinDobleAbordaje(int minutos) {
        this.minutos = minutos;
    }

    @Override
    public String revisar(Persona persona, Repositorio repositorio, LocalDateTime ahora) {
        RegistroAbordaje ultimo = repositorio.ultimoAceptado(persona.getCarnet());
        if (ultimo == null || ultimo.getFechaHora().isAfter(ahora)) {
            return null;
        }
        long pasaron = Duration.between(ultimo.getFechaHora(), ahora).toMinutes();
        if (pasaron < minutos) {
            return "Este carnet ya abordo hace " + pasaron + " min, posible carnet prestado";
        }
        return null;
    }
}
