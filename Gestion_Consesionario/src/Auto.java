public class Auto extends Vehiculo {
    private int numPuertas;
    private String tipoCombustible;

    // Constructor con parámetros básicos (para App.java)
    public Auto(String marca, String modelo, int anio, double precio, boolean disponible) {
        super(marca, modelo, anio, precio, disponible);
        this.numPuertas = 4;
        this.tipoCombustible = "Gasolina";
    }

    // Constructor completo con parámetros adicionales
    public Auto(String marca, String modelo, int anio, double precio, boolean disponible,
                int numPuertas, String tipoCombustible) {
        super(marca, modelo, anio, precio, disponible);
        this.numPuertas = numPuertas;
        this.tipoCombustible = tipoCombustible;
    }

    // Constructor para cargar desde CSV (incluye ID)
    public Auto(int id, String marca, String modelo, int anio, double precio, boolean disponible) {
        super(id, marca, modelo, anio, precio, disponible);
        this.numPuertas = 4;
        this.tipoCombustible = "Gasolina";
    }

    public int getNumPuertas() { return numPuertas; }
    public void setNumPuertas(int numPuertas) { this.numPuertas = numPuertas; }
    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public void updatePrice(double nuevoPrecio) {
        if (nuevoPrecio <= 0) throw new IllegalArgumentException("El precio debe ser mayor a 0");
        this.setPrecio(nuevoPrecio);
    }

    public String toCSV() {
        return String.format("%d;%s;%s;%d;%.2f;%s",
                getId(), getMarca(), getModelo(), getAnio(), getPrecio(), isDisponible());
    }

    public static void setNextId(int nextId) {
        // No se usa aquí, el contador está en Vehiculo
    }

    @Override
    public String getDetalles() {
        return super.toString() +
               " | Puertas: " + numPuertas +
               " | Combustible: " + tipoCombustible;
    }
}
