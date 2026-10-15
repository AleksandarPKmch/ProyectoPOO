import java.util.ArrayList;
import java.util.List;

public class listaEstudiante {
    private List<EstudianteData> estudiantes;

    public listaEstudiante() {
        this.estudiantes = new ArrayList<>();
    }

    public boolean addEstudiante(EstudianteData estudiante) {
        if (estudiante == null) {
            return false;
        }
        estudiantes.add(estudiante);
        return true;
    }

    public boolean eliminarPorCarnet(int carnet) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getCarnet() == carnet) {
                estudiantes.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<EstudianteData> getEstudiantes() {
        return estudiantes;
    }

    public List<EstudianteData> getCarnetEstudiante() {
        return estudiantes;
    }

    public List<EstudianteData> getCarreraEstudiante(Carrera carrera) {
        List<EstudianteData> filtrados = new ArrayList<>();
        for (EstudianteData estudiante : estudiantes) {
            if (estudiante.getCarrera() == carrera) {
                filtrados.add(estudiante);
            }
        }
        return filtrados;
    }

    public EstudianteData buscarPorCarnet(int carnet) {
        for (EstudianteData estudiante : estudiantes) {
            if (estudiante.getCarnet() == carnet) {
                return estudiante;
            }
        }
        return null;
    }
}