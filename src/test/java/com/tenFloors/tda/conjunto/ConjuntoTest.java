package test.java.com.tenFloors.tda.conjunto;

import main.java.com.tenFloors.model.Jugador;
import main.java.com.tenFloors.tda.conjunto.Conjunto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias automatizadas con JUnit 5 para el TDA Conjunto.
 * Verifica la robustez del registro de usuarios online y el control de colisiones.
 */
public class ConjuntoTest {

    private Conjunto<Jugador> conjuntoOnline;
    private Jugador jugador1;
    private Jugador jugador2;

    @BeforeEach
    public void setUp() {
        // Inicializamos el TDA y un par de instancias de prueba antes de cada test
        conjuntoOnline = new Conjunto<>();
        jugador1 = new Jugador("ACC-001", "Arthas");
        jugador2 = new Jugador("ACC-002", "Sylvanas");
    }

    @Test
    public void testAgregarJugadorExitoso() {
        assertTrue(conjuntoOnline.estaVacio(), "El conjunto debería arrancar vacío.");

        boolean seAgrego = conjuntoOnline.agregar(jugador1);

        assertTrue(seAgrego, "El jugador debería agregarse exitosamente por primera vez.");
        assertEquals(1, conjuntoOnline.getTamanio(), "El tamaño del conjunto debería ser 1.");
        assertTrue(conjuntoOnline.contiene(jugador1), "El conjunto debería contener al jugador ingresado.");
    }

    @Test
    public void testEvitarIniciosDeSesionDuplicados() {
        conjuntoOnline.agregar(jugador1);

        // Intentamos agregar una instancia distinta pero con el mismo ID de cuenta (Clon)
        Jugador clonArthas = new Jugador("ACC-001", "Arthas_Impostor");

        boolean seAgregoDuplicado = conjuntoOnline.agregar(clonArthas);

        assertFalse(seAgregoDuplicado, "El sistema NO debe permitir registrar un ID de cuenta que ya está online.");
        assertEquals(1, conjuntoOnline.getTamanio(), "El tamaño no debió incrementarse al rechazar el duplicado.");
    }

    @Test
    public void testEliminarJugador() {
        conjuntoOnline.agregar(jugador1);
        conjuntoOnline.agregar(jugador2);

        boolean seElimino = conjuntoOnline.eliminar(jugador1);

        assertTrue(seElimino, "Debería retornar true al eliminar un jugador que sí estaba online.");
        assertFalse(conjuntoOnline.contiene(jugador1), "El jugador ya no debería figurar en el conjunto.");
        assertTrue(conjuntoOnline.contiene(jugador2), "El otro jugador no debería verse afectado.");
        assertEquals(1, conjuntoOnline.getTamanio(), "El tamaño final debería decrementarse a 1.");
    }

    @Test
    public void testEliminarJugadorInexistente() {
        conjuntoOnline.agregar(jugador1);

        boolean seElimino = conjuntoOnline.eliminar(jugador2); // Sylvanas nunca entró

        assertFalse(seElimino, "Debería retornar false al intentar eliminar a alguien que no inició sesión.");
        assertEquals(1, conjuntoOnline.getTamanio(), "El tamaño debe permanecer intacto.");
    }

    @Test
    public void testLanzarExcepcionConElementoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            conjuntoOnline.agregar(null);
        }, "Debe arrojar IllegalArgumentException si se intenta inyectar un null.");
    }

    @Test
    public void testComportamientoDelRehashDinamico() {
        // La capacidad inicial es 16 y el factor de carga es 0.75 (Límite: 12 elementos antes del Rehash)
        // Insertamos 20 jugadores de forma masiva para forzar al menos un ciclo completo de Rehash
        for (int i = 1; i <= 20; i++) {
            Jugador j = new Jugador("ACC-ID-" + i, "PlayerName_" + i);
            boolean insertado = conjuntoOnline.agregar(j);
            assertTrue(insertado, "Cada jugador masivo individual debería insertarse de forma única.");
        }

        assertEquals(20, conjuntoOnline.getTamanio(), "La tabla debió redimensionarse y contener los 20 registros.");

        // Verificamos que el primer y el último elemento sigan siendo perfectamente accesibles en O(1)
        assertTrue(conjuntoOnline.contiene(new Jugador("ACC-ID-1", "PlayerName_1")), "El primer registro debe persistir tras el rehash.");
        assertTrue(conjuntoOnline.contiene(new Jugador("ACC-ID-20", "PlayerName_20")), "El último registro insertado debe validarse correctamente.");
    }
}