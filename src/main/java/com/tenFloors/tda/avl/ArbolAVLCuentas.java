package main.java.com.tenFloors.tda.avl;

public class ArbolAVLCuentas<T> {
    private NodoAVL<T> raiz;

    public ArbolAVLCuentas() { this.raiz = null; }

    public int mostrarAltura() { return altura(raiz); }

    private int altura(NodoAVL<T> n) { return (n == null) ? 0 : n.getAltura(); }

    public int factorEquilibrio(NodoAVL<T> n) {
        return (n == null) ? 0 : altura(n.getIzquierdo()) - altura(n.getDerecho());
    }

    // --- ROTACIONES ---

    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.getIzquierdo();
        NodoAVL<T> T2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(T2);

        y.setAltura(Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);
        x.setAltura(Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);

        return x;
    }

    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.getDerecho();
        NodoAVL<T> T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        x.setAltura(Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);
        y.setAltura(Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);

        return y;
    }

    // --- INSERCIÓN: Ahora requiere especificar el String ID ---
    public void insertar(String id, T dato) {
        raiz = insertarRec(raiz, id, dato);
    }

    private NodoAVL<T> insertarRec(NodoAVL<T> n, String id, T dato) {
        if (n == null) return new NodoAVL<>(id, dato);

        // Uso de compareTo para Strings
        int comparacion = id.compareTo(n.getId());

        if (comparacion < 0) {
            n.setIzquierdo(insertarRec(n.getIzquierdo(), id, dato));
        } else if (comparacion > 0) {
            n.setDerecho(insertarRec(n.getDerecho(), id, dato));
        } else {
            return n; // No se permiten IDs duplicados
        }

        n.setAltura(1 + Math.max(altura(n.getIzquierdo()), altura(n.getDerecho())));
        int fe = factorEquilibrio(n);

        // Caso Izquierda-Izquierda
        if (fe > 1 && id.compareTo(n.getIzquierdo().getId()) < 0) return rotacionDerecha(n);

        // Caso Derecha-Derecha
        if (fe < -1 && id.compareTo(n.getDerecho().getId()) > 0) return rotacionIzquierda(n);

        // Caso Izquierda-Derecha
        if (fe > 1 && id.compareTo(n.getIzquierdo().getId()) > 0) {
            n.setIzquierdo(rotacionIzquierda(n.getIzquierdo()));
            return rotacionDerecha(n);
        }

        // Caso Derecha-Izquierda
        if (fe < -1 && id.compareTo(n.getDerecho().getId()) < 0) {
            n.setDerecho(rotacionDerecha(n.getDerecho()));
            return rotacionIzquierda(n);
        }
        return n;
    }

    // --- BÚSQUEDA ---
    public T buscar(String id) {
        NodoAVL<T> actual = raiz;
        while (actual != null) {
            int comparacion = id.compareTo(actual.getId());
            if (comparacion == 0) return actual.getDato();
            actual = (comparacion < 0) ? actual.getIzquierdo() : actual.getDerecho();
        }
        return null;
    }

    // --- ELIMINACIÓN ---
    public void eliminar(String id) { raiz = eliminarRec(raiz, id); }

    private NodoAVL<T> eliminarRec(NodoAVL<T> n, String id) {
        if (n == null) return null;

        int comparacion = id.compareTo(n.getId());

        if (comparacion < 0) {
            n.setIzquierdo(eliminarRec(n.getIzquierdo(), id));
        } else if (comparacion > 0) {
            n.setDerecho(eliminarRec(n.getDerecho(), id));
        } else {
            // ¡Nodo encontrado!
            if (n.getIzquierdo() == null || n.getDerecho() == null) {
                NodoAVL<T> temp = (n.getIzquierdo() != null) ? n.getIzquierdo() : n.getDerecho();
                n = temp;
            } else {
                // Nodo con dos hijos
                NodoAVL<T> temp = n.getDerecho();
                while(temp.getIzquierdo() != null) temp = temp.getIzquierdo();

                // IMPORTANTE: Al reemplazar, debemos mover tanto el Dato como su ID correspondiente
                n.setDato(temp.getDato());
                n.setId(temp.getId());

                n.setDerecho(eliminarRec(n.getDerecho(), temp.getId()));
            }
        }

        if (n == null) return null;

        // Recalcular altura
        n.setAltura(Math.max(altura(n.getIzquierdo()), altura(n.getDerecho())) + 1);

        // Rebalanceo (las alturas no cambian su lógica por usar Strings)
        int fe = factorEquilibrio(n);

        if (fe > 1 && factorEquilibrio(n.getIzquierdo()) >= 0) return rotacionDerecha(n);
        if (fe > 1 && factorEquilibrio(n.getIzquierdo()) < 0) {
            n.setIzquierdo(rotacionIzquierda(n.getIzquierdo()));
            return rotacionDerecha(n);
        }

        if (fe < -1 && factorEquilibrio(n.getDerecho()) <= 0) return rotacionIzquierda(n);
        if (fe < -1 && factorEquilibrio(n.getDerecho()) > 0) {
            n.setDerecho(rotacionDerecha(n.getDerecho()));
            return rotacionIzquierda(n);
        }

        return n;
    }
}