package controller;

import model.Venta;
import service.VentaService;

public class VentaController {
    private VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    public void registrarVenta(Venta venta) {
        try {
            ventaService.crearVenta(venta);
            System.out.println("Venta registrada con éxito.");
        } catch (Exception e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
        }
    }
}
