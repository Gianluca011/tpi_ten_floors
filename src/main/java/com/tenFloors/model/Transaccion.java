package main.java.com.tenFloors.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

    public String getFechaFormateada() {
        LocalDateTime fecha = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(this.timestamp),
                ZoneId.systemDefault()
        );
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fecha.format(formateador);
    }

    @Override
    public String toString() {
        return "Tx[" + id + " | " + item + " | $" + precioFinal + "]";
    }
}
