import java.io.*;
import java.util.*;

public class RegistroGastos {
    private static final String ARCHIVO_GASTOS = "gastos.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Registro de Gastos Personales ---");
            System.out.println("1. Añadir gasto");
            System.out.println("2. Ver todos los gastos");
            System.out.println("3. Calcular total de gastos");
            System.out.println("4. Ver gastos por categoría");
            System.out.println("5. Editar un gasto");
            System.out.println("6. Eliminar un gasto");
            System.out.println("7. Buscar gastos por rango de fechas");
            System.out.println("8. Exportar gastos a archivo CSV");
            System.out.println("9. Mostrar estadísticas básicas");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            switch (opcion) {
                case 1:
                    anadirGasto(scanner);
                    break;
                case 2:
                    verGastos();
                    break;
                case 3:
                    calcularTotalGastos();
                    break;
                case 4:
                    verGastosPorCategoria(scanner);
                    break;
                case 5:
                    editarGasto(scanner);
                    break;
                case 6:
                    eliminarGasto(scanner);
                    break;
                case 7:
                    buscarGastosPorFecha(scanner);
                    break;
                case 8:
                    exportarGastosCSV();
                    break;
                case 9:
                    mostrarEstadisticas();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        scanner.close();
    }

    private static void anadirGasto(Scanner scanner) {
        System.out.print("Introduce la fecha (DD/MM/YYYY): ");
        String fecha = scanner.nextLine();
        System.out.print("Introduce la categoría: ");
        String categoria = scanner.nextLine();
        System.out.print("Introduce la descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Introduce la cantidad: ");
        double cantidad = scanner.nextDouble();
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_GASTOS, true))) {
            writer.println(fecha + "," + categoria + "," + descripcion + "," + cantidad);
            System.out.println("Gasto registrado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al registrar el gasto: " + e.getMessage());
        }
    }

    private static void verGastos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            System.out.println("\n--- Todos los Gastos ---");
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                System.out.println("Fecha: " + partes[0] + ", Categoría: " + partes[1] +
                        ", Descripción: " + partes[2] + ", cantidad: $" + partes[3]);
            }
        } catch (IOException e) {
            System.out.println("Error al leer los gastos: " + e.getMessage());
        }
    }

    private static void calcularTotalGastos() {
        double total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                total += Double.parseDouble(partes[3]);
            }
            System.out.println("Total de gastos: $" + total);
        } catch (IOException e) {
            System.out.println("Error al calcular el total de gastos: " + e.getMessage());
        }
    }

    private static void verGastosPorCategoria(Scanner scanner) {
        System.out.print("Introduce la categoría a buscar: ");
        String categoriaBuscada = scanner.nextLine().toLowerCase();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            boolean encontrado = false;
            System.out.println("\n--- Gastos de la categoría '" + categoriaBuscada + "' ---");
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[1].toLowerCase().equals(categoriaBuscada)) {
                    System.out.println("Fecha: " + partes[0] + ", Descripción: " + partes[2] + ", cantidad: $" + partes[3]);
                    encontrado = true;
                }
            }
            if (!encontrado) {
                System.out.println("No se encontraron gastos en esta categoría.");
            }
        } catch (IOException e) {
            System.out.println("Error al buscar gastos por categoría: " + e.getMessage());
        }
    }

    private static void editarGasto(Scanner scanner) {
        System.out.print("Introduce la fecha del gasto a editar (DD/MM/YYYY): ");
        String fechaBusqueda = scanner.nextLine();
        List<String> gastos = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].equals(fechaBusqueda)) {
                    encontrado = true;
                    System.out.println("Introduce la nueva categoría: ");
                    partes[1] = scanner.nextLine();
                    System.out.println("Introduce la nueva descripción: ");
                    partes[2] = scanner.nextLine();
                    System.out.println("Introduce la nueva cantidad: ");
                    partes[3] = String.valueOf(scanner.nextDouble());
                }
                gastos.add(String.join(",", partes));
            }
        } catch (IOException e) {
            System.out.println("Error al editar el gasto: " + e.getMessage());
        }

        if (encontrado) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_GASTOS))) {
                for (String gasto : gastos) {
                    writer.println(gasto);
                }
                System.out.println("Gasto editado correctamente.");
            } catch (IOException e) {
                System.out.println("Error al guardar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró un gasto en la fecha proporcionada.");
        }
    }

    private static void eliminarGasto(Scanner scanner) {
        System.out.print("Introduce la fecha del gasto a eliminar (DD/MM/YYYY): ");
        String fechaBusqueda = scanner.nextLine();
        List<String> gastos = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (!partes[0].equals(fechaBusqueda)) {
                    gastos.add(linea);
                } else {
                    encontrado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar el gasto: " + e.getMessage());
        }

        if (encontrado) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_GASTOS))) {
                for (String gasto : gastos) {
                    writer.println(gasto);
                }
                System.out.println("Gasto eliminado correctamente.");
            } catch (IOException e) {
                System.out.println("Error al guardar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró un gasto en la fecha proporcionada.");
        }
    }

    private static void buscarGastosPorFecha(Scanner scanner) {
        System.out.print("Introduce la fecha inicial (DD/MM/YYYY): ");
        String fechaInicial = scanner.nextLine();
        System.out.print("Introduce la fecha final (DD/MM/YYYY): ");
        String fechaFinal = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            System.out.println("\n--- Gastos entre " + fechaInicial + " y " + fechaFinal + " ---");
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].compareTo(fechaInicial) >= 0 && partes[0].compareTo(fechaFinal) <= 0) {
                    System.out.println("Fecha: " + partes[0] + ", Categoría: " + partes[1] +
                            ", Descripción: " + partes[2] + ", cantidad: $" + partes[3]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar los gastos: " + e.getMessage());
        }
    }

    private static void exportarGastosCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS));
             PrintWriter writer = new PrintWriter(new FileWriter("gastos_exportados.csv"))) {
            String linea;
            writer.println("Fecha,Categoría,Descripción,Cantidad");
            while ((linea = reader.readLine()) != null) {
                writer.println(linea);
            }
            System.out.println("Gastos exportados correctamente a gastos_exportados.csv");
        } catch (IOException e) {
            System.out.println("Error al exportar los gastos: " + e.getMessage());
        }
    }

    private static void mostrarEstadisticas() {
        int cantidadGastos = 0;
        double totalGastos = 0;
        double mayorGasto = Double.MIN_VALUE;
        double menorGasto = Double.MAX_VALUE;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                double cantidad = Double.parseDouble(partes[3]);
                totalGastos += cantidad;
                cantidadGastos++;

                if (cantidad > mayorGasto) {
                    mayorGasto = cantidad;
                }

                if (cantidad < menorGasto) {
                    menorGasto = cantidad;
                }
            }

            if (cantidadGastos > 0) {
                double promedio = totalGastos / cantidadGastos;
                System.out.println("Total de gastos registrados: " + cantidadGastos);
                System.out.println("Suma total de gastos: $" + totalGastos);
                System.out.println("Mayor gasto registrado: $" + mayorGasto);
                System.out.println("Menor gasto registrado: $" + menorGasto);
                System.out.println("Gasto promedio: $" + promedio);
            } else {
                System.out.println("No hay gastos registrados.");
            }
        } catch (IOException e) {
            System.out.println("Error al mostrar estadísticas: " + e.getMessage());
        }
    }
}
