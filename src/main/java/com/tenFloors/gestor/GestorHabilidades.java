package main.java.com.tenFloors.gestor;

import main.java.com.tenFloors.model.ClaseHabilidad;
import main.java.com.tenFloors.model.Jugador;
import main.java.com.tenFloors.tda.arbol.ArbolGenerico;

public class GestorHabilidades {

    public void sincronizarHabilidadesAutomatica(Jugador jugador, ArbolGenerico<ClaseHabilidad> arbolHabilidadesGlobal) {
        if (arbolHabilidadesGlobal == null || arbolHabilidadesGlobal.estaVacio()) {
            return;
        }
        procesarDesbloqueoRecursivo(arbolHabilidadesGlobal.getRaiz(), jugador, false);
    }

    private void procesarDesbloqueoRecursivo(ArbolGenerico.NodoArbol<ClaseHabilidad> nodo, Jugador jugador, boolean ramaDeClaseActiva) {
        if (nodo == null) {
            return;
        }

        ClaseHabilidad infoHabilidad = nodo.getDato();
        boolean banderaHijos = ramaDeClaseActiva;

        if (infoHabilidad.getTipo().equals("CLASE") && infoHabilidad.getNombre().equalsIgnoreCase(jugador.getClase())) {
            banderaHijos = true;
        }

        if (banderaHijos && infoHabilidad.getTipo().equals("HABILIDAD")) {
            if (jugador.getNivel() >= infoHabilidad.getNivelRequerido()) {
                jugador.getHabilidadesAprendidas().agregar(infoHabilidad.getNombre());
            }
        }

        procesarDesbloqueoRecursivo(nodo.getPrimerHijo(), jugador, banderaHijos);
        procesarDesbloqueoRecursivo(nodo.getSiguienteHermano(), jugador, ramaDeClaseActiva);
    }
}