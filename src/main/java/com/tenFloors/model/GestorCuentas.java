package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.avl.ArbolAVL;

/**
 * GESTOR DE NEGOCIO GLOBAL: Administra todas las cuentas del servidor.
 * Delega la persistencia y el ordenamiento en el TDA Árbol AVL.
 */
public class GestorCuentas {

    // El motor AVL. Configurado estrictamente para guardar objetos de tipo <Jugador>.
    private final ArbolAVL<Jugador> indiceGlobalCuentas;

    public GestorCuentas() {
        this.indiceGlobalCuentas = new ArbolAVL<>();
    }

    // --- ALTA DE CUENTAS ---
    public void registrarCuenta(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        // Regla de Negocio: No puede haber dos cuentas con el mismo ID (ahora String).
        // Asumiendo que jugador.getIdCuenta() ahora devuelve un String.
        if (indiceGlobalCuentas.buscar(jugador.getIdCuenta()) != null) {
            System.out.println("[AVL] Error: El ID " + jugador.getIdCuenta() + " ya está registrado.");
            return;
        }

        // Se envía de forma explícita la clave String y el objeto jugador
        indiceGlobalCuentas.insertar(jugador.getIdCuenta(), jugador);
        System.out.println("[AVL] Cuenta '" + jugador.getNombre() + "' dada de alta.");
    }

    // --- BAJA DE CUENTAS ---
    public void darDeBajaCuenta(String idCuenta) {
        // Buscamos para ver si el ID ingresado realmente existe
        Jugador jugador = indiceGlobalCuentas.buscar(idCuenta);
        if (jugador == null) {
            System.out.println("[AVL] Error: No se encontró la cuenta con ID " + idCuenta);
            return;
        }

        // Ejecutamos la eliminación por String ID.
        indiceGlobalCuentas.eliminar(idCuenta);
        System.out.println("[AVL] Cuenta de " + jugador.getNombre() + " eliminada.");
    }

    // --- BÚSQUEDA ---
    public Jugador obtenerCuenta(String idCuenta) {
        return indiceGlobalCuentas.buscar(idCuenta);
    }
}