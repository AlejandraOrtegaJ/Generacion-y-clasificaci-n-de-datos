package GeneraciónClasificaciónDatos;

import java.io.*;
import java.util.*;

public class FileProcessor {

    // Método para leer el archivo de vendedores
    public static Map<Long, String> loadSalesMen(String filePath) {
        Map<Long, String> salesMen = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                long id = Long.parseLong(parts[1]);  // NúmeroDocumento
                String fullName = parts[2] + " " + parts[3];  // NombresVendedor ApellidosVendedor
                salesMen.put(id, fullName);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de vendedores: " + e.getMessage());
        }
        return salesMen;
    }

    // Método para leer el archivo de productos y almacenar la información de productos en un mapa
    public static Map<Integer, Double> loadProducts(String filePath) {
        Map<Integer, Double> products = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int productId = Integer.parseInt(parts[0]);  // IDProducto
                String priceWithComma = parts[2];            // Precio con coma
                double price = Double.parseDouble(priceWithComma.replace(",", "."));  // Reemplaza la coma por un punto
                products.put(productId, price);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de productos: " + e.getMessage());
        }
        return products;
    }

    // Método para procesar las ventas de los vendedores
    public static Map<Long, Double> processSales(String folderPath, Map<Integer, Double> productPrices) {
        Map<Long, Double> salesData = new HashMap<>();
        File folder = new File(folderPath);
        File[] salesFiles = folder.listFiles((dir, name) -> name.startsWith("vendedor_"));

        if (salesFiles == null || salesFiles.length == 0) {
            System.out.println("No se encontraron los archivos de ventas.");
            return salesData;
        }

        for (File salesFile : salesFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
                String line = reader.readLine();  // Leer la primera línea (ID del vendedor)
                if (line != null) {
                    String[] vendedorInfo = line.split(";");
                    long vendedorId = Long.parseLong(vendedorInfo[1]);  // ID del vendedor

                    double totalSales = 0.0;

                    // Leer las ventas del vendedor
                    while ((line = reader.readLine()) != null) {
                        String[] salesInfo = line.split(";");
                        int productId = Integer.parseInt(salesInfo[0]);  // ID del producto
                        int quantitySold = Integer.parseInt(salesInfo[1]);  // Cantidad vendida

                        if (productPrices.containsKey(productId)) {
                            totalSales += productPrices.get(productId) * quantitySold;
                        }
                    }

                    // Agregar las ventas al vendedor
                    salesData.put(vendedorId, salesData.getOrDefault(vendedorId, 0.0) + totalSales);
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de ventas: " + e.getMessage());
            }
        }

        return salesData;
    }

    // Método para generar el archivo de reporte de ventas por vendedor
    public static void generateSalesReport(String filePath, Map<Long, String> salesMen, Map<Long, Double> salesData) {
        List<Map.Entry<Long, Double>> sortedSales = new ArrayList<>(salesData.entrySet());
        sortedSales.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));  // Orden descendente

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<Long, Double> entry : sortedSales) {
                long salesmanId = entry.getKey();
                double totalSales = entry.getValue();
                writer.write(salesMen.get(salesmanId) + ";" + String.format("%.2f", totalSales));
                writer.newLine();
            }
            System.out.println("Archivo de reporte de ventas por vendedor fue generado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte: " + e.getMessage());
        }
    }

    // Método para generar el archivo de reporte de productos vendidos
    public static void generateProductReport(String filePath, Map<Integer, Integer> productSales, Map<Integer, String> products) {
        List<Map.Entry<Integer, Integer>> sortedProducts = new ArrayList<>(productSales.entrySet());
        sortedProducts.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));  // Orden descendente

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<Integer, Integer> entry : sortedProducts) {
                int productId = entry.getKey();
                int totalSold = entry.getValue();
                writer.write(products.get(productId) + ";" + totalSold);
                writer.newLine();
            }
            System.out.println("Archivo de reporte de productos vendidos fue generado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte de productos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Cargar información de vendedores y productos
        Map<Long, String> salesMen = loadSalesMen("informacion_vendedores.txt");
        Map<Integer, Double> products = loadProducts("productos.txt");

        // Procesar las ventas
        Map<Long, Double> salesData = processSales(".", products);

        // Generar reportes
        generateSalesReport("reporte_vendedores.txt", salesMen, salesData);
     // Generación del reporte de productos vendidos: esta parte está pendiente de implementación.
     // El método generateProductReport debe procesar las ventas por producto y generar un reporte 
     // similar al de ventas por vendedor. Esta tarea será completada en fases posteriores.


    }
}
