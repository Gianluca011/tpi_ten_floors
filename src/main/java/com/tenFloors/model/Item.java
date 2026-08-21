package main.java.com.tenFloors.model;

/**
 * Implementación concreta de la entidad Item para su uso en el simulador de juego.
 */
public class Item {

    private final String idItem;
    private final String nombre;
    private final String rareza;

    public Item(String idItem, String nombre, String rareza) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.rareza = rareza;
    }

    public String getId() {
        return this.idItem;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRareza() {
        return rareza;
    }

    public void usar() {
        System.out.println("[SISTEMA] Has equipado/utilizado el ítem: " + getNombre());
    }

    public String toString() {
        return "[" + idItem + "] " + nombre + " (" + rareza + ")";
    }
}