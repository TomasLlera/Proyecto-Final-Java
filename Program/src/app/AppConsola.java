package app;

import dao.impl.ClienteDAOImpl;
import dao.impl.ProductoDAOImpl;
import dao.impl.VentaDAOImpl;
import model.*;
import service.VentaService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class AppConsola {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:tienda.db")) {

            ClienteDAOImpl clienteDAO = new ClienteDAOImpl(conn);
            ProductoDAOImpl productoDAO = new ProductoDAOImpl(conn);
            VentaDAOImpl ventaDAO = new VentaDAOImpl(conn);

            VentaService ventaService = new VentaService(ventaDAO, productoDAO, clienteDAO);

            int opcion;
            do {
                mostrarMenu();
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listarClientes(clienteDAO);
                    case 2 -> crearCliente(clienteDAO);
                    case 3 -> eliminarCliente(clienteDAO);
                    case 4 -> listarProductos(productoDAO);
                    case 5 -> crearProducto(productoDAO);
                    case 6 -> eliminarProducto(productoDAO);
                    case 7 -> registrarVenta(ventaService);
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } while (opcion != 0);

        } catch (Exception e) {
            System.err.println("Error de conexión o inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("""
                === SISTEMA DE VENTAS ===
                1. Listar clientes
                2. Crear cliente
                3. Eliminar cliente
                4. Listar productos
                5. Crear producto
                6. Eliminar producto
                7. Registrar venta
                0. Salir
                -------------------------
                Seleccione una opción:
                """);
    }

    // CRUD Cliente
    private static void listarClientes(ClienteDAOImpl dao) {
        try {
            System.out.println("=== LISTA DE CLIENTES ===");
            List<Cliente> clientes = dao.findAll();
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
            } else {
                for (Cliente c : clientes) {
                    System.out.printf("%d - %s | %s | %s | %s%n",
                            c.getIdCliente(), c.getNombre(), c.getDireccion(), c.getTelefono(), c.getEmail());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
    }

    private static void crearCliente(ClienteDAOImpl dao) {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine().trim();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("El nombre es obligatorio.");
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setDireccion(direccion);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);

            dao.insert(cliente);
            System.out.println("Cliente guardado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
        }
    }

    private static void eliminarCliente(ClienteDAOImpl dao) {
        try {
            System.out.print("ID del cliente a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            dao.delete(id);
            System.out.println("Cliente eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    // CRUD Producto
    private static void listarProductos(ProductoDAOImpl dao) {
        try {
            System.out.println("=== LISTA DE PRODUCTOS ===");
            List<Producto> productos = dao.findAll();
            if (productos.isEmpty()) {
                System.out.println("No hay productos registrados.");
            } else {
                for (Producto p : productos) {
                    System.out.printf("%d - %s | Precio: $%.2f | Stock: %d%n",
                            p.getIdProducto(), p.getNombre(), p.getPrecio(), p.getStock());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    private static void crearProducto(ProductoDAOImpl dao) {
        try {
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());

            if (nombre.isEmpty()) {
                System.out.println("El nombre es obligatorio.");
                return;
            }

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setStock(stock);

            dao.save(producto);
            System.out.println("Producto guardado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }

    private static void eliminarProducto(ProductoDAOImpl dao) {
        try {
            System.out.print("ID del producto a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            dao.delete(id);
            System.out.println("Producto eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    // Registrar venta
    private static void registrarVenta(VentaService ventaService) {
        try {
            System.out.print("ID del cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());

            Cliente cliente = new Cliente();
            cliente.setIdCliente(idCliente);

            List<DetalleVenta> detalles = new ArrayList<>();
            String continuar;

            do {
                System.out.print("ID del producto: ");
                int idProducto = Integer.parseInt(scanner.nextLine());

                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                Producto producto = new Producto();
                producto.setIdProducto(idProducto);

                detalles.add(new DetalleVenta(producto, cantidad));

                System.out.print("¿Agregar otro producto? (s/n): ");
                continuar = scanner.nextLine();
            } while (continuar.equalsIgnoreCase("s"));

            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setDetalles(detalles);
            venta.setFecha(new Date());
            venta.setEstadoEnvio("En proceso");

            ventaService.crearVenta(venta);
            System.out.println("Venta registrada con éxito.");
        } catch (Exception e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
        }
    }
}
