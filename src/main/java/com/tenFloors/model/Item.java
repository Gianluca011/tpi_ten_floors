package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.avl.IIdentificable;

/**
 * Representa la entidad base de cualquier objeto almacenable.
 * Implementa IIdentificable para permitir la gestión en el Árbol ABB.
 */
public abstract class Item implements IIdentificable {

    // Atributos definidos como final para asegurar que una vez
    // creado el ítem, sus propiedades básicas no se alteren.
    private final int idItem;
    private final String nombre;
    private final String rareza;

    public Item(int idItem, String nombre, String rareza) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.rareza = rareza;
    }

    // --- CONTRATO DE IDENTIFICACIÓN ---
    // Este método es el que permite que el Árbol ABB (GestorInventario)
    // sepa cómo comparar este objeto con otros.
    @Override
    public int getId() {
        return this.idItem;
    }

    // --- GETTERS ---
    public String getNombre() {
        return nombre;
    }

    public String getRareza() {
        return rareza;
    }

    /**
     * MÉTODO ABSTRACTO: Toda clase hija (Espada, Pocion, etc.)
     * está OBLIGADA a definir qué pasa cuando el jugador usa el ítem.
     */
    public abstract void usar();

    @Override
    public String toString() {
        return "[" + idItem + "] " + nombre + " (" + rareza + ")";
    }
}