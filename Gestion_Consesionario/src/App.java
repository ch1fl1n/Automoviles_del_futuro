// src/App.java
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class App {
    private static final String STORAGE_FILE = "inventario.csv";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // para consistencia en números
        Concesionaria c = new Concesionaria("Automóviles del Futuro", STORAGE_FILE);

        System.out.println("======================================");
        System.out.println("  ¡Bienvenido a Automóviles del Futuro!");
        System.out.println("======================================");

        Scanner sc = new Scanner(System.in);

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1":
                    listarInventarioTabla(c.listarTodos());
                    break;
                case "2":
                    listarInventarioTabla(c.listarAutosDisponibles());
                    break;
                case "3":
                    agregarAutoInteractivo(c, sc);
                    break;
                case "4":
                    venderAutoInteractivo(c, sc);
                    break;
                case "5":
                    actualizarPrecioInteractivo(c, sc);
                    break;
                case "6":
                    System.out.println("Guardando y saliendo...");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
            System.out.println();
        }

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("Menú:");
        System.out.println("1) Ver inventario completo");
        System.out.println("2) Ver autos disponibles");
        System.out.println("3) Agregar un auto nuevo");
        System.out.println("4) Vender un auto (marcar como vendido)");
        System.out.println("5) Actualizar precio de un auto");
        System.out.println("6) Salir");
    }

    // Muestra una "tabla" formateada en consola
    private static void listarInventarioTabla(List<Auto> lista) {
        if (lista.isEmpty()) {
            System.out.println("-- No hay autos para mostrar --");
            return;
        }
        String fmtHeader = "%-4s | %-12s | %-12s | %-6s | %-11s | %-10s%n";
        String fmtRow    = "%-4d | %-12s | %-12s | %-6d | $%10.2f | %-10s%n";
        System.out.printf(fmtHeader, "ID", "Marca", "Modelo", "Año", "Precio", "Disponible");
        System.out.println("--------------------------------------------------------------------");
        for (Auto a : lista) {
            System.out.printf(fmtRow, a.getId(), a.getMarca(), a.getModelo(), a.getAnio(), a.getPrecio(), a.isDisponible() ? "Sí" : "No");
        }
    }

    private static void agregarAutoInteractivo(Concesionaria c, Scanner sc) {
        try {
            System.out.print("Marca: ");
            String marca = sc.nextLine().trim();
            System.out.print("Modelo: ");
            String modelo = sc.nextLine().trim();
            System.out.print("Año de fabricación (ej. 2022): ");
            int anio = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Precio (ej. 18000.00): ");
            double precio = Double.parseDouble(sc.nextLine().trim());
            boolean disponible = true; // al crear, por defecto disponible

            Auto nuevo = new Auto(marca, modelo, anio, precio, disponible);
            c.agregarAuto(nuevo);
            System.out.println("Auto agregado con éxito. ID asignado: " + nuevo.getId());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida: asegúrese de escribir números válidos para año y precio.");
        } catch (Exception e) {
            System.out.println("Error al agregar auto: " + e.getMessage());
        }
    }

    private static void venderAutoInteractivo(Concesionaria c, Scanner sc) {
        try {
            System.out.print("Ingrese ID del auto a vender: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            boolean ok = c.venderAuto(id);
            if (ok) System.out.println("Auto marcado como vendido.");
            else System.out.println("No se pudo vender: ID no existe o auto ya estaba vendido.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    private static void actualizarPrecioInteractivo(Concesionaria c, Scanner sc) {
        try {
            System.out.print("Ingrese ID del auto a actualizar precio: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Ingrese nuevo precio: ");
            double nuevo = Double.parseDouble(sc.nextLine().trim());
            boolean ok = c.actualizarPrecioAuto(id, nuevo);
            if (ok) System.out.println("Precio actualizado correctamente.");
            else System.out.println("No se pudo actualizar el precio (ID no existe o validación falló).");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }
}
 