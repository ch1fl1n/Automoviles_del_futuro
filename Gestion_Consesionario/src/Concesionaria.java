// src/Concesionaria.java
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Concesionaria {
    private final String nombre;
    private final List<Auto> inventario;
    private final File storageFile;

    // archivo por defecto inventario.csv en la carpeta del proyecto
    public Concesionaria(String nombre, String storageFilePath) {
        this.nombre = nombre;
        this.inventario = new ArrayList<>();
        this.storageFile = new File(storageFilePath);
        // intentar cargar inventario al iniciar
        loadFromFile();
    }

    // Agregar un auto al inventario (usa el constructor de Auto que asigna ID automáticamente)
    public void agregarAuto(Auto auto) {
        if (auto == null) throw new IllegalArgumentException("Auto no puede ser null");
        inventario.add(auto);
        saveToFile(); // guardamos cada cambio
    }

    // Listar autos disponibles
    public List<Auto> listarAutosDisponibles() {
        List<Auto> disponibles = new ArrayList<>();
        for (Auto a : inventario) {
            if (a.isDisponible()) disponibles.add(a);
        }
        return disponibles;
    }

    // Listar todos
    public List<Auto> listarTodos() {
        return new ArrayList<>(inventario);
    }

    // Buscar por ID
    public Optional<Auto> buscarPorId(int id) {
        return inventario.stream().filter(a -> a.getId() == id).findFirst();
    }

    // Vender auto por id (verifica disponibilidad)
    public boolean venderAuto(int id) {
        Optional<Auto> opt = buscarPorId(id);
        if (opt.isEmpty()) return false;
        Auto a = opt.get();
        if (!a.isDisponible()) return false;
        a.setDisponible(false);
        saveToFile();
        return true;
    }

    // Actualizar precio por id (usa método updatePrice del Auto)
    public boolean actualizarPrecioAuto(int id, double nuevoPrecio) {
        Optional<Auto> opt = buscarPorId(id);
        if (opt.isEmpty()) return false;
        try {
            opt.get().updatePrice(nuevoPrecio);
            saveToFile();
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error al actualizar precio: " + e.getMessage());
            return false;
        }
    }

    // ---------------- Persistencia en CSV simple ----------------
    // Formato: id;marca;modelo;anio;precio;disponible
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(storageFile, false))) {
            for (Auto a : inventario) {
                pw.println(a.toCSV());
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar el inventario: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        if (!storageFile.exists()) return;
        List<Auto> cargados = new ArrayList<>();
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // separar por ;
                String[] parts = line.split(";", -1);
                if (parts.length < 6) continue;
                int id = Integer.parseInt(parts[0]);
                String marca = parts[1];
                String modelo = parts[2];
                int anio = Integer.parseInt(parts[3]);
                double precio = Double.parseDouble(parts[4]);
                boolean disponible = Boolean.parseBoolean(parts[5]);
                Auto a = new Auto(id, marca, modelo, anio, precio, disponible);
                cargados.add(a);
                if (id > maxId) maxId = id;
            }
            // reemplazamos inventario y ajustamos nextId
            inventario.clear();
            inventario.addAll(cargados);
            Auto.setNextId(maxId + 1);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error cargando inventario: " + e.getMessage());
        }
    }

    public String getNombre() { return nombre; }
}
