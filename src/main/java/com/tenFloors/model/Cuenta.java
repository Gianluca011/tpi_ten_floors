package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.abb.ArbolABB;
import main.java.com.tenFloors.tda.pila.Pila;

/**
 * Representa la cuenta global de un usuario indexada en el AVL.
 * Agrupa de forma estructurada el perfil del jugador, su mochila (ABB) y su historial (Pila).
 */
public class Cuenta {
    private final Jugador jugador;
    private final ArbolABB<Item> inventario;
    private final Pila<Transaccion> historialComercio;

    public Cuenta(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador asociado a la cuenta no puede ser nulo.");
        }
        this.jugador = jugador;
        this.inventario = new ArbolABB<>();
        this.historialComercio = new Pila<>();
    }

    public Jugador getJugador() {
        return jugador;
    }

    public ArbolABB<Item> getInventario() {
        return inventario;
    }

    public Pila<Transaccion> getHistorialComercio() {
        return historialComercio;
    }
}