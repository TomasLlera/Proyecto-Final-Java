package dao;

import model.Producto;
import java.util.List;

public interface ProductoDAO {
    Producto findById(int id);
    List<Producto> findAll();
    void save(Producto producto);
    void update(Producto producto);
    void delete(int id);
}
