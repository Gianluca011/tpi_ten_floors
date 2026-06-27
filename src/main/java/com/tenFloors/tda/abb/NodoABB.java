package main.java.com.tenFloors.tda.abb;

public class NodoABB<T> {
    private String id;
    private T dato;
    private NodoABB<T> izquierdo;
    private NodoABB<T> derecho;

    public NodoABB(String id, T dato) {
        this.id = id;
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
    }

    // --- GETTERS Y SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }

    public NodoABB<T> getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoABB<T> izquierdo) { this.izquierdo = izquierdo; }

    public NodoABB<T> getDerecho() { return derecho; }
    public void setDerecho(NodoABB<T> derecho) { this.derecho = derecho; }
}