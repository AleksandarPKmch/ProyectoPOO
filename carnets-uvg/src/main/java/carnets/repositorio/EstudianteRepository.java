package carnets.repositorio;

import carnets.modelo.EstudianteData;

import java.util.List;

/** Contrato unico de guardado. Memoria y MongoDB lo cumplen igual. */
public interface EstudianteRepository extends AutoCloseable {

    String nombreAlmacenamiento();

    boolean insertar(EstudianteData estudiante);

    EstudianteData buscarPorCarnet(int carnet);

    List<EstudianteData> listar();

    boolean eliminarPorCarnet(int carnet);

    int siguienteCarnet();

    @Override
    void close();
}
