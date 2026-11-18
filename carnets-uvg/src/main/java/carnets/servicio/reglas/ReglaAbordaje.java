package carnets.servicio.reglas;

import carnets.modelo.Persona;
import carnets.repositorio.Repositorio;

import java.time.LocalDateTime;

/**
 * Una regla que una persona registrada debe cumplir para abordar.
 * Para agregar una regla nueva basta con crear otra clase que implemente esta interfaz.
 */
public interface ReglaAbordaje {

    /** Devuelve null si la persona cumple la regla, o el motivo del rechazo si no la cumple. */
    String revisar(Persona persona, Repositorio repositorio, LocalDateTime ahora);
}
