package view;

import controller.Schlange;
import model.Spielfeld;

import javax.swing.*;

public class SpielfeldFrame extends JFrame {
    private Spielfeld spielfeld = new Spielfeld();

    public SpielfeldFrame() {
        this.add(spielfeld);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        Schlange schlange = new Schlange(spielfeld.getSpielfeld());
        spielfeld.setSchlange(schlange);
    }

    public Spielfeld getSpielfeld() {
        return spielfeld;
    }
}
