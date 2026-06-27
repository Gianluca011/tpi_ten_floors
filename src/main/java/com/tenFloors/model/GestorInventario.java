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
        // Validación Defensiva (Estilo Lautaro): Si nos pasan un objeto nulo,
        // cortamos la ejecución tirando una excepción en vez de que el programa explote más adelante.
        if (nuevoItem == null) {
            throw new IllegalArgumentException("El ítem no puede ser nulo.");
        }
        // Llamamos al método matemático del TDA
        arbolMochila.insertar(nuevoItem);
        // Mensaje de consola homologado para el usuario
        System.out.println("[ABB] Has recogido: " + nuevoItem.getNombre());
    }

    // --- BAJA DE ÍTEMS ---
    public void descartarItem(int idItem) {
        // Primero verificamos si realmente existe en tiempo O(log n)
        Item itemBuscado = arbolMochila.buscar(idItem);

        if (itemBuscado != null) {
            // Si existe, lo borramos del árbol (contemplando los 3 casos de eliminación)
            arbolMochila.eliminar(idItem);
            System.out.println("[ABB] Has descartado: " + itemBuscado.getNombre());
        } else {
            System.out.println("[ABB] El ítem no existe en tu inventario.");
        }
    }

    // --- BÚSQUEDA RÁPIDA ---
    public Item buscarItem(int idItem) {
        return arbolMochila.buscar(idItem);
    }

    // --- LISTADO ORDENADO ---
    public void mostrarMochila() {
        System.out.println("\n--- INVENTARIO DEL JUGADOR ---");

        // Le pedimos al TDA que haga un recorrido Inorden (Izquierda -> Raíz -> Derecha).
        // Esto nos garantiza que la lista devuelta ya viene ordenada de menor a mayor ID.
        List<Item> itemsOrdenados = arbolMochila.obtenerInorden();

        if (itemsOrdenados.isEmpty()) {
            System.out.println("La mochila está vacía.");
            return;
        }

        // Recorremos la lista ya procesada y la imprimimos prolijamente
        for (Item item : itemsOrdenados) {
            System.out.println(" -> " + item.toString());
        }
    }
}