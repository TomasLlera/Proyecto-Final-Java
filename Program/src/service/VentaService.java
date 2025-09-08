package service;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import exception.EntidadNoEncontradaException;
import exception.StockInsuficienteException;
import model.*;

import java.util.Date;
import java.util.List;

public class VentaService {
    private VentaDAO ventaDAO;
    private ProductoDAO productoDAO;
    private ClienteDAO clienteDAO;

    public VentaService(VentaDAO ventaDAO, ProductoDAO productoDAO, ClienteDAO clienteDAO) {
        this.ventaDAO = ventaDAO;
        this.productoDAO = productoDAO;
        this.clienteDAO = clienteDAO;
    }

    public void crearVenta(Venta venta) throws EntidadNoEncontradaException, StockInsuficienteException, Exception {
        // Validar cliente
        Cliente cliente = clienteDAO.findById(venta.getCliente().getIdCliente());
        if (cliente == null) {
            throw new EntidadNoEncontradaException("Cliente con ID " + venta.getCliente().getIdCliente() + " no existe.");
        }

        // Validar stock de cada producto
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoDAO.findById(detalle.getProducto().getIdProducto());

            if (producto == null) {
                throw new EntidadNoEncontradaException("Producto con ID " + detalle.getProducto().getIdProducto() + " no existe.");
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre());
            }
        }

        // Actualizar stock
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoDAO.findById(detalle.getProducto().getIdProducto());
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoDAO.update(producto);
        }

        // Guardar venta
        ventaDAO.save(venta);
    }

    public ResumenVentas generarResumenPorFecha(Date fecha) {
        List<Venta> ventas = ventaDAO.findByDate(fecha);
        double totalVentas = ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();

        int cantidadTransacciones = ventas.size();

        return new ResumenVentas(fecha, totalVentas, cantidadTransacciones);
    }
}
