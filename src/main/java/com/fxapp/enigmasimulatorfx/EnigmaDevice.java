/*
    This class emulates the Enigma Device itself
 */

package com.fxapp.enigmasimulatorfx;

public class EnigmaDevice {
    // Instantiate the objects present on an enigma device
    private Rotor leftRotor;
    private Rotor midRotor;
    private Rotor rightRotor;
    private Plugboard plugboard;

    // The constructor of the class
    public EnigmaDevice() {

        leftRotor = new Rotor("HKMFLGDQVZNTOWYEXUSPAIBRCJ");
        midRotor = new Rotor("AJDKSIRUXBWHPTMCQGZNLYFVOE");
        rightRotor = new Rotor("CDFHQLBPRTNVZXYEIMGAKWUSJO");
        plugboard = new Plugboard();
    }

    // Switching between letters
    public String switchLetter(String letter) {
        // The algorithm that switches the letters
        // It uses methods from above objects on an enigma machine
        String plugged = plugboard.switchLetter(letter);
        String one = rightRotor.switchLetter(plugged);
        String two = midRotor.switchLetter(one);
        String three = leftRotor.switchLetter(two);
        String reflect = Reflector.reflect(three);
        String four = leftRotor.invSwitchLetter(reflect);
        String five = midRotor.invSwitchLetter(four);
        String six = rightRotor.invSwitchLetter(five);
        plugged = plugboard.switchLetter(six);

        // Rotating the rotors when the position of right rotor is at "W" and "F"
        rightRotor.advance();
        if (rightRotor.isAt("W")) {
            midRotor.advance();
            if (midRotor.isAt("F")) {
                leftRotor.advance();
            }
        }
        return plugged;
    }

    // Getters and Setters of Rotors
    public Rotor getLeftRotor() {
        return leftRotor;
    }

    public Rotor getMiddleRotor() {
        return midRotor;
    }

    public Rotor getRightRotor() {
        return rightRotor;
    }

    public Plugboard getPlugboard() {
        return plugboard;
    }
}
