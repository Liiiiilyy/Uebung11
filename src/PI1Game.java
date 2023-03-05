import java.util.stream.Stream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Dies ist die Hauptklasse eines Spiels. Sie enthält die Hauptmethode, die zum
 * Starten des Spiels aufgerufen werden muss.
 *
 * @author Thomas Röfer
 */
abstract class PI1Game extends Game
{
    /**
     * Das Spiel beginnt durch Aufruf dieser Methode.
     * @param address Die IP-Adresse des Rechners, auf dem die Spielfigur
     *         ferngesteuert werden soll. Ist sie null, ist stattdessen
     *         die hier erzeugte Spielfigur fernsteuerbar.
     * @param port Der Port, über den die Fernsteuerverbindung läuft.
     */
    public static void main1(final String address, final int port)
    {
        // Den Level erzeugen
        final Level level = new Level("levels/1.lvl", address, port);

        // Die Hauptschleife des Spiels
        // Die durch den Datenstrom transportierten Werte werden überhaupt nicht
        // verwendet. Einzig wichtig ist, dass keine mehr erzeugt werden, sobald
        // die Bedingung, die als zweiter Parameter formuliert ist, falsch wird.
        // Dann endet die Verarbeitung.
        Stream.generate(() -> level.getActors())
                .takeWhile(actors -> actors.get(0).isVisible())
                .forEach(actors -> actors.forEach(actor -> actor.act()));
        Stream.iterate(level.getActors(), actors -> actors.get(0).isVisible(), actors -> actors)
                .forEach(actors -> actors.forEach(actor -> actor.act()));

        level.hide();
    }
    public static int getAvailableTcpPort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int localPort = serverSocket.getLocalPort();
            //这里一定要close(),不然这个端口无法被其他程序使用
            serverSocket.close();
            return localPort;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void main(String[] args)
    {
        main1("", getAvailableTcpPort());
    }

}
