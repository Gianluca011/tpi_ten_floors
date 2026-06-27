package test.java.com.tenFloors.tda.avl;

import main.java.com.tenFloors.tda.avl.ArbolAVLCuentas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArbolAVLCuentasTest {

    private ArbolAVLCuentas<CuentaMock> arbol;

    // Clase interna limpia: ya no implementa IIdentificable y usa String ID
    private static class CuentaMock {
        private final String id;
        public CuentaMock(String id) { this.id = id; }
        public String getId() { return id; }
    }

    @BeforeEach
    public void setUp() {
        arbol = new ArbolAVLCuentas<>();
    }

    @Test
    public void testInsercionYBusqueda() {
        CuentaMock c1 = new CuentaMock("10");
        CuentaMock c2 = new CuentaMock("20");
        CuentaMock c3 = new CuentaMock("05");

        arbol.insertar(c1.getId(), c1);
        arbol.insertar(c2.getId(), c2);
        arbol.insertar(c3.getId(), c3);

        assertNotNull(arbol.buscar("10"));
        assertNotNull(arbol.buscar("20"));
        assertNotNull(arbol.buscar("05"));
        assertNull(arbol.buscar("99"), "La búsqueda de un ID inexistente debería devolver null.");
    }

    @Test
    public void testAutoBalanceo() {
        // Al usar "1", "2", "3", "4", "5", el orden alfabético coincide con el numérico,
        // provocando el desbalanceo secuencial hacia la derecha que activa el AVL.
        arbol.insertar("1", new CuentaMock("1"));
        arbol.insertar("2", new CuentaMock("2"));
        arbol.insertar("3", new CuentaMock("3"));
        arbol.insertar("4", new CuentaMock("4"));
        arbol.insertar("5", new CuentaMock("5"));

        // Un AVL con 5 elementos equilibrados jamás debe superar la altura de 3
        assertTrue(arbol.mostrarAltura() <= 3, "El árbol debería estar balanceado (altura <= 3)");
    }

    @Test
    public void testEliminacion() {
        CuentaMock c1 = new CuentaMock("10");
        CuentaMock c2 = new CuentaMock("05");

        arbol.insertar(c1.getId(), c1);
        arbol.insertar(c2.getId(), c2);

        arbol.eliminar("05");

        assertNull(arbol.buscar("05"), "El nodo debería haber sido eliminado.");
        assertNotNull(arbol.buscar("10"), "La raíz debería persistir.");
    }
}