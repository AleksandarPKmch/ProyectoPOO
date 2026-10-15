import java.util.List;

public class RegistrarEstudiante {
    private ListaEstudiante lista;

    public RegistrarEstudiante() {
        this.lista = new ListaEstudiante();
    }

    public boolean registrarEstudiante(String nombre, String apellido, Carrera carrera, int carnet) {
        if (carrera == null) return false;
        EstudianteData nuevo = new EstudianteData(nombre, apellido, carrera, carnet);
        return lista.addEstudiante(nuevo);
    }

    public boolean registrarEstudiante(String nombre, String apellido, String carreraTexto, int carnet) {
        Carrera carrera = Carrera.fromString(carreraTexto);
        if (carrera == null) return false;
        EstudianteData nuevo = new EstudianteData(nombre, apellido, carrera, carnet);
        return lista.addEstudiante(nuevo);
    }

    public boolean eliminarEstudiantePorCarnet(int carnet) {
        return lista.eliminarPorCarnet(carnet);
    }

    public EstudianteData buscarEstudiantePorCarnet(int carnet) {
        return lista.buscarPorCarnet(carnet);
    }

    public void carnetEstudiante(int carnet) {
        EstudianteData e = lista.buscarPorCarnet(carnet);
        if (e != null) {
            System.out.println(e.toString());
        } else {
            System.out.println("No encontrado");
        }
    }

    public void carreraEstudiante(Carrera carrera) {
        List<EstudianteData> filtrados = lista.getCarreraEstudiante(carrera);
        for (EstudianteData e : filtrados) {
            System.out.println(e.toString());
        }
    }

    public ListaEstudiante getListaEstudiante() {
        return lista;
    }

    public List<EstudianteData> getEstudiantes() {
        return lista.getEstudiantes();
    }
}
