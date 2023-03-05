import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Diese Klasse definiert die Figur, die von der Spieler:in gesteuert
 * wird. Die Figur bewegt sich dabei auf der Gitterstruktur des
 * Spielfeldes. Zusätzlich wird ihre Bewegung per Netzwerk zu übertragen,
 * um dieselbe Figur in einer entfernten Instanz des Spiels fernzusteuern.
 *
 * @author Thomas Röfer
 */
class RemotePlayer extends Player
{
    /** Der Socket, über den die Befehle geschickt werden. */
    private final Socket socket = new Socket();

    /**
     * Erzeugen und Anzeigen einer neuen Spielfigur.
     * @param x Die x-Koordinate dieser Spielfigur im Gitter.
     * @param y Die y-Koordinate dieser Spielfigur im Gitter.
     * @param rotation Die Rotation dieser Spielfigur (0 = rechts ... 3 = oben).
     * @param field Das Spielfeld, auf dem sich diese Spielfigur bewegt.
     * @param address Die IP-Adresse des Rechners mit der ferngesteuerten Figur.
     * @param port Der Portauf dem Rechner mit der ferngesteuerten Figur.
     */
    RemotePlayer(final int x, final int y, final int rotation, final Field field,
            final String address, final int port)
    {
        super(x, y, rotation, field);
        try {
            socket.connect(new InetSocketAddress(address, port));
        }
        catch (final IOException e) {
            System.err.println("Kann mit nicht mit " + address + ":" + port
                    + " verbinden.");
            setVisible(false);
        }
    }

    /**
     * Die Spielfigur bewegt sich entsprechend der von der Spieler:in
     * bewegten Tasten. Diese Methode kehrt erst zurück, wenn ein
     * gültiger Zug gemacht wurde. Sie überträgt zudem die Bewegungsrichtung
     * per Netzwerk.
     */
    @Override
    void act()
    {
        super.act();
        try {
            socket.getOutputStream().write(getRotation());
            socket.getOutputStream().flush();
        }
        catch (final IOException e) {
            System.err.println("Kann Bewegung nicht senden.");
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
        if (!visible) {
            try {
                socket.close();
            }
            catch (final IOException e) {
                // Ignorieren
            }
        }
    }
}
