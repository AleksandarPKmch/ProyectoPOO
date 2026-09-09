    public class Planeta {
    private String codigo;
    private String nombre;
    private double distancia;
    private double temperatura;
    private double nivelHabitabilidad;

    public Planeta(String codigo, String nombre, double distancia, double temperatura, double nivelHabitabilidad) {
        this.codigo = validarTexto(codigo, "El codigo");
        this.nombre = validarTexto(nombre, "El nombre");
        setDistancia(distancia);
        setTemperatura(temperatura);
        setNivelHabitabilidad(nivelHabitabilidad);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getDistancia() {
        return distancia;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getNivelHabitabilidad() {
        return nivelHabitabilidad;
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "El nombre");
    }

    public void setDistancia(double distancia) {
        if (distancia <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que 0.");
        }
        this.distancia = distancia;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public void setNivelHabitabilidad(double nivelHabitabilidad) {
        if (nivelHabitabilidad < 0 || nivelHabitabilidad > 100) {
            throw new IllegalArgumentException("La habitabilidad debe estar entre 0 y 100.");
        }
        this.nivelHabitabilidad = nivelHabitabilidad;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo + " | Nombre: " + nombre + " | Distancia: " + distancia
                + " | Temperatura: " + temperatura + " | Habitabilidad: " + nivelHabitabilidad;
    }
}
