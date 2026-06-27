package main.java.com.tenFloors.model;
import main.java.com.tenFloors.tda.avl.IIdentificable;

public class Jugador implements IIdentificable {
    private final int idCuenta;
    private final String nombre;
    private final String clasePersonaje;

    // COMPOSICIÓN: Todo jugador nace instanciando su propio GestorInventario.
    // Si se borra la cuenta del jugador, su inventario se destruye con él.
    private final GestorInventario inventario;

    public Jugador(int idCuenta, String nombre, String clasePersonaje) {
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.clasePersonaje = clasePersonaje;
        // Instanciamos la mochila al crear el personaje
        this.inventario = new GestorInventario();
    }

    // --- CUMPLIMIENTO DEL CONTRATO AVL ---
    @Override
    public int getId() { return this.idCuenta; }

    public String getNombre() { return nombre; }
    public String getClasePersonaje() { return clasePersonaje; }

    // Exponemos el gestor para que desde el Main podamos hacer:
    // jugador.getInventario().agregarItem(...)
    public GestorInventario getInventario() { return inventario; }

    @Override
    public String toString() {
        return "Jugador [ID=" + idCuenta + "] " + nombre + " (" + clasePersonaje + ")";
    }
}