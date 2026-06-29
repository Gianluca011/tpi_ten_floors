package main.java.com.tenFloors.controlador;

import main.java.com.tenFloors.consulta.SistemaAuditoriaGremios;
import main.java.com.tenFloors.consulta.SistemaComercioSeguro;
import main.java.com.tenFloors.consulta.SistemaSoporteVIP;
import main.java.com.tenFloors.consulta.SistemaViajeYParty;
import main.java.com.tenFloors.gestor.GestorMisiones;
import main.java.com.tenFloors.gestor.GestorHabilidades;
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

    private final ArbolAVL<Cuenta> indiceCuentas = new ArbolAVL<>();
    private final ArbolABB<Item> baseGlobalItems = new ArbolABB<>();
    private final GestorMisiones gestorMisiones = new GestorMisiones();
    private final GestorHabilidades gestorHabilidades = new GestorHabilidades();
    private final SistemaComercioSeguro sistemaComercio;
    private final SistemaAuditoriaGremios sistemaAuditoria = new SistemaAuditoriaGremios();
    private final Grafo<String> mapaGlobal = new Grafo<>();
    private final Conjunto<Jugador> jugadoresOnline = new Conjunto<>();
    private final SistemaViajeYParty sistemaViaje;
    private final ColaPrioridad<Ticket> colaTicketsVIP = new ColaPrioridad<>();
    private final ArbolB<Transaccion> historialSubastas = new ArbolB<>(2);
    private final SistemaSoporteVIP sistemaSoporte;
    private final ArbolGenerico<ClaseHabilidad> arbolHabilidadesGlobal = new ArbolGenerico<>();
    private Gremio gremioDemo;

    // Clase contenedora estática para reportar el resultado de misiones al Main de forma limpia
    public static class ResultadoDespacho {
        public boolean exito;
        public String motivoError;
        public Jugador jugador;
        public Mision mision;
        public Item premioOtorgado;
        public int nivelAnterior;
        public int nivelNuevo;
    }

    public ControladorJuego() {
        this.sistemaComercio = new SistemaComercioSeguro(indiceCuentas, baseGlobalItems);
        this.sistemaSoporte = new SistemaSoporteVIP(colaTicketsVIP, historialSubastas, indiceCuentas, baseGlobalItems);
        this.sistemaViaje = new SistemaViajeYParty(mapaGlobal, jugadoresOnline);
        this.inicializarDatosDemo();
    }

    private void inicializarDatosDemo() {
        ClaseHabilidad raizServidor = new ClaseHabilidad("RAIZ", "SISTEMA", 0);
        arbolHabilidadesGlobal.agregarHijo(null, raizServidor);

        ClaseHabilidad espadachin = new ClaseHabilidad("ESPADACHIN", "CLASE", 1);
        arbolHabilidadesGlobal.agregarHijo(raizServidor, espadachin);
        ClaseHabilidad catActivasEspada = new ClaseHabilidad("Habilidades Activas", "CATEGORIA", 1);
        ClaseHabilidad catPasivasEspada = new ClaseHabilidad("Habilidades Pasivas", "CATEGORIA", 1);
        arbolHabilidadesGlobal.agregarHijo(espadachin, catActivasEspada);
        arbolHabilidadesGlobal.agregarHijo(espadachin, catPasivasEspada);
        arbolHabilidadesGlobal.agregarHijo(catActivasEspada, new ClaseHabilidad("Estocada Certera", "HABILIDAD", 10));
        arbolHabilidadesGlobal.agregarHijo(catActivasEspada, new ClaseHabilidad("Torbellino de Espadas", "HABILIDAD", 30));
        arbolHabilidadesGlobal.agregarHijo(catPasivasEspada, new ClaseHabilidad("Maestría en Espadas", "HABILIDAD", 5));

        ClaseHabilidad mago = new ClaseHabilidad("MAGO", "CLASE", 1);
        arbolHabilidadesGlobal.agregarHijo(raizServidor, mago);
        ClaseHabilidad catHechizos = new ClaseHabilidad("Hechizos de Destrucción", "CATEGORIA", 1);
        arbolHabilidadesGlobal.agregarHijo(mago, catHechizos);
        arbolHabilidadesGlobal.agregarHijo(catHechizos, new ClaseHabilidad("Bola de Fuego", "HABILIDAD", 5));
        arbolHabilidadesGlobal.agregarHijo(catHechizos, new ClaseHabilidad("Ventisca Helada", "HABILIDAD", 20));
        arbolHabilidadesGlobal.agregarHijo(catHechizos, new ClaseHabilidad("Impacto Trueno", "HABILIDAD", 40));

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

        baseGlobalItems.insertar("ITM-701", new Item("ITM-701", "Espada del Inframundo", "Legendaria"));
        baseGlobalItems.insertar("ITM-702", new Item("ITM-702", "Poción de Vida Mayor", "Común"));
        baseGlobalItems.insertar("ITM-703", new Item("ITM-703", "Escudo del Olimpo", "Épica"));

        Jugador lauti = new Jugador("ACC-77", "Lauti_Salto", "MAGO");
        lauti.setNivel(60);
        lauti.setPisoActual(10);
        Cuenta cuentaLauti = new Cuenta(lauti);

        long tiempoActual = System.currentTimeMillis();
        cuentaLauti.getHistorialComercio().apilar(new Transaccion(5001L, "ITM-701", 9999.0, tiempoActual - 100000));
        cuentaLauti.getHistorialComercio().apilar(new Transaccion(5002L, "ITM-702", 150.0, tiempoActual - 20000));
        cuentaLauti.getInventario().insertar("ITM-703", baseGlobalItems.buscar("ITM-703"));
        indiceCuentas.insertar(cuentaLauti.getJugador().getIdCuenta(), cuentaLauti);

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

        mapaGlobal.agregarVertice("Pueblo de los Inicios");
        mapaGlobal.agregarVertice("Bosque Oscuro");
        mapaGlobal.agregarVertice("Mazmorra del Piso 10");
        mapaGlobal.agregarArista("Pueblo de los Inicios", "Bosque Oscuro");
        mapaGlobal.agregarArista("Bosque Oscuro", "Mazmorra del Piso 10");

        jugadoresOnline.agregar(lauti);
        jugadoresOnline.agregar(cuentaGian.getJugador());

        historialSubastas.insertar(90001L, new Transaccion(90001L, "ITM-701", 15000.0, System.currentTimeMillis() - 400000));
        historialSubastas.insertar(90002L, new Transaccion(90002L, "ITM-703", 45000.0, System.currentTimeMillis() - 200000));

        colaTicketsVIP.insertar(new Ticket(1001L, "ACC-02", 90001L, "Compré una espada pero se me desconectó el cliente y no la veo", 3), 3);
        colaTicketsVIP.insertar(new Ticket(1002L, "ACC-77", 90002L, "Perdí mis fondos y el escudo del Olimpo falló al reclamarse", 9), 9);

        gestorMisiones.registrarMision(new Mision(1, "Juntar 10 Hierbas Curativas", "Misión básica de recolección.", Mision.TipoMision.RECOLECCION_ESTANDAR, 1, "ITM-702"));
        gestorMisiones.registrarMision(new Mision(2, "Matar al Dragón Ancestral", "Derrotar al jefe supremo de la torre.", Mision.TipoMision.JEFE_MUNDO, 10, "ITM-701"));
        gestorMisiones.registrarMision(new Mision(3, "Festival de la Luna", "Evento temporal de recolección nocturna.", Mision.TipoMision.EVENTO_TEMPORAL, 3, "ITM-703"));
        gestorMisiones.registrarMision(new Mision(4, "Invasión de Orcos", "Frenar la oleada antes de que destruyan el campamento.", Mision.TipoMision.EVENTO_TEMPORAL, 5, "ITM-702"));

        gestorHabilidades.sincronizarHabilidadesAutomatica(lauti, arbolHabilidadesGlobal);
        gestorHabilidades.sincronizarHabilidadesAutomatica(cuentaGian.getJugador(), arbolHabilidadesGlobal);
    }

    public boolean darAltaCuenta(String id, String nombre, String clase) {
        if (indiceCuentas.buscar(id) != null) return false;
        Cuenta nuevaCuenta = new Cuenta(new Jugador(id, nombre, clase));
        gestorHabilidades.sincronizarHabilidadesAutomatica(nuevaCuenta.getJugador(), arbolHabilidadesGlobal);
        indiceCuentas.insertar(id, nuevaCuenta);
        return true;
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
        if (c == null) return 1;

        Item it = baseGlobalItems.buscar(idItem);
        if (it == null) return 2;

        c.getInventario().insertar(idItem, it);
        return 0;
    }

    public Cuenta buscarCuenta(String id) {
        return indiceCuentas.buscar(id);
    }

    public void registrarNuevaMision(int idM, String nomM, String descM, Mision.TipoMision tipoM, int pisoM, String recompensaM) {
        gestorMisiones.registrarMision(new Mision(idM, nomM, descM, tipoM, pisoM, recompensaM));
    }

    public int getCantidadMisionesPendientes() {
        return gestorMisiones.getCantidadMisionesPendientes();
    }

    public ResultadoDespacho procesarDespachoMision(String idCuenta) {
        ResultadoDespacho res = new ResultadoDespacho();
        Cuenta cuentaJugador = indiceCuentas.buscar(idCuenta);

        if (cuentaJugador == null) {
            res.exito = false;
            res.motivoError = "LA_CUENTA_NO_EXISTE";
            return res;
        }

        Jugador jugador = cuentaJugador.getJugador();
        Mision misionUrgente = gestorMisiones.procesarSiguienteMision();

        if (misionUrgente == null) {
            res.exito = false;
            res.motivoError = "NO_HAY_MISIONES_PENDIENTES";
            return res;
        }

        res.jugador = jugador;
        res.mision = misionUrgente;
        res.nivelAnterior = jugador.getNivel();

        if (jugador.getPisoActual() < misionUrgente.getPisoRequerido()) {
            res.exito = false;
            res.motivoError = "PISO_INSUFICIENTE";
            return res;
        }

        String idPremio = misionUrgente.getIdItemRecompensa();
        Item premioCatalogo = baseGlobalItems.buscar(idPremio);

        if (premioCatalogo != null) {
            cuentaJugador.getInventario().insertar(idPremio, premioCatalogo);
            res.premioOtorgado = premioCatalogo;
        }

        jugador.setNivel(res.nivelAnterior + 2);
        res.nivelNuevo = jugador.getNivel();

        gestorHabilidades.sincronizarHabilidadesAutomatica(jugador, arbolHabilidadesGlobal);
        res.exito = true;
        return res;
    }

    // --- ACCESORES DE DATOS PARA HITOS Y CONSULTAS ---

    public Cola<Jugador> ejecutarHito1() {
        Jugador[] posiblesCandidatos = {
                indiceCuentas.buscar("ACC-77").getJugador(),
                indiceCuentas.buscar("ACC-02").getJugador(),
                indiceCuentas.buscar("ACC-03").getJugador()
        };
        return sistemaViaje.solicitarViajeYArmarParty("Pueblo de los Inicios", "Mazmorra del Piso 10", posiblesCandidatos);
    }

    public boolean ejecutarHito2(String idItemCompensacion) {
        return sistemaSoporte.atenderProximoTicket(idItemCompensacion);
    }

    public Conjunto<Jugador> ejecutarHito3() {
        return sistemaAuditoria.auditarLideresGremio(gremioDemo, indiceCuentas);
    }

    public boolean ejecutarHito4(String idBuscado) {
        return sistemaComercio.revertirUltimaTransaccion(idBuscado);
    }

    public Cola<Cuenta> obtenerCuentasInorden() {
        return indiceCuentas.obtenerInorden(); // Nota: Cambiar a obtainInorden o el nombre exacto de tu AVL
    }

    public Cola<Item> obtenerItemsInorden() {
        return baseGlobalItems.obtenerInorden();
    }

    public Grafo<String> getMapaGlobal() {
        return mapaGlobal;
    }

    public Transaccion buscarTransaccionArbolB(long idTransaccion) {
        return historialSubastas.buscar(idTransaccion);
    }

    public ArbolGenerico<ClaseHabilidad> getArbolHabilidadesGlobal() {
        return arbolHabilidadesGlobal;
    }

    public Gremio getGremioDemo() {
        return gremioDemo;
    }
}