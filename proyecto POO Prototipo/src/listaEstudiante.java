import java.util.ArrayList;
import java.util.List;

public class listaEstudiante {
    private List<EstudianteData> estudiantes;

    public listaEstudiante() {
        this.estudiantes = new ArrayList<>();
    }

    // agregar estudiante a la lista.
    public boolean addEstudiante(EstudianteData estudiante) {
        return false;
    }

    // eliminar estudiante de la lista por carnet.
    public boolean eliminarPorCarnet(int carnet) {
        return false;
    }

    public List<EstudianteData> getEstudiantes() {
        return estudiantes;
    }

    public List<EstudianteData> getCarnetEstudiante() {
        return estudiantes;
    }

    // filtrar estudiantes por carrera.
    public List<EstudianteData> getCarreraEstudiante(Carrera carrera) {
        return new ArrayList<>();
    }

    //buscar estudiante por numero de carnet.
    public EstudianteData buscarPorCarnet(int carnet) {
        return null;
    }
}