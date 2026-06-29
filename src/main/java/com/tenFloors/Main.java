package main.java.com.tenFloors;

import main.java.com.tenFloors.model.*;
import main.java.com.tenFloors.tda.cola.Cola;
import main.java.com.tenFloors.tda.pila.Pila;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Pila<String> historialMenus = new Pila<>();
    private static boolean running = true;

    // Instancia única del Controlador del Juego según patrón GRASP
    private static ControladorJuego controlador;

    public static void main(String[] args) {
        // Inicializamos el controlador maestro
        controlador = new ControladorJuego();

        System.out.println("==================================================");
        System.out.println("             BIENVENIDO A TEN FLOORS             ");
        System.out.println("==================================================");

        historialMenus.apilar("PRINCIPAL");

        // Bucle principal de ejecución de consola
        while (running && !historialMenus.estaVacia()) {
            String menuActual = historialMenus.verTope();

            switch (menuActual) {
                case "PRINCIPAL" -> showMainMenu();
                case "GESTION_DATOS" -> showGestionDatosMenu();
                case "CONSULTAS_COMPLEJAS" -> showConsultasComplejasMenu();
                case "CONSULTAS_DATOS" -> showConsultasDatosMenu();
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
        System.out.println("4. Ver Estado e Inspección de Cuenta (AVL + ABB + Pila)");
        System.out.println("5. Registrar Nueva Misión (Cola de Prioridad)");
        System.out.println("6. Procesar Siguiente Misión Más Urgente (Despachar)");
        System.out.println("7. Consultas (Panel de Inspección de TDAs)");
        System.out.println("8. <- Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> {
                System.out.print("Ingrese el ID único de cuenta (ej. ACC-12): ");
                String id = scanner.nextLine().trim().toUpperCase();
                System.out.print("Ingrese el nombre del personaje: ");
                String nombre = scanner.nextLine().trim();
                System.out.println("Ingrese la clase del presonaje (MAGO/ASESINO/ESPADACHIN): ");
                String clase = scanner.nextLine().trim().toUpperCase();
                if (!id.isEmpty() && !nombre.isEmpty()) {
                    controlador.darAltaCuenta(id, nombre, clase);
                } else {
                    System.out.println("[ERROR] Entradas vacías no permitidas.");
                }
            }
            case 2 -> {
                System.out.print("Ingrese el ID de la cuenta a dar de baja: ");
                String id = scanner.nextLine().trim();
                if (controlador.darBajaCuenta(id)) {
                    System.out.println("[AVL] Cuenta removida correctamente. Árbol auto-balanceado.");
                } else {
                    System.out.println("[ERROR] La cuenta especificada no existe.");
                }
            }
            case 3 -> {
                System.out.print("Ingrese el ID de cuenta del jugador: ");
                String idC = scanner.nextLine().trim();
                System.out.print("Ingrese ID de ítem del catálogo (ITM-701, ITM-702, ITM-703): ");
                String idI = scanner.nextLine().trim();

                int resultado = controlador.agregarItemMochila(idC, idI);
                switch (resultado) {
                    case 0 -> System.out.println("[ABB] Ítem indexado en el inventario del jugador.");
                    case 1 -> System.out.println("[ERROR] Cuenta no encontrada.");
                    case 2 -> System.out.println("[ERROR] Ítem inexistente en el catálogo global ABB.");
                }
            }
            case 4 -> {
                System.out.print("Ingrese ID de cuenta a inspeccionar (Pruebe con 'ACC-77'): ");
                String id = scanner.nextLine().trim();
                Cuenta c = controlador.buscarCuenta(id);
                if (c != null) {
                    System.out.println("\n==================================================");
                    System.out.println("AUDITORÍA DE CUENTA AVL: " + c.getJugador().getIdCuenta());
                    System.out.println("==================================================");
                    System.out.println("Personaje: " + c.getJugador().getNombre() + " | Nivel: " + c.getJugador().getNivel());
                    System.out.println("Ubicación Actual: Piso " + c.getJugador().getPisoActual());

                    System.out.println("\n-> Mochila del Jugador (Recorrido Inorden ABB):");
                    Cola<Item> items = c.getInventario().obtenerInorden();
                    if (items.estaVacia()) {
                        System.out.println("   [La mochila está vacía]");
                    } else {
                        while (!items.estaVacia()) {
                            Item item = items.desencolar();
                            System.out.println("   * " + item.toString());
                        }
                    }

                    System.out.println("\n-> Habilidades Desbloqueadas Automáticamente [CONJUNTO]:");
                    if (c.getJugador().getHabilidadesAprendidas().estaVacio()) {
                        System.out.println("   [No cumple requisitos para ninguna habilidad de su clase]");
                    } else {
                        String[] habilidadesCatalogo = {
                                "Estocada Certera", "Torbellino de Espadas", "Golpe de Escudo", "Maestría en Espadas", "Reflejos de Acero", "Coraza de Titán",
                                "Bola de Fuego", "Ventisca Helada", "Impacto Trueno",
                                "Emboscada", "Hoja Envenenada", "Ejecución", "Paso Sombrío", "Manto de Invisibilidad"
                        };
                        for (String hab : habilidadesCatalogo) {
                            if (c.getJugador().getHabilidadesAprendidas().contiene(hab)) {
                                System.out.println("   ✓ " + hab);
                            }
                        }
                    }

                    System.out.println("\n-> Operaciones comerciales pendientes en Pila (Tamanio): " + c.getHistorialComercio().getTamanio());
                    System.out.println("==================================================");
                } else {
                    System.out.println("[ERROR] No se encontró ninguna cuenta asociada a ese ID.");
                }
            }
            case 5 -> {
                System.out.println("\n--- REGISTRAR NUEVA MISIÓN ---");
                System.out.print("Ingrese ID numérico de la misión: ");
                int idM = leerOpcion();
                System.out.print("Ingrese nombre de la misión: ");
                String nomM = scanner.nextLine().trim();
                System.out.print("Ingrese descripción detallada: ");
                String descM = scanner.nextLine().trim();
                System.out.println("Seleccione el Tipo de Misión:");
                System.out.println("  1. RECOLECCION_ESTANDAR (Prioridad Baja)");
                System.out.println("  2. EVENTO_TEMPORAL     (Prioridad Media)");
                System.out.println("  3. JEFE_MUNDO          (Prioridad Crítica)");
                System.out.print("Opción: ");
                int tipoOpt = leerOpcion();

                Mision.TipoMision tipoM = switch (tipoOpt) {
                    case 2 -> Mision.TipoMision.EVENTO_TEMPORAL;
                    case 3 -> Mision.TipoMision.JEFE_MUNDO;
                    default -> Mision.TipoMision.RECOLECCION_ESTANDAR;
                };

                System.out.print("Ingrese el piso mínimo requerido de la torre: ");
                int pisoM = leerOpcion();

                System.out.print("Ingrese el ID del Ítem de recompensa (ej. ITM-701): ");
                String recompensaM = scanner.nextLine().trim().toUpperCase();

                if (idM > 0 && !nomM.isEmpty() && !recompensaM.isEmpty()) {
                    controlador.registrarNuevaMision(idM, nomM, descM, tipoM, pisoM, recompensaM);
                } else {
                    System.out.println("[ERROR] Datos inválidos o recompensa vacía. Cancelando alta.");
                }
            }
            case 6 -> {
                System.out.println("\n--- SISTEMA DE DESPACHO Y COMPLETITUD DE MISIONES ---");
                if (controlador.getCantidadMisionesPendientes() == 0) {
                    System.out.println("[SISTEMA] No hay misiones activas pendientes en el registro.");
                    break;
                }
                System.out.print("Ingrese el ID de la cuenta del jugador que completó la hazaña (ej. ACC-77): ");
                String idCuenta = scanner.nextLine().trim().toUpperCase();
                controlador.procesarDespachoMision(idCuenta);
            }
            case 7 -> historialMenus.apilar("CONSULTAS_DATOS");
            case 8 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    // --- MENÚ 2: CONSULTAS COMPLEJAS ---
    private static void showConsultasComplejasMenu() {
        System.out.println("\n--- CONSULTAS COMPLEJAS (HITOS GRUPALES) ---");
        System.out.println("1. Hito 1: Viaje Rápido y Formación de Party");
        System.out.println("2. Hito 2: Soporte Técnico VIP");
        System.out.println("3. Hito 3: Auditoría de Gremios");
        System.out.println("4. Hito 4: Sistema de Comercio Seguro (Ejecutar Rollback)");
        System.out.println("5. <- Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> controlador.ejecutarHito1();
            case 2 -> controlador.ejecutarHito2("ITM-701");
            case 3 -> controlador.ejecutarHito3();
            case 4 -> {
                System.out.print("Ingrese el ID de la cuenta para revertir su última transacción (ej: ACC-77): ");
                String idBuscado = scanner.nextLine().trim();
                controlador.ejecutarHito4(idBuscado);
            }
            case 5 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    // --- MENÚ 3: PANEL DE INSPECCIÓN DE TDAs ---
    private static void showConsultasDatosMenu() {
        System.out.println("\n--- PANEL DE INSPECCIÓN DE TDAs ---");
        System.out.println("1. Ver todos los jugadores (En orden) [AVL]");
        System.out.println("2. Ver catálogo global de ítems (En orden) [ABB]");
        System.out.println("3. Ver conectividad del mapa (Recorrido BFS) [GRAFO]");
        System.out.println("4. Buscar transacción en la Casa de Subastas [ÁRBOL B]");
        System.out.println("5. Ver Árbol de Habilidades (Estructurado - Preorden) [ÁRBOL GENÉRICO]");
        System.out.println("6. Ver Árbol de Habilidades (Dependencias - Postorden) [ÁRBOL GENÉRICO]");
        System.out.println("7. Ver cantidad de misiones globales pendientes [COLA PRIORIDAD]");
        System.out.println("8. <- Volver al menú anterior");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> controlador.mostrarJugadoresAVL();
            case 2 -> controlador.mostrarItemsABB();
            case 3 -> controlador.mostrarConectividadGrafo();
            case 4 -> {
                System.out.print("Ingrese el ID numérico de la transacción a buscar (Pruebe con 90001 o 90002): ");
                try {
                    String input = scanner.nextLine().trim();
                    long idTransaccion = Long.parseLong(input);
                    controlador.buscarTransaccionArbolB(idTransaccion);
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] El ID de transacción debe ser un número entero largo (Long).");
                }
            }
            case 5 -> controlador.mostrarHabilidadesPreorden();
            case 6 -> controlador.mostrarHabilidadesPostorden();
            case 7 -> {
                int total = controlador.getCantidadMisionesPendientes();
                System.out.println("[COLA PRIORIDAD] Misiones actualmente activas en memoria: " + total);
            }
            case 8 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    // --- METODO DE VALIDACIÓN DE ENTRADA ---
    private static int leerOpcion() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}