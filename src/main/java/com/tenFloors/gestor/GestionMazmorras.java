package main.java.com.tenFloors.gestor;

import main.java.com.tenFloors.model.Jugador;
import main.java.com.tenFloors.tda.cola.Cola;

/**
 * Controla el flujo de ingreso a las instancias de las mazmorras del juego.
 */
public class GestionMazmorras {
    private final Cola<Jugador> salaDeEspera;
    private final String nombreMazmorra;
    private final int capacidadMaxima;

    public GestionMazmorras(String nombreMazmorra, int capacidadMaxima) {
        this.salaDeEspera = new Cola<>();
        this.nombreMazmorra = nombreMazmorra;
        this.capacidadMaxima = capacidadMaxima;
    }

    /**
     * Añade un jugador a la cola de espera.
     */
    public void anotarEnFila(Jugador jugador) {
        System.out.println("[Cola] Añadiendo a " + jugador.getNombre() + " a la fila de " + nombreMazmorra);
        salaDeEspera.encolar(jugador);
    }

    /**
     * Despacha al siguiente grupo de jugadores si hay suficientes en fila.
     */
    public void iniciarInstancia() {
        if (salaDeEspera.getTamanio() < capacidadMaxima) {
            System.out.println("[Cola] Faltan jugadores. Actualmente en fila: " + salaDeEspera.getTamanio() + "/" + capacidadMaxima);
            return;
        }

        System.out.println("--- ¡La Mazmorra " + nombreMazmorra + " está iniciando! ---");
        for (int i = 0; i < capacidadMaxima; i++) {
            Jugador jugadorAdentro = salaDeEspera.desencolar();
            System.out.println("-> " + jugadorAdentro.getNombre() + " ha ingresado a la sala del Boss.");
        }
    }

    public void mostrarSiguienteEnFila() {
        if (salaDeEspera.estaVacia()) {
            System.out.println("No hay nadie esperando para esta mazmorra.");
        } else {
            System.out.println("El próximo jugador en ingresar será: " + salaDeEspera.frente().getNombre());
        }
    }
}