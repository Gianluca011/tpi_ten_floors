package main.java.com.tenFloors.model;

public class Transaccion {
    private final long id;
    private final String item;
    private final double precioFinal;
    private final long timestamp;

    public Transaccion(long id, String item, double precioFinal, long timestamp) {
        this.id = id;
        this.item = item;
        this.precioFinal = precioFinal;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public String getItem() { return item; }
    public double getPrecioFinal() { return precioFinal; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Tx[" + id + " | " + item + " | $" + precioFinal + "]";
    }
}
