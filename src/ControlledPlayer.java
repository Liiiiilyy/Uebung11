import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Diese Klasse definiert eine Figur, die von einer Instanz der
 * Klasse RemotePlayer per Netzwerk ferngesteuert wird.
 *
 * @author Thomas Röfer
 */
class ControlledPlayer extends Player
{
    /** Der Socket, über den die Befehle empfangen werden. */
    private Socket socket = null;

    /**
     * Erzeugen und Anzeigen einer neuen Spielfigur.
     * @param x Die x-Koordinate dieser Spielfigur im Gitter.
     * @param y Die y-Koordinate dieser Spielfigur im Gitter.
     * @param rotation Die Rotation dieser Spielfigur (0 = rechts ... 3 = oben).
     * @param field Das Spielfeld, auf dem sich diese Spielfigur bewegt.
     * @param port Auf diesem Port wird auf eine Verbindung gewartet.
     */
    ControlledPlayer(final int x, final int y, final int rotation, final Field field,
            final int port)
    {
        super(x, y, rotation, field);
        try (final ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(port));
            socket = server.accept();
        }
        catch (final IOException e) {
            System.err.println("Akzeptieren von Verbindung auf Port " + port
                    + " fehlgeschlagen.");
            setVisible(false);
        }
    }

    /**
     * Die Spielfigur bewegt sich entsprechend der aus dem Netzwerk
     * empfangenen Richtung.
     */
    @Override
    void act()
    {
        try {
            final int direction = socket.getInputStream().read();
            if (direction == -1) {
                System.err.println("Verbindung wurde beendet.");
                setVisible(false);
            }
            else {
                setRotation(direction);
                final int[][] offsets = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                setLocation(getX() + offsets[getRotation()][0], getY() + offsets[getRotation()][1]);
                playSound("step");
                sleep(200);
            }
        }
        catch (final IOException e) {
            System.err.println("Kann Bewegung nicht empfangen.");
            setVisible(false);
        }
    }

    /**
     * Macht dieses Spielobjekt sichtbar bzw. versteckt es. Wenn es
     * versteckt wird, wird zudem die Netzwerkverbindung geschlossen.
     * @param visible Soll das Objekt sichtbar in der Zeichenfläche sein?
     */
    @Override
    public void setVisible(final boolean visible)
    {
        super.setVisible(visible);
        if (!visible && socket != null) {
            try {
                socket.close();
            }
            catch (final IOException e) {
                // Ignorieren
            }
        }
    }
}
