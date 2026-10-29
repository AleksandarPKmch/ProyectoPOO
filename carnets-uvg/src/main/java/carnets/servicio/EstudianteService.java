package carnets.servicio;

import carnets.modelo.Carrera;
import carnets.modelo.EstudianteData;
import carnets.repositorio.EstudianteRepository;

import java.util.List;

/** Reglas del sistema: valida los datos y asigna el carnet. */
public class EstudianteService {

    private final EstudianteRepository repo;

    public EstudianteService(EstudianteRepository repo) {
        this.repo = repo;
    }

    public String almacenamiento() {
        return repo.nombreAlmacenamiento();
    }

    /** Devuelve el estudiante ya guardado, con su carnet. */
    public EstudianteData registrar(String nombre, String apellido, Carrera carrera) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio.");
        }
        if (carrera == null) {
            throw new IllegalArgumentException("Hay que elegir una carrera.");
        }
        int carnet = repo.siguienteCarnet();
        EstudianteData nuevo = new EstudianteData(nombre.trim(), apellido.trim(), carrera, carnet);
        if (!repo.insertar(nuevo)) {
            throw new IllegalStateException("No se pudo guardar al estudiante.");
        }
        return nuevo;
    }

    public EstudianteData buscar(int carnet) {
        return repo.buscarPorCarnet(carnet);
    }

    public List<EstudianteData> listar() {
        return repo.listar();
    }

    public boolean eliminar(int carnet) {
        return repo.eliminarPorCarnet(carnet);
    }
}
