// src/Auto.java
import java.util.Locale;

public class Auto {
    private static int nextId = 1;

    private final int id;
    private String marca;
    private String modelo;
    private int anio;
    private double precio;
    private boolean disponible;

    // Constructor para crear nuevos autos (se asigna id automáticamente)
    public Auto(String marca, String modelo, int anio, double precio, boolean disponible) {
        this.id = nextId++;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
        this.disponible = disponible;
    }

    // Constructor para cargar desde archivo con id conocido
    public Auto(int id, String marca, String modelo, int anio, double precio, boolean disponible) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
        this.disponible = disponible;
    }

    // --- Encapsulamiento: getters y setters ---
    public int getId() { return id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public double getPrecio() { return precio; }
    protected void setPrecio(double precio) { this.precio = precio; } // protegido para control
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    // Método solicitado: obtener información detallada del auto
    public String getDetalles() {
        // Usamos Locale.US para asegurar punto decimal al mostrar
        return String.format(Locale.US,
                "ID: %d | Marca: %s | Modelo: %s | Año: %d | Precio: $%.2f | Disponible: %s",
                id, marca, modelo, anio, precio, disponible ? "Sí" : "No");
    }

    // Gestión de precios: actualizar precio con validación
    public void updatePrice(double nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        // ejemplo de validación adicional: no permitir reducciones > 90% (opcional)
        // if (nuevoPrecio < this.precio * 0.1) throw new IllegalArgumentException("Reducción excesiva.");
        setPrecio(nuevoPrecio);
    }

    // Para persistencia: representacion CSV (separador ;)
    public String toCSV() {
        return String.format(Locale.US, "%d;%s;%s;%d;%.2f;%b",
                id, escapeCSV(marca), escapeCSV(modelo), anio, precio, disponible);
    }

    private String escapeCSV(String s) {
        if (s == null) return "";
        return s.replace(";", ","); // simple: reemplazamos ; por , para no romper el separador
    }

    // Setter para controlar nextId al cargar desde archivo
    public static void setNextId(int next) {
        if (next > nextId) nextId = next;
    }
}
