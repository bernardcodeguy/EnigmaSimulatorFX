/*
    This class emulates the rotors on the enigma machine
 */

package com.fxapp.enigmasimulatorfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Rotor {
    // The aplhabet of each rotor object
    public ObservableList<String> alphabet;

    // The alphabets to be used for switching with rotor objects
    private static String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Class constructor
    public Rotor (String alphabet) {
        this.alphabet = FXCollections.observableArrayList();
        for (char ch: alphabet.toCharArray()) {
            this.alphabet.add(String.valueOf(ch));
        }
    }

    // A method to find whether a rotor letter is at a particular alphabet
    public boolean isAt(String letter) {
        return alphabet.get(0).equals(letter);
    }

    // A method to pass a letter to rotor alphabets and plugboards
    public String switchLetter(String letter) {
        int i = alpha.indexOf(letter);
        String c = alphabet.get(i);
        return c;
    }

    // Method for switches related to rotors and reflector
    public String invSwitchLetter(String letter) {
        int i = alphabet.indexOf(letter);
        String c = alpha.substring(i, i+1);
        return c;
    }

    // A method to rotate forward a rotor position
    public void advance() {
        alphabet.add(alphabet.get(0));
        alphabet.remove(0);
    }

    // A method to rotate backward a rotor position
    public void advanceBack() {
        int len = alphabet.size();
        alphabet.add(0, alphabet.get(len - 1));
        alphabet.remove(len);
    }
}
