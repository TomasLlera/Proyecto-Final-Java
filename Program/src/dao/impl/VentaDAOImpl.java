package dao.impl;

import dao.VentaDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {
    private Connection conn;

    public VentaDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Venta findById(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM venta WHERE idVenta = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapVenta(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Venta> findAll() {
        List<Venta> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM venta");
            while (rs.next()) {
                list.add(mapVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(Venta venta) {
        try {
            conn.setAutoCommit(false);

            // Insertar venta
            PreparedStatement stmtVenta = conn.prepareStatement(
                    "INSERT INTO venta (fecha, idCliente, estadoEnvio) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmtVenta.setDate(1, new java.sql.Date(venta.getFecha().getTime()));
            stmtVenta.setInt(2, venta.getCliente().getIdCliente());
            stmtVenta.setString(3, venta.getEstadoEnvio());
            stmtVenta.executeUpdate();

            ResultSet rs = stmtVenta.getGeneratedKeys();
            if (rs.next()) {
                int idVenta = rs.getInt(1);
                venta.setIdVenta(idVenta);
            }

            // Insertar detalles
            for (DetalleVenta detalle : venta.getDetalles()) {
                PreparedStatement stmtDetalle = conn.prepareStatement(
                        "INSERT INTO detalle_venta (idVenta, idProducto, cantidad, subtotal) VALUES (?, ?, ?, ?)"
                );
                stmtDetalle.setInt(1, venta.getIdVenta());
                stmtDetalle.setInt(2, detalle.getProducto().getIdProducto());
                stmtDetalle.setInt(3, detalle.getCantidad());
                stmtDetalle.setDouble(4, detalle.getSubtotal());
                stmtDetalle.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Venta> findByDate(Date fecha) {
        List<Venta> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM venta WHERE fecha = ?")) {
            stmt.setDate(1, new java.sql.Date(fecha.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Venta mapVenta(ResultSet rsVenta) throws SQLException {
        int idVenta = rsVenta.getInt("idVenta");

        Cliente cliente = new Cliente(); // Solo para ejemplo
        cliente.setIdCliente(rsVenta.getInt("idCliente"));

        Venta venta = new Venta();
        venta.setIdVenta(idVenta);
        venta.setFecha(rsVenta.getDate("fecha"));
        venta.setCliente(cliente);
        venta.setEstadoEnvio(rsVenta.getString("estadoEnvio"));

        // Cargar detalles
        PreparedStatement stmtDetalles = conn.prepareStatement("SELECT * FROM detalle_venta WHERE idVenta = ?");
        stmtDetalles.setInt(1, idVenta);
        ResultSet rsDetalles = stmtDetalles.executeQuery();
        List<DetalleVenta> detalles = new ArrayList<>();

        while (rsDetalles.next()) {
            Producto producto = new Producto();
            producto.setIdProducto(rsDetalles.getInt("idProducto"));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(rsDetalles.getInt("cantidad"));
            detalles.add(detalle);
        }

        venta.setDetalles(detalles);
        return venta;
    }
}
