package com.fxapp.enigmasimulatorfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Plugboard {
    private ObservableList<Plug> plugs;

    public Plugboard(){
        this.plugs = FXCollections.observableArrayList();
    }

    public String switchLetter(String letter) {
        for (Plug plug : plugs) {
            if (plug.connectsWith(letter)) {
                return plug.getOpposite(letter);
            }
        }
        return letter;
    }

    public void addPlug(String a, String b) {
        addPlug(new Plug(a, b));
    }

    public void addPlug(Plug p) {
        plugs.add(p);
    }

    public void removePlug(int i) {
        plugs.remove(i);
    }

    public boolean isPlugged(String l) {
        for (Plug plug: plugs) {
            if (plug.connectsWith(l)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<Plug> getPlugs() {
        return plugs;
    }



    public static class Plug {
        final String oldLetter;
        final String newLetter;

        public Plug(String oldLetter, String newLetter) {
            this.oldLetter = oldLetter;
            this.newLetter = newLetter;
        }

        public String getOpposite(String l) {
            if (l.equals(oldLetter)) {
                return newLetter;
            } else {
                return oldLetter;
            }
        }

        public boolean connectsWith(String l) {
            return l.equals(this.oldLetter) || l.equals(this.newLetter);
        }

        public boolean equals(Plug other) {
            return other.oldLetter.equals(this.oldLetter) && other.newLetter.equals(this.newLetter);
        }

        public String toString() {
            return this.oldLetter + " \u2B0C " + this.newLetter;
        }

    }
}
