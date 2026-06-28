package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.grafo.Grafo;
import main.java.com.tenFloors.tda.conjunto.Conjunto;
import main.java.com.tenFloors.tda.cola.Cola;

public class SistemaViajeParty {

    private final Grafo<String> mapaMundo;
    private final Conjunto<Jugador> jugadoresOnline; // Diccionario

    public SistemaViajeParty(Grafo<String> mapaMundo, Conjunto<Jugador> jugadoresOnline) {
        this.mapaMundo = mapaMundo;
        this.jugadoresOnline = jugadoresOnline;
    }

    // Verifica la ruta hacia la zona destino y arma una Party con los jugadores online.
    public Cola<Jugador> solicitarViajeYArmarParty(String zonaOrigen, String zonaDestino, Jugador[] candidatos) {
        System.out.println("\n[SISTEMA] Iniciando protocolo de Viaje Rápido desde '" + zonaOrigen + "' hacia '" + zonaDestino + "'...");

        // 1. GRAFO: Verificamos las conexiones de la zona usando el BFS
        System.out.println("[GRAFO] Trazando rutas disponibles:");
        mapaMundo.bfs(zonaOrigen);

        // 2. COLA: Inicializamos la sala de espera para la Party
        Cola<Jugador> partyQueue = new Cola<>();

        System.out.println("\n[SISTEMA] Escaneando jugadores cercanos para armar la Party...");

        // 3. CONJUNTO: Verificamos quiénes de los candidatos están online y los encolamos
        for (Jugador jugador : candidatos) {
            if (jugador != null) {
                // Buscamos en el Conjunto en tiempo constante O(1)
                if (jugadoresOnline.contiene(jugador)) {
                    System.out.println("[CONJUNTO] Jugador " + jugador.getNombre() + " está ONLINE. Añadiendo a la Party.");
                    partyQueue.encolar(jugador); // Encolamos
                } else {
                    System.out.println("[CONJUNTO] Jugador " + jugador.getNombre() + " está OFFLINE. Ignorado.");
                }
            }
        }

        return partyQueue;
    }
}