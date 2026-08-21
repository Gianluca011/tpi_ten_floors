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

                    // Si la cola de habilidades está vacía, se indica que no cumple requisitos; de lo contrario, se listan las habilidades desbloqueadas
                    if (habilidades.estaVacia()) {
                        System.out.println("   [No cumple requisitos para ninguna habilidad de su clase]");
                    } else {
                        while (!habilidades.estaVacia()) {
                            String hab = habilidades.desencolar();
                            System.out.println("   ✓ " + hab);
                        }
                    }

                    // Mostramos el tamaño de la pila de operaciones comerciales pendientes del jugador
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

                // Validamos que la opción de tipo de misión sea válida
                Mision.TipoMision tipoM = switch (tipoOpt) {
                    case 2 -> Mision.TipoMision.EVENTO_TEMPORAL;
                    case 3 -> Mision.TipoMision.JEFE_MUNDO;
                    default -> Mision.TipoMision.RECOLECCION_ESTANDAR;
                };

                System.out.print("Ingrese el piso mínimo requerido de la torre: ");
                int pisoM = leerOpcion();

                System.out.print("Ingrese el ID del Ítem de recompensa (ej. ITM-701): ");
                String recompensaM = scanner.nextLine().trim().toUpperCase(); // Validamos que el ID del ítem de recompensa esté en mayúsculas para consistencia

                // Validamos que los datos ingresados sean válidos antes de registrar la misión
                if (idM > 0 && !nomM.isEmpty() && !recompensaM.isEmpty()) {
                    controlador.registrarNuevaMision(idM, nomM, descM, tipoM, pisoM, recompensaM); // Registramos la misión en la cola de prioridad global
                    System.out.println("[COLA PRIORIDAD] Misión integrada y reordenada en el pool global.");
                } else {
                    System.out.println("[ERROR] Datos inválidos o recompensa vacía. Cancelando alta.");
                }
            }
            case 6 -> {
                System.out.println("\n--- SISTEMA DE DESPACHO Y COMPLETITUD DE MISIONES ---");
                // Verificamos si hay misiones pendientes antes de solicitar el ID de cuenta del jugador
                if (controlador.getCantidadMisionesPendientes() == 0) {
                    System.out.println("[SISTEMA] No hay misiones activas pendientes en el registro.");
                    break;
                }

                // Solicitamos al usuario que ingrese el ID de la cuenta del jugador que completó la misión
                System.out.print("Ingrese el ID de la cuenta del jugador que completó la hazaña (ej. ACC-77): ");
                String idCuenta = scanner.nextLine().trim().toUpperCase();

                // Procesamos el despacho de la misión para la cuenta especificada y obtenemos el resultado
                ControladorJuego.ResultadoDespacho res = controlador.procesarDespachoMision(idCuenta);

                // Dependiendo del resultado del despacho, mostramos mensajes de éxito o error
                if (!res.exito) {
                    if ("LA_CUENTA_NO_EXISTE".equals(res.motivoError)) { // Si la cuenta no existe, mostramos un mensaje de error correspondiente
                        System.out.println("[ERROR] La cuenta especificada no existe en el índice AVL global.");
                    } else if ("PISO_INSUFICIENTE".equals(res.motivoError)) { // Si el jugador no cumple con el piso requerido para la misión, mostramos un mensaje de rechazo
                        System.out.println("\n==================================================");
                        System.out.println("PROCESANDO RECLAMO DE MISIÓN CRÍTICA");
                        System.out.println("==================================================");
                        System.out.println("Evaluando a : " + res.jugador.getNombre() + " (Piso Actual: " + res.jugador.getPisoActual() + ")");
                        System.out.println("Misión       : " + res.mision.getNombre() + " [" + res.mision.getTipo() + "]");
                        System.out.println("Requisito    : Piso " + res.mision.getPisoRequerido() + " de la torre.");
                        System.out.println("\n[RECHAZADO] El jugador no cumple con el piso requerido para esta misión.");
                        System.out.println("[SISTEMA] La misión se descarta por nivel insuficiente.");
                        System.out.println("==================================================");
                    }
                } else {
                    // Si el despacho fue exitoso, mostramos los detalles de la misión completada y la recompensa otorgada
                    System.out.println("\n==================================================");
                    System.out.println("PROCESANDO RECLAMO DE MISIÓN CRÍTICA");
                    System.out.println("==================================================");
                    System.out.println("Evaluando a : " + res.jugador.getNombre() + " (Piso Actual: " + res.jugador.getPisoActual() + ")");
                    System.out.println("Misión       : " + res.mision.getNombre() + " [" + res.mision.getTipo() + "]");
                    System.out.println("Requisito    : Piso " + res.mision.getPisoRequerido() + " de la torre.");

                    if (res.premioOtorgado != null) { // Si la recompensa existe en el catálogo maestro, se muestra un mensaje de éxito y se indica que se añadió a la mochila del jugador
                        System.out.println("¡RECOMPENSA OTORGADA! Se añadió '" + res.premioOtorgado.toString() + "' a su mochila [ABB].");
                    } else {
                        // Si la recompensa no existe en el catálogo maestro, se muestra un mensaje de alerta
                        System.out.println("[ALERTA] El ítem de recompensa no existe en el catálogo maestro.");
                    }
                    System.out.println("¡SUBIDA DE NIVEL! El personaje progresó: Lvl " + res.nivelAnterior + " -> Lvl " + res.nivelNuevo);
                    System.out.println("[SISTEMA] Árbol de habilidades re-evaluado para la clase: " + res.jugador.getClase());
                    System.out.println("==================================================");
                }
            }
            case 7 -> historialMenus.apilar("CONSULTAS_DATOS"); // Navegamos al menú de consultas de datos
            case 8 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Muestra el menú de consultas complejas (hitos grupales).
     */
    private static void showConsultasComplejasMenu() {
        System.out.println("\n--- CONSULTAS COMPLEJAS (HITOS GRUPALES) ---");
        System.out.println("1. Hito 1: Viaje Rápido y Formación de Party");
        System.out.println("2. Hito 2: Soporte Técnico VIP");
        System.out.println("3. Hito 3: Auditoría de Gremios");
        System.out.println("4. Hito 4: Sistema de Comercio Seguro (Ejecutar Rollback)");
        System.out.println("5. <- Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();

        // Dependiendo de la opción seleccionada, ejecutamos la acción correspondiente
        switch (opcion) {
            case 1 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 1 (VIAJE RÁPIDO Y PARTY) ---");

                // Ejecutamos el hito 1 y obtenemos la cola de jugadores que se unieron a la party
                Cola<Jugador> partyArmada = controlador.ejecutarHito1();

                System.out.println("\n==================================================");
                System.out.println("RESULTADO DE LA COLA DE PARTY (FIFO)");
                System.out.println("==================================================");
                
                // Verificamos si la cola de jugadores está vacía; si no, mostramos los jugadores en orden de llegada
                if (partyArmada == null || partyArmada.estaVacia()) {
                    // Si no hay jugadores en la party, mostramos un mensaje indicando que nadie se unió
                    System.out.println("Nadie se unió a la Party."); 
                } else {
                    // Si hay jugadores en la party, iteramos sobre la cola y mostramos cada jugador con su posición y nivel
                    int posicion = 1;

                    // Mientras la cola no esté vacía, desencolamos a cada jugador y mostramos su información
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
                
                // Ejecutamos el hito 2 y obtenemos un booleano indicando si la operación fue exitosa
                boolean exitoSoporte = controlador.ejecutarHito2("ITM-701");

                // Dependiendo del resultado de la operación, mostramos un mensaje de éxito o error
                if (exitoSoporte) {
                    // Si la operación fue exitosa, mostramos un mensaje indicando que se procesaron los tickets correctamente
                    System.out.println("[SISTEMA] Operación de Soporte VIP finalizada correctamente.");
                } else {
                    // Si la operación falló, mostramos un mensaje indicando que no se pudo procesar ningún ticket
                    System.out.println("[SISTEMA] No se pudo procesar ningún ticket.");
                }
            }
            case 3 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 3 (AUDITORÍA DE GREMIOS) ---");

                // Obtenemos un gremio de demostración desde el controlador para mostrar su estructura jerárquica
                Gremio g = controlador.getGremioDemo();
                
                System.out.println("Datos Generales del " + g.toString());
                System.out.println("Visualización estructural por Consola (Preorden del TDA):");
                
                // Mostramos la estructura jerárquica del gremio en preorden
                g.getEstructuraJerarquica().preorden();

                // Ejecutamos el hito 3 y obtenemos un conjunto de jugadores líderes que fueron auditados y validados
                Conjunto<Jugador> lideresAudita = controlador.ejecutarHito3();

                // Mostramos los resultados de la auditoría de gremios, incluyendo el total de líderes únicos validados y almacenados, así como la verificación de existencia de claves específicas
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
                
                String idBuscado = scanner.nextLine().trim().toUpperCase(); // Convertimos el ID ingresado a mayúsculas para mantener consistencia en la búsqueda
                
                System.out.println("\n--- EJECUCIÓN: HITO 4 (SISTEMA DE COMERCIO SEGURO) ---");
                
                // Ejecutamos el hito 4 y verificamos si la transacción se pudo revertir correctamente
                if (controlador.ejecutarHito4(idBuscado)) {
                    // Si la transacción se revirtió correctamente, mostramos un mensaje indicando que el flujo se completó de forma segura y exitosa
                    System.out.println("[SISTEMA] Flujo completado de forma segura y exitosa.");
                } else {
                    // Si la transacción no se pudo revertir, mostramos un mensaje indicando que el protocolo de comercio seguro fue abortado debido a fallas en las condiciones
                    System.out.println("[SISTEMA] Protocolo Comercio Seguro abortado / Falla de condiciones.");
                }
            }
            case 5 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Método para mostrar el menú de consultas de datos
     */
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

        // Leemos la opción ingresada por el usuario
        int opcion = leerOpcion();

        // Dependiendo de la opción seleccionada, ejecutamos la acción correspondiente
        switch (opcion) {
            case 1 -> {
                System.out.println("\n--- LISTA DE JUGADORES REGISTRADOS (ÁRBOL AVL) ---");

                // Obtenemos la cola de cuentas en orden inorden desde el árbol AVL global
                Cola<Cuenta> cuentas = controlador.obtenerCuentasInorden();
                
                // Verificamos si la cola de cuentas está vacía; si no, mostramos cada cuenta con su información relevante
                if (cuentas == null || cuentas.estaVacia()) {
                    // Si no hay jugadores registrados, mostramos un mensaje indicando que no hay cuentas en el índice AVL
                    System.out.println("No hay jugadores registrados.");
                } else {
                    // Mientras la cola de cuentas no esté vacía, desencolamos cada cuenta y mostramos su información
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
                
                // Obtenemos la cola de ítems en orden inorden desde el árbol ABB global
                Cola<Item> itemsCatalogo = controlador.obtenerItemsInorden();

                // Verificamos si la cola de ítems está vacía; si no, mostramos cada ítem con su información relevante
                if (itemsCatalogo == null || itemsCatalogo.estaVacia()) {
                    System.out.println("El catálogo está vacío.");
                } else {
                    // Mientras la cola de ítems no esté vacía, desencolamos cada ítem y mostramos su información
                    while (!itemsCatalogo.estaVacia()) {
                        Item item = itemsCatalogo.desencolar();
                        System.out.println("- " + item.toString());
                    }
                }
            }
            case 3 -> {
                System.out.println("\n--- RECORRIDO EN ANCHURA DEL MUNDO ---");
                System.out.println("Iniciando exploración síncrona desde el nodo raíz...");

                // Ejecutamos el recorrido BFS desde el nodo "Pueblo de los Inicios" en el grafo global
                controlador.getMapaGlobal().bfs("Pueblo de los Inicios");
                
                System.out.println("\n[GRAFO] Recorrido de adyacencias completado.");
            }
            case 4 -> {
                System.out.print("Ingrese el ID numérico de la transacción a buscar (Pruebe con 90001 o 90002): ");
                
                // Intentamos leer el ID de transacción ingresado por el usuario y buscarlo en el árbol B
                try {
                    // Leemos la entrada del usuario y la convertimos a un número largo (Long)
                    String input = scanner.nextLine().trim();
                    long idTransaccion = Long.parseLong(input);

                    System.out.println("\n--- CONSULTA DE REGISTROS MASIVOS (ÁRBOL B) ---");
                    
                    // Buscamos la transacción en el árbol B utilizando el ID ingresado
                    Transaccion tx = controlador.buscarTransaccionArbolB(idTransaccion);

                    // Si la transacción se encuentra, mostramos sus detalles; de lo contrario, indicamos que no se encontró ninguna transacción con ese ID
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
                    // Si la entrada no es un número válido, mostramos un mensaje de error indicando que el ID debe ser un número entero largo (Long)
                    System.out.println("[ERROR] El ID de transacción debe ser un número entero largo (Long).");
                }
            }
            case 5 -> {
                System.out.println("\n--- ÁRBOL DE PROGRESIÓN DE CLASES Y HABILIDADES (PREORDEN) ---");
                System.out.println("Visualización jerárquica de la rama de talentos:");

                // Ejecutamos el recorrido preorden del árbol de habilidades global para mostrar la estructura jerárquica de las habilidades
                controlador.getArbolHabilidadesGlobal().preorden();

                System.out.println("\n[ÁRBOL GENÉRICO] Exploración estructural finalizada.");
            }
            case 6 -> {
                System.out.println("\n--- ÁRBOL DE PROGRESIÓN DE CLASES Y HABILIDADES (POSTORDEN) ---");
                System.out.println("Orden de ejecución / cálculo de dependencias de habilidades:");

                // Ejecutamos el recorrido postorden del árbol de habilidades global para mostrar el orden de ejecución y cálculo de dependencias de habilidades
                controlador.getArbolHabilidadesGlobal().postorden();

                System.out.println("\n[ÁRBOL GENÉRICO] Exploración de dependencias finalizada.");
            }
            case 7 -> {
                // Obtenemos la cantidad de misiones pendientes en la cola de prioridad global
                int total = controlador.getCantidadMisionesPendientes();

                System.out.println("[COLA PRIORIDAD] Misiones actualmente activas en memoria: " + total);
            }
            case 8 -> historialMenus.desapilar();
            default -> System.out.println("[ERROR] Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Método para leer la opción ingresada por el usuario desde la consola.
     * @return La opción ingresada como un entero. Si la entrada no es válida, retorna -1.
     */
    private static int leerOpcion() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}