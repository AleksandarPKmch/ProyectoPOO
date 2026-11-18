package carnets.modelo;

/**
 * Cualquier persona de la comunidad UVG que puede abordar el bus.
 * El carnet es el carnet real de UVG y no se puede cambiar.
 */
public abstract class Persona {

    private final int carnet;
    private final String nombre;
    private final String apellido;
    private boolean activo;

    protected Persona(int carnet, String nombre, String apellido, boolean activo) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
    }

    public int getCarnet() {
        return carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /** Un carnet suspendido existe pero no puede abordar. */
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public abstract TipoPersona getTipo();

    /** Dato propio de cada tipo: carrera, departamento o area. */
    public abstract String getDetalle();

    @Override
    public String toString() {
        return carnet + " | " + getNombreCompleto() + " | " + getTipo().getDescripcion() + " | " + getDetalle()
                + (activo ? "" : " | SUSPENDIDO");
    }
}
