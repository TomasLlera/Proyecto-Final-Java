package model;

import java.util.Date;
import java.util.List;

public class Venta {
    private int idVenta;
    private Date fecha;
    private Cliente cliente;
    private String estadoEnvio;  // Ej: "En proceso", "Entregado"
    private List<DetalleVenta> detalles;

    public Venta() {}

    public Venta(int idVenta, Date fecha, Cliente cliente, String estadoEnvio, List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.cliente = cliente;
        this.estadoEnvio = estadoEnvio;
        this.detalles = detalles;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getEstadoEnvio() { return estadoEnvio; }
    public void setEstadoEnvio(String estadoEnvio) { this.estadoEnvio = estadoEnvio; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }

    public double getTotal() {
        return detalles.stream()
                       .mapToDouble(DetalleVenta::getSubtotal)
                       .sum();
    }
}

