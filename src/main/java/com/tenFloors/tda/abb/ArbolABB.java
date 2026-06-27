package main.java.com.tenFloors.tda.abb;

import java.util.ArrayList;
import java.util.List;

public class ArbolABB<T> {
    private NodoABB<T> raiz;

    public ArbolABB() {
        this.raiz = null;
    }

    // INSERCIÓN: Ahora requiere pasar el ID explícito como String
    public void insertar(String id, T dato) {
        raiz = insertarRec(raiz, id, dato);
    }

    private NodoABB<T> insertarRec(NodoABB<T> nodo, String id, T dato) {
        if (nodo == null) {
            return new NodoABB<>(id, dato);
        }

        int comparacion = id.compareTo(nodo.getId());

        if (comparacion < 0) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), id, dato));
        } else if (comparacion > 0) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), id, dato));
        }

        return nodo; // No se permiten duplicados con el mismo ID
    }

    // BUSQUEDA por String ID
    public T buscar(String id) {
        return buscarRec(raiz, id);
    }

    private T buscarRec(NodoABB<T> nodo, String id) {
        if (nodo == null) return null;

        int comparacion = id.compareTo(nodo.getId());

        if (comparacion == 0) {
            return nodo.getDato(); // Encontrado
        }

        if (comparacion < 0) {
            return buscarRec(nodo.getIzquierdo(), id);
        } else {
            return buscarRec(nodo.getDerecho(), id);
        }
    }

    // ELIMINACIÓN por String ID
    public void eliminar(String id) {
        raiz = eliminarRec(raiz, id);
    }

    private NodoABB<T> eliminarRec(NodoABB<T> nodo, String id) {
        if (nodo == null) return null;

        int comparacion = id.compareTo(nodo.getId());

        if (comparacion < 0) {
            nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), id));
        } else if (comparacion > 0) {
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), id));
        } else {
            // CASO 1 y 2: Es hoja o tiene un solo hijo
            if (nodo.getIzquierdo() == null) return nodo.getDerecho();
            if (nodo.getDerecho() == null) return nodo.getIzquierdo();

            // CASO 3: Tiene dos hijos.
            NodoABB<T> sucesor = obtenerMinimo(nodo.getDerecho());

            // IMPORTANTE: Se deben sobrescribir tanto el dato como el ID
            // para no romper las futuras búsquedas del árbol.
            nodo.setDato(sucesor.getDato());
            nodo.setId(sucesor.getId());

            // Eliminamos el nodo sucesor de su posición original usando su ID
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor.getId()));
        }
        return nodo;
    }

    private NodoABB<T> obtenerMinimo(NodoABB<T> nodo) {
        NodoABB<T> actual = nodo;
        while (actual.getIzquierdo() != null) {
            actual = actual.getIzquierdo();
        }
        return actual;
    }

    // RECORRIDO INORDEN (Devuelve lista ordenada alfabéticamente por ID)
    public List<T> obtenerInorden() {
        List<T> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }

    private void inordenRec(NodoABB<T> nodo, List<T> lista) {
        if (nodo != null) {
            inordenRec(nodo.getIzquierdo(), lista);
            lista.add(nodo.getDato());
            inordenRec(nodo.getDerecho(), lista);
        }
    }
}