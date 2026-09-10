public enum Carrera {
    INGENIERIA("Ingenieria"),
    EDUCACION("Educacion"),
    CIENCIAS_Y_HUMANIDADES("Ciencias y Humanidades"),
    BUSINESS("Business"),
    CIENCIAS_SOCIALES("Ciencias Sociales"),
    ART_SCHOOL("Art School"),
    COLEGIO("Colegiado"),
    ARQUITECTURA("Arquitectura");

    private final String descripcion;

    Carrera(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    //convertir texto ingresado a un valor del enum.
    public static Carrera fromString(String texto) {
        // 1. Validar texto.
        // 2. Normalizar mayusculas/minusculas.
        // 3. Recorrer valores del enum.
        // 4. Devolver la carrera encontrada o null si no existe.
        return null;
    }
}
