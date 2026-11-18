package carnets.repositorio;

import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;

import java.util.List;

/** Contrato unico de guardado. Memoria y MongoDB lo cumplen igual. */
public interface Repositorio extends AutoCloseable {

    String nombreAlmacenamiento();

    /** Devuelve false si el carnet ya existe o si no se pudo guardar. */
    boolean insertarPersona(Persona persona);

    Persona buscarPersona(int carnet);

    List<Persona> listarPersonas();

    boolean eliminarPersona(int carnet);

    boolean actualizarActivo(int carnet, boolean activo);

    void guardarAbordaje(RegistroAbordaje registro);

    /** Toda la bitacora, del mas reciente al mas antiguo. */
    List<RegistroAbordaje> listarAbordajes();

    /** Ultimo abordaje aceptado de ese carnet, o null si nunca ha abordado. */
    RegistroAbordaje ultimoAceptado(int carnet);

    @Override
    void close();
}
