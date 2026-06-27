package test.java.com.tenFloors.tda.arbol;

import main.java.com.tenFloors.model.ClaseHabilidad;
import main.java.com.tenFloors.tda.arbol.ArbolGenerico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

public class ArbolGenericoTest {

    private ArbolGenerico<ClaseHabilidad> arbolProgrecion;
    private ClaseHabilidad swordsman;
    private ClaseHabilidad activas;
    private ClaseHabilidad pasivas;
    private ClaseHabilidad corteVertical;

    @BeforeEach
    public void setUp() {
        arbolProgrecion = new ArbolGenerico<>();
        swordsman = new ClaseHabilidad("Swordsman", "CLASE", 1);
        activas = new ClaseHabilidad("Habilidades Activas", "CATEGORIA", 1);
        pasivas = new ClaseHabilidad("Habilidades Pasivas", "CATEGORIA", 1);
        corteVertical = new ClaseHabilidad("Corte Vertical", "HABILIDAD", 5);
    }

    @Test
    public void testAgregarRaizCuandoArbolEstaVacio() {
        arbolProgrecion.agregarHijo(null, swordsman);
        assertFalse(arbolProgrecion.estaVacio());
        assertEquals(swordsman, arbolProgrecion.getRaiz().getDato());
    }

    @Test
    public void testAgregarHijosMultiplesYVerificarJerarquia() {
        arbolProgrecion.agregarHijo(null, swordsman);
        arbolProgrecion.agregarHijo(swordsman, activas);
        arbolProgrecion.agregarHijo(swordsman, pasivas);
        arbolProgrecion.agregarHijo(activas, corteVertical);

        // La raíz debe seguir siendo Swordsman
        assertEquals(swordsman, arbolProgrecion.getRaiz().getDato());

        // El primer hijo de la raíz debe ser la categoría de activas
        assertEquals(activas, arbolProgrecion.getRaiz().getPrimerHijo().getDato());

        // El hermano del primer hijo debe ser la categoría de pasivas (LCRS)
        assertEquals(pasivas, arbolProgrecion.getRaiz().getPrimerHijo().getSiguienteHermano().getDato());
    }

    @Test
    public void testAgregarHijoAPadreInexistenteLanzaExcepcion() {
        arbolProgrecion.agregarHijo(null, swordsman);
        ClaseHabilidad magoInexistente = new ClaseHabilidad("Mage", "CLASE", 1);

        assertThrows(NoSuchElementException.class, () -> {
            arbolProgrecion.agregarHijo(magoInexistente, activas);
        });
    }

    @Test
    public void testAgregarHijoNuloLanzaExcepcion() {
        arbolProgrecion.agregarHijo(null, swordsman);

        assertThrows(IllegalArgumentException.class, () -> {
            arbolProgrecion.agregarHijo(swordsman, null);
        });
    }

    @Test
    public void testAgregarRaizConPadreNoNuloLanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> {
            arbolProgrecion.agregarHijo(activas, swordsman);
        });
    }
}
