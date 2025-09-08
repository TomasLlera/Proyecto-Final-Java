package dao;

import model.Cliente;
import java.util.List;

public interface ClienteDAO {
    Cliente findById(int id) throws Exception;
    List<Cliente> findAll() throws Exception;
    void insert(Cliente cliente) throws Exception;
    void update(Cliente cliente) throws Exception;
    void delete(int id) throws Exception;
}

