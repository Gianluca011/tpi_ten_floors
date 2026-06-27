package test.java.com.tenFloors.tda.avl;

import main.java.com.tenFloors.tda.avl.ArbolAVLCuentas;
import main.java.com.tenFloors.tda.avl.IIdentificable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArbolAVLCuentasTest {

    private ArbolAVLCuentas<CuentaMock> arbol;

    // Clase interna para probar el AVL sin depender de la clase Jugador real
    private static class CuentaMock implements IIdentificable {
        private final int id;
        public CuentaMock(int id) { this.id = id; }
        @Override public int getId() { return id; }
    }

    @BeforeEach
    public void setUp() {
        arbol = new ArbolAVLCuentas<>();
    }

    @Test
    public void testInsercionYBusqueda() {
        arbol.insertar(new CuentaMock(10));
        arbol.insertar(new CuentaMock(20));
        arbol.insertar(new CuentaMock(5));

        assertNotNull(arbol.buscar(10));
        assertNotNull(arbol.buscar(20));
        assertNotNull(arbol.buscar(5));
        assertNull(arbol.buscar(99), "La búsqueda de un ID inexistente debería devolver null.");
    }

    @Test
    public void testAutoBalanceo() {
        // Insertamos datos en orden ascendente para forzar el desbalanceo
        arbol.insertar(new CuentaMock(1));
        arbol.insertar(new CuentaMock(2));
        arbol.insertar(new CuentaMock(3));
        arbol.insertar(new CuentaMock(4));
        arbol.insertar(new CuentaMock(5));

        // Un AVL con 5 elementos nunca debería tener altura mayor a 3
        assertTrue(arbol.mostrarAltura() <= 3, "El árbol debería estar balanceado (altura <= 3)");
    }

    @Test
    public void testEliminacion() {
        arbol.insertar(new CuentaMock(10));
        arbol.insertar(new CuentaMock(5));
        arbol.eliminar(5);

        assertNull(arbol.buscar(5), "El nodo debería haber sido eliminado.");
        assertNotNull(arbol.buscar(10), "La raíz debería persistir.");
    }
}