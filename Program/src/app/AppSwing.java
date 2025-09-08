package app;
import dao.ClienteDAO;
import javax.swing.*;
import java.awt.*;
import dao.impl.ClienteDAOImpl;
import java.awt.event.*;

public class AppSwing {

    private JFrame frame;
    private ClienteDAO clienteDAO;

    public AppSwing(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;

        frame = new JFrame("Sistema de Ventas - Menú Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Centrar ventana

        JPanel panel = new JPanel(new GridLayout(1, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnClientes = new JButton("Gestionar Clientes");
        panel.add(btnClientes);

        frame.add(panel);

        btnClientes.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new ClientesFrame(clienteDAO));
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        var conn = java.sql.DriverManager.getConnection("jdbc:sqlite:tienda.db");
        ClienteDAO clienteDAO = new ClienteDAOImpl(conn);

        SwingUtilities.invokeLater(() -> new AppSwing(clienteDAO));
    }
}
