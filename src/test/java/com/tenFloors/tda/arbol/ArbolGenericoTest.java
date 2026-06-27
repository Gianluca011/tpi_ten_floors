package test.java.com.tenFloors.tda.arbol;

import main.java.com.tenFloors.model.ClaseHabilidad;
import main.java.com.tenFloors.tda.arbol.ArbolGenerico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

public class ArbolGenericoTest {

    private ArbolGenerico<ClaseHabilidad> arbolProgresion;
    private ClaseHabilidad swordsman;
    private ClaseHabilidad activas;
    private ClaseHabilidad pasivas;
    private ClaseHabilidad corteVertical;

    @BeforeEach
    public void setUp() {
        arbolProgresion = new ArbolGenerico<>();
        swordsman = new ClaseHabilidad("Swordsman", "CLASE", 1);
        activas = new ClaseHabilidad("Habilidades Activas", "CATEGORIA", 1);
        pasivas = new ClaseHabilidad("Habilidades Pasivas", "CATEGORIA", 1);
        corteVertical = new ClaseHabilidad("Corte Vertical", "HABILIDAD", 5);
    }

    @Test
    public void testAgregarRaizCuandoArbolEstaVacio() {
        arbolProgresion.agregarHijo(null, swordsman);
        assertFalse(arbolProgresion.estaVacio());
        assertEquals(swordsman, arbolProgresion.getRaiz().getDato());
    }

    @Test
    public void testAgregarHijosMultiplesYVerificarJerarquia() {
        arbolProgresion.agregarHijo(null, swordsman);
        arbolProgresion.agregarHijo(swordsman, activas);
        arbolProgresion.agregarHijo(swordsman, pasivas);
        arbolProgresion.agregarHijo(activas, corteVertical);

        // La raíz debe seguir siendo Swordsman
        assertEquals(swordsman, arbolProgresion.getRaiz().getDato());

        // El primer hijo de la raíz debe ser la categoría de activas
        assertEquals(activas, arbolProgresion.getRaiz().getPrimerHijo().getDato());

        // El hermano del primer hijo debe ser la categoría de pasivas (LCRS)
        assertEquals(pasivas, arbolProgresion.getRaiz().getPrimerHijo().getSiguienteHermano().getDato());
    }

    @Test
    public void testAgregarHijoAPadreInexistenteLanzaExcepcion() {
        arbolProgresion.agregarHijo(null, swordsman);
        ClaseHabilidad magoInexistente = new ClaseHabilidad("Mage", "CLASE", 1);

        assertThrows(NoSuchElementException.class, () -> {
            arbolProgresion.agregarHijo(magoInexistente, activas);
        });
    }

    @Test
    public void testAgregarHijoNuloLanzaExcepcion() {
        arbolProgresion.agregarHijo(null, swordsman);

        assertThrows(IllegalArgumentException.class, () -> {
            arbolProgresion.agregarHijo(swordsman, null);
        });
    }

    @Test
    public void testAgregarRaizConPadreNoNuloLanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> {
            arbolProgresion.agregarHijo(activas, swordsman);
        });
    }
}
