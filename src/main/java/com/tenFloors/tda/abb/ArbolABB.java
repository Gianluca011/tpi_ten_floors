package main.java.com.tenFloors.tda.abb;
import main.java.com.tenFloors.tda.avl.IIdentificable;
import java.util.ArrayList;
import java.util.List;

public class ArbolABB<T extends IIdentificable> {
    private NodoABB<T> raiz;

    public ArbolABB() {
        this.raiz = null;
    }

    // INSERCIÓN
    public void insertar(T dato) {
        raiz = insertarRec(raiz, dato);
    }

    private NodoABB<T> insertarRec(NodoABB<T> nodo, T dato) {
        // Caso base: llegamos a un espacio vacio
        if (nodo == null) {
            return new NodoABB<>(dato);
        }

        // Comparamos IDs para decidir por qué rama bajar
        if (dato.getId() < nodo.getDato().getId()) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), dato));
        } else if (dato.getId() > nodo.getDato().getId()) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), dato));
        }

        // Retornamos el nodo (no se permiten duplicados con el mismo ID)
        return nodo;
    }

    // BUSQUEDA
    public T buscar(int id) {
        return buscarRec(raiz, id);
    }

    private T buscarRec(NodoABB<T> nodo, int id) {
        if (nodo == null) return null; // No existe

        if (id == nodo.getDato().getId()) {
            return nodo.getDato(); // Encontrado
        }

        // Búsqueda binaria descartando mitades
        if (id < nodo.getDato().getId()) {
            return buscarRec(nodo.getIzquierdo(), id);
        } else {
            return buscarRec(nodo.getDerecho(), id);
        }
    }

    // ELIMINACIÓN (Los 3 casos de borrado)
    public void eliminar(int id) {
        raiz = eliminarRec(raiz, id);
    }

    private NodoABB<T> eliminarRec(NodoABB<T> nodo, int id) {
        if (nodo == null) return null;

        // 1. Fase de búsqueda
        if (id < nodo.getDato().getId()) {
            nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), id));
        } else if (id > nodo.getDato().getId()) {
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), id));
        } else {
            // 2. Nodo encontrado. Evaluamos cómo borrarlo:

            // CASO 1 y 2: Es hoja o tiene un solo hijo
            if (nodo.getIzquierdo() == null) return nodo.getDerecho();
            if (nodo.getDerecho() == null) return nodo.getIzquierdo();

            // CASO 3: Tiene dos hijos.
            // Buscamos el Sucesor Inorden (el más pequeño de los mayores)
            NodoABB<T> sucesor = obtenerMinimo(nodo.getDerecho());

            // Sobrescribimos el dato
            nodo.setDato(sucesor.getDato());

            // Eliminamos el nodo sucesor de su posición original
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor.getDato().getId()));
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

    // RECORRIDO INORDEN (Devuelve lista ordenada de menor a mayor ID)
    public List<T> obtenerInorden() {
        List<T> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }

    private void inordenRec(NodoABB<T> nodo, List<T> lista) {
        if (nodo != null) {
            inordenRec(nodo.getIzquierdo(), lista); // Visita rama izquierda
            lista.add(nodo.getDato());              // Agrega el dato
            inordenRec(nodo.getDerecho(), lista);   // Visita rama derecha
        }
    }
}