package app;

import dao.ClienteDAO;
import dao.impl.ClienteDAOImpl;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientesFrame extends JFrame {

    private ClienteDAO clienteDAO;
    private JTable tablaClientes;
    private DefaultTableModel model;

    public ClientesFrame(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;

        setTitle("Gestión de Clientes");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Tabla y modelo
        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Teléfono", "Email"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // No editable directo
            }
        };
        tablaClientes = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);

        // Panel botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Cliente");
        JButton btnEliminar = new JButton("Eliminar Cliente");
        JButton btnRefrescar = new JButton("Refrescar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarClientes();

        // Acciones botones

        btnAgregar.addActionListener(e -> {
            Cliente nuevo = mostrarDialogoAgregar();
            if (nuevo != null) {
                try {
                    clienteDAO.insert(nuevo);
                    cargarClientes();
                    JOptionPane.showMessageDialog(this, "Cliente agregado correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al agregar cliente: " + ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar.");
                return;
            }
            int id = (int) model.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    clienteDAO.delete(id);
                    cargarClientes();
                    JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage());
                }
            }
        });

        btnRefrescar.addActionListener(e -> cargarClientes());

        setVisible(true);
    }

    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.findAll();
            model.setRowCount(0);
            for (Cliente c : clientes) {
                model.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre(),
                        c.getDireccion(),
                        c.getTelefono(),
                        c.getEmail()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando clientes: " + e.getMessage());
        }
    }

    private Cliente mostrarDialogoAgregar() {
        JTextField nombreField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] message = {
                "Nombre:", nombreField,
                "Dirección:", direccionField,
                "Teléfono:", telefonoField,
                "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return null;
            }
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setDireccion(direccionField.getText().trim());
            cliente.setTelefono(telefonoField.getText().trim());
            cliente.setEmail(emailField.getText().trim());
            return cliente;
        }
        return null;
    }
}
