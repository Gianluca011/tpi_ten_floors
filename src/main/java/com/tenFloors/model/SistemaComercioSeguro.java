package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.avl.ArbolAVLCuentas;
import main.java.com.tenFloors.tda.abb.ArbolABB;
import main.java.com.tenFloors.tda.pila.Pila;

/**
 * Motor síncrono encargado de ejecutar auditorías y reversión de transacciones
 * mediante la integración cruzada de los TDAs Pila, ABB y AVL.
 */
public class SistemaComercioSeguro {

    private final ArbolAVLCuentas<Cuenta> indiceCuentas;
    private final ArbolABB<Item> baseGlobalItems;

    /**
     * Constructor del sistema de comercio seguro.
     * @param indiceCuentas Instancia del Árbol AVL global de cuentas.
     * @param baseGlobalItems Instancia del Árbol ABB de ítems válidos en el servidor.
     */
    public SistemaComercioSeguro(ArbolAVLCuentas<Cuenta> indiceCuentas, ArbolABB<Item> baseGlobalItems) {
        this.indiceCuentas = indiceCuentas;
        this.baseGlobalItems = baseGlobalItems;
    }

    /**
     * HITO 4: Revierte de forma segura la última transacción comercial de una cuenta.
     * Extrae de la Pila, valida en el ABB global y actualiza el estado interno de la cuenta del AVL.
     *
     * @param idCuenta Identificador único de la cuenta a auditar.
     * @return true si la transacción pudo ser revertida con éxito; false en caso contrario.
     */
    public boolean revertirUltimaTransaccion(String idCuenta) {
        System.out.println("\n[AUDITORÍA] Iniciando protocolo de Comercio Seguro para cuenta: " + idCuenta);

        // 1. BÚSQUEDA EN AVL: Localizar la cuenta afectada en el índice global auto-balanceado
        Cuenta cuenta = indiceCuentas.buscar(idCuenta);
        if (cuenta == null) {
            System.out.println("[ERROR - AVL] La cuenta '" + idCuenta + "' no existe en el registro del servidor.");
            return false;
        }

        System.out.println("[AVL] Cuenta verificada. Propietario: " + cuenta.getJugador().getNombre());

        // 2. EXTRACCIÓN DESDE PILA: Obtener la última transacción comercial realizada (LIFO)
        Pila<Transaccion> pilaTransacciones = cuenta.getHistorialComercio();
        if (pilaTransacciones.estaVacia()) {
            System.out.println("[INFO - PILA] La cuenta no registra transacciones comerciales reversibles en su historial.");
            return false;
        }

        // Desapilamos la acción para procesar el rollback técnico
        Transaccion ultimaTransaccion = pilaTransacciones.desapilar();
        System.out.println("[PILA] Última transacción comercial extraída con éxito: " + ultimaTransaccion.toString());

        // El campo item de Transaccion contiene el ID alfanumérico del objeto comercializado
        String idItemInvolucrado = ultimaTransaccion.getItem();

        // 3. VALIDACIÓN EN ABB GLOBAL: Verificar la existencia e integridad del ítem en el catálogo base del servidor
        Item itemCatalogo = baseGlobalItems.buscar(idItemInvolucrado);
        if (itemCatalogo == null) {
            System.out.println("[CRÍTICO - ABB] Falla de integridad. El ítem '" + idItemInvolucrado + "' no pertenece al catálogo global.");
            System.out.println("[SISTEMA] Re-apilando transacción para evitar corrupción del historial comercial.");
            pilaTransacciones.apilar(ultimaTransaccion); // Deshacemos el pop por seguridad
            return false;
        }

        System.out.println("[ABB GLOBAL] Validación exitosa. Ítem '" + itemCatalogo.getNombre() + "' (" + itemCatalogo.getRareza() + ") validado íntegramente.");

        // 4. ACTUALIZACIÓN DEL ESTADO (ABB JUGADOR): Restaurar el ítem en la mochila del personaje
        // En nuestro dominio, revertir una transacción significa reintegrar el ítem vendido a la mochila.
        ArbolABB<Item> inventarioJugador = cuenta.getInventario();
        Item itemExistente = inventarioJugador.buscar(idItemInvolucrado);

        if (itemExistente != null) {
            System.out.println("[INFO - ABB JUGADOR] El jugador ya reincorporó este ítem previamente. No se altera la mochila.");
        } else {
            // Insertamos el ítem recuperado de vuelta en el ABB del inventario del jugador
            inventarioJugador.insertar(idItemInvolucrado, itemCatalogo);
            System.out.println("[ABB JUGADOR] ¡Ítem '" + itemCatalogo.getNombre() + "' restaurado con éxito en la mochila!");
        }

        // Al modificarse por referencia el objeto guardado, el índice AVL refleja la actualización al instante.
        System.out.println("[AVL] Sincronización completa. Estado de cuenta actualizado con éxito.");
        return true;
    }
}