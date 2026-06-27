package main.java.com.tenFloors.tda.avl;

public class ArbolAVLCuentas<T extends IIdentificable> {
    private NodoAVL<T> raiz;

    public ArbolAVLCuentas() { this.raiz = null; }
    // La altura se mide desde el nodo hasta la hoja más lejana.
    public int mostrarAltura() { return altura(raiz); }

    private int altura(NodoAVL<T> n) { return (n == null) ? 0 : n.getAltura(); }

    // Factor de Equilibrio: Diferencia de alturas entre subárboles.
    // Si el resultado es > 1 o < -1, el nodo está desbalanceado.
    public int factorEquilibrio(NodoAVL<T> n) {
        return (n == null) ? 0 : altura(n.getIzquierdo()) - altura(n.getDerecho());
    }

    // --- ROTACIONES: La clave del rendimiento O(log n) ---

    // Rotación Derecha: Se aplica cuando el desbalance es por el hijo izquierdo.
    // El hijo izquierdo (x) sube de nivel y el padre (y) baja a la derecha.
    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.getIzquierdo();
        NodoAVL<T> T2 = x.getDerecho(); // Subárbol que debe moverse de dueño

        // Realizamos el cambio de punteros (el giro)
        x.setDerecho(y);
        y.setIzquierdo(T2);

        // Recalculamos alturas: Primero el nodo que bajo (y), luego el que subio (x)
        y.setAltura(Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);
        x.setAltura(Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);

        return x; // x es la nueva raíz del subárbol
    }

    // Rotación Izquierda: Es el espejo exacto de la rotación derecha.
    // Se aplica cuando el desbalance es hacia la derecha (hijo derecho muy pesado).
    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.getDerecho();
        NodoAVL<T> T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        x.setAltura(Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);
        y.setAltura(Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);

        return y;
    }

    // --- INSERCIÓN: O(log n) ---
    public void insertar(T dato) { raiz = insertarRec(raiz, dato); }

    private NodoAVL<T> insertarRec(NodoAVL<T> n, T dato) {
        //Inserción estándar de ABB
        if (n == null) return new NodoAVL<>(dato);
        if (dato.getId() < n.getDato().getId()) n.setIzquierdo(insertarRec(n.getIzquierdo(), dato));
        else if (dato.getId() > n.getDato().getId()) n.setDerecho(insertarRec(n.getDerecho(), dato));
        else return n; // No permitimos IDs duplicados

        //Actualizar altura del ancestro (básico para el balanceo)
        n.setAltura(1 + Math.max(altura(n.getIzquierdo()), altura(n.getDerecho())));
        int fe = factorEquilibrio(n);

        //Balanceo: 4 casos posibles basados en el factor de equilibrio

        // Caso Izquierda-Izquierda (Rotación Simple Derecha)
        if (fe > 1 && dato.getId() < n.getIzquierdo().getDato().getId()) return rotacionDerecha(n);

        // Caso Derecha-Derecha (Rotación Simple Izquierda)
        if (fe < -1 && dato.getId() > n.getDerecho().getDato().getId()) return rotacionIzquierda(n);

        // Caso Izquierda-Derecha (Rotación Doble: Izquierda en hijo, luego Derecha en padre)
        if (fe > 1 && dato.getId() > n.getIzquierdo().getDato().getId()) {
            n.setIzquierdo(rotacionIzquierda(n.getIzquierdo()));
            return rotacionDerecha(n);
        }

        // Caso Derecha-Izquierda (Rotación Doble: Derecha en hijo, luego Izquierda en padre)
        if (fe < -1 && dato.getId() < n.getDerecho().getDato().getId()) {
            n.setDerecho(rotacionDerecha(n.getDerecho()));
            return rotacionIzquierda(n);
        }
        return n;
    }

    // --- BÚSQUEDA ---
    public T buscar(int id) {
        NodoAVL<T> actual = raiz;
        while (actual != null) {
            if (id == actual.getDato().getId()) return actual.getDato();
            // Descarte logarítmico: si el ID es menor, ignoramos toda la rama derecha
            actual = (id < actual.getDato().getId()) ? actual.getIzquierdo() : actual.getDerecho();
        }
        return null;
    }
    // Metodo eliminar (Igual estructura, balancea al retornar)
    public void eliminar(int id) { raiz = eliminarRec(raiz, id); }

    private NodoAVL<T> eliminarRec(NodoAVL<T> n, int id) {
        // --- PASO 1: Búsqueda del nodo (igual que en un ABB) ---
        if (n == null) return null; // El ID no existe en el árbol

        if (id < n.getDato().getId()) {
            n.setIzquierdo(eliminarRec(n.getIzquierdo(), id));
        } else if (id > n.getDato().getId()) {
            n.setDerecho(eliminarRec(n.getDerecho(), id));
        } else {
            // ¡Nodo encontrado! Se activan los 3 casos de borrado

            // CASO 1 y 2: Nodo sin hijos o con un solo hijo
            if (n.getIzquierdo() == null || n.getDerecho() == null) {
                NodoAVL<T> temp = (n.getIzquierdo() != null) ? n.getIzquierdo() : n.getDerecho();

                // Si temp es null, no tenía hijos. Si no, reemplazamos con el hijo único.
                n = temp;
            } else {
                // CASO 3: Nodo con dos hijos.
                // No podemos simplemente borrarlo, debemos reemplazarlo por su sucesor.
                // Buscamos el nodo con el valor inmediatamente superior (el más pequeño del subárbol derecho).
                NodoAVL<T> temp = n.getDerecho();
                while(temp.getIzquierdo() != null) temp = temp.getIzquierdo();

                // Sobrescribimos el dato del nodo actual con el dato del sucesor
                n.setDato(temp.getDato());

                // Borramos el sucesor original (que ahora es una hoja o tiene un solo hijo)
                n.setDerecho(eliminarRec(n.getDerecho(), temp.getDato().getId()));
            }
        }

        // Si el árbol quedó vacío tras borrar, no hay nada que balancear
        if (n == null) return null;

        // --- PASO 2: Recalcular la altura ---
        // Al quitar un nodo, la altura del camino hacia la raíz pudo haber cambiado
        n.setAltura(Math.max(altura(n.getIzquierdo()), altura(n.getDerecho())) + 1);

        // --- PASO 3: Rebalanceo (Idéntico a la inserción) ---
        // Evaluamos el Factor de Equilibrio para ver si el borrado dejó el árbol "cojo"
        int fe = factorEquilibrio(n);

        // Si FE > 1, el desbalance está a la izquierda
        if (fe > 1 && factorEquilibrio(n.getIzquierdo()) >= 0) return rotacionDerecha(n);
        if (fe > 1 && factorEquilibrio(n.getIzquierdo()) < 0) {
            n.setIzquierdo(rotacionIzquierda(n.getIzquierdo()));
            return rotacionDerecha(n);
        }

        // Si FE < -1, el desbalance está a la derecha
        if (fe < -1 && factorEquilibrio(n.getDerecho()) <= 0) return rotacionIzquierda(n);
        if (fe < -1 && factorEquilibrio(n.getDerecho()) > 0) {
            n.setDerecho(rotacionDerecha(n.getDerecho()));
            return rotacionIzquierda(n);
        }

        // Retornamos el nodo (ya sea el original, el reemplazado o el rotado)
        return n;
    }
}