package model;

import controller.Schlange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Spielfeld extends JPanel implements ActionListener {
    static final int EINHEITEN_PRO_SEITE = 20;
    static final int PANEL_PIXEL = 900;
    static final int EINHEITEN_PIXEL = PANEL_PIXEL / EINHEITEN_PRO_SEITE;
    static final int PROZENT = 20;
    private String[][] spielfeld;
    private Schlange schlange;
    private static int wartezeit = 250;
    private String aktuelleTaste = "w";
    private int zeileApfel, spalteApfel;
    private Random random = new Random();
    private Timer timer;

    public Spielfeld() {
        this.setPreferredSize(new Dimension(PANEL_PIXEL, PANEL_PIXEL));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MeinKeyAdapter());
        starteSpiel();
    }

    public String[][] getSpielfeld() {
        return spielfeld;
    }

    public void setSchlange(Schlange schlange) {
        this.schlange = schlange;
    }

    public static int getWartezeit() {
        return wartezeit;
    }

    public static void setWartezeit(int wartezeit) {
        Spielfeld.wartezeit = wartezeit;
    }

    public void starteSpiel() {
        spielfeldInitialisieren();
        timer = new Timer(wartezeit,this);
        timer.start();
    }

    public void spielfeldInitialisieren() {
        spielfeld = new String[EINHEITEN_PRO_SEITE][EINHEITEN_PRO_SEITE];
        for (int zeile = 0; zeile < EINHEITEN_PRO_SEITE; zeile++) {
            for (int spalte = 0; spalte < EINHEITEN_PRO_SEITE; spalte++) {
                spielfeld[zeile][spalte] = "o";
            }
        }
    }

    public void zeichneSpielfeld(Graphics g) {
        for (int zeile = 0; zeile < EINHEITEN_PRO_SEITE; zeile++) {
            for (int spalte = 0; spalte < EINHEITEN_PRO_SEITE; spalte++) {
                if (spielfeld[zeile][spalte].equals("o")) {
                    g.setColor(new Color(24, 24, 27));
                    g.fillRect(spalte * EINHEITEN_PIXEL, zeile * EINHEITEN_PIXEL, EINHEITEN_PIXEL, EINHEITEN_PIXEL);
                } else if (spielfeld[zeile][spalte].equals("A")) {
                    g.setColor(new Color(238, 255, 247));
                    g.fillOval(spalte * EINHEITEN_PIXEL, zeile * EINHEITEN_PIXEL, EINHEITEN_PIXEL, EINHEITEN_PIXEL);
                } else if (spielfeld[zeile][spalte].equals("E")) {
                    g.setColor(new Color(77, 222, 150));
                    g.fillOval(spalte * EINHEITEN_PIXEL, zeile * EINHEITEN_PIXEL, EINHEITEN_PIXEL, EINHEITEN_PIXEL);
                } else if (spielfeld[zeile][spalte].equals("S")) {
                    if (schlange.getX() == spalte && schlange.getY() == zeile) {
                        g.setColor(new Color(77, 222, 150)); // Farbe Kopf der Schlange
                    } else {
                        g.setColor(new Color(44, 126, 85)); // Farbe Koerper der Schlange
                    }
                    g.fillRect(spalte * EINHEITEN_PIXEL, zeile * EINHEITEN_PIXEL, EINHEITEN_PIXEL, EINHEITEN_PIXEL);
                }
            }
        }

        g.setColor(new Color(160, 160, 174));
        g.setFont( new Font("Dialog",Font.BOLD, 36));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Punkte: " + schlange.getPunkte() + "     Tempo: " + wartezeit, (PANEL_PIXEL - metrics.stringWidth("Punkte: "+ schlange.getPunkte() + "     Tempo: " + wartezeit))/2, g.getFont().getSize());
    }

    public void setApfel() {
        do {
            zeileApfel = (int) (Math.random() * (EINHEITEN_PRO_SEITE- 2)) + 1;
            spalteApfel = (int) (Math.random() * (EINHEITEN_PRO_SEITE- 2)) + 1;
        } while (spielfeld[zeileApfel][spalteApfel].equals("S")); // Apfel darf nicht auf Schlange gesetzt werden

        if (Math.random() * 100 < PROZENT / 4) {
            spielfeld[zeileApfel][spalteApfel] = "E";
        } else {
            spielfeld[zeileApfel][spalteApfel] = "A";
        }
    }

    public boolean keinApfelVorhanden() {
        for (int zeile = 0; zeile < EINHEITEN_PRO_SEITE; zeile++) {
            for (int spalte = 0; spalte < EINHEITEN_PRO_SEITE; spalte++) {
                if (spielfeld[zeile][spalte].equals("A") || spielfeld[zeile][spalte].equals("E")) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Checkt, ob Apfel gesetzt werden soll
        if (random.nextInt(100) < PROZENT && keinApfelVorhanden()) {
            setApfel();
        }

        // Checkt, ob Geschwindigkeit erhöhnt wird
        if (random.nextInt(100) < PROZENT) {
            if (wartezeit > 150) {
                wartezeit--;
                timer.setDelay(wartezeit);
            }
        }

        schlange.bewegen(aktuelleTaste);
        repaint();
    }

    public class MeinKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (!aktuelleTaste.equals("s")) {
                        aktuelleTaste = "w";
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (!aktuelleTaste.equals("d")) {
                        aktuelleTaste = "a";
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!aktuelleTaste.equals("w")) {
                        aktuelleTaste = "s";
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!aktuelleTaste.equals("a")) {
                        aktuelleTaste = "d";
                    }
                    break;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        zeichneSpielfeld(g);
    }
}
