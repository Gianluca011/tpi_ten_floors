package test.java.com.tenFloors.tda.cola;

import main.java.com.tenFloors.model.Jugador;
import main.java.com.tenFloors.tda.cola.Cola;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias automatizadas para el TDA Cola utilizando JUnit 5.
 * Verifica el comportamiento síncrono y los contratos del flujo FIFO.
 */
public class ColaTest {

    private Cola<Jugador> salaEspera;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;

    @BeforeEach
    public void setUp() {
        // Inicializamos una cola limpia y datos de prueba antes de cada test
        salaEspera = new Cola<>();
        jugador1 = new Jugador("PJ00001", "Gian_Swordsman", "ESPADACHIN");
        jugador2 = new Jugador("PJ00002", "Lau_Wizard","MAGO");
        jugador3 = new Jugador("PJ00003", "Axel_Tank", "TANQUE");
    }

    @Test
    public void testColaInicialmenteVacia() {
        assertTrue(salaEspera.estaVacia(), "La cola recién creada debería estar vacía.");
        assertEquals(0, salaEspera.getTamanio(), "El tamaño inicial de la cola debería ser 0.");
    }

    @Test
    public void testencolarIncrementaTamanioYModificaEstado() {
        salaEspera.encolar(jugador1);

        assertFalse(salaEspera.estaVacia(), "La cola no debería estar vacía tras un encolar.");
        assertEquals(1, salaEspera.getTamanio(), "El tamaño de la cola debería ser 1.");
        assertEquals(jugador1, salaEspera.frente(), "El frente de la cola debería ser el jugador ingresado.");
    }

    @Test
    public void testComportamientoFIFO() {
        // Encolamos en orden: 1, 2, 3
        salaEspera.encolar(jugador1);
        salaEspera.encolar(jugador2);
        salaEspera.encolar(jugador3);

        assertEquals(3, salaEspera.getTamanio(), "La cola debería contener 3 elementos.");

        // Desencolamos y verificamos que se respete estrictamente el orden de llegada
        assertEquals(jugador1, salaEspera.desencolar(), "El primero en salir debe ser jugador1 (FIFO).");
        assertEquals(jugador2, salaEspera.desencolar(), "El segundo en salir debe ser jugador2 (FIFO).");
        assertEquals(jugador3, salaEspera.desencolar(), "El tercero en salir debe ser jugador3 (FIFO).");

        assertTrue(salaEspera.estaVacia(), "La cola debería quedar vacía tras extraer todos los elementos.");
        assertEquals(0, salaEspera.getTamanio(), "El tamaño final debería volver a ser 0.");
    }

    @Test
    public void testFrenteNoModificaLaEstructura() {
        salaEspera.encolar(jugador1);
        salaEspera.encolar(jugador2);

        // Consultamos el frente múltiples veces
        Jugador primeroConsulta1 = salaEspera.frente();
        Jugador primeroConsulta2 = salaEspera.frente();

        assertEquals(jugador1, primeroConsulta1, "El frente debe ser jugador1.");
        assertEquals(jugador1, primeroConsulta2, "Llamar a frente() no debe alterar quién está al principio.");
        assertEquals(2, salaEspera.getTamanio(), "Llamar a frente() no debe alterar el tamaño de la cola.");
    }

    @Test
    public void testExcepcionAlDesencolarColaVacia() {
        assertThrows(NoSuchElementException.class, () -> {
            salaEspera.desencolar();
        }, "Debería lanzar NoSuchElementException al intentar desencolar de una cola vacía.");
    }

    @Test
    public void testExcepcionAlVerFrenteEnColaVacia() {
        assertThrows(NoSuchElementException.class, () -> {
            salaEspera.frente();
        }, "Debería lanzar NoSuchElementException al intentar inspeccionar el frente de una cola vacía.");
    }

    @Test
    public void testExcepcionAlEncolarElementoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            salaEspera.encolar(null);
        }, "Debería lanzar IllegalArgumentException al intentar encolar un objeto nulo.");
    }

    @Test
    public void testReutilizacionDeCola() {
        // Flujo de llenado y vaciado continuo para comprobar la estabilidad de los punteros internos
        salaEspera.encolar(jugador1);
        salaEspera.desencolar();

        assertTrue(salaEspera.estaVacia());

        salaEspera.encolar(jugador2);
        salaEspera.encolar(jugador3);

        assertEquals(2, salaEspera.getTamanio());
        assertEquals(jugador2, salaEspera.frente());
    }
}