package carnets.servicio.reglas;

import carnets.modelo.Persona;
import carnets.repositorio.Repositorio;

import java.time.LocalDateTime;

/** Un carnet suspendido, vencido o reportado como perdido no puede abordar. */
public class CarnetActivo implements ReglaAbordaje {

    @Override
    public String revisar(Persona persona, Repositorio repositorio, LocalDateTime ahora) {
        return persona.isActivo() ? null : "Carnet suspendido, debe pasar a Registro";
    }
}
