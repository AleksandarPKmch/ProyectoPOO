package carnets.modelo;

public enum Carrera {
    INGENIERIA(1, "Ingenieria"),
    EDUCACION(2, "Educacion"),
    CIENCIAS_Y_HUMANIDADES(3, "Ciencias y Humanidades"),
    BUSINESS(4, "Business"),
    CIENCIAS_SOCIALES(5, "Ciencias Sociales"),
    ART_SCHOOL(6, "Art School"),
    COLEGIO(7, "Colegiado"),
    ARQUITECTURA(8, "Arquitectura");

    private final int opcion;
    private final String descripcion;

    Carrera(int opcion, String descripcion) {
        this.opcion = opcion;
        this.descripcion = descripcion;
    }

    public int getOpcion() {
        return opcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Acepta el numero, el nombre interno o la descripcion. */
    public static Carrera fromString(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        String t = texto.trim();
        for (Carrera c : values()) {
            if (String.valueOf(c.opcion).equals(t)
                    || c.name().equalsIgnoreCase(t)
                    || c.descripcion.equalsIgnoreCase(t)) {
                return c;
            }
        }
        return null;
    }
}
