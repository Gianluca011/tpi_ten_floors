package main.java.com.tenFloors.model;

/**
 * Representa una entidad de progresion en Ten Floors.
 * Puede actuar como una Clase Base, una Categoría de habilidades o una Habilidad concreta.
 */
public class ClaseHabilidad {
    private final String nombre;
    private final String tipo; // "CLASE", "CATEGORIA", "HABILIDAD"
    private final int nivelRequerido;

    public ClaseHabilidad(String nombre, String tipo, int nivelRequerido) {
        if (nombre == null || tipo == null) {
            throw new IllegalArgumentException("El nombre y el tipo no pueden ser nulos.");
        }
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivelRequerido = nivelRequerido;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public int getNivelRequerido() {
        return this.nivelRequerido;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClaseHabilidad otra = (ClaseHabilidad) obj;
        return this.nombre.equalsIgnoreCase(otra.nombre);
    }

    @Override
    public int hashCode() {
        return this.nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return this.nombre + " [" + this.tipo + " - Requisito Nivel: " + this.nivelRequerido + "]";
    }
}
