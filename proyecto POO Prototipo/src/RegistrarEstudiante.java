import java.util.List;

public class RegistrarEstudiante {
    private listaEstudiante lista;

    public RegistrarEstudiante() {
        this.lista = new listaEstudiante();
    }

    // Crear un estudiante con una Carrera valida.
    public boolean registrarEstudiante(String nombre, String apellido, Carrera carrera, int carnet) {    
        return false;
    }

    // Crear un estudiante con texto de carrera e internamente convertirlo.
    public boolean registrarEstudiante(String nombre, String apellido, String carreraTexto, int carnet) {
        return false;
    }

    // Eliminar estudiante por carnet.
    public boolean eliminarEstudiantePorCarnet(int carnet) {
        return false;
    }

    // Buscar estudiante por carnet.
    public EstudianteData buscarEstudiantePorCarnet(int carnet) {
        return null;
    }

    // Mostrar la informacion de un estudiante por carnet.
    public void carnetEstudiante(int carnet) {
        // usar lista.buscarPorCarnet(carnet)
    }

    // Mostrar todos los estudiantes de una carrera.
    public void carreraEstudiante(Carrera carrera) {
        // usar lista.getCarreraEstudiante(carrera)
    }

    public listaEstudiante getListaEstudiante() {
        return lista;
    }

    public List<EstudianteData> getEstudiantes() {
        return lista.getEstudiantes();
    }

    //public static void main(String[] args) {
     //   System.out.println("Harry kane balon de oro");
   // }
}
