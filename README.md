# Ten Floors - MMORPG Backend Simulator

Trabajo Práctico Integrador para la materia **Algoritmos y Estructuras de Datos II** (UADE).  

El proyecto consiste en el desarrollo del backend de un videojuego de rol multijugador masivo en línea (MMORPG) titulado **"Ten Floors"**. Los aventureros deben explorar una colosal torre de 10 pisos únicos, afrontando desafíos concurrentes y transaccionales que justifican la implementación de estructuras de datos avanzadas optimizadas para el rendimiento y la consistencia del servidor.

---

## Integrantes y Roles

* **Gianluca Chia**: Arquitecto de Mundo, Habilidades y Documentación.
* **Axel Mendoza**: Especialista en Indexación, Almacenamiento e Inventarios.
* **Lautaro Salto**: Ingeniero de Flujos, Control de Accesos y Consola.

---

## Arquitectura del Sistema (Mapeo de TDAs)

El sistema se compone de los siguientes Trabajos de Datos Abstractos (TDAs) distribuidos eficientemente:

### Entorno y Progresión (A cargo de Gianluca)
* **Grafo No Dirigido (Mapa del Mundo)**: Representa los 10 pisos de la torre mediante una *Lista de Adyacencia*. Los vértices son zonas y las aristas son portales de teletransporte. Implementa recorridos BFS y DFS.
* **Árbol Genérico N-ario (Árbol de Habilidades)**: Jerarquía de clases de personajes (ej. Swordsman -> Habilidades Activas/Pasivas -> Sub-habilidades) con recorridos Preorden y Postorden.

### Almacenamiento e Indexación (A cargo de Axel)
* **Árbol AVL (Índice Global de Usuarios)**: Índice auto-balanceado que utiliza el ID de cuenta como clave de ordenamiento, garantizando logeos e inserciones eficientes mediante rotaciones simples y dobles.
* **ABB (Inventario del Jugador)**: Almacena los ítems de la mochila de cada jugador ordenados por ID numérico. Utiliza recorrido inorden para listarlos.
* **Árbol B (Historial de la Casa de Subastas)**: Estructura masiva simulada configurada con un grado pequeño ($t=2$ o $t=3$) que maneja transacciones a gran escala mediante división de nodos (*split*).

### Control de Flujos y Accesos (A cargo de Lautaro)
* **Pila (Historial de Menús)**: Navegación de la interfaz mediante comportamiento LIFO (`push`, `pop`, `peek`).
* **Cola Estándar (Espera de Mazmorras)**: Gestión FIFO para emparejamiento de *parties*.
* **Cola con Prioridad (Registro de Misiones / Tickets VIP)**
* **Conjunto o Diccionario (Jugadores Online)**: Registro rápido en tiempo constante para el estado online de las cuentas, evitando accesos duplicados.

---

## Consultas Complejas (Hitos Grupales)

El núcleo del sistema integra las estructuras individuales en 4 flujos lógicos cruzados:
1. **Viaje Rápido y Formación de Party**: Conecta el Grafo (búsqueda de rutas cortas con BFS), el Diccionario (verificación de amigos online) y la Cola (sala de espera de la mazmorra).
2. **Soporte Técnico VIP**: Extrae el ticket más urgente de la Cola con Prioridad, audita las transacciones en el Árbol B y compensa al jugador insertando un ítem en su ABB (Inventario).
3. **Auditoría de Gremios**: Recorre el Árbol Genérico para listar líderes, extrae sus datos del AVL global y los consolida en un Conjunto dinámico de líderes del servidor.
4. **Sistema de Comercio Seguro**: Permite deshacer la última transacción del jugador mediante la Pila, valida el ítem en el ABB global y actualiza los índices de cuenta en el AVL.

---

---

## Estructura del Proyecto
```text
ten-floors-mmorpg/
├── .gitignore
├── README.md
├── assets/                          # Capturas de pantalla, diagramas UML y PDFs de la cátedra
│   ├── diagramas/
│   └── documentos/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── tenfloors/
│   │               ├── Main.java                 # Punto de entrada exclusivo (Scanner / Switch-case)
│   │               │
│   │               ├── model/                    # ENTIDADES PURAS (Solo datos y lógica interna del objeto)
│   │               │     ├── ClaseHabilidad.java
│   │               │     ├── Cuenta.java
│   │               │     ├── Gremio.java
│   │               │     ├── Item.java
│   │               │     ├── Jugador.java
│   │               │     ├── Mision.java
│   │               │     ├── Ticket.java
│   │               │     └── Transaccion.java
│   │               │
│   │               ├── controlador/              # ORQUESTADOR CENTRAL
│   │               │     └── ControladorJuego.java
│   │               │
│   │               ├── gestor/                   # LÓGICA DE NEGOCIO Y MANIPULACIÓN DE TDAs DEL JUEGO
│   │               │     ├── GestionMazmorras.java
│   │               │     ├── GestionOnline.java
│   │               │     ├── GestorCuentas.java
│   │               │     ├── GestorInventario.java
│   │               │     ├── GestorMapa.java
│   │               │     ├── GestorMisiones.java
│   │               │     ├── GestorTransacciones.java
│   │               │     └── GestorHabilidades.java
│   │               │
│   │               ├── consulta/                 # HITOS GRUPALES (Las 4 Consultas Complejas requeridas)
│   │               │     ├── SistemaAuditoriaGremios.java
│   │               │     ├── SistemaComercioSeguro.java
│   │               │     ├── SistemaSoporteVIP.java
│   │               │     └── SistemaViajeYParty.java
│   │               │
│   │               └── tda/         # Estructuras de datos (TDAs individuales)
│   │                   ├── abb/
│   │                   ├── arbol/
│   │                   ├── arbolB/
│   │                   ├── avl/
│   │                   ├── cola/
│   │                   ├── colaPrioridad/
│   │                   ├── diccionario/
│   │                   ├── grafo/
│   │                   └── pila/
│   └── test/                           # Panel de testeo aislado para cada integrante
│       └── java/
│           └── com/
│               └── tenfloors/
│                   └── tda/
│                       └── TestCola.java # Por ejemplo
```

---

## Reglas de Branching (Git)

Para mantener el repositorio limpio y evitar conflictos durante los merges, cada miembro trabajará estrictamente en su propia rama de desarrollo:
* `feature/gian`
* `feature/axel`
* `feature/lauti`

Toda integración a la rama principal (`main`) se realizará mediante Pull Requests validados de forma conjunta.
