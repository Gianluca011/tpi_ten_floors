package main.java.com.tenFloors.tda.abb;

import main.java.com.tenFloors.tda.avl.IIdentificable;

public class NodoABB<T extends IIdentificable> {
    private T dato;
    private NodoABB<T> izquierdo;
    private NodoABB<T> derecho;

    public NodoABB(T dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
    }

    // --- GETTERS Y SETTERS ---
    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }

    public NodoABB<T> getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoABB<T> izquierdo) { this.izquierdo = izquierdo; }

    public NodoABB<T> getDerecho() { return derecho; }
    public void setDerecho(NodoABB<T> derecho) { this.derecho = derecho; }
}