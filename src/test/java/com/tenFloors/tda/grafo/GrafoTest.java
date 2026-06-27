package test.java.com.tenFloors.tda.grafo;

import main.java.com.tenFloors.tda.grafo.Grafo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias automatizadas para el TDA Grafo utilizando JUnit 5 (Jupiter v5.14.0).
 * Diseñado para validar el correcto funcionamiento del mapa sin requerir ejecuciones en el Main.
 */
public class GrafoTest {

    private Grafo<String> grafoTest;

    @BeforeEach
    public void setUp() {
        this.grafoTest = new Grafo<>();
    }

    @Test
    public void testAgregarVerticesYContaje() {
        assertEquals(0, grafoTest.getCantidadVertices(), "El grafo inicial debería estar vacío.");

        grafoTest.agregarVertice("Piso 1 - Ciudad de Inicio");
        grafoTest.agregarVertice("Piso 2 - Zona de Caza");

        assertEquals(2, grafoTest.getCantidadVertices(), "El conteo de vértices registrados es incorrecto.");
    }

    @Test
    public void testAgregarVerticeDuplicadoNoIncrementaTamanio() {
        grafoTest.agregarVertice("Piso 10 - Sala del Jefe");
        grafoTest.agregarVertice("Piso 10 - Sala del Jefe"); // Intento de duplicación

        assertEquals(1, grafoTest.getCantidadVertices(), "El grafo no debería permitir zonas duplicadas con la misma clave.");
    }

    @Test
    public void testAgregarVerticeNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            grafoTest.agregarVertice(null);
        }, "Debería lanzar IllegalArgumentException al ingresar un vértice nulo.");
    }

    @Test
    public void testAgregarAristaConVerticesInexistentesLanzaExcepcion() {
        grafoTest.agregarVertice("Piso 1");

        assertThrows(NoSuchElementException.class, () -> {
            grafoTest.agregarArista("Piso 1", "Piso 99"); // Piso 99 no existe en el mapa
        }, "Debería lanzar NoSuchElementException si uno de los puntos del portal no existe.");
    }

    @Test
    public void testEjecucionCorrectaDeRecorridos() {
        grafoTest.agregarVertice("Piso 1");
        grafoTest.agregarVertice("Piso 2");
        grafoTest.agregarVertice("Piso 3");

        grafoTest.agregarArista("Piso 1", "Piso 2");
        grafoTest.agregarArista("Piso 2", "Piso 3");

        // Verificamos que los métodos BFS y DFS se ejecuten por completo sin lanzar NullPointerException o bucles infinitos
        assertDoesNotThrow(() -> grafoTest.bfs("Piso 1"), "El recorrido BFS falló durante la simulación de exploración.");
        assertDoesNotThrow(() -> grafoTest.dfs("Piso 1"), "El recorrido DFS falló durante la simulación de exploración.");
    }

    @Test
    public void testRecorridosConOrigenInexistenteLanzaExcepcion() {
        grafoTest.agregarVertice("Piso 1");

        assertThrows(NoSuchElementException.class, () -> grafoTest.bfs("Zona Inexistente"));
        assertThrows(NoSuchElementException.class, () -> grafoTest.dfs("Zona Inexistente"));
    }
}
