public class Auto extends Vehiculo {

    private int numPuertas;
    private String tipoCombustible;

    public Auto(String marca, String modelo, int anio, double precio, boolean disponible,
                int numPuertas, String tipoCombustible) {
        super(marca, modelo, anio, precio, disponible);
        this.numPuertas = numPuertas;
        this.tipoCombustible = tipoCombustible;
    }

    @Override
    public String getDetalles() {
        return super.toString() +
               " | Puertas: " + numPuertas +
               " | Combustible: " + tipoCombustible;
    }
}
