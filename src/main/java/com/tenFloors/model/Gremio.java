package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.arbol.ArbolGenerico;

/**
 * Entidad de Negocio que representa un Gremio dentro de Ten Floors.
 * Encapsula el árbol genérico n-ario para manejar el organigrama de poder de forma nativa.
 */
public class Gremio {
    private final String nombre;
    private final String tag;
    // El árbol almacena los IDs de las cuentas de los integrantes
    private final ArbolGenerico<String> estructuraJerarquica;

    public Gremio(String nombre, String tag) {
        if (nombre == null || tag == null) {
            throw new IllegalArgumentException("El nombre y el TAG del gremio no pueden ser nulos.");
        }
        this.nombre = nombre;
        this.tag = tag;
        this.estructuraJerarquica = new ArbolGenerico<>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getTag() {
        return this.tag;
    }

    public ArbolGenerico<String> getEstructuraJerarquica() {
        return this.estructuraJerarquica;
    }

    @Override
    public String toString() {
        return "Gremio: <" + tag + "> " + nombre;
    }
}
