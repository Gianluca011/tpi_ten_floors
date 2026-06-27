package main.java.com.tenFloors.model;

import java.util.Objects;

/**
 * Entidad simplificada de Jugador para pruebas unitarias y de integración de TDAs.
 */
public class Jugador {
    private final String idCuenta;
    private final String nombre;
    private int nivel;
    private int pisoActual;

    public Jugador(String idCuenta, String nombre) {
        if (idCuenta == null || nombre == null) {
            throw new IllegalArgumentException("El ID de cuenta y el nombre no pueden ser nulos.");
        }
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.nivel = 1;
        this.pisoActual = 1; // Arranca en el piso 1 de la torre
    }

    public String getIdCuenta() { return idCuenta; }
    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getPisoActual() { return pisoActual; }
    public void setPisoActual(int pisoActual) { this.pisoActual = pisoActual; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        // Dos jugadores son idénticos si poseen el mismo ID de cuenta global
        return Objects.equals(idCuenta, jugador.idCuenta);
    }

    @Override
    public int hashCode() {
        // Genera el código hash basado en el string identificador único
        return idCuenta != null ? idCuenta.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Jugador[" + idCuenta + " - " + nombre + " | Piso: " + pisoActual + " | Lvl: " + nivel + "]";
    }
}