package main.java.com.tenFloors.model;

import main.java.com.tenFloors.tda.arbol.ArbolGenerico;
import main.java.com.tenFloors.tda.avl.ArbolAVL;
import main.java.com.tenFloors.tda.conjunto.Conjunto;

/**
 * Servicio encargado de resolver el Hito 3: Auditoría de Gremios.
 * Cruza la jerarquía del modelo Gremio, la validez del AVL de cuentas y la unicidad del Conjunto.
 */
public class SistemaAuditoriaGremios {

    /**
     * Ejecuta la auditoría completa extrayendo el árbol del gremio provisto.
     * * @param gremio        Entidad gremio a auditar.
     * @param indiceCuentas Índice global auto-balanceado AVL de cuentas del servidor.
     * @return Un Conjunto nativo con los perfiles de Jugador consolidados de forma única.
     */
    public Conjunto<Jugador> auditarLideresGremio(Gremio gremio, ArbolAVL<Cuenta> indiceCuentas) {
        Conjunto<Jugador> lideresConsolidados = new Conjunto<>();

        if (gremio == null) {
            System.out.println("[AUDITORÍA] Error: El gremio especificado no existe.");
            return lideresConsolidados;
        }

        ArbolGenerico<String> arbolJerarquia = gremio.getEstructuraJerarquica();

        if (arbolJerarquia == null || arbolJerarquia.estaVacio()) {
            System.out.println("[AUDITORÍA] El gremio '" + gremio.getNombre() + "' no posee miembros en su jerarquía.");
            return lideresConsolidados;
        }

        System.out.println("\n[PROCESO] Iniciando auditoría orgánica sobre el " + gremio.toString() + "...");

        // Iniciamos el procesamiento recursivo desde la raíz del árbol jerárquico
        procesarNodoRecursivo(arbolJerarquia.getRaiz(), indiceCuentas, lideresConsolidados);

        return lideresConsolidados;
    }

    /**
     * Recorre de forma recursiva el árbol utilizando la lógica de "Primer Hijo / Siguiente Hermano".
     */
    private void procesarNodoRecursivo(ArbolGenerico.NodoArbol<String> nodoActual,
                                       ArbolAVL<Cuenta> indiceCuentas,
                                       Conjunto<Jugador> conjuntoDestino) {
        if (nodoActual == null) {
            return;
        }

        // 1. EXTRAER: Obtenemos el ID de cuenta guardado en el nodo
        String idCuenta = nodoActual.getDato();

        // 2. BUSCAR: Verificamos existencia real en el índice AVL global de Axel
        Cuenta cuentaAsociada = indiceCuentas.buscar(idCuenta);

        if (cuentaAsociada != null) {
            Jugador perfilJugador = cuentaAsociada.getJugador();

            // 3. CONSOLIDAR: Agregamos al conjunto hash de Lautaro para garantizar unicidad
            boolean insertado = conjuntoDestino.agregar(perfilJugador);

            if (insertado) {
                System.out.println("   -> [VALIDADO & CONSOLIDADO] " + perfilJugador.getNombre() + " (" + idCuenta + ")");
            } else {
                System.out.println("   -> [OMITIDO] " + idCuenta + " ya fue procesado previamente.");
            }
        } else {
            System.out.println("   -> [ALERTA CRÍTICA] El ID '" + idCuenta + "' figura en el gremio pero NO existe en el AVL.");
        }

        // 4. NAVEGAR: Descendemos linealmente hacia el primer hijo directo
        ArbolGenerico.NodoArbol<String> hijoActual = nodoActual.getPrimerHijo();
        while (hijoActual != null) {
            procesarNodoRecursivo(hijoActual, indiceCuentas, conjuntoDestino);

            // 5. NAVEGAR: Nos desplazamos lateralmente por toda la lista de hermanos
            hijoActual = hijoActual.getSiguienteHermano();
        }
    }
}