package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.abb.ArbolABB;
import java.util.List;

public class GestorInventario {

    private final ArbolABB<Item> arbolMochila;

    public GestorInventario() {
        this.arbolMochila = new ArbolABB<>();
    }

    // --- ALTA DE ÍTEMS ---
    public void agregarItem(Item nuevoItem) {
        if (nuevoItem == null) {
            throw new IllegalArgumentException("El ítem no puede ser nulo.");
        }

        // Pasamos el ID (String) y el objeto ítem por separado al ABB
        arbolMochila.insertar(nuevoItem.getId(), nuevoItem);

        System.out.println("[ABB] Has recogido: " + nuevoItem.getNombre());
    }

    // --- BAJA DE ÍTEMS ---
    public void descartarItem(String idItem) {
        // Primero verificamos si realmente existe mediante su ID de texto
        Item itemBuscado = arbolMochila.buscar(idItem);

        if (itemBuscado != null) {
            arbolMochila.eliminar(idItem);
            System.out.println("[ABB] Has descartado: " + itemBuscado.getNombre());
        } else {
            System.out.println("[ABB] El ítem no existe en tu inventario.");
        }
    }

    // --- BÚSQUEDA RÁPIDA ---
    public Item buscarItem(String idItem) {
        return arbolMochila.buscar(idItem);
    }

    // --- LISTADO ORDENADO ---
    public void mostrarMochila() {
        System.out.println("\n--- INVENTARIO DEL JUGADOR ---");

        // El recorrido Inorden nos garantiza el orden alfabético de los IDs de los ítems
        List<Item> itemsOrdenados = arbolMochila.obtenerInorden();

        if (itemsOrdenados.isEmpty()) {
            System.out.println("La mochila está vacía.");
            return;
        }

        for (Item item : itemsOrdenados) {
            System.out.println(" -> " + item.toString());
        }
    }
}