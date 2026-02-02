public abstract class Vehiculo {
    private static int contadorIds = 1;

    private final int id;
    private String marca;
    private String modelo;
    private int anio;
    private double precio;
    private boolean disponible;

    public Vehiculo(String marca, String modelo, int anio, double precio, boolean disponible) {
        this.id = contadorIds++;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
        this.disponible = disponible;
    }

    // Constructor para cargar desde persistencia con ID específico
    public Vehiculo(int id, String marca, String modelo, int anio, double precio, boolean disponible) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
        this.disponible = disponible;
        if (id >= contadorIds) contadorIds = id + 1;
    }

    
    public int getId() { return id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public double getPrecio() { return precio; }
    protected void setPrecio(double precio) { this.precio = precio; } 
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    
    public abstract String getDetalles();

    @Override
    public String toString() {
        return String.format("ID: %d | %s %s (%d) | $%.2f | Disponible: %s",
                id, marca, modelo, anio, precio, disponible ? "Sí" : "No");
    }
}
