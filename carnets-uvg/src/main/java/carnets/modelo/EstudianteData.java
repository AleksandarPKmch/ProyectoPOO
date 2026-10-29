package carnets.modelo;

public class EstudianteData {
    private String nombre;
    private String apellido;
    private Carrera carrera;
    private int carnet;

    public EstudianteData() {
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

    /** Linea corta para listas. */
    @Override
    public String toString() {
        return carnet + " | " + nombre + " " + apellido + " | " + carrera.getDescripcion();
    }

    /** Tarjeta pequena para mostrar a un estudiante. */
    public String tarjeta() {
        String l1 = "Carnet " + carnet;
        String l2 = nombre + " " + apellido;
        String l3 = carrera.getDescripcion();
        int ancho = Math.max(l1.length(), Math.max(l2.length(), l3.length())) + 4;
        String borde = "+" + "-".repeat(ancho) + "+";
        return borde + "\n" + fila(l1, ancho) + "\n" + fila(l2, ancho) + "\n" + fila(l3, ancho) + "\n" + borde;
    }

    private static String fila(String texto, int ancho) {
        return "|  " + texto + " ".repeat(ancho - texto.length() - 2) + "|";
    }
}
