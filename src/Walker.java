/**
 * Diese Klasse definiert eine Spaziergänger:in, die dieselbe Strecke immer
 * auf und ab läuft. Dabei werden bei der Konstruktion die Startposition und
 * -richtung angegeben, sowie das Spielfeld, auf dem sie sich bewegt.
 *
 * @author Thomas Röfer
 */
class Walker extends Actor
{
    /** Die Spielfigur. */
    final Player player;

    /**
     * Erzeugt eine neue Spaziergänger:in.
     * @param x Die x-Koordinate der Spaziergänger:in.
     * @param y Die y-Koordinate der Spaziergänger:in.
     * @param rotation Die Rotation der Spaziergänger:in (0 = rechts ... 3 = oben).
     * @param field Das Spielfeld, auf dem sich die Spaziergänger:in bewegt.
     * @param player Die Spielfigur.
     */
    Walker(final int x, final int y, final int rotation, final String fileName,
            final Field field, final Player player)
    {
        super(x, y, rotation, fileName, field);
        this.player = player;
    }

    /**
     * Die Methode definiert das oben beschriebene Verhalten der Spaziergänger:in.
     */
    @Override
    void act()
    {
        // Vorwärts bewegen
        if (getRotation() == 0) {
            setLocation(getX() + 1, getY());
        }
        else if (getRotation() == 1) {
            setLocation(getX(), getY() + 1);
        }
        else if (getRotation() == 2) {
            setLocation(getX() - 1, getY());
        }
        else {
            setLocation(getX(), getY() - 1);
        }

        // Sound dazu abspielen
        playSound("step");

        // Umdrehen, wenn nächster Schritt nicht mehr ausführbar
        if (!canWalk(getRotation())) {
            setRotation(getRotation() + 2);
        }

        // Wenn gleiche Position wie Spielfigur, lasse diese verschwinden
        if (getX() == player.getX() && getY() == player.getY()) {
            player.setVisible(false);
            playSound("go-away");
        }
    }
}
