package carnets.servicio;

import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;
import carnets.repositorio.Repositorio;
import carnets.servicio.reglas.CarnetActivo;
import carnets.servicio.reglas.ReglaAbordaje;
import carnets.servicio.reglas.SinDobleAbordaje;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** El corazon del sistema: decide si alguien sube al bus y deja constancia de cada intento. */
public class AbordajeService {

    /** Acepta 26949 y tambien lo que mande un lector de QR, por ejemplo UVG-26949. */
    private static final Pattern LECTURA = Pattern.compile("(?i)^(?:UVG[\\s:-]*)?(\\d{1,9})$");

    private final Repositorio repositorio;
    private final List<ReglaAbordaje> reglas;
    private final Clock reloj;

    public AbordajeService(Repositorio repositorio) {
        this(repositorio, List.of(new CarnetActivo(), new SinDobleAbordaje(5)), Clock.systemDefaultZone());
    }

    /** Constructor completo, util para pruebas con otras reglas u otra hora. */
    public AbordajeService(Repositorio repositorio, List<ReglaAbordaje> reglas, Clock reloj) {
        this.repositorio = repositorio;
        this.reglas = reglas;
        this.reloj = reloj;
    }

    public ResultadoValidacion validar(String lectura) {
        LocalDateTime ahora = LocalDateTime.now(reloj);
        String texto = lectura == null ? "" : lectura.trim();
        if (texto.length() > 40) {
            texto = texto.substring(0, 40);
        }

        Matcher m = LECTURA.matcher(texto);
        if (!m.matches()) {
            return rechazar(ahora, texto, null, "Lectura no valida, el carnet debe ser un numero");
        }
        int carnet = Integer.parseInt(m.group(1));
        Persona persona = repositorio.buscarPersona(carnet);
        if (persona == null) {
            return rechazar(ahora, String.valueOf(carnet), null, "Carnet no registrado en UVG");
        }
        for (ReglaAbordaje regla : reglas) {
            String motivo = regla.revisar(persona, repositorio, ahora);
            if (motivo != null) {
                return rechazar(ahora, String.valueOf(carnet), persona, motivo);
            }
        }
        RegistroAbordaje ok = new RegistroAbordaje(ahora, String.valueOf(carnet), persona.getTipo(), true, "Acceso permitido");
        repositorio.guardarAbordaje(ok);
        return new ResultadoValidacion(ok, persona);
    }

    private ResultadoValidacion rechazar(LocalDateTime ahora, String leido, Persona persona, String motivo) {
        RegistroAbordaje r = new RegistroAbordaje(ahora, leido, persona == null ? null : persona.getTipo(), false, motivo);
        repositorio.guardarAbordaje(r);
        return new ResultadoValidacion(r, persona);
    }

    public List<RegistroAbordaje> bitacora() {
        return repositorio.listarAbordajes();
    }

    public Estadisticas estadisticas() {
        return new Estadisticas(repositorio.listarAbordajes());
    }

    public int aceptadosHoy() {
        LocalDate hoy = LocalDate.now(reloj);
        int n = 0;
        for (RegistroAbordaje r : repositorio.listarAbordajes()) {
            if (r.isAceptado() && r.getFechaHora().toLocalDate().equals(hoy)) {
                n++;
            }
        }
        return n;
    }
}
