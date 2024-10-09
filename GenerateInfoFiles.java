package generador.archivos; 

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    // Método para generar un archivo con información de un vendedor y sus ventas
    public static void createSalesMenFile(int randomSalesCount, String name, long id) {
        String fileName = "vendedor_" + id + ".txt"; // Nombre del archivo del vendedor
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("CC;" + id + "\n"); // Escribe el tipo de documento y el número de documento
            Random rand = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                int productId = rand.nextInt(100) + 1; // ID de producto entre 1 y 100
                int productQuantity = rand.nextInt(20) + 1; // Cantidad vendida entre 1 y 20
                writer.write(productId + ";" + productQuantity + ";\n"); // Escribe el producto y cantidad
            }
            System.out.println("Archivo generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de vendedor: " + e.getMessage());
        }
    }

    // Método para generar un archivo de productos
    public static void createProductsFile(int productsCount) {
        String fileName = "productos.txt"; // Nombre del archivo de productos
        try (FileWriter writer = new FileWriter(fileName)) {
            Random rand = new Random();
            for (int i = 0; i < productsCount; i++) {
                int productId = i + 1; // ID único para el producto
                String productName = "Producto_" + productId; // Nombre del producto
                double productPrice = rand.nextDouble() * 100; // Precio aleatorio entre 0 y 100
                writer.write(productId + ";" + productName + ";" + String.format("%.2f", productPrice) + "\n");
            }
            System.out.println("Archivo de productos generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de productos: " + e.getMessage());
        }
    }

    // Método para generar un archivo con la información de los vendedores
    public static void createSalesManInfoFile(int salesmanCount) {
        String fileName = "informacion_vendedores.txt"; // Nombre del archivo de información de vendedores
        try (FileWriter writer = new FileWriter(fileName)) {
            String[] nombres = {"Leonardo", "Alejandra", "Alexis", "Ana", "Sofia"};
            String[] apellidos = {"Ortega", "Gómez", "Rodríguez", "López", "Martínez"};
            Random rand = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                String nombre = nombres[rand.nextInt(nombres.length)];
                String apellido = apellidos[rand.nextInt(apellidos.length)];
                long id = rand.nextInt(100000000); // ID aleatorio para el vendedor
                writer.write("CC;" + id + ";" + nombre + ";" + apellido + "\n"); // Escribe la información del vendedor
            }
            System.out.println("Archivo de información de vendedores generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de vendedores: " + e.getMessage());
        }
    }

    // Método principal para generar todos los archivos
    public static void main(String[] args) {
        // Genera el archivo de un vendedor con 10 ventas
        createSalesMenFile(10, "Leonardo", 123456789);
        
        // Genera el archivo de productos con 5 productos
        createProductsFile(5);
        
        // Genera el archivo de información de 3 vendedores
        createSalesManInfoFile(3);
    }
}