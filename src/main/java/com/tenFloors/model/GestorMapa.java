package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.grafo.Grafo;

/**
 * Sistema de gestión sincrono encargado de administrar el mapa del mundo (pisos y portales) de Ten Floors.
 */
public class GestorMapa {

    private final Grafo<String> mapaMundo;

    public GestorMapa() {
        this.mapaMundo = new Grafo<>();
    }

    /**
     * Registra un nuevo piso o zona dentro de la torre colosal.
     */
    public void registrarZona(String nombreZona) {
        if (nombreZona == null || nombreZona.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la zona a registrar no puede ser nulo o vacío.");
        }
        mapaMundo.agregarVertice(nombreZona);
    }

    /**
     * Establece un portal de teletransporte bidireccional activo entre dos zonas de la torre.
     */
    public void conectarZonas(String zonaOrigen, String zonaDestino) {
        // El TDA Grafo ya valida internamente la existencia de ambos vértices
        mapaMundo.agregarArista(zonaOrigen, zonaDestino);
    }

    /**
     * Ejecuta y muestra la exploración en anchura (BFS) para trazar rutas óptimas.
     */
    public void simularExploracionBFS(String zonaInicio) {
        mapaMundo.bfs(zonaInicio);
    }

    /**
     * Ejecuta y muestra la exploración en profundidad (DFS) para auditoría de zonas ocultas.
     */
    public void simularExploracionDFS(String zonaInicio) {
        mapaMundo.dfs(zonaInicio);
    }

    /**
     * Retorna la cantidad actual de zonas dadas de alta en el mapa del servidor.
     */
    public int getCantidadZonasRegistradas() {
        return mapaMundo.getCantidadVertices();
    }
}
