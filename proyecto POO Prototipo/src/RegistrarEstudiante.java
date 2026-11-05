import java.util.ArrayList;
import java.util.List;

public class RegistrarEstudiante extends listaEstudiante {
    public boolean registrarEstudiante(String nombre, String apellido, Carrera carrera, int carnet) {
        if (carrera == null) return false;
        EstudianteData estudiante = new EstudianteData(nombre, apellido, carrera, carnet);
        return super.addEstudiante(estudiante);
    }

    public boolean registrarEstudiante(String nombre, String apellido, String carreraTexto, int carnet) {
        Carrera carrera = Carrera.fromString(carreraTexto);
        if (carrera == null) return false;
        EstudianteData estudiante = new EstudianteData(nombre, apellido, carrera, carnet);
        return super.addEstudiante(estudiante);
    }

    public boolean eliminarEstudiantePorCarnet(int carnet) {
        boolean eliminado = false;
        while (super.eliminarPorCarnet(carnet)) {
            eliminado = true;
        }
        return eliminado;
    }

    public EstudianteData buscarEstudiantePorCarnet(int carnet) {
        return super.buscarPorCarnet(carnet);
    }

    public void carnetEstudiante(int carnet) {
        EstudianteData e = buscarEstudiantePorCarnet(carnet);
        if (e != null) {
            System.out.println(e.toString());
        } else {
            System.out.println("No encontrado");
        }
    }

    public void carreraEstudiante(Carrera carrera) {
        List<EstudianteData> filtrados = super.getCarreraEstudiante(carrera).stream()
                .filter(e -> e.getCarrera() == carrera)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);;
        for (EstudianteData e : filtrados) {
            System.out.println(e.toString());
        }
    }

    public List<EstudianteData> getListaEstudiante() {
        return super.getEstudiantes();
    }
}
