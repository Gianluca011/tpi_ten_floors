package test.java.com.tenFloors.tda.colaPrioridad;

import main.java.com.tenFloors.model.Item;
import main.java.com.tenFloors.model.Mision;
import main.java.com.tenFloors.model.Mision.TipoMision;
import main.java.com.tenFloors.tda.abb.ArbolABB;
import main.java.com.tenFloors.tda.colaPrioridad.ColaPrioridad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - TDA Cola con Prioridad (Ordenada)")
public class ColaPrioridadTest {

    private ColaPrioridad<String> colaStrings;
    private static final ArbolABB<Item> baseGlobalItems = new ArbolABB<>();
    private ColaPrioridad<Mision> colaMisiones;

    @BeforeEach
    public void setUp() {
        this.colaStrings = new ColaPrioridad<>();
        this.colaMisiones = new ColaPrioridad<>();
    }

    @Test
    @DisplayName("Una cola nueva debe estar vacía y con tamaño cero")
    public void testColaInicialVacia() {
        assertTrue(colaStrings.estaVacio(), "La cola debería inicializarse vacía.");
        assertEquals(0, colaStrings.getTamanio(), "El tamaño inicial debería ser 0.");
    }

    @Test
    @DisplayName("Debe ordenar correctamente sin importar el orden de inserción (Caso General)")
    public void testInsercionYOrdenamiento() {
        // Insertamos desordenado
        colaStrings.insertar("Prioridad Media", 5);
        colaStrings.insertar("Prioridad Baja", 1);
        colaStrings.insertar("Prioridad Alta", 10);
        colaStrings.insertar("Prioridad Media-Alta", 7);

        assertEquals(4, colaStrings.getTamanio());
        assertFalse(colaStrings.estaVacio());

        // Verificamos que se extraigan estrictamente en orden descendente de prioridad
        assertEquals("Prioridad Alta", colaStrings.extraerMaximo());
        assertEquals("Prioridad Media-Alta", colaStrings.extraerMaximo());
        assertEquals("Prioridad Media", colaStrings.extraerMaximo());
        assertEquals("Prioridad Baja", colaStrings.extraerMaximo());

        assertTrue(colaStrings.estaVacio());
    }

    @Test
    @DisplayName("Debe respetar el orden FIFO o mantener la estabilidad ante prioridades idénticas")
    public void testPrioridadesDuplicadas() {
        colaStrings.insertar("Primera de prioridad 5", 5);
        colaStrings.insertar("Segunda de prioridad 5", 5);
        colaStrings.insertar("Urgente", 10);

        assertEquals("Urgente", colaStrings.extraerMaximo());
        // Al recorrer con '>=', el nuevo nodo con prioridad igual se encola detrás del existente
        assertEquals("Primera de prioridad 5", colaStrings.extraerMaximo());
        assertEquals("Segunda de prioridad 5", colaStrings.extraerMaximo());
    }

    @Test
    @DisplayName("Debe lanzar NoSuchElementException al extraer de una cola vacía")
    public void testExtractMaxEnColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> {
            colaStrings.extraerMaximo();
        }, "Debería lanzar NoSuchElementException si no hay elementos.");
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al intentar insertar un elemento nulo")
    public void testInsertarNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            colaStrings.insertar(null, 10);
        }, "No se permite la inserción de objetos nulos.");
    }

    @Test
    @DisplayName("Integración con Dominio: Validar prioridad estricta de Jefes de Mundo y Eventos con sus Recompensas")
    public void testPrioridadMisionesDominio() {
        // Actualizados los constructores incorporando los IDs de ítems de recompensa simulados del catálogo
        Mision mEstandar1 = new Mision(1, "Matar 10 ratas", "Mochila llena", TipoMision.RECOLECCION_ESTANDAR, 1, "ITM-702");
        Mision mJefe = new Mision(2, "Ignis el Dragón", "Jefe de piso", TipoMision.JEFE_MUNDO, 10, "ITM-701");
        Mision mEvento = new Mision(3, "Invasión de Duendes", "Evento por tiempo", TipoMision.EVENTO_TEMPORAL, 3, "ITM-703");
        Mision mEstandar2 = new Mision(4, "Recolectar plantas", "Hierbas de curación", TipoMision.RECOLECCION_ESTANDAR, 2, "ITM-702");

        // Insertamos en un orden totalmente aleatorio
        colaMisiones.insertar(mEstandar1, mEstandar1.getTipo().getNivelPrioridad());
        colaMisiones.insertar(mJefe, mJefe.getTipo().getNivelPrioridad());
        colaMisiones.insertar(mEstandar2, mEstandar2.getTipo().getNivelPrioridad());
        colaMisiones.insertar(mEvento, mEvento.getTipo().getNivelPrioridad());

        // El primer extractMax() nos DEBE dar obligatoriamente el Jefe de Mundo (Prioridad 3)
        Mision primeraPrio = colaMisiones.extraerMaximo();
        assertEquals(TipoMision.JEFE_MUNDO, primeraPrio.getTipo());
        assertEquals("Ignis el Dragón", primeraPrio.getNombre());
        assertEquals("ITM-701", primeraPrio.getIdItemRecompensa(), "La recompensa del Jefe de Mundo debe ser la espada legendaria.");

        // El segundo extractMax() debe ser el Evento Temporal (Prioridad 2)
        Mision segundaPrio = colaMisiones.extraerMaximo();
        assertEquals(TipoMision.EVENTO_TEMPORAL, segundaPrio.getTipo());
        assertEquals("ITM-703", segundaPrio.getIdItemRecompensa());

        // Las últimas deben ser las de recolección estándar (Prioridad 1)
        assertEquals(TipoMision.RECOLECCION_ESTANDAR, colaMisiones.extraerMaximo().getTipo());
        assertEquals(TipoMision.RECOLECCION_ESTANDAR, colaMisiones.extraerMaximo().getTipo());

        assertTrue(colaMisiones.estaVacio());
    }
}