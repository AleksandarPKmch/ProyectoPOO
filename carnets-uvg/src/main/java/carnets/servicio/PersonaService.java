package carnets.servicio;

import carnets.modelo.Carrera;
import carnets.modelo.Catedratico;
import carnets.modelo.Estudiante;
import carnets.modelo.Persona;
import carnets.modelo.Personal;
import carnets.modelo.TipoPersona;
import carnets.repositorio.Repositorio;

import java.util.List;

/** Reglas para dar de alta, suspender y eliminar personas. */
public class PersonaService {

    private final Repositorio repositorio;

    public PersonaService(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Registra a una persona con su carnet real de UVG.
     * El dato es la carrera para estudiantes, el departamento para catedraticos y el area para personal.
     */
    public Persona registrar(TipoPersona tipo, String carnetTexto, String nombre, String apellido, String dato) {
        if (tipo == null) {
            throw new IllegalArgumentException("Elija el tipo de persona.");
        }
        int carnet = leerCarnet(carnetTexto);
        String n = obligatorio(nombre, "El nombre no puede quedar vacio.");
        String a = obligatorio(apellido, "El apellido no puede quedar vacio.");
        if (repositorio.buscarPersona(carnet) != null) {
            throw new IllegalArgumentException("Ya existe una persona con el carnet " + carnet + ".");
        }

        Persona nueva;
        switch (tipo) {
            case ESTUDIANTE:
                Carrera carrera = Carrera.fromString(dato);
                if (carrera == null) {
                    throw new IllegalArgumentException("Elija una carrera de la lista.");
                }
                nueva = new Estudiante(carnet, n, a, carrera, true);
                break;
            case CATEDRATICO:
                nueva = new Catedratico(carnet, n, a, obligatorio(dato, "Escriba el departamento."), true);
                break;
            default:
                nueva = new Personal(carnet, n, a, obligatorio(dato, "Escriba el area de trabajo."), true);
        }
        if (!repositorio.insertarPersona(nueva)) {
            throw new IllegalStateException("No se pudo guardar a la persona, revise la conexion.");
        }
        return nueva;
    }

    public List<Persona> listar() {
        return repositorio.listarPersonas();
    }

    public Persona buscar(int carnet) {
        return repositorio.buscarPersona(carnet);
    }

    public boolean eliminar(int carnet) {
        return repositorio.eliminarPersona(carnet);
    }

    public boolean cambiarEstado(int carnet, boolean activo) {
        return repositorio.actualizarActivo(carnet, activo);
    }

    /** Convierte el texto en carnet o explica por que no sirve. */
    public static int leerCarnet(String texto) {
        String t = texto == null ? "" : texto.trim();
        if (!t.matches("\\d{1,9}")) {
            throw new IllegalArgumentException("El carnet debe ser un numero de hasta 9 digitos, por ejemplo 26949.");
        }
        int carnet = Integer.parseInt(t);
        if (carnet <= 0) {
            throw new IllegalArgumentException("El carnet debe ser mayor que 0.");
        }
        return carnet;
    }

    private static String obligatorio(String texto, String mensaje) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
        return texto.trim();
    }
}
