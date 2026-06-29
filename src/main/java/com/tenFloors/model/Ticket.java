package main.java.com.tenFloors.model;

/**
 * Entidad que representa un reclamo de Soporte Técnico VIP.
 * Vincula una cuenta afectada con una transacción del Árbol B para su auditoría.
 */
public class Ticket {
    private final long idTicket;
    private final String idCuenta;
    private final long idTransaccionReclamada;
    private final String detalleReclamo;
    private final int nivelUrgencia;

    public Ticket(long idTicket, String idCuenta, long idTransaccionReclamada, String detalleReclamo, int nivelUrgencia) {
        if (idCuenta == null || detalleReclamo == null) {
            throw new IllegalArgumentException("El ID de cuenta y el detalle no pueden ser nulos.");
        }
        this.idTicket = idTicket;
        this.idCuenta = idCuenta;
        this.idTransaccionReclamada = idTransaccionReclamada;
        this.detalleReclamo = detalleReclamo;
        this.nivelUrgencia = nivelUrgencia;
    }

    public long getIdTicket() { return idTicket; }
    public String getIdCuenta() { return idCuenta; }
    public long getIdTransaccionReclamada() { return idTransaccionReclamada; }
    public String getDetalleReclamo() { return detalleReclamo; }
    public int getNivelUrgencia() { return nivelUrgencia; }

    @Override
    public String toString() {
        return "Ticket VIP #" + idTicket + " [Cuenta: " + idCuenta + " | Urgencia: " + nivelUrgencia + " | Tx Relacionada: " + idTransaccionReclamada + "]";
    }
}
