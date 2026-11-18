package carnets.servicio;

import carnets.modelo.RegistroAbordaje;
import carnets.modelo.TipoPersona;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Resumen calculado a partir de la bitacora. */
public class Estadisticas {

    private int total;
    private int aceptados;
    private int rechazados;
    private final int[] aceptadosPorHora = new int[24];
    private final Map<TipoPersona, Integer> aceptadosPorTipo = new EnumMap<>(TipoPersona.class);

    public Estadisticas(List<RegistroAbordaje> bitacora) {
        for (TipoPersona t : TipoPersona.values()) {
            aceptadosPorTipo.put(t, 0);
        }
        for (RegistroAbordaje r : bitacora) {
            total++;
            if (r.isAceptado()) {
                aceptados++;
                aceptadosPorHora[r.getFechaHora().getHour()]++;
                if (r.getTipo() != null) {
                    aceptadosPorTipo.merge(r.getTipo(), 1, Integer::sum);
                }
            } else {
                rechazados++;
            }
        }
    }

    public int getTotal() {
        return total;
    }

    public int getAceptados() {
        return aceptados;
    }

    public int getRechazados() {
        return rechazados;
    }

    public double getPorcentajeRechazo() {
        return total == 0 ? 0 : 100.0 * rechazados / total;
    }

    public int[] getAceptadosPorHora() {
        return aceptadosPorHora.clone();
    }

    public Map<TipoPersona, Integer> getAceptadosPorTipo() {
        return aceptadosPorTipo;
    }

    /** Hora con mas abordajes aceptados, o -1 si todavia no hay ninguno. */
    public int getHoraPico() {
        int pico = -1;
        for (int h = 0; h < 24; h++) {
            if (aceptadosPorHora[h] > 0 && (pico == -1 || aceptadosPorHora[h] > aceptadosPorHora[pico])) {
                pico = h;
            }
        }
        return pico;
    }
}
