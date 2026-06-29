package main.java.com.tenFloors.gestor;

import main.java.com.tenFloors.model.Jugador;
import main.java.com.tenFloors.tda.conjunto.Conjunto;

/**
 * Componente del modelo encargado de orquestar el flujo de accesos concurrentes
 * y validar el estado de conectividad en tiempo real en los servidores.
 */
public class GestionOnline {

    // Instancia del TDA nativo creado por Lautaro Salto
    private final Conjunto<Jugador> registrosOnline;

    public GestionOnline() {
        this.registrosOnline = new Conjunto<>();
    }

    /**
     * REQUERIMIENTO DE NEGOCIO: Autenticar e ingresar un usuario al entorno simulado.
     * Valida de manera síncrona en O(1) que no existan accesos simultáneos con la misma cuenta.
     */
    public void iniciarSesion(Jugador jugador) {
        if (jugador == null) {
            System.out.println("[ERROR] Instancia de jugador inválida.");
            return;
        }

        // El método agregar() de nuestro conjunto ya rechaza duplicados internamente
        boolean exitoAlConectar = registrosOnline.agregar(jugador);

        if (exitoAlConectar) {
            System.out.println("[CONEXIÓN] " + jugador.getNombre() + " (ID: "
                    + jugador.getIdCuenta() + ") ingresó al servidor correctamente.");
        } else {
            System.out.println("[RECHAZADO] ¡Seguridad! La cuenta '" + jugador.getIdCuenta()
                    + "' ya tiene una sesión activa en este servidor.");
        }
    }

    /**
     * REQUERIMIENTO DE NEGOCIO: Desconectar al usuario del juego.
     * Libera el slot de la tabla hash para que la cuenta pueda volver a ingresar a futuro.
     */
    public void cerrarSesion(Jugador jugador) {
        if (jugador == null) return;

        boolean desconectado = registrosOnline.eliminar(jugador);

        if (desconectado) {
            System.out.println("[DESCONEXIÓN] " + jugador.getNombre() + " abandonó el mundo.");
        } else {
            System.out.println("[ERROR] No se pudo cerrar sesión. El jugador no figuraba online.");
        }
    }

    /**
     * REQUERIMIENTO PARA HITOS COMPLEJOS (Hito 1: Viaje rápido y Party).
     * Permite comprobar instantáneamente si un amigo o miembro específico del gremio está activo.
     */
    public boolean verificarConectividad(Jugador jugador) {
        return registrosOnline.contiene(jugador);
    }

    /**
     * Retorna la cantidad de jugadores activos para métricas de consola (Admin).
     */
    public int totalUsuariosEnLinea() {
        return registrosOnline.getTamanio();
    }
}
