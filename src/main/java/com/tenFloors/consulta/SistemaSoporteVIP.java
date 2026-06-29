package main.java.com.tenFloors.consulta;

import main.java.com.tenFloors.model.Cuenta;
import main.java.com.tenFloors.model.Item;
import main.java.com.tenFloors.model.Ticket;
import main.java.com.tenFloors.model.Transaccion;
import main.java.com.tenFloors.tda.colaPrioridad.ColaPrioridad;
import main.java.com.tenFloors.tda.arbolB.ArbolB;
import main.java.com.tenFloors.tda.avl.ArbolAVL;
import main.java.com.tenFloors.tda.abb.ArbolABB;

/**
 * Orquestador del Hito 2. Cruza de forma síncrona 4 estructuras nativas
 * para auditar fraudes o pérdidas en la casa de subastas y resarcir al jugador.
 */
public class SistemaSoporteVIP {

    private final ColaPrioridad<Ticket> colaTickets;
    private final ArbolB<Transaccion> historialSubastas;
    private final ArbolAVL<Cuenta> indiceCuentas;
    private final ArbolABB<Item> baseGlobalItems;

    public SistemaSoporteVIP(ColaPrioridad<Ticket> colaTickets,
                             ArbolB<Transaccion> historialSubastas,
                             ArbolAVL<Cuenta> indiceCuentas,
                             ArbolABB<Item> baseGlobalItems) {
        this.colaTickets = colaTickets;
        this.historialSubastas = historialSubastas;
        this.indiceCuentas = indiceCuentas;
        this.baseGlobalItems = baseGlobalItems;
    }

    /**
     * Procesa el reclamo más urgente, audita el historial y entrega una compensación segura.
     * @param idItemCompensacion ID del ítem del catálogo global que se usará como resarcimiento.
     * @return true si el flujo se completó con éxito, false en caso contrario.
     */
    public boolean atenderProximoTicket(String idItemCompensacion) {
        // 1. Extraer el ticket con mayor urgencia de la Cola con Prioridad
        if (colaTickets.estaVacio()) {
            System.out.println("[SOPORTE] No existen tickets VIP pendientes de atención en la cola.");
            return false;
        }

        Ticket ticketActual = colaTickets.extraerMaximo();
        System.out.println("\n==================================================");
        System.out.println("DESENCOLANDO RECLAMO VIP MÁS URGENTE");
        System.out.println("==================================================");
        System.out.println("Ejecutando: " + ticketActual);
        System.out.println("Mensaje del Usuario: \"" + ticketActual.getDetalleReclamo() + "\"");

        // 2. Buscar e inspeccionar la transacción histórica en el Árbol B
        long idTx = ticketActual.getIdTransaccionReclamada();
        System.out.println("\n[PASO 1: AUDITORÍA] Inspeccionando Árbol B de la Casa de Subastas para la clave: " + idTx);
        Transaccion transaccionAudita = historialSubastas.buscar(idTx);

        if (transaccionAudita == null) {
            System.out.println("[ALERTA - AUDITORÍA] La transacción #" + idTx + " NO figura en el registro del Árbol B.");
            System.out.println("[INFO] Se asume pérdida de sincronía. Procediendo con el resarcimiento de buena fe.");
        } else {
            System.out.println("[ÉXITO - AUDITORÍA] Transacción legítima hallada: " + transaccionAudita);
        }

        // 3. Buscar el perfil de la cuenta en el Árbol AVL Global
        String idCuenta = ticketActual.getIdCuenta();
        Cuenta cuentaAfectada = indiceCuentas.buscar(idCuenta);

        if (cuentaAfectada == null) {
            System.out.println("[ERROR CRÍTICO] La cuenta " + idCuenta + " asociada al ticket no existe en el índice AVL.");
            return false;
        }

        // 4. Validar existencia del ítem compensatorio en el catálogo base ABB
        Item itemPremio = baseGlobalItems.buscar(idItemCompensacion);
        if (itemPremio == null) {
            System.out.println("[ERROR CORRUPCIÓN] El ítem '" + idItemCompensacion + "' no existe en la base de datos de ítems.");
            return false;
        }

        // 5. Aplicar la compensación directa en la mochila ordenada (ABB de la Cuenta)
        System.out.println("\n[PASO 2: COMPENSACIÓN] Insertando ítem en la mochila del jugador...");
        cuentaAfectada.getInventario().insertar(itemPremio.getId(), itemPremio);

        System.out.println("==================================================");
        System.out.println("RECLAMO RESUELTO CON ÉXITO");
        System.out.println("==================================================");
        System.out.println("Beneficiario: " + cuentaAfectada.getJugador().getNombre());
        System.out.println("Ítem Otorgado: " + itemPremio.getNombre() + " [" + itemPremio.getId() + "]");
        System.out.println("==================================================");

        return true;
    }
}