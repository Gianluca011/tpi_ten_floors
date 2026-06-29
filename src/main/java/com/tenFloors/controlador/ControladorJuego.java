package main.java.com.tenFloors.controlador;

import main.java.com.tenFloors.consulta.SistemaAuditoriaGremios;
import main.java.com.tenFloors.consulta.SistemaComercioSeguro;
import main.java.com.tenFloors.consulta.SistemaSoporteVIP;
import main.java.com.tenFloors.consulta.SistemaViajeYParty;
import main.java.com.tenFloors.gestor.GestorMisiones;
import main.java.com.tenFloors.model.*;
import main.java.com.tenFloors.tda.arbol.ArbolGenerico;
import main.java.com.tenFloors.tda.arbolB.ArbolB;
import main.java.com.tenFloors.tda.cola.Cola;
import main.java.com.tenFloors.tda.colaPrioridad.ColaPrioridad;
import main.java.com.tenFloors.tda.conjunto.Conjunto;
import main.java.com.tenFloors.tda.grafo.Grafo;
import main.java.com.tenFloors.tda.avl.ArbolAVL;
import main.java.com.tenFloors.tda.abb.ArbolABB;

public class ControladorJuego {

    // Almacenamiento síncrono e Índices globales para la ejecución del ecosistema
    private final ArbolAVL<Cuenta> indiceCuentas = new ArbolAVL<>();
    private final ArbolABB<Item> baseGlobalItems = new ArbolABB<>();
    private final GestorMisiones gestorMisiones = new GestorMisiones();
    private final SistemaComercioSeguro sistemaComercio;
    private final SistemaAuditoriaGremios sistemaAuditoria = new SistemaAuditoriaGremios();
    private Gremio gremioDemo;
    private final Grafo<String> mapaGlobal = new Grafo<>();
    private final Conjunto<Jugador> jugadoresOnline = new Conjunto<>();
    private final SistemaViajeYParty sistemaViaje;
    private final ColaPrioridad<Ticket> colaTicketsVIP = new ColaPrioridad<>();
    private final ArbolB<Transaccion> historialSubastas = new ArbolB<>(2);
    private final SistemaSoporteVIP sistemaSoporte;
    private final ArbolGenerico<ClaseHabilidad> arbolHabilidadesGlobal = new ArbolGenerico<>();

    public ControladorJuego() {
        // Inicializamos los motores del hito técnico cruzando los índices requeridos
        this.sistemaComercio = new SistemaComercioSeguro(indiceCuentas, baseGlobalItems);
        this.sistemaSoporte = new SistemaSoporteVIP(colaTicketsVIP, historialSubastas, indiceCuentas, baseGlobalItems);
        this.sistemaViaje = new SistemaViajeYParty(mapaGlobal, jugadoresOnline);

        // Pre-carga automática de datos mock controlados
        this.inicializarDatosDemo();
    }

    private void inicializarDatosDemo() {
        // --- CARGA DE DATOS PARA ÁRBOL DE HABILIDADES (ÁRBOL GENÉRICO) ---
        ClaseHabilidad raizServidor = new ClaseHabilidad("RAIZ", "SISTEMA", 0);
        arbolHabilidadesGlobal.agregarHijo(null, raizServidor);

        // RAMA 1: ESPADACHÍN
        ClaseHabilidad espadachin = new ClaseHabilidad("ESPADACHIN", "CLASE", 1);
        arbolHabilidadesGlobal.agregarHijo(raizServidor, espadachin);
        ClaseHabilidad catActivasEspada = new ClaseHabilidad("Habilidades Activas", "CATEGORIA", 1);
        ClaseHabilidad catPasivasEspada = new ClaseHabilidad("Habilidades Pasivas", "CATEGORIA", 1);
        arbolHabilidadesGlobal.agregarHijo(espadachin, catActivasEspada);
        arbolHabilidadesGlobal.agregarHijo(espadachin, catPasivasEspada);
        arbolHabilidadesGlobal.agregarHijo(catActivasEspada, new ClaseHabilidad("Estocada Certera", "HABILIDAD", 10));
        arbolHabilidadesGlobal.agregarHijo(catActivasEspada, new ClaseHabilidad("Torbellino de Espadas", "HABILIDAD", 30));
        arbolHabilidadesGlobal.agregarHijo(catPasivasEspada, new ClaseHabilidad("Maestría en Espadas", "HABILIDAD", 5));

        // RAMA 2: MAGO
        ClaseHabilidad mago = new ClaseHabilidad("MAGO", "CLASE", 1);
        arbolHabilidadesGlobal.agregarHijo(raizServidor, mago);
        ClaseHabilidad catHechizos = new ClaseHabilidad("Hechizos de Destrucción", "CATEGORIA", 1);
        arbolHabilidadesGlobal.agregarHijo(mago, catHechizos);
        arbolHabilidadesGlobal.agregarHijo(catHechizos, new ClaseHabilidad("Bola de Fuego", "HABILIDAD", 5));
        arbolHabilidadesGlobal.agregarHijo(catHechizos, new ClaseHabilidad("Ventisca Helada", "HABILIDAD", 20));
        arbolHabilidadesGlobal.agregarHijo(catHechizos, new ClaseHabilidad("Impacto Trueno", "HABILIDAD", 40));

        // RAMA 3: ASESINO
        ClaseHabilidad asesino = new ClaseHabilidad("ASESINO", "CLASE", 1);
        arbolHabilidadesGlobal.agregarHijo(raizServidor, asesino);
        ClaseHabilidad catArtesAsesinato = new ClaseHabilidad("Artes del Asesinato", "CATEGORIA", 1);
        ClaseHabilidad catTacticasSigilo = new ClaseHabilidad("Tácticas de Sigilo", "CATEGORIA", 1);
        arbolHabilidadesGlobal.agregarHijo(asesino, catArtesAsesinato);
        arbolHabilidadesGlobal.agregarHijo(asesino, catTacticasSigilo);
        arbolHabilidadesGlobal.agregarHijo(catArtesAsesinato, new ClaseHabilidad("Emboscada", "HABILIDAD", 5));
        arbolHabilidadesGlobal.agregarHijo(catArtesAsesinato, new ClaseHabilidad("Hoja Envenenada", "HABILIDAD", 12));
        arbolHabilidadesGlobal.agregarHijo(catArtesAsesinato, new ClaseHabilidad("Ejecución", "HABILIDAD", 45));
        arbolHabilidadesGlobal.agregarHijo(catTacticasSigilo, new ClaseHabilidad("Paso Sombrío", "HABILIDAD", 20));
        arbolHabilidadesGlobal.agregarHijo(catTacticasSigilo, new ClaseHabilidad("Manto de Invisibilidad", "HABILIDAD", 35));

        // Catálogo global en ABB
        baseGlobalItems.insertar("ITM-701", new Item("ITM-701", "Espada del Inframundo", "Legendaria"));
        baseGlobalItems.insertar("ITM-702", new Item("ITM-702", "Poción de Vida Mayor", "Común"));
        baseGlobalItems.insertar("ITM-703", new Item("ITM-703", "Escudo del Olimpo", "Épica"));

        // Cuenta base de Lautaro Salto (ID: ACC-77)
        Jugador lauti = new Jugador("ACC-77", "Lauti_Salto", "MAGO");
        lauti.setNivel(60);
        lauti.setPisoActual(10);
        Cuenta cuentaLauti = new Cuenta(lauti);

        long tiempoActual = System.currentTimeMillis();
        cuentaLauti.getHistorialComercio().apilar(new Transaccion(5001L, "ITM-701", 9999.0, tiempoActual - 100000));
        cuentaLauti.getHistorialComercio().apilar(new Transaccion(5002L, "ITM-702", 150.0, tiempoActual - 20000));
        cuentaLauti.getInventario().insertar("ITM-703", baseGlobalItems.buscar("ITM-703"));
        indiceCuentas.insertar(cuentaLauti.getJugador().getIdCuenta(), cuentaLauti);

        // Configuración de Gremio para Hito 3
        gremioDemo = new Gremio("Los Conquistadores de Aincrad", "LCA");
        Cuenta cuentaGian = new Cuenta(new Jugador("ACC-02", "Gian_Chia", "ESPADACHIN"));
        cuentaGian.getJugador().setNivel(12);
        Cuenta cuentaAxel = new Cuenta(new Jugador("ACC-03", "Axel_Menz", "ASESINO"));
        cuentaAxel.getJugador().setNivel(32);

        indiceCuentas.insertar("ACC-02", cuentaGian);
        indiceCuentas.insertar("ACC-03", cuentaAxel);

        ArbolGenerico<String> tree = gremioDemo.getEstructuraJerarquica();
        tree.agregarHijo(null, "ACC-77");
        tree.agregarHijo("ACC-77", "ACC-02");
        tree.agregarHijo("ACC-77", "ACC-03");
        tree.agregarHijo("ACC-03", "ACC-FANTASMA");

        // Conectividad del mapa para Hito 1
        mapaGlobal.agregarVertice("Pueblo de los Inicios");
        mapaGlobal.agregarVertice("Bosque Oscuro");
        mapaGlobal.agregarVertice("Mazmorra del Piso 10");
        mapaGlobal.agregarArista("Pueblo de los Inicios", "Bosque Oscuro");
        mapaGlobal.agregarArista("Bosque Oscuro", "Mazmorra del Piso 10");

        jugadoresOnline.agregar(lauti);
        jugadoresOnline.agregar(cuentaGian.getJugador());

        // Historial global de subastas (Árbol B) e Historial de Soporte (Heap) para Hito 2
        historialSubastas.insertar(90001L, new Transaccion(90001L, "ITM-701", 15000.0, System.currentTimeMillis() - 400000));
        historialSubastas.insertar(90002L, new Transaccion(90002L, "ITM-703", 45000.0, System.currentTimeMillis() - 200000));

        colaTicketsVIP.insertar(new Ticket(1001L, "ACC-02", 90001L, "Compré una espada pero se me desconectó el cliente y no la veo", 3), 3);
        colaTicketsVIP.insertar(new Ticket(1002L, "ACC-77", 90002L, "Perdí mis fondos y el escudo del Olimpo falló al reclamarse", 9), 9);

        gestorMisiones.registrarMision(new Mision(1, "Juntar 10 Hierbas Curativas", "Misión básica de recolección.", Mision.TipoMision.RECOLECCION_ESTANDAR, 1, "ITM-702"));
        gestorMisiones.registrarMision(new Mision(2, "Matar al Dragón Ancestral", "Derrotar al jefe supremo de la torre.", Mision.TipoMision.JEFE_MUNDO, 10, "ITM-701"));
        gestorMisiones.registrarMision(new Mision(3, "Festival de la Luna", "Evento temporal de recolección nocturna.", Mision.TipoMision.EVENTO_TEMPORAL, 3, "ITM-703"));
        gestorMisiones.registrarMision(new Mision(4, "Invasión de Orcos", "Frenar la oleada antes de que destruyan el campamento.", Mision.TipoMision.EVENTO_TEMPORAL, 5, "ITM-702"));

        sincronizarHabilidadesAutomatica(lauti);
        sincronizarHabilidadesAutomatica(cuentaGian.getJugador());
    }

    public void sincronizarHabilidadesAutomatica(Jugador jugador) {
        if (arbolHabilidadesGlobal.estaVacio()) {
            return;
        }
        procesarDesbloqueoRecursivo(arbolHabilidadesGlobal.getRaiz(), jugador, false);
    }

    private void procesarDesbloqueoRecursivo(ArbolGenerico.NodoArbol<ClaseHabilidad> nodo, Jugador jugador, boolean ramaDeClaseActiva) {
        if (nodo == null) {
            return;
        }

        ClaseHabilidad infoHabilidad = nodo.getDato();
        boolean banderaHijos = ramaDeClaseActiva;

        if (infoHabilidad.getTipo().equals("CLASE") && infoHabilidad.getNombre().equalsIgnoreCase(jugador.getClase())) {
            banderaHijos = true;
        }

        if (banderaHijos && infoHabilidad.getTipo().equals("HABILIDAD")) {
            if (jugador.getNivel() >= infoHabilidad.getNivelRequerido()) {
                jugador.getHabilidadesAprendidas().agregar(infoHabilidad.getNombre());
            }
        }

        procesarDesbloqueoRecursivo(nodo.getPrimerHijo(), jugador, banderaHijos);
        procesarDesbloqueoRecursivo(nodo.getSiguienteHermano(), jugador, ramaDeClaseActiva);
    }

    // --- ACCIONES DE GESTIÓN DE DATOS ---

    public void darAltaCuenta(String id, String nombre, String clase) {
        Cuenta nuevaCuenta = new Cuenta(new Jugador(id, nombre, clase));
        sincronizarHabilidadesAutomatica(nuevaCuenta.getJugador());
        indiceCuentas.insertar(id, nuevaCuenta);
        System.out.println("[AVL] Cuenta registrada e índice rebalanceado exitosamente.");
    }

    public boolean darBajaCuenta(String id) {
        if (indiceCuentas.buscar(id) != null) {
            indiceCuentas.eliminar(id);
            return true;
        }
        return false;
    }

    public int agregarItemMochila(String idCuenta, String idItem) {
        Cuenta c = indiceCuentas.buscar(idCuenta);
        if (c == null) return 1; // Cuenta no existe

        Item it = baseGlobalItems.buscar(idItem);
        if (it == null) return 2; // Ítem no existe en catálogo global

        c.getInventario().insertar(idItem, it);
        return 0; // Éxito
    }

    public Cuenta buscarCuenta(String id) {
        return indiceCuentas.buscar(id);
    }

    public void registrarNuevaMision(int idM, String nomM, String descM, Mision.TipoMision tipoM, int pisoM, String recompensaM) {
        gestorMisiones.registrarMision(new Mision(idM, nomM, descM, tipoM, pisoM, recompensaM));
        System.out.println("[COLA PRIORIDAD] Misión integrada y reordenada en el pool global.");
    }

    public int getCantidadMisionesPendientes() {
        return gestorMisiones.getCantidadMisionesPendientes();
    }

    public void procesarDespachoMision(String idCuenta) {
        Cuenta cuentaJugador = indiceCuentas.buscar(idCuenta);
        if (cuentaJugador == null) {
            System.out.println("[ERROR] La cuenta especificada no existe en el índice AVL global.");
            return;
        }

        Jugador jugador = cuentaJugador.getJugador();
        Mision misionUrgente = gestorMisiones.procesarSiguienteMision();

        if (misionUrgente != null) {
            System.out.println("\n==================================================");
            System.out.println("PROCESANDO RECLAMO DE MISIÓN CRÍTICA");
            System.out.println("==================================================");
            System.out.println("Evaluando a : " + jugador.getNombre() + " (Piso Actual: " + jugador.getPisoActual() + ")");
            System.out.println("Misión       : " + misionUrgente.getNombre() + " [" + misionUrgente.getTipo() + "]");
            System.out.println("Requisito    : Piso " + misionUrgente.getPisoRequerido() + " de la torre.");

            if (jugador.getPisoActual() < misionUrgente.getPisoRequerido()) {
                System.out.println("\n[RECHAZADO] El jugador no cumple con el piso requerido para esta misión.");
                System.out.println("[SISTEMA] La misión se descarta por intento de fraude o nivel insuficiente.");
                System.out.println("==================================================");
                return;
            }

            String idPremio = misionUrgente.getIdItemRecompensa();
            Item premioCatalogo = baseGlobalItems.buscar(idPremio);

            if (premioCatalogo != null) {
                cuentaJugador.getInventario().insertar(idPremio, premioCatalogo);
                System.out.println("¡RECOMPENSA OTORGADA! Se añadió '" + premioCatalogo.toString() + "' a su mochila [ABB].");
            } else {
                System.out.println("[ALERTA] El ítem de recompensa (" + idPremio + ") no existe en el catálogo maestro.");
            }

            int nivelViejo = jugador.getNivel();
            jugador.setNivel(nivelViejo + 2);
            System.out.println("¡SUBIDA DE NIVEL! El personaje progresó: Lvl " + nivelViejo + " -> Lvl " + jugador.getNivel());

            sincronizarHabilidadesAutomatica(jugador);
            System.out.println("[SISTEMA] Árbol de habilidades re-evaluado para la clase: " + jugador.getClase());
            System.out.println("==================================================");
        }
    }

    // --- ACCIONES DE CONSULTAS COMPLEJAS ---

    public void ejecutarHito1() {
        System.out.println("\n--- EJECUCIÓN: HITO 1 (VIAJE RÁPIDO Y PARTY) ---");
        Jugador[] posiblesCandidatos = {
                indiceCuentas.buscar("ACC-77").getJugador(),
                indiceCuentas.buscar("ACC-02").getJugador(),
                indiceCuentas.buscar("ACC-03").getJugador()
        };

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

    public void ejecutarHito2(String idItemCompensacion) {
        System.out.println("\n--- EJECUCIÓN: HITO 2 (SOPORTE TÉCNICO VIP) ---");
        boolean exitoSoporte = sistemaSoporte.atenderProximoTicket(idItemCompensacion);
        if (exitoSoporte) {
            System.out.println("[SISTEMA] Operación de Soporte VIP finalizada correctamente.");
        } else {
            System.out.println("[SISTEMA] No se pudo procesar ningún ticket.");
        }
    }

    public void ejecutarHito3() {
        System.out.println("\n--- EJECUCIÓN: HITO 3 (AUDITORÍA DE GREMIOS) ---");
        System.out.println("Datos Generales del " + gremioDemo.toString());
        System.out.println("Visualización estructural por Consola (Preorden del TDA):");

        gremioDemo.getEstructuraJerarquica().preorden();

        Conjunto<Jugador> lideresAudita = sistemaAuditoria.auditarLideresGremio(gremioDemo, indiceCuentas);

        System.out.println("\n==================================================");
        System.out.println("RESULTADO DE CONSOLIDACIÓN EN CONJUNTO TEMPORAL");
        System.out.println("==================================================");
        System.out.println("Total de líderes únicos validados y almacenados: " + lideresAudita.getTamanio());
        System.out.println("Verificación de Existencia de Claves:");
        System.out.println("   ¿Se consolidó al GM (ACC-77)?: " + lideresAudita.contiene(new Jugador("ACC-77", "", "")));
        System.out.println("   ¿Se consolidó al ID FANTASMA?: " + lideresAudita.contiene(new Jugador("ACC-FANTASMA", "", "")));
        System.out.println("==================================================");
    }

    public void ejecutarHito4(String idBuscado) {
        System.out.println("\n--- EJECUCIÓN: HITO 4 (SISTEMA DE COMERCIO SEGURO) ---");
        boolean exito = sistemaComercio.revertirUltimaTransaccion(idBuscado);
        if (exito) {
            System.out.println("[SISTEMA] Flujo completado de forma segura y exitosa.");
        } else {
            System.out.println("[SISTEMA] Protocolo Comercio Seguro abortado / Falla de condiciones.");
        }
    }

    // --- ACCIONES DEL PANEL DE INSPECCIÓN DE TDAs ---

    public void mostrarJugadoresAVL() {
        System.out.println("\n--- LISTA DE JUGADORES REGISTRADOS (ÁRBOL AVL) ---");
        Cola<Cuenta> cuentas = indiceCuentas.obtenerInorden();
        if (cuentas.estaVacia()) {
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

    public void mostrarItemsABB() {
        System.out.println("\n--- CATÁLOGO GLOBAL DE ÍTEMS (ÁRBOL ABB) ---");
        Cola<Item> itemsCatalogo = baseGlobalItems.obtenerInorden();
        if (itemsCatalogo.estaVacia()) {
            System.out.println("El catálogo está vacío.");
        } else {
            while (!itemsCatalogo.estaVacia()) {
                Item item = itemsCatalogo.desencolar();
                System.out.println("- " + item.toString());
            }
        }
    }

    public void mostrarConectividadGrafo() {
        System.out.println("\n--- RECORRIDO EN ANCHURA DEL MUNDO ---");
        System.out.println("Iniciando exploración síncrona desde el nodo raíz...");
        mapaGlobal.bfs("Pueblo de los Inicios");
        System.out.println("\n[GRAFO] Recorrido de adyacencias completado.");
    }

    public void buscarTransaccionArbolB(long idTransaccion) {
        System.out.println("\n--- CONSULTA DE REGISTROS MASIVOS (ÁRBOL B) ---");
        Transaccion tx = historialSubastas.buscar(idTransaccion);

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
    }

    public void mostrarHabilidadesPreorden() {
        System.out.println("\n--- ÁRBOL DE PROGRESIÓN DE CLASES Y HABILIDADES (PREORDEN) ---");
        System.out.println("Visualización jerárquica de la rama de talentos:");
        arbolHabilidadesGlobal.preorden();
        System.out.println("\n[ÁRBOL GENÉRICO] Exploración estructural finalizada.");
    }

    public void mostrarHabilidadesPostorden() {
        System.out.println("\n--- ÁRBOL DE PROGRESIÓN DE CLASES Y HABILIDADES (POSTORDEN) ---");
        System.out.println("Orden de ejecución / cálculo de dependencias de habilidades:");
        arbolHabilidadesGlobal.postorden();
        System.out.println("\n[ÁRBOL GENÉRICO] Exploración de dependencias finalizada.");
    }
}