package main.java.com.tenFloors.model;

/**
 * Implementación concreta de la entidad Item para su uso en el simulador de juego.
 */
public class ItemJuego extends Item {

    public ItemJuego(String idItem, String nombre, String rareza) {
        super(idItem, nombre, rareza);
    }

    @Override
    public void usar() {
        System.out.println("[SISTEMA] Has equipado/utilizado el ítem: " + getNombre());
    }
}