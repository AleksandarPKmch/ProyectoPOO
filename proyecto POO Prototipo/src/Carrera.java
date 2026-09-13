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
    
    public static Carrera fromString(String texto) {
        if (texto == null ||texto.trim().isEmpty()) {
            return null;
        }
      
        String textoNormalizado = texto.trim().toUpperCase();
        for (Carrera carrera : Carrera.values()) {
            if (carrera.name().equals(textoNormalizado))
            carrera.getDescripcion().equalsIgnoreCase(texto.trim()); {
                return carrera;
            }
            
        }

        return null;
    }
}
