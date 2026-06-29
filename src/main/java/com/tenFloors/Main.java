package main.java.com.tenFloors;

// Importacion de clases y paquetes necesarios, los TDAs
import main.java.com.tenFloors.controlador.ControladorJuego;
import main.java.com.tenFloors.model.*;
import main.java.com.tenFloors.tda.cola.Cola;
import main.java.com.tenFloors.tda.conjunto.Conjunto;
import main.java.com.tenFloors.tda.pila.Pila;

// Importacion de Scanner para la lectura de entradas por consola
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in); // Scanner para leer entradas de consola
    private static final Pila<String> historialMenus = new Pila<>(); // Pila para mantener el historial de menús
    private static boolean running = true; // Flag para controlar la ejecución del programa
    private static ControladorJuego controlador; // Instancia del controlador del juego

    public static void main(String[] args) {
        controlador = new ControladorJuego(); // Inicialización del controlador del juego

        System.out.println("==================================================");
        System.out.println("             BIENVENIDO A TEN FLOORS             ");
        System.out.println("==================================================");

        historialMenus.apilar("PRINCIPAL"); // Apilamos el menú principal como el primer estado de navegación

        while (running && !historialMenus.estaVacia()) { // Mientras el programa esté corriendo y haya menús en el historial
            // Obtenemos el menú actual desde la cima de la pila
            String menuActual = historialMenus.verTope();

            // Dependiendo del menú actual, mostramos el menú correspondiente
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

        // Saludo de cierre del programa 
        System.out.println("\n[SISTEMA] Servidor cerrado correctamente.");
        scanner.close();
    }

    /**
     * Muestra el menú principal del sistema.
     */
    private static void showMainMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Gestión de Datos (Altas/Bajas/Mapeos)");
        System.out.println("2. Consultas Complejas (Hitos de Negocio)");
        System.out.println("3. Salir de Ten Floors");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion(); // Leemos la opción ingresada por el usuario
        switch (opcion) {
            case 1 -> historialMenus.apilar("GESTION_DATOS");
            case 2 -> historialMenus.apilar("CONSULTAS_COMPLEJAS");
            case 3 -> running = false; // Cambiamos el flag para salir del bucle principal
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Muestra el menú de gestión de datos.
     * Aca se pueden realizar operaciones de alta, baja, inspección y registro de misiones.
     */
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

        int opcion = leerOpcion(); // Leemos la opción ingresada por el usuario

        // Dependiendo de la opción seleccionada, ejecutamos la acción correspondiente
        switch (opcion) {
            case 1 -> {
                System.out.print("Ingrese el ID único de cuenta (ej. ACC-12): ");
                String id = scanner.nextLine().trim().toUpperCase(); // El ID siempre se maneja en mayúsculas para consistencia
                System.out.print("Ingrese el nombre del personaje: ");
                String nombre = scanner.nextLine().trim();
                System.out.print("Ingrese la clase del personaje (MAGO/ASESINO/ESPADACHIN): ");
                String clase = scanner.nextLine().trim().toUpperCase(); // La clase también se maneja en mayúsculas para consistencia

                // Validamos que las entradas no estén vacías antes de intentar dar de alta la cuenta
                if (!id.isEmpty() && !nombre.isEmpty() && !clase.isEmpty()) {
                    if (controlador.darAltaCuenta(id, nombre, clase)) { // Si la cuenta se da de alta correctamente, se imprime un mensaje de éxito
                        System.out.println("[AVL] Cuenta registrada e índice rebalanceado exitosamente.");
                    } else {
                        System.out.println("[ERROR] El ID de cuenta ya existe en el índice global.");
                    }
                } else {
                    System.out.println("[ERROR] Entradas vacías no permitidas.");
                }
            }
            case 2 -> {
                System.out.print("Ingrese el ID de la cuenta a dar de baja: ");
                String id = scanner.nextLine().trim().toUpperCase();

                // Validamos que el ID no esté vacío antes de intentar dar de baja la cuenta
                if (!id.isEmpty()) {
                    if (controlador.darBajaCuenta(id)) {
                        System.out.println("[AVL] Cuenta removida correctamente. Árbol auto-balanceado.");
                    } else {
                        System.out.println("[ERROR] La cuenta especificada no existe.");
                    }
                } else {
                    System.out.println("[ERROR] ID de cuenta vacío no permitido.");
                }
            }
            case 3 -> {
                System.out.print("Ingrese el ID de cuenta del jugador: ");
                String idC = scanner.nextLine().trim().toUpperCase();
                System.out.print("Ingrese ID de ítem del catálogo (ITM-701, ITM-702, ITM-703): ");
                String idI = scanner.nextLine().trim().toUpperCase();

                // Validamos que ambos IDs no estén vacíos antes de intentar agregar el ítem a la mochila
                if (idC.isEmpty() || idI.isEmpty()) {
                    System.out.println("[ERROR] ID de cuenta o ítem vacío no permitido.");
                    break;
                }

                // Si la cuenta y el ítem son válidos, se intenta agregar el ítem a la mochila del jugador
                int resultado = controlador.agregarItemMochila(idC, idI);

                // Segun el resultado de la operación, se imprime un mensaje correspondiente
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

                if (c != null) { // Si la cuenta existe, se imprime un resumen de su estado y contenido
                    System.out.println("\n==================================================");
                    System.out.println("AUDITORÍA DE CUENTA AVL: " + c.getJugador().getIdCuenta());
                    System.out.println("==================================================");
                    System.out.println("Personaje: " + c.getJugador().getNombre() + " | Nivel: " + c.getJugador().getNivel());
                    System.out.println("Ubicación Actual: Piso " + c.getJugador().getPisoActual());

                    System.out.println("\n-> Mochila del Jugador (Recorrido Inorden ABB):");
                    // Obtenemos los ítems de la mochila del jugador en orden inorden desde el ABB
                    Cola<Item> items = controlador.obtenerMochilaJugador(id);

                    if (items.estaVacia()) {
                        System.out.println("   [La mochila está vacía]");
                    } else {
                        while (!items.estaVacia()) {
                            Item item = items.desencolar();
                            System.out.println("   * " + item.toString());
                        }
                    }

                    System.out.println("\n-> Habilidades Desbloqueadas Automáticamente [CONJUNTO]:");
                    // Se verifica si el conjunto de habilidades aprendidas está vacío; si no, se listan las habilidades desbloqueadas
                    Cola<String> habilidades = controlador.obtenerHabilidadesJugador(id);

                    if (habilidades.estaVacia()) {
                        System.out.println("   [No cumple requisitos para ninguna habilidad de su clase]");
                    } else {
                        while (!habilidades.estaVacia()) {
                            String hab = habilidades.desencolar();
                            System.out.println("   ✓ " + hab);
                        }
                    }

                    System.out.println("\n-> Operaciones comerciales pendientes en Pila (Tamaño): " + c.getHistorialComercio().getTamanio());
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
                    System.out.println("[COLA PRIORIDAD] Misión integrada y reordenada en el pool global.");
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

                ControladorJuego.ResultadoDespacho res = controlador.procesarDespachoMision(idCuenta);

                if (!res.exito) {
                    if ("LA_CUENTA_NO_EXISTE".equals(res.motivoError)) {
                        System.out.println("[ERROR] La cuenta especificada no existe en el índice AVL global.");
                    } else if ("PISO_INSUFICIENTE".equals(res.motivoError)) {
                        System.out.println("\n==================================================");
                        System.out.println("PROCESANDO RECLAMO DE MISIÓN CRÍTICA");
                        System.out.println("==================================================");
                        System.out.println("Evaluando a : " + res.jugador.getNombre() + " (Piso Actual: " + res.jugador.getPisoActual() + ")");
                        System.out.println("Misión       : " + res.mision.getNombre() + " [" + res.mision.getTipo() + "]");
                        System.out.println("Requisito    : Piso " + res.mision.getPisoRequerido() + " de la torre.");
                        System.out.println("\n[RECHAZADO] El jugador no cumple con el piso requerido para esta misión.");
                        System.out.println("[SISTEMA] La misión se descarta por intento de fraude o nivel insuficiente.");
                        System.out.println("==================================================");
                    }
                } else {
                    System.out.println("\n==================================================");
                    System.out.println("PROCESANDO RECLAMO DE MISIÓN CRÍTICA");
                    System.out.println("==================================================");
                    System.out.println("Evaluando a : " + res.jugador.getNombre() + " (Piso Actual: " + res.jugador.getPisoActual() + ")");
                    System.out.println("Misión       : " + res.mision.getNombre() + " [" + res.mision.getTipo() + "]");
                    System.out.println("Requisito    : Piso " + res.mision.getPisoRequerido() + " de la torre.");

                    if (res.premioOtorgado != null) {
                        System.out.println("¡RECOMPENSA OTORGADA! Se añadió '" + res.premioOtorgado.toString() + "' a su mochila [ABB].");
                    } else {
                        System.out.println("[ALERTA] El ítem de recompensa no existe en el catálogo maestro.");
                    }
                    System.out.println("¡SUBIDA DE NIVEL! El personaje progresó: Lvl " + res.nivelAnterior + " -> Lvl " + res.nivelNuevo);
                    System.out.println("[SISTEMA] Árbol de habilidades re-evaluado para la clase: " + res.jugador.getClase());
                    System.out.println("==================================================");
                }
            }
            case 7 -> historialMenus.apilar("CONSULTAS_DATOS");
            case 8 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

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
            case 1 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 1 (VIAJE RÁPIDO Y PARTY) ---");
                Cola<Jugador> partyArmada = controlador.ejecutarHito1();
                System.out.println("\n==================================================");
                System.out.println("RESULTADO DE LA COLA DE PARTY (FIFO)");
                System.out.println("==================================================");
                if (partyArmada == null || partyArmada.estaVacia()) {
                    System.out.println("Nadie se unió a la Party.");
                } else {
                    int posicion = 1;
                    while (!partyArmada.estaVacia()) {
                        Jugador j = partyArmada.desencolar();
                        System.out.println("Slot " + posicion + ": " + j.getNombre() + " (Nivel " + j.getNivel() + ")");
                        posicion++;
                    }
                }
                System.out.println("==================================================");
            }
            case 2 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 2 (SOPORTE TÉCNICO VIP) ---");
                boolean exitoSoporte = controlador.ejecutarHito2("ITM-701");
                if (exitoSoporte) {
                    System.out.println("[SISTEMA] Operación de Soporte VIP finalizada correctamente.");
                } else {
                    System.out.println("[SISTEMA] No se pudo procesar ningún ticket.");
                }
            }
            case 3 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 3 (AUDITORÍA DE GREMIOS) ---");
                Gremio g = controlador.getGremioDemo();
                System.out.println("Datos Generales del " + g.toString());
                System.out.println("Visualización estructural por Consola (Preorden del TDA):");
                g.getEstructuraJerarquica().preorden();

                Conjunto<Jugador> lideresAudita = controlador.ejecutarHito3();

                System.out.println("\n==================================================");
                System.out.println("RESULTADO DE CONSOLIDACIÓN EN CONJUNTO TEMPORAL");
                System.out.println("==================================================");
                System.out.println("Total de líderes únicos validados y almacenados: " + lideresAudita.getTamanio());
                System.out.println("Verificación de Existencia de Claves:");
                System.out.println("   ¿Se consolidó al GM (ACC-77)?: " + lideresAudita.contiene(new Jugador("ACC-77", "", "")));
                System.out.println("   ¿Se consolidó al ID FANTASMA?: " + lideresAudita.contiene(new Jugador("ACC-FANTASMA", "", "")));
                System.out.println("==================================================");
            }
            case 4 -> {
                System.out.print("Ingrese el ID de la cuenta para revertir su última transacción (ej: ACC-77): ");
                String idBuscado = scanner.nextLine().trim().toUpperCase();
                System.out.println("\n--- EJECUCIÓN: HITO 4 (SISTEMA DE COMERCIO SEGURO) ---");
                if (controlador.ejecutarHito4(idBuscado)) {
                    System.out.println("[SISTEMA] Flujo completado de forma segura y exitosa.");
                } else {
                    System.out.println("[SISTEMA] Protocolo Comercio Seguro abortado / Falla de condiciones.");
                }
            }
            case 5 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

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
            case 1 -> {
                System.out.println("\n--- LISTA DE JUGADORES REGISTRADOS (ÁRBOL AVL) ---");
                Cola<Cuenta> cuentas = controlador.obtenerCuentasInorden();
                if (cuentas == null || cuentas.estaVacia()) {
                    System.out.println("No hay jugadores registrados.");
                } else {
                    while (!cuentas.estaVacia()) {
                        Cuenta c = cuentas.desencolar();
                        System.out.println("- ID: " + c.getJugador().getIdCuenta() +
                                " | Personaje: " + c.getJugador().getNombre() +
                                " | Nivel: " + c.getJugador().getNivel() +
                                " | Piso: " + c.getJugador().getPisoActual());
                    }
                }
            }
            case 2 -> {
                System.out.println("\n--- CATÁLOGO GLOBAL DE ÍTEMS (ÁRBOL ABB) ---");
                Cola<Item> itemsCatalogo = controlador.obtenerItemsInorden();
                if (itemsCatalogo == null || itemsCatalogo.estaVacia()) {
                    System.out.println("El catálogo está vacío.");
                } else {
                    while (!itemsCatalogo.estaVacia()) {
                        Item item = itemsCatalogo.desencolar();
                        System.out.println("- " + item.toString());
                    }
                }
            }
            case 3 -> {
                System.out.println("\n--- RECORRIDO EN ANCHURA DEL MUNDO ---");
                System.out.println("Iniciando exploración síncrona desde el nodo raíz...");
                controlador.getMapaGlobal().bfs("Pueblo de los Inicios");
                System.out.println("\n[GRAFO] Recorrido de adyacencias completado.");
            }
            case 4 -> {
                System.out.print("Ingrese el ID numérico de la transacción a buscar (Pruebe con 90001 o 90002): ");
                try {
                    String input = scanner.nextLine().trim();
                    long idTransaccion = Long.parseLong(input);
                    System.out.println("\n--- CONSULTA DE REGISTROS MASIVOS (ÁRBOL B) ---");
                    Transaccion tx = controlador.buscarTransaccionArbolB(idTransaccion);

                    if (tx != null) {
                        System.out.println("\n==================================================");
                        System.out.println("TRANSACCIÓN ENCONTRADA EN ÁRBOL B");
                        System.out.println("==================================================");
                        System.out.println("ID Transacción : " + tx.getId());
                        System.out.println("ID del Ítem    : " + tx.getItem());
                        System.out.println("Precio Oro     : " + tx.getPrecioFinal() + "g");
                        System.out.println("Timestamp      : " + tx.getFechaFormateada());
                        System.out.println("==================================================");
                    } else {
                        System.out.println("[ÁRBOL B] No se encontró ninguna transacción con ese ID.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] El ID de transacción debe ser un número entero largo (Long).");
                }
            }
            case 5 -> {
                System.out.println("\n--- ÁRBOL DE PROGRESIÓN DE CLASES Y HABILIDADES (PREORDEN) ---");
                System.out.println("Visualización jerárquica de la rama de talentos:");
                controlador.getArbolHabilidadesGlobal().preorden();
                System.out.println("\n[ÁRBOL GENÉRICO] Exploración estructural finalizada.");
            }
            case 6 -> {
                System.out.println("\n--- ÁRBOL DE PROGRESIÓN DE CLASES Y HABILIDADES (POSTORDEN) ---");
                System.out.println("Orden de ejecución / cálculo de dependencias de habilidades:");
                controlador.getArbolHabilidadesGlobal().postorden();
                System.out.println("\n[ÁRBOL GENÉRICO] Exploración de dependencias finalizada.");
            }
            case 7 -> {
                int total = controlador.getCantidadMisionesPendientes();
                System.out.println("[COLA PRIORIDAD] Misiones actualmente activas en memoria: " + total);
            }
            case 8 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    private static int leerOpcion() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}