import managers.GestorCombate;
import managers.GestorExploracion;
import models.Enemigo;
import models.Jugador;
import models.Mapa;
import models.Ubicacion;
import ui.Interfaz;

public class Juego {
    private Mapa mapa;
    private Interfaz interfaz;
    private Jugador jugador;
    private GestorCombate gestorCombate;
    private GestorExploracion gestorExploracion;

    public Juego() {
        mapa = new Mapa();
        jugador = new Jugador("Link", mapa.getUbicacion("Pueblo Inicio"));
        interfaz = new Interfaz(mapa, jugador);
        gestorCombate = new GestorCombate(jugador, interfaz);
        gestorExploracion = new GestorExploracion(jugador, interfaz);
    }

    public void iniciar() {
        while (true) {
            interfaz.actualizarPantalla();
            String opcion = interfaz.obtenerEntrada();

            switch (opcion) {
                case "e":
                    explorarUbicacion();
                    break;
                case "i":
                    interfaz.mostrarInventario();
                    break;
                case "l":
                    luchar();
                    break;
                case "m":
                    moverJugador();
                    break;
                case "s":
                    interfaz.mostrarMensaje("Gracias por jugar!");
                    return;
                default:
                    interfaz.mostrarMensaje("Opción no válida.");
            }
        }
    }

    private void explorarUbicacion() {
        String resultado = gestorExploracion.explorarUbicacion();
        interfaz.mostrarResultadoExploracion(resultado);
    }

    private void luchar() {
        Enemigo enemigo = jugador.getUbicacionActual().getEnemigoActual();
        if (enemigo != null) {
            gestorCombate.pelear(enemigo);
            if (jugador.estaVivo()) {
                jugador.getUbicacionActual().eliminarEnemigo();
                interfaz.mostrarMensaje("Has derrotado al enemigo!");
            } else {
                interfaz.mostrarMensaje("Has sido derrotado. Fin del juego.");
                System.exit(0);
            }
        } else {
            interfaz.mostrarMensaje("- No hay enemigos en esta ubicación.");
        }
    }

    private void moverJugador() {
        String destino = interfaz.pedirDestinoViaje();
        Ubicacion nuevaUbicacion = mapa.getUbicacion(destino);
        if (nuevaUbicacion != null) {
            gestorExploracion.viajar(nuevaUbicacion);
            interfaz.mostrarResultadoViaje(true, destino);
        } else {
            interfaz.mostrarResultadoViaje(false, destino);
        }
    }

    public static void main(String[] args) {
        new Juego().iniciar();
    }
}