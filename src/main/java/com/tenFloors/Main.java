package main.java.com.tenFloors;

import main.java.com.tenFloors.tda.pila.Pila;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Pila<String> historialMenus = new Pila<>();
    private static boolean running = true;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("             BIENVENIDOS A TEN FLOORS             ");
        System.out.println("==================================================");

        // Iniciamos el historial con el estado principal
        historialMenus.apilar("PRINCIPAL");

        // Bucle principal
        while (running && !historialMenus.estaVacia()) {
            String menuActual = historialMenus.verTope();

            switch (menuActual) {
                case "PRINCIPAL" -> showMainMenu();
                case "GESTION_DATOS" -> showGestionDatosMenu();
                case "CONSULTAS_COMPLEJAS" -> showConsultasComplejasMenu();
                default -> {
                    System.out.println("[ERROR] Estado de navegación inválido. Reestableciendo...");
                    historialMenus.apilar("PRINCIPAL");
                }
            }
        }

        System.out.println("\n[SISTEMA] Servidor cerrado correctamente.");
        scanner.close();
    }

    // --- MENÚ PRINCIPAL ---
    private static void showMainMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Gestión de Datos (Altas/Bajas/Mapeos)");
        System.out.println("2. Consultas Complejas (Hitos de Negocio)");
        System.out.println("3. Salir de Ten Floors");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> historialMenus.apilar("GESTION_DATOS");
            case 2 -> historialMenus.apilar("CONSULTAS_COMPLEJAS");
            case 3 -> running = false;
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    // --- SUBMENÚ 1: GESTIÓN DE DATOS ---
    private static void showGestionDatosMenu() {
        System.out.println("\n--- GESTIÓN DE DATOS (ADMIN) ---");
        System.out.println("1. Dar de Alta Cuenta (Árbol AVL Global)");
        System.out.println("2. Dar de Baja Cuenta (Árbol AVL Global)");
        System.out.println("3. Agregar Ítem a Mochila de Jugador (ABB)");
        System.out.println("4. Eliminar Ítem de Mochila de Jugador (ABB)");
        System.out.println("5. Ver Historial de Subastas (Árbol B)");
        System.out.println("6. <- Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> {
                // TODO: Axel Mendoza - Lógica de inserción y balanceo en Árbol AVL
                System.out.println("[PLACEHOLDER] Ejecutando alta de cuenta en AVL...");
            }
            case 2 -> {
                // TODO: Axel Mendoza - Lógica de eliminación y balanceo en Árbol AVL
                System.out.println("[PLACEHOLDER] Ejecutando baja de cuenta en AVL...");
            }
            case 3 -> {
                // TODO: Axel Mendoza - Lógica de inserción en ABB (Inventario)
                System.out.println("[PLACEHOLDER] Insertando ítem en el ABB del jugador...");
            }
            case 4 -> {
                // TODO: Axel Mendoza - Lógica de eliminación en ABB (Inventario)
                System.out.println("[PLACEHOLDER] Eliminando ítem del ABB del jugador...");
            }
            case 5 -> {
                // TODO: Axel Mendoza - Lógica de búsqueda/recorrido en Árbol B
                System.out.println("[PLACEHOLDER] Mostrando historial de transacciones del Árbol B...");
            }
            case 6 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opcion invalida. Intente nuevamente.");
        }
    }

    // --- SUBMENÚ 2: CONSULTAS COMPLEJAS ---
    private static void showConsultasComplejasMenu() {
        System.out.println("\n--- CONSULTAS COMPLEJAS (HITOS GRUPALES) ---");
        System.out.println("1. Hito 1: Viaje Rápido y Formación de Party");
        System.out.println("2. Hito 2: Soporte Técnico VIP");
        System.out.println("3. Hito 3: Auditoría de Gremios");
        System.out.println("4. Hito 4: Sistema de Comercio Seguro");
        System.out.println("5. <- Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> {
                // TODO: Integración Hito 1 (Grafo BFS + Diccionario Online + Cola Estándar)
                System.out.println("[PLACEHOLDER] Ejecutando Hito 1...");
            }
            case 2 -> {
                // TODO: Integración Hito 2 (Cola Prioridad Heap + Árbol B + ABB)
                System.out.println("[PLACEHOLDER] Ejecutando Hito 2...");
            }
            case 3 -> {
                // TODO: Integración Hito 3 (Árbol N-ario + AVL + Diccionario Conjunto)
                System.out.println("[PLACEHOLDER] Ejecutando Hito 3...");
            }
            case 4 -> {
                // TODO: Integración Hito 4 (Pila Historial + ABB + AVL)
                System.out.println("[PLACEHOLDER] Ejecutando Hito 4...");
            }
            case 5 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    // --- METODO DE CONTROL Y VALIDACIÓN DE ENTRADA ---
    private static int leerOpcion() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1; // Captura errores de entrada (letras, vacíos) sin romper la ejecución
        }
    }
}