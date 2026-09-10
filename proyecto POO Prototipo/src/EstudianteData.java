public class EstudianteData {
    private String nombre;
    private String apellido;
    private Carrera carrera;
    private int carnet;

    public EstudianteData() {
        // constructor base
    }

    public EstudianteData(String nombre, String apellido, Carrera carrera, int carnet) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.carnet = carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public int getCarnet() {
        return carnet;
    }

    public void setCarnet(int carnet) {
        this.carnet = carnet;
    }

    // Ejercicio: construir representacion del estudiante.
    @Override
    public String toString() {
        return "";
    }
}
