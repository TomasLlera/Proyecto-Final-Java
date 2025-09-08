package main;

import controller.VentaController;
import dao.impl.ClienteDAOImpl;
import dao.impl.ProductoDAOImpl;
import dao.impl.VentaDAOImpl;
import model.*;
import service.VentaService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        // Conexión simple a una base SQLite
        Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db");

        // Crear DAOs
        var clienteDAO = new ClienteDAOImpl(conn);
        var productoDAO = new ProductoDAOImpl(conn);
        var ventaDAO = new VentaDAOImpl(conn);

        // Crear servicio y controlador
        var ventaService = new VentaService(ventaDAO, productoDAO, clienteDAO);
        var ventaController = new VentaController(ventaService);

        // Crear objetos de ejemplo
        Cliente cliente = clienteDAO.findById(1); // asume que existe

        Producto prod1 = productoDAO.findById(1); // asume que existe
        DetalleVenta d1 = new DetalleVenta(prod1, 2);

        Venta venta = new Venta();
        venta.setFecha(new Date());
        venta.setCliente(cliente);
        venta.setEstadoEnvio("En proceso");
        venta.setDetalles(List.of(d1));

        ventaController.registrarVenta(venta);
    }
}
