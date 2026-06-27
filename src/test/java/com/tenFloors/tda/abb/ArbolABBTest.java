package test.java.com.tenFloors.tda.abb;

import main.java.com.tenFloors.model.Item;
import main.java.com.tenFloors.tda.abb.ArbolABB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ArbolABBTest {

    private ArbolABB<Item> mochila;

    // Clase concreta interna adaptada al constructor con String ID
    private static class ItemPrueba extends Item {
        public ItemPrueba(String id, String nombre, String rareza) { super(id, nombre, rareza); }
        @Override public void usar() {}
    }

    @BeforeEach
    public void setUp() {
        mochila = new ArbolABB<>();
    }

    @Test
    public void testInsercionYOrdenamientoInorden() {
        Item espada = new ItemPrueba("50", "Espada", "Comun");
        Item pocion = new ItemPrueba("20", "Pocion", "Comun");
        Item escudo = new ItemPrueba("80", "Escudo", "Raro");

        // Insertamos pasando (ID, Objeto) de forma explícita
        mochila.insertar(espada.getId(), espada);
        mochila.insertar(pocion.getId(), pocion);
        mochila.insertar(escudo.getId(), escudo);

        List<Item> resultado = mochila.obtenerInorden();

        // Verificamos que el recorrido Inorden los ordene alfabéticamente por ID
        assertEquals(3, resultado.size());
        assertEquals("20", resultado.get(0).getId());
        assertEquals("50", resultado.get(1).getId());
        assertEquals("80", resultado.get(2).getId());
    }

    @Test
    public void testBusquedaYEliminacion() {
        Item item1 = new ItemPrueba("10", "Item10", "C");
        Item item2 = new ItemPrueba("05", "Item5", "C");

        mochila.insertar(item1.getId(), item1);
        mochila.insertar(item2.getId(), item2);

        // Buscamos usando String ID
        assertNotNull(mochila.buscar("10"));

        // Eliminamos usando String ID
        mochila.eliminar("05");
        assertNull(mochila.buscar("05"), "El ítem con ID '05' debería haber sido eliminado.");
        assertNotNull(mochila.buscar("10"), "El ítem con ID '10' debería seguir existiendo.");
    }
}