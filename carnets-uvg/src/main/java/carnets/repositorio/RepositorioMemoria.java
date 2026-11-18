package carnets.repositorio;

import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Guarda todo en listas. Sirve para probar y para la demostracion sin internet, se pierde al cerrar. */
public class RepositorioMemoria implements Repositorio {

    private final Map<Integer, Persona> personas = new LinkedHashMap<>();
    private final List<RegistroAbordaje> abordajes = new ArrayList<>();

    @Override
    public String nombreAlmacenamiento() {
        return "memoria, los datos se pierden al cerrar";
    }

    @Override
    public boolean insertarPersona(Persona persona) {
        if (personas.containsKey(persona.getCarnet())) {
            return false;
        }
        personas.put(persona.getCarnet(), persona);
        return true;
    }

    @Override
    public Persona buscarPersona(int carnet) {
        return personas.get(carnet);
    }

    @Override
    public List<Persona> listarPersonas() {
        List<Persona> copia = new ArrayList<>(personas.values());
        copia.sort(Comparator.comparingInt(Persona::getCarnet));
        return copia;
    }

    @Override
    public boolean eliminarPersona(int carnet) {
        return personas.remove(carnet) != null;
    }

    @Override
    public boolean actualizarActivo(int carnet, boolean activo) {
        Persona p = personas.get(carnet);
        if (p == null) {
            return false;
        }
        p.setActivo(activo);
        return true;
    }

    @Override
    public void guardarAbordaje(RegistroAbordaje registro) {
        abordajes.add(registro);
    }

    @Override
    public List<RegistroAbordaje> listarAbordajes() {
        List<RegistroAbordaje> copia = new ArrayList<>(abordajes);
        copia.sort(Comparator.comparing(RegistroAbordaje::getFechaHora).reversed());
        return copia;
    }

    @Override
    public RegistroAbordaje ultimoAceptado(int carnet) {
        String clave = String.valueOf(carnet);
        RegistroAbordaje ultimo = null;
        for (RegistroAbordaje r : abordajes) {
            if (r.isAceptado() && r.getCarnetLeido().equals(clave)
                    && (ultimo == null || r.getFechaHora().isAfter(ultimo.getFechaHora()))) {
                ultimo = r;
            }
        }
        return ultimo;
    }

    @Override
    public void close() {
    }
}
