package main.java.com.tenFloors;

import main.java.com.tenFloors.model.*;
import main.java.com.tenFloors.tda.arbol.ArbolGenerico;
import main.java.com.tenFloors.tda.cola.Cola;
import main.java.com.tenFloors.tda.conjunto.Conjunto;
import main.java.com.tenFloors.tda.grafo.Grafo;
import main.java.com.tenFloors.tda.pila.Pila;
import main.java.com.tenFloors.tda.avl.ArbolAVLCuentas;
import main.java.com.tenFloors.tda.abb.ArbolABB;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Pila<String> historialMenus = new Pila<>();
    private static boolean running = true;

    // Almacenamiento síncrono e Índices globales para la ejecución del ecosistema
    private static final ArbolAVLCuentas<Cuenta> indiceCuentas = new ArbolAVLCuentas<>();
    private static final ArbolABB<Item> baseGlobalItems = new ArbolABB<>();
    private static SistemaComercioSeguro sistemaComercio;
    private static final SistemaAuditoriaGremios sistemaAuditoria = new SistemaAuditoriaGremios();
    private static Gremio gremioDemo; // Definido de forma global para persistir en memoria síncrona
    private static final Grafo<String> mapaGlobal = new Grafo<>();
    private static final Conjunto<Jugador> jugadoresOnline = new Conjunto<>();
    private static SistemaViajeYParty sistemaViaje;

    public static void main(String[] args) {
        // Inicializamos el motor del hito técnico cruzando los índices requeridos
        sistemaComercio = new SistemaComercioSeguro(indiceCuentas, baseGlobalItems);

        // Pre-carga de datos reales para posibilitar pruebas inmediatas sin placeholders
        inicializarDatosDemo();

        System.out.println("==================================================");
        System.out.println("             BIENVENIDO A TEN FLOORS             ");
        System.out.println("==================================================");

        // Iniciamos el historial con el estado principal
        historialMenus.apilar("PRINCIPAL");

        // Bucle principal de ejecución de consola
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

    /**
     * Carga de datos mock controlados para validar el comportamiento del Hito 4 de forma autónoma.
     */
    private static void inicializarDatosDemo() {

        // Para Hito 4
        // 1. Poblamos el catálogo del servidor (ABB) con ítems íntegros
        baseGlobalItems.insertar("ITM-701", new ItemJuego("ITM-701", "Espada del Inframundo", "Legendaria"));
        baseGlobalItems.insertar("ITM-702", new ItemJuego("ITM-702", "Poción de Vida Mayor", "Común"));
        baseGlobalItems.insertar("ITM-703", new ItemJuego("ITM-703", "Escudo del Olimpo", "Épica"));

        // 2. Registramos una cuenta de prueba para Lautaro Salto (ID: ACC-77)
        Jugador lauti = new Jugador("ACC-77", "Lauti_Salto");
        lauti.setNivel(60);
        lauti.setPisoActual(10);
        Cuenta cuentaLauti = new Cuenta(lauti);

        // Simulamos que Lauti vendió la Espada y la Poción (Se apilan en su historial comercial)
        long tiempoActual = System.currentTimeMillis();
        cuentaLauti.getHistorialComercio().apilar(new Transaccion(5001L, "ITM-701", 9999.0, tiempoActual - 100000));
        cuentaLauti.getHistorialComercio().apilar(new Transaccion(5002L, "ITM-702", 150.0, tiempoActual - 20000));

        cuentaLauti.getInventario().insertar("ITM-703", baseGlobalItems.buscar("ITM-703"));

        // 3. Insertamos la cuenta en el índice AVL global
        indiceCuentas.insertar(cuentaLauti.getJugador().getIdCuenta(), cuentaLauti);

        // Para Hito 3
        gremioDemo = new Gremio("Los Conquistadores de Aincrad", "LCA");

        // Registramos cuentas de oficiales adicionales en el AVL global de Axel
        Cuenta cuentaGian = new Cuenta(new Jugador("ACC-02", "Gian_Chia"));
        cuentaGian.getJugador().setNivel(55);
        Cuenta cuentaAxel = new Cuenta(new Jugador("ACC-03", "Axel_Menz"));
        cuentaAxel.getJugador().setNivel(58);

        indiceCuentas.insertar("ACC-02", cuentaGian);
        indiceCuentas.insertar("ACC-03", cuentaAxel);

        // Accedemos al árbol interno del gremio para establecer los rangos
        ArbolGenerico<String> tree = gremioDemo.getEstructuraJerarquica();

        // El Guild Master en la raíz (Padre null) es Lauti
        tree.agregarHijo(null, "ACC-77");

        // Gian y Axel son Oficiales directos de Lauti
        tree.agregarHijo("ACC-77", "ACC-02");
        tree.agregarHijo("ACC-77", "ACC-03");

        // Forzamos la falla de auditoría metiendo una cuenta inexistente en el AVL
        tree.agregarHijo("ACC-03", "ACC-FANTASMA");

        // Para Hito 1 (Viaje y Party)
        // Mapeamos algunas zonas en el Grafo
        mapaGlobal.agregarVertice("Pueblo de los Inicios");
        mapaGlobal.agregarVertice("Bosque Oscuro");
        mapaGlobal.agregarVertice("Mazmorra del Piso 10");
        mapaGlobal.agregarArista("Pueblo de los Inicios", "Bosque Oscuro");
        mapaGlobal.agregarArista("Bosque Oscuro", "Mazmorra del Piso 10");

        // Conectamos jugadores (Lauti y Gian están online, Axel no)
        jugadoresOnline.agregar(lauti);
        jugadoresOnline.agregar(cuentaGian.getJugador());

        // Inicializamos el motor del Hito 1
        sistemaViaje = new SistemaViajeYParty(mapaGlobal, jugadoresOnline);
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
        System.out.println("5. <- Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1 -> {
                System.out.print("Ingrese el ID único de cuenta (ej. ACC-12): ");
                String id = scanner.nextLine().trim();
                System.out.print("Ingrese el nombre del personaje: ");
                String nombre = scanner.nextLine().trim();
                if (!id.isEmpty() && !nombre.isEmpty()) {
                    Cuenta nuevaCuenta = new Cuenta(new Jugador(id, nombre));
                    indiceCuentas.insertar(id, nuevaCuenta);
                    System.out.println("[AVL] Cuenta registrada e índice rebalanceado exitosamente.");
                } else {
                    System.out.println("[ERROR] Entradas vacías no permitidas.");
                }
            }
            case 2 -> {
                System.out.print("Ingrese el ID de la cuenta a dar de baja: ");
                String id = scanner.nextLine().trim();
                if (indiceCuentas.buscar(id) != null) {
                    indiceCuentas.eliminar(id);
                    System.out.println("[AVL] Cuenta removida correctamente. Árbol auto-balanceado.");
                } else {
                    System.out.println("[ERROR] La cuenta especificada no existe.");
                }
            }
            case 3 -> {
                System.out.print("Ingrese el ID de cuenta del jugador: ");
                String idC = scanner.nextLine().trim();
                Cuenta c = indiceCuentas.buscar(idC);
                if (c != null) {
                    System.out.print("Ingrese ID de ítem del catálogo (ITM-701, ITM-702, ITM-703): ");
                    String idI = scanner.nextLine().trim();
                    Item it = baseGlobalItems.buscar(idI);
                    if (it != null) {
                        c.getInventario().insertar(idI, it);
                        System.out.println("[ABB] Ítem indexado en el inventario del jugador.");
                    } else {
                        System.out.println("[ERROR] Ítem inexistente en el catálogo global ABB.");
                    }
                } else {
                    System.out.println("[ERROR] Cuenta no encontrada.");
                }
            }
            case 4 -> {
                System.out.print("Ingrese ID de cuenta a inspeccionar (Pruebe con 'ACC-77'): ");
                String id = scanner.nextLine().trim();
                Cuenta c = indiceCuentas.buscar(id);
                if (c != null) {
                    System.out.println("\n==================================================");
                    System.out.println("AUDITORÍA DE CUENTA AVL: " + c.getJugador().getIdCuenta());
                    System.out.println("==================================================");
                    System.out.println("Personaje: " + c.getJugador().getNombre() + " | Nivel: " + c.getJugador().getNivel());
                    System.out.println("Ubicación Actual: Piso " + c.getJugador().getPisoActual());

                    System.out.println("\n-> Mochila del Jugador (Recorrido Inorden ABB):");
                    List<Item> items = c.getInventario().obtenerInorden();
                    if (items.isEmpty()) {
                        System.out.println("   [La mochila está vacía]");
                    } else {
                        for (Item item : items) {
                            System.out.println("   * " + item.toString());
                        }
                    }
                    System.out.println("\n-> Operaciones comerciales pendientes en Pila (Tamanio): " + c.getHistorialComercio().getTamanio());
                    System.out.println("==================================================");
                } else {
                    System.out.println("[ERROR] No se encontró ninguna cuenta asociada a ese ID.");
                }
            }
            case 5 -> historialMenus.desapilar();
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
            case 1 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 1 (VIAJE RÁPIDO Y PARTY) ---");
                // Simulamos un arreglo de jugadores que están en la zona buscando party
                Jugador[] posiblesCandidatos = {
                        indiceCuentas.buscar("ACC-77").getJugador(), // Lauti
                        indiceCuentas.buscar("ACC-02").getJugador(), // Gian
                        indiceCuentas.buscar("ACC-03").getJugador()  // Axel
                };

                // Ejecutamos el servicio cruzado
                Cola<Jugador> partyArmada = sistemaViaje.solicitarViajeYArmarParty("Pueblo de los Inicios", "Mazmorra del Piso 10", posiblesCandidatos);

                System.out.println("\n==================================================");
                System.out.println("RESULTADO DE LA COLA DE PARTY (FIFO)");
                System.out.println("==================================================");
                if (partyArmada.estaVacia()) {
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
            case 2 -> System.out.println("[INFO] El Hito 2 (Soporte Técnico VIP) aún está en desarrollo.");
            case 3 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 3 (AUDITORÍA DE GREMIOS) ---");
                System.out.println("Datos Generales del " + gremioDemo.toString());
                System.out.println("Visualización estructural por Consola (Preorden del TDA):");

                // Invocamos el preorden nativo de Gianluca para pintar las sangrías en consola
                gremioDemo.getEstructuraJerarquica().preorden();

                // Disparamos el motor cruzado de los 3 TDAs pasando el objeto Gremio
                Conjunto<Jugador> lideresAudita = sistemaAuditoria.auditarLideresGremio(gremioDemo, indiceCuentas);

                System.out.println("\n==================================================");
                System.out.println("RESULTADO DE CONSOLIDACIÓN EN CONJUNTO TEMPORAL");
                System.out.println("==================================================");
                System.out.println("Total de líderes únicos validados y almacenados: " + lideresAudita.getTamanio());
                System.out.println("Verificación de Existencia de Claves:");
                System.out.println("   ¿Se consolidó al GM (ACC-77)?: " + lideresAudita.contiene(new Jugador("ACC-77", "")));
                System.out.println("   ¿Se consolidó al ID FANTASMA?: " + lideresAudita.contiene(new Jugador("ACC-FANTASMA", "")));
                System.out.println("==================================================");
            }
            case 4 -> {
                System.out.println("\n--- EJECUCIÓN: HITO 4 (SISTEMA DE COMERCIO SEGURO) ---");
                System.out.print("Ingrese el ID de la cuenta para revertir su última transacción (ej: ACC-77): ");
                String idBuscado = scanner.nextLine().trim();

                boolean exito = sistemaComercio.revertirUltimaTransaccion(idBuscado);
                if (exito) {
                    System.out.println("[SISTEMA] Flujo completado de forma segura y exitosa.");
                } else {
                    System.out.println("[SISTEMA] Protocolo Comercio Seguro abortado / Falla de condiciones.");
                }
            }
            case 5 -> historialMenus.desapilar();
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