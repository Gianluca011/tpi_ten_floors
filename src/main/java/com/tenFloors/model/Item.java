package main.java.com.tenFloors.model;

/**
 * Representa la entidad base de cualquier objeto almacenable.
 * Ya no depende de ninguna interfaz externa de identificación.
 */
public abstract class Item {

    private final String idItem;
    private final String nombre;
    private final String rareza;

    public Item(String idItem, String nombre, String rareza) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.rareza = rareza;
    }

    // Mantenemos el metodo para obtener el ID, pero ahora retorna String
    public String getId() {
        return this.idItem;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRareza() {
        return rareza;
    }

    /**
     * METODO ABSTRACTO: Toda clase hija (Espada, Pocion, etc.)
     * está OBLIGADA a definir qué pasa cuando el jugador usa el ítem.
     */
    public abstract void usar();

    @Override
    public String toString() {
        return "[" + idItem + "] " + nombre + " (" + rareza + ")";
    }
}