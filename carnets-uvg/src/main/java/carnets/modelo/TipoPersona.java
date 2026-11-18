package carnets.modelo;

/** Los tres tipos de persona que pueden abordar el bus. */
public enum TipoPersona {
    ESTUDIANTE("Estudiante"),
    CATEDRATICO("Catedratico"),
    PERSONAL("Personal UVG");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
