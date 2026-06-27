package main.java.com.tenFloors.tda.avl;

public class NodoAVL<T> {
    private String id;
    private T dato;
    private int altura;
    private NodoAVL<T> izquierdo;
    private NodoAVL<T> derecho;

    public NodoAVL(String id, T dato) {
        this.id = id;
        this.dato = dato;
        this.altura = 1;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }
    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }
    public NodoAVL<T> getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoAVL<T> izquierdo) { this.izquierdo = izquierdo; }
    public NodoAVL<T> getDerecho() { return derecho; }
    public void setDerecho(NodoAVL<T> derecho) { this.derecho = derecho; }
}