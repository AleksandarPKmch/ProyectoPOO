package carnets.repositorio;

import carnets.modelo.EstudianteData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Guarda en una lista. Sirve para probar rapido, se pierde al cerrar. */
public class RepositorioMemoria implements EstudianteRepository {

    private static final int PRIMER_CARNET = 1001;
    private final List<EstudianteData> lista = new ArrayList<>();

    @Override
    public String nombreAlmacenamiento() {
        return "memoria, los datos se pierden al cerrar";
    }

    @Override
    public boolean insertar(EstudianteData estudiante) {
        if (buscarPorCarnet(estudiante.getCarnet()) != null) {
            return false;
        }
        return lista.add(estudiante);
    }

    @Override
    public EstudianteData buscarPorCarnet(int carnet) {
        for (EstudianteData e : lista) {
            if (e.getCarnet() == carnet) {
                return e;
            }
        }
        return null;
    }

    @Override
    public List<EstudianteData> listar() {
        List<EstudianteData> copia = new ArrayList<>(lista);
        copia.sort(Comparator.comparingInt(EstudianteData::getCarnet));
        return copia;
    }

    @Override
    public boolean eliminarPorCarnet(int carnet) {
        return lista.removeIf(e -> e.getCarnet() == carnet);
    }

    @Override
    public int siguienteCarnet() {
        int max = PRIMER_CARNET - 1;
        for (EstudianteData e : lista) {
            max = Math.max(max, e.getCarnet());
        }
        return max + 1;
    }

    @Override
    public void close() {
    }
}
