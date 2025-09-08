package dao;

import model.Venta;
import java.util.Date;
import java.util.List;

public interface VentaDAO {
    Venta findById(int id);
    List<Venta> findAll();
    void save(Venta venta);
    List<Venta> findByDate(Date fecha);
}
