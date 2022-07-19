/*
    Class that emulates the plugboard of an enigma device
 */

package com.fxapp.enigmasimulatorfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Plugboard {
    // List of plugs
    private ObservableList<Plug> plugs;

    // Plugboard constructor
    public Plugboard(){
        this.plugs = FXCollections.observableArrayList();
    }

    // Switching a two letters in the plugboard

    public String switchLetter(String letter) {
        for (Plug plug : plugs) {
            if (plug.connectsWith(letter)) {
                return plug.getOpposite(letter);
            }
        }
        return letter;
    }

    // Adds a plug when it is called recursively
    public void addPlug(String a, String b) {
        addPlug(new Plug(a, b));
    }

    public void addPlug(Plug p) {
        plugs.add(p);
    }

    // Removes a plug when it is called
    public void removePlug(int i) {
        plugs.remove(i);
    }

    // A boolean that checked if there is plugged in activity or not
    public boolean isPlugged(String l) {
        for (Plug plug: plugs) {
            if (plug.connectsWith(l)) {
                return true;
            }
        }
        return false;
    }

    // Returns plugs in the plugboard
    public ObservableList<Plug> getPlugs() {
        return plugs;
    }


    // Plug Inner class
    public static class Plug {
        final String oldLetter;
        final String newLetter;

        // Plug constructor
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

        // Checks if a one letter plug connects with another
        public boolean connectsWith(String l) {
            return l.equals(this.oldLetter) || l.equals(this.newLetter);
        }

        // Checks if the first plug letter is equal to itself
        public boolean equals(Plug other) {
            return other.oldLetter.equals(this.oldLetter) && other.newLetter.equals(this.newLetter);
        }

        // String that displays the plugged in letters in the plugboard
        public String toString() {
            return this.oldLetter + " \u2B0C " + this.newLetter;
        }

    }
}
