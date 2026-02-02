import java.awt.*;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class App {

    private static final String STORAGE_FILE = "inventario.csv";
    private static Concesionaria concesionaria;

    private static final Color AZUL = new Color(18, 76, 163);
    private static final Color AZUL_CARD = new Color(33, 150, 243);
    private static final Color BG = new Color(245, 248, 252);

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        concesionaria = new Concesionaria("Automóviles del Futuro", STORAGE_FILE);
        SwingUtilities.invokeLater(App::ui);
    }

    private static void ui() {
        JFrame f = new JFrame("Automóviles del Futuro");
        f.setSize(900, 560);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setLayout(new BorderLayout());

        // ===== HEADER APP STYLE =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AZUL);
        header.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel title = new JLabel("AUTOMÓVILES DEL FUTURO");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel sub = new JLabel("Sistema de Gestión de Inventario");
        sub.setForeground(new Color(210, 230, 255));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titles = new JPanel(new GridLayout(2,1));
        titles.setOpaque(false);
        titles.add(title);
        titles.add(sub);

        header.add(titles, BorderLayout.WEST);

        // ===== DASHBOARD =====
        JPanel grid = new JPanel(new GridLayout(2,3,22,22));
        grid.setBackground(BG);
        grid.setBorder(new EmptyBorder(30,30,30,30));

        grid.add(card("📋", "Inventario", () ->
                mostrarTabla(concesionaria.listarTodos(), "Inventario Completo")));

        grid.add(card("✅", "Disponibles", () ->
                mostrarTabla(concesionaria.listarAutosDisponibles(), "Autos Disponibles")));

        grid.add(card("➕", "Agregar Auto", App::agregar));

        grid.add(card("💰", "Vender Auto", App::vender));

        grid.add(card("✏️", "Actualizar Precio", App::precio));

        grid.add(card("🚪", "Salir", () -> System.exit(0)));

        f.add(header, BorderLayout.NORTH);
        f.add(grid, BorderLayout.CENTER);
        f.setVisible(true);
    }

    // ===== CARD STYLE BUTTON =====

    private static JPanel card(String icon, String text, Runnable action) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AZUL_CARD);
        p.setBorder(new CompoundBorder(
                new LineBorder(new Color(0,0,0,30),1,true),
                new EmptyBorder(18,18,18,18)
        ));

        JLabel ic = new JLabel(icon, JLabel.CENTER);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        ic.setForeground(Color.WHITE);

        JLabel tx = new JLabel(text, JLabel.CENTER);
        tx.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tx.setForeground(Color.WHITE);

        p.add(ic, BorderLayout.CENTER);
        p.add(tx, BorderLayout.SOUTH);

        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                p.setBackground(AZUL.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                p.setBackground(AZUL_CARD);
            }
        });

        return p;
    }

    // ===== TABLE MODERNA =====

    private static void mostrarTabla(List<Auto> lista, String titulo) {
        String[] cols = {"ID","Marca","Modelo","Año","Precio","Disponible"};
        DefaultTableModel m = new DefaultTableModel(cols,0);

        for (Auto a: lista) {
            m.addRow(new Object[]{
                    a.getId(),
                    a.getMarca(),
                    a.getModelo(),
                    a.getAnio(),
                    String.format("$%.2f", a.getPrecio()),
                    a.isDisponible() ? "Sí":"No"
            });
        }

        JTable t = new JTable(m);
        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(AZUL);
        t.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0;i<t.getColumnCount();i++)
            t.getColumnModel().getColumn(i).setCellRenderer(c);

        JScrollPane sp = new JScrollPane(t);

        JFrame w = new JFrame(titulo);
        w.setSize(720,380);
        w.add(sp);
        w.setLocationRelativeTo(null);
        w.setVisible(true);
    }

    // ===== FORM DIALOGS =====

    private static void agregar() {
        try {
            JTextField marca = new JTextField();
            JTextField modelo = new JTextField();
            JTextField anio = new JTextField();
            JTextField precio = new JTextField();

            Object[] msg = {
                    "Marca:", marca,
                    "Modelo:", modelo,
                    "Año:", anio,
                    "Precio:", precio
            };

            if (JOptionPane.showConfirmDialog(null,msg,"Nuevo Auto",
                    JOptionPane.OK_CANCEL_OPTION)==0) {

                Auto a = new Auto(
                        marca.getText(),
                        modelo.getText(),
                        Integer.parseInt(anio.getText()),
                        Double.parseDouble(precio.getText()),
                        true
                );
                concesionaria.agregarAuto(a);
                JOptionPane.showMessageDialog(null,"Auto creado ID "+a.getId());
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Datos inválidos");
        }
    }

    private static void vender() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID a vender"));
            JOptionPane.showMessageDialog(null,
                    concesionaria.venderAuto(id) ?
                            "Venta registrada" : "No disponible");
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"ID inválido");
        }
    }

    private static void precio() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID"));
            double p = Double.parseDouble(JOptionPane.showInputDialog("Nuevo precio"));
            JOptionPane.showMessageDialog(null,
                    concesionaria.actualizarPrecioAuto(id,p) ?
                            "Precio actualizado":"Error");
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Datos inválidos");
        }
    }
}