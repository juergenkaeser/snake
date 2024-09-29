package controller;

import model.Spielfeld;

import java.util.ArrayList;

public class Schlange {
    private ArrayList<int[][]> schlange = new ArrayList<>();
    private int x, y, punkte = 0;
    private String[][] spielfeld;
    public Schlange(String[][] spielfeld) {
        this.x = 10;
        this.y = 10;
        this.spielfeld = spielfeld;
        spielfeld[this.y][this.x] = "S";
        schlange.add(new int[][] {{this.x, this.y}});
        System.out.println("Spiel gestartet!");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPunkte() {
        return punkte;
    }

    public void bewegen(String richtung) {
        switch (richtung) {
            case "w":
                y--;
                break;
            case "a":
                x--;
                break;
            case "s":
                y++;
                break;
            case "d":
                x++;
                break;
        }

        try  {
            // Neue Position in ArrayList speichern
            schlange.add(new int[][] {{this.x, this.y}});

            // Test, ob Schlange gefressen hat
            if (!spielfeld[this.y][this.x].equals("A")) {
                spielfeld[schlange.get(0)[0][1]][schlange.get(0)[0][0]] = "o";
                schlange.remove(0);
                punkte++;
            } else {
                punkte += 100;
            }

            // Test, ob es sich um einen Extra-Apfel handelt. Dadurch verlangsamt ich das Spiel.
            if (spielfeld[this.y][this.x].equals("E")) {
                Spielfeld.setWartezeit(Spielfeld.getWartezeit() + 50);
            }

            // Test, ob Schlange sich selbst frisst
            if (spielfeld[this.y][this.x].equals("S")) {
                spielBeendet();
            }
            spielfeld[this.y][this.x] = "S";
        } catch (ArrayIndexOutOfBoundsException e) {
            spielBeendet();
        }
    }

    public void spielBeendet() {
        System.out.println("Spiel beeendet!");
        System.out.println("Erreichte Punkte: " + punkte);
        System.exit(0);
    }
}
