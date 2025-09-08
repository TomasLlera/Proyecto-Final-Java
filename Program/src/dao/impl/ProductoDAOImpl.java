package dao.impl;

import dao.ProductoDAO;
import exception.ErrorDeBaseDeDatosException;
import model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Connection conn;

    public ProductoDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Producto findById(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM producto WHERE idProducto = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new ErrorDeBaseDeDatosException("Error al buscar producto por ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<Producto> findAll() {
        List<Producto> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM producto");
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new ErrorDeBaseDeDatosException("Error al listar productos", e);
        }
        return list;
    }

    @Override
    public void save(Producto producto) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO producto (nombre, precio, stock) VALUES (?, ?, ?)")) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorDeBaseDeDatosException("Error al guardar producto", e);
        }
    }

    @Override
    public void update(Producto producto) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE producto SET nombre = ?, precio = ?, stock = ? WHERE idProducto = ?")) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getIdProducto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorDeBaseDeDatosException("Error al actualizar producto con ID: " + producto.getIdProducto(), e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM producto WHERE idProducto = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorDeBaseDeDatosException("Error al eliminar producto con ID: " + id, e);
        }
    }

    private Producto mapResultSet(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        return p;
    }
}
