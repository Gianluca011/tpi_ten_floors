package test.java.com.tenFloors.tda.arbolB;

import main.java.com.tenFloors.tda.arbolB.ArbolB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias para el TDA Árbol B")
class ArbolBTest {

    private ArbolB<String> arbol;

    @BeforeEach
    void setUp() {
        // Inicializamos con un grado mínimo bajo (t = 2) para facilitar la prueba de divisiones.
        // Capacidad máxima de claves por nodo: (2 * 2) - 1 = 3 claves.
        arbol = new ArbolB<>(2);
    }

    @Test
    @DisplayName("Debería inicializar correctamente con el constructor por defecto (t=3)")
    void testConstructorPorDefecto() {
        ArbolB<Integer> arbolDefecto = new ArbolB<>();
        assertTrue(arbolDefecto.estaVacio(), "El árbol por defecto debería iniciar vacío.");
        assertNull(arbolDefecto.getRaiz(), "La raíz debería ser nula.");
    }

    @Test
    @DisplayName("Debería lanzar excepción si el grado mínimo 't' es menor a 2")
    void testConstructorGradoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new ArbolB<>(1),
                "Debería lanzar excepción si t es 1.");
        assertThrows(IllegalArgumentException.class, () -> new ArbolB<>(0),
                "Debería lanzar excepción si t es 0.");
        assertThrows(IllegalArgumentException.class, () -> new ArbolB<>(-5),
                "Debería lanzar excepción si t es negativo.");
    }

    @Test
    @DisplayName("Debería cambiar el estado de vacío al insertar un elemento")
    void testEstaVacio() {
        assertTrue(arbol.estaVacio(), "El árbol recién creado debe estar vacío.");

        arbol.insertar(100L, "Subasta Antigua");

        assertFalse(arbol.estaVacio(), "El árbol ya no debería estar vacío tras una inserción.");
    }

    @Test
    @DisplayName("Debería lanzar excepción si se intenta insertar un dato nulo")
    void testInsertarDatoNulo() {
        assertThrows(IllegalArgumentException.class, () -> arbol.insertar(10L, null),
                "No se permite almacenar valores nulos en el árbol.");
    }

    @Test
    @DisplayName("Debería retornar null al buscar en un árbol vacío o una clave inexistente")
    void testBuscarNoEncontrado() {
        assertNull(arbol.buscar(50L), "Buscar en un árbol vacío debe retornar null.");

        arbol.insertar(10L, "Item 10");
        arbol.insertar(20L, "Item 20");

        assertNull(arbol.buscar(99L), "Buscar una clave que no existe debe retornar null.");
    }

    @Test
    @DisplayName("Debería insertar y recuperar exitosamente múltiples elementos")
    void testInsertarYBuscarBasico() {
        arbol.insertar(15L, "Pintura Renacentista");
        arbol.insertar(5L, "Escultura de Mármol");
        arbol.insertar(25L, "Moneda de Oro");

        assertEquals("Pintura Renacentista", arbol.buscar(15L));
        assertEquals("Escultura de Mármol", arbol.buscar(5L));
        assertEquals("Moneda de Oro", arbol.buscar(25L));
    }

    @Test
    @DisplayName("Debería realizar correctamente la división de la raíz cuando se llena")
    void testDivisionDeRaiz() {
        // Insertamos 3 elementos (capacidad límite para t=2 en un solo nodo)
        arbol.insertar(10L, "Datos 10");
        arbol.insertar(20L, "Datos 20");
        arbol.insertar(30L, "Datos 30");

        // Verificamos que siguen en un único nodo hoja (la raíz misma)
        ArbolB.NodoB<String> raizAntes = arbol.getRaiz();
        assertNotNull(raizAntes);
        assertTrue(raizAntes.isEsHoja(), "La raíz debería ser hoja antes de llenarse por completo.");
        assertEquals(3, raizAntes.getN(), "La raíz debe contener exactamente 3 claves.");

        // Insertar el 4to elemento causa que la antigua raíz se divida de manera preventiva
        arbol.insertar(40L, "Datos 40");

        ArbolB.NodoB<String> nuevaRaiz = arbol.getRaiz();
        assertNotNull(nuevaRaiz);
        assertFalse(nuevaRaiz.isEsHoja(), "La nueva raíz ya no debe ser un nodo hoja.");
        assertEquals(1, nuevaRaiz.getN(), "La nueva raíz tras la división debe quedarse con 1 sola clave (la mediana).");

        // Verificar que los punteros hijos se hayan creado correctamente
        assertNotNull(nuevaRaiz.getHijos()[0], "El hijo izquierdo no debe ser nulo.");
        assertNotNull(nuevaRaiz.getHijos()[1], "El hijo derecho no debe ser nulo.");

        // Garantizar que la búsqueda siga funcionando perfectamente post-división
        assertEquals("Datos 10", arbol.buscar(10L));
        assertEquals("Datos 20", arbol.buscar(20L));
        assertEquals("Datos 30", arbol.buscar(30L));
        assertEquals("Datos 40", arbol.buscar(40L));
    }

    @Test
    @DisplayName("Debería mantener el ordenamiento manual de claves tras múltiples inserciones desordenadas")
    void testOrdenamientoEInsercionMasiva() {
        // Insertamos datos con claves desordenadas
        long[] claves = {50, 10, 30, 20, 40, 70, 60};
        for (long c : claves) {
            arbol.insertar(c, "Valor-" + c);
        }

        // Comprobamos que todos se insertaron y recuperan correctamente respetando el direccionamiento indexado
        for (long c : claves) {
            assertEquals("Valor-" + c, arbol.buscar(c), "Fallo al recuperar la clave: " + c);
        }
    }

    @Test
    @DisplayName("Debería ejecutar el método mostrarHistorial sin lanzar excepciones")
    void testMostrarHistorialSinErrores() {
        // Caso de árbol vacío
        assertDoesNotThrow(() -> arbol.mostrarHistorial(),
                "El método mostrarHistorial() falló con el árbol vacío.");

        // Caso con elementos distribuidos
        arbol.insertar(10L, "A");
        arbol.insertar(20L, "B");
        arbol.insertar(5L, "C");
        arbol.insertar(30L, "D"); // Genera división estructural

        assertDoesNotThrow(() -> arbol.mostrarHistorial(),
                "El método mostrarHistorial() falló al imprimir un árbol con divisiones.");
    }
}