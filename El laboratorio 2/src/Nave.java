import java.util.ArrayList;

public class Nave {
    public static final int MAX_MODULOS = 5;

    private final String nombre;
    private final String codigoIdentificacion;
    private final String comandante;
    private final Modulo[] modulos;
    private final ArrayList<Planeta> planetas;

    public Nave(String nombre, String codigoIdentificacion, String comandante) {
        this.nombre = validarTexto(nombre, "El nombre de la nave");
        this.codigoIdentificacion = validarTexto(codigoIdentificacion, "El codigo de identificacion");
        this.comandante = validarTexto(comandante, "El comandante");
        this.modulos = new Modulo[MAX_MODULOS];
        this.planetas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoIdentificacion() {
        return codigoIdentificacion;
    }

    public String getComandante() {
        return comandante;
    }

    public void instalarModulo(int posicion, Modulo modulo) {
        validarPosicion(posicion);
        if (modulos[posicion] != null) {
            throw new IllegalStateException("La posicion ya contiene un modulo.");
        }
        if (modulo == null) {
            throw new IllegalArgumentException("El modulo no puede ser null.");
        }
        modulos[posicion] = modulo;
    }

    public Modulo consultarModulo(int posicion) {
        validarPosicion(posicion);
        return modulos[posicion];
    }

    public Modulo[] getModulos() {
        return modulos;
    }

    public int contarModulos() {
        int cantidad = 0;
        for (Modulo modulo : modulos) {
            if (modulo != null) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public int espaciosDisponibles() {
        return MAX_MODULOS - contarModulos();
    }

    public void retirarModulo(int posicion) {
        validarPosicion(posicion);
        if (modulos[posicion] == null) {
            throw new IllegalStateException("La posicion no contiene un modulo.");
        }
        modulos[posicion] = null;
    }

    public void registrarPlaneta(Planeta planeta) {
        if (planeta == null) {
            throw new IllegalArgumentException("El planeta no puede ser null.");
        }
        if (buscarPlaneta(planeta.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un planeta con ese codigo.");
        }
        planetas.add(planeta);
    }

    public ArrayList<Planeta> getPlanetas() {
        return planetas;
    }

    public Planeta buscarPlaneta(String codigo) {
        for (Planeta planeta : planetas) {
            if (planeta.getCodigo().equalsIgnoreCase(codigo)) {
                return planeta;
            }
        }
        return null;
    }

    public void eliminarPlaneta(String codigo) {
        Planeta planeta = buscarPlaneta(codigo);
        if (planeta == null) {
            throw new IllegalArgumentException("No existe un planeta con ese codigo.");
        }
        planetas.remove(planeta);
    }

    private void validarPosicion(int posicion) {
        if (posicion < 0 || posicion >= MAX_MODULOS) {
            throw new IndexOutOfBoundsException("La posicion debe estar entre 0 y " + (MAX_MODULOS - 1) + ".");
        }
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        return valor.trim();
    }
}
