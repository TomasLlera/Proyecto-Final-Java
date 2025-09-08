package model;

import java.util.Date;

public class ResumenVentas {
    private Date fecha;
    private double totalVentas;
    private int cantidadTransacciones;

    public ResumenVentas() {}

    public ResumenVentas(Date fecha, double totalVentas, int cantidadTransacciones) {
        this.fecha = fecha;
        this.totalVentas = totalVentas;
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(double totalVentas) { this.totalVentas = totalVentas; }

    public int getCantidadTransacciones() { return cantidadTransacciones; }
    public void setCantidadTransacciones(int cantidadTransacciones) { this.cantidadTransacciones = cantidadTransacciones; }
}
