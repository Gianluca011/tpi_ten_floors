package test.java.com.tenFloors.tda.pila;

import main.java.com.tenFloors.tda.pila.Pila;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias automatizadas para el TDA Pila utilizando JUnit 5 (Jupiter v5.14.0).
 * Diseñado para verificar de forma aislada el comportamiento LIFO y la robustez del manejo de memoria.
 */
public class PilaTest {

    private Pila<String> pilaTest;

    @BeforeEach
    public void setUp() {
        this.pilaTest = new Pila<>();
    }

    @Test
    public void testPilaInicialEstaVacia() {
        assertTrue(pilaTest.estaVacia(), "La pila recién instanciada debería estar vacía.");
        assertEquals(0, pilaTest.getTamanio(), "El tamaño inicial de la pila debe ser 0.");
    }

    @Test
    public void testApilarIncrementaTamanioYCambiaTope() {
        pilaTest.apilar("Menú Principal");

        assertFalse(pilaTest.estaVacia(), "La pila no debería estar vacía tras un apilamiento.");
        assertEquals(1, pilaTest.getTamanio(), "El tamaño de la pila debería ser 1.");
        assertEquals("Menú Principal", pilaTest.verTope(), "El tope no coincide con el último elemento apilado.");
    }

    @Test
    public void testComportamientoLIFOCorrecto() {
        pilaTest.apilar("Menú Principal");
        pilaTest.apilar("Opciones de Inventario");
        pilaTest.apilar("Detalle de Ítem");

        // El último en entrar debe ser el primero en salir
        assertEquals(3, pilaTest.getTamanio());
        assertEquals("Detalle de Ítem", pilaTest.desapilar(), "No se respetó el orden LIFO al desapilar el primer elemento.");

        assertEquals(2, pilaTest.getTamanio());
        assertEquals("Opciones de Inventario", pilaTest.verTope(), "El tope no se actualizó correctamente tras desapilar.");
        assertEquals("Opciones de Inventario", pilaTest.desapilar());

        assertEquals(1, pilaTest.getTamanio());
        assertEquals("Menú Principal", pilaTest.desapilar());

        assertTrue(pilaTest.estaVacia(), "La pila debería haber quedado vacía tras retirar todos los elementos.");
    }

    @Test
    public void testDesapilarEnPilaVaciaLanzaExcepcion() {
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            pilaTest.desapilar();
        }, "Debería lanzar IllegalStateException al intentar desapilar en una pila vacía.");

        assertEquals("[ERROR] La pila de navegacion esta vacia.", excepcion.getMessage(), "El mensaje de error no coincide.");
    }

    @Test
    public void testVerTopeEnPilaVaciaLanzaExcepcion() {
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            pilaTest.verTope();
        }, "Debería lanzar IllegalStateException al intentar ver el tope de una pila vacía.");

        assertEquals("[ERROR] No hay elementos en el tope.", excepcion.getMessage(), "El mensaje de error no coincide.");
    }

    @Test
    public void testSoportaTiposPrimitivosMedianteWrappers() {
        Pila<Integer> pilaEnteros = new Pila<>();
        pilaEnteros.apilar(100); // ID de transacción simulación
        pilaEnteros.apilar(200);

        assertEquals(200, pilaEnteros.verTope());
        assertEquals(2, pilaEnteros.getTamanio());
    }
}
