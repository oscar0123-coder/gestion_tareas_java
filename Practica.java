import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class Tarea {
    private int id;
    private String nombre;
    private String descripcion;
    private String estado;

    public Tarea(int id, String nombre, String descripcion , String estado ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

public class Practica {
    private static ArrayList<Tarea> tareas = new ArrayList<>();
    private static int idTarea = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        cargarTareasDesdeArchivo();

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Consultar tareas");
            System.out.println("3. Modificar tarea");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Salir");
            System.out.print("Ingrese su opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        agregarTarea();
                        break;
                    case 2:
                        consultarTareas();
                        break;
                    case 3:
                        modificarTarea();
                        break;
                    case 4:
                        eliminarTarea();
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        guardarTareasEnArchivo();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        }
    }

    static void agregarTarea() {
        System.out.print("Ingrese el nombre de la tarea: ");
        String nombre = scanner.nextLine();
        if (nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Ingrese la descripción de la tarea: ");
        String descripcion = scanner.nextLine();
        if (descripcion.trim().isEmpty()) {
            System.out.println("La descripción no puede estar vacía.");
            return;
        }

        String estado;
        while (true) {
            System.out.println("Ingrese el estado de la tarea (pendiente, en progreso, completado): ");
            estado = scanner.nextLine().toLowerCase();
            if (estado.equals("pendiente") || estado.equals("en progreso") || estado.equals("completado")) {
                break;
            } else {
                System.out.println("Estado inválido. Por favor ingrese: pendiente, en progreso o completado.");
            }
        }

        Tarea tarea = new Tarea(idTarea++, nombre, descripcion, estado);
        tareas.add(tarea);
        guardarTareasEnArchivo();
        System.out.println("Tarea agregada con éxito");
    }

    static void consultarTareas() {
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas registradas");
        } else {
            for (Tarea tarea : tareas) {
                System.out.println("ID: " + tarea.getId());
                System.out.println("Nombre: " + tarea.getNombre());
                System.out.println("Descripción: " + tarea.getDescripcion());
                System.out.println("Estado: " + tarea.getEstado());
                System.out.println();
            }
        }
    }

    static void modificarTarea() {
        System.out.print("Ingrese el ID de la tarea a modificar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            for (Tarea tarea : tareas) {
                if (tarea.getId() == id) {
                    String estado;
                    while (true) {
                        System.out.println("Ingrese el nuevo estado (pendiente, en progreso, completado): ");
                        estado = scanner.nextLine().toLowerCase();
                        if (estado.equals("pendiente") || estado.equals("en progreso") || estado.equals("completado")) {
                            break;
                        } else {
                            System.out.println("Estado inválido. Por favor ingrese: pendiente, en progreso o completado.");
                        }
                    }

                    tarea.setEstado(estado);
                    guardarTareasEnArchivo();
                    System.out.println("Tarea modificada con éxito");
                    return;
                }
            }
            System.out.println("Tarea no encontrada");
        } catch (Exception e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            scanner.nextLine();
        }
    }

    static void eliminarTarea() {
        System.out.print("Ingrese el ID de la tarea a eliminar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            boolean eliminada = tareas.removeIf(tarea -> tarea.getId() == id);
            if (eliminada) {
                guardarTareasEnArchivo();
                System.out.println("Tarea eliminada con éxito");
            } else {
                System.out.println("Tarea no encontrada");
            }
        } catch (Exception e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            scanner.nextLine();
        }
    }

    static void guardarTareasEnArchivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tareas.txt"))) {
            for (Tarea tarea : tareas) {
                writer.println(tarea.getId() + ";" + tarea.getNombre() + ";" + tarea.getDescripcion() + ";" + tarea.getEstado());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar las tareas: " + e.getMessage());
        }
    }

    static void cargarTareasDesdeArchivo() {
        File archivo = new File("tareas.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    int id = Integer.parseInt(partes[0]);
                    String nombre = partes[1];
                    String descripcion = partes[2];
                    String estado = partes[3];
                    tareas.add(new Tarea(id, nombre, descripcion, estado));
                    if (id >= idTarea) {
                        idTarea = id + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las tareas: " + e.getMessage());
        }
    }
}
