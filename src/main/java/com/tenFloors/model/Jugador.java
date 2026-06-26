package main.java.com.tenFloors.model;

/**
 * Entidad simplificada de Jugador para pruebas unitarias y de integración de TDAs.
 */
public class Jugador {
    private final String nombre;
    private final int nivel;

    public Jugador(String nombre, int nivel) {
        this.nombre = nombre;
        this.nivel = nivel;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getNivel() {
        return this.nivel;
    }

    @Override
    public String toString() {
        return "Jugador{Nombre: '" + nombre + "', Nivel: " + nivel + "}";
    }
}