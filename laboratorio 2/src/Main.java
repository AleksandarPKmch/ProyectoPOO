import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Nave nave = crearNave();
        boolean continuar = true;

        while (continuar) {
            mostrarMenu(nave);
            try {
                int opcion = leerEntero("Seleccione una opcion: ");
                switch (opcion) {
                        case 1 -> instalarModulo(nave);
                        case 2 -> consultarModulos(nave);
                        case 3 -> consultarModulo(nave);
                        case 4 -> modificarModulo(nave);
                        case 5 -> retirarModulo(nave);
                        case 6 -> registrarPlaneta(nave);
                        case 7 -> consultarPlanetas(nave);
                        case 8 -> buscarPlaneta(nave);
                        case 9 -> modificarPlaneta(nave);
                        case 10 -> eliminarPlaneta(nave);
                        case 11 -> mostrarResumen(nave);
                        case 0 -> continuar = false;
                        default -> System.out.println("Opcion no valida.");
                }
            } catch (InputMismatchException | IllegalArgumentException | IllegalStateException
                    | IndexOutOfBoundsException exception) {
                System.out.println("Operacion no realizada: " + exception.getMessage());
            } finally {
                System.out.println("----------------------------------------");
            }
        }
        SCANNER.close();
        System.out.println("Mision finalizada.");
    }

    private static Nave crearNave() {
        System.out.println("=== Configuracion inicial de la nave ===");
        while (true) {
            try {
                String nombre = leerTexto("Nombre: ");
                String codigo = leerTexto("Codigo de identificacion: ");
                String comandante = leerTexto("Nombre del comandante: ");
                return new Nave(nombre, codigo, comandante);
            } catch (IllegalArgumentException exception) {
                System.out.println("Dato no valido: " + exception.getMessage());
            }
        }
    }

    private static void mostrarMenu(Nave nave) {
        System.out.println("\n=== Mision " + nave.getNombre() + " ===");
        System.out.println("1. Instalar modulo");
        System.out.println("2. Consultar modulos");
        System.out.println("3. Consultar modulo por posicion");
        System.out.println("4. Modificar modulo");
        System.out.println("5. Retirar modulo");
        System.out.println("6. Registrar planeta");
        System.out.println("7. Consultar planetas");
        System.out.println("8. Buscar planeta por codigo");
        System.out.println("9. Modificar planeta");
        System.out.println("10. Eliminar planeta");
        System.out.println("11. Mostrar resumen");
        System.out.println("0. Salir");
    }

    private static void instalarModulo(Nave nave) {
        int posicion = leerEntero("Posicion (0-4): ");
        Modulo modulo = new Modulo(leerTexto("Codigo: "), leerTexto("Nombre: "),
                leerTexto("Tipo: "), leerDouble("Consumo de energia: "), leerTexto("Estado: "));
        nave.instalarModulo(posicion, modulo);
        System.out.println("Modulo instalado correctamente.");
    }

    private static void consultarModulos(Nave nave) {
        boolean hayModulos = false;
        for (int posicion = 0; posicion < Nave.MAX_MODULOS; posicion++) {
            Modulo modulo = nave.consultarModulo(posicion);
            if (modulo != null) {
                System.out.println("Posicion " + posicion + ": " + modulo);
                hayModulos = true;
            }
        }
        if (!hayModulos) {
            System.out.println("No hay modulos instalados.");
        }
    }

    private static void consultarModulo(Nave nave) {
        int posicion = leerEntero("Posicion (0-4): ");
        Modulo modulo = nave.consultarModulo(posicion);
        System.out.println(modulo == null ? "La posicion esta vacia." : modulo);
    }

    private static void modificarModulo(Nave nave) {
        int posicion = leerEntero("Posicion (0-4): ");
        Modulo modulo = nave.consultarModulo(posicion);
        if (modulo == null) {
            System.out.println("La posicion esta vacia.");
            return;
        }
        modulo.setConsumoEnergia(leerDouble("Nuevo consumo de energia: "));
        modulo.setEstado(leerTexto("Nuevo estado: "));
        System.out.println("Modulo modificado correctamente.");
    }

    private static void retirarModulo(Nave nave) {
        nave.retirarModulo(leerEntero("Posicion (0-4): "));
        System.out.println("Modulo retirado correctamente.");
    }

    private static void registrarPlaneta(Nave nave) {
        Planeta planeta = leerDatosPlaneta(null);
        nave.registrarPlaneta(planeta);
        System.out.println("Planeta registrado correctamente.");
    }

    private static void consultarPlanetas(Nave nave) {
        if (nave.getPlanetas().isEmpty()) {
            System.out.println("Todavia no hay planetas descubiertos.");
            return;
        }
        for (Planeta planeta : nave.getPlanetas()) {
            System.out.println(planeta);
        }
    }

    private static void buscarPlaneta(Nave nave) {
        Planeta planeta = nave.buscarPlaneta(leerTexto("Codigo del planeta: "));
        System.out.println(planeta == null ? "Planeta no encontrado." : planeta);
    }

    private static void modificarPlaneta(Nave nave) {
        String codigo = leerTexto("Codigo del planeta a modificar: ");
        Planeta planeta = nave.buscarPlaneta(codigo);
        if (planeta == null) {
            System.out.println("Planeta no encontrado.");
            return;
        }
        leerDatosPlaneta(planeta);
        System.out.println("Planeta modificado correctamente.");
    }

    private static Planeta leerDatosPlaneta(Planeta planeta) {
        String codigo = planeta == null ? leerTexto("Codigo: ") : planeta.getCodigo();
        String nombre = leerTexto("Nombre: ");
        double distancia = leerDouble("Distancia desde la nave: ");
        double temperatura = leerDouble("Temperatura: ");
        double habitabilidad = leerDouble("Nivel de habitabilidad (0-100): ");
        if (planeta == null) {
            return new Planeta(codigo, nombre, distancia, temperatura, habitabilidad);
        }
        planeta.setNombre(nombre);
        planeta.setDistancia(distancia);
        planeta.setTemperatura(temperatura);
        planeta.setNivelHabitabilidad(habitabilidad);
        return planeta;
    }

    private static void eliminarPlaneta(Nave nave) {
        nave.eliminarPlaneta(leerTexto("Codigo del planeta: "));
        System.out.println("Planeta eliminado correctamente.");
    }

    private static void mostrarResumen(Nave nave) {
        System.out.println("Modulos instalados: " + nave.contarModulos());
        System.out.println("Espacios disponibles: " + nave.espaciosDisponibles());
        Modulo mayorConsumo = null;
        for (Modulo modulo : nave.getModulos()) {
            if (modulo != null && (mayorConsumo == null
                    || modulo.getConsumoEnergia() > mayorConsumo.getConsumoEnergia())) {
                mayorConsumo = modulo;
            }
        }
        System.out.println("Modulo con mayor consumo: "
                + (mayorConsumo == null ? "No hay modulos." : mayorConsumo));

        if (nave.getPlanetas().isEmpty()) {
            System.out.println("No hay planetas para calcular estadisticas.");
            return;
        }
        Planeta mayor = nave.getPlanetas().get(0);
        Planeta menor = mayor;
        double suma = 0;
        for (Planeta planeta : nave.getPlanetas()) {
            suma += planeta.getNivelHabitabilidad();
            if (planeta.getNivelHabitabilidad() > mayor.getNivelHabitabilidad()) {
                mayor = planeta;
            }
            if (planeta.getNivelHabitabilidad() < menor.getNivelHabitabilidad()) {
                menor = planeta;
            }
        }
        System.out.println("Planetas descubiertos: " + nave.getPlanetas().size());
        System.out.println("Mayor habitabilidad: " + mayor);
        System.out.println("Menor habitabilidad: " + menor);
        System.out.printf("Promedio de habitabilidad: %.2f%n", suma / nave.getPlanetas().size());
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SCANNER.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            return SCANNER.nextInt();
        } catch (InputMismatchException exception) {
            throw new InputMismatchException("Debe ingresar un numero entero.");
        } finally {
            SCANNER.nextLine();
        }
    }

    private static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        try {
            return SCANNER.nextDouble();
        } catch (InputMismatchException exception) {
            throw new InputMismatchException("Debe ingresar un numero.");
        } finally {
            SCANNER.nextLine();
        }
    }

    @SuppressWarnings("unused")
    private static void limpiarEntradaNumerica() {
        if (SCANNER.hasNextLine()) {
            SCANNER.nextLine();
        }
    }
}
