package test.java.com.tenFloors.tda.abb;

import main.java.com.tenFloors.model.Item;
import main.java.com.tenFloors.tda.abb.ArbolABB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ArbolABBTest {

    private ArbolABB<Item> mochila;

    // Clase concreta interna para testeo rápido sin depender de subclases de Item
    private static class ItemPrueba extends Item {
        public ItemPrueba(int id, String nombre, String rareza) { super(id, nombre, rareza); }
        @Override public void usar() {}
    }

    @BeforeEach
    public void setUp() {
        mochila = new ArbolABB<>();
    }

    @Test
    public void testInsercionYOrdenamientoInorden() {
        // Insertamos ítems en orden aleatorio
        mochila.insertar(new ItemPrueba(50, "Espada", "Comun"));
        mochila.insertar(new ItemPrueba(20, "Pocion", "Comun"));
        mochila.insertar(new ItemPrueba(80, "Escudo", "Raro"));

        List<Item> resultado = mochila.obtenerInorden();

        // Verificamos que el recorrido Inorden los ordene por ID
        assertEquals(3, resultado.size());
        assertEquals(20, resultado.get(0).getId());
        assertEquals(50, resultado.get(1).getId());
        assertEquals(80, resultado.get(2).getId());
    }

    @Test
    public void testBusquedaYEliminacion() {
        mochila.insertar(new ItemPrueba(10, "Item10", "C"));
        mochila.insertar(new ItemPrueba(5, "Item5", "C"));

        // Buscamos un nodo existente
        assertNotNull(mochila.buscar(10));

        // Eliminamos un nodo (Caso 1 o 2)
        mochila.eliminar(5);
        assertNull(mochila.buscar(5), "El ítem con ID 5 debería haber sido eliminado.");
        assertNotNull(mochila.buscar(10), "El ítem con ID 10 debería seguir existiendo.");
    }
}