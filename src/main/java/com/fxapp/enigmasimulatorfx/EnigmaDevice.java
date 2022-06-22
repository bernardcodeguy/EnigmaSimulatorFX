package com.fxapp.enigmasimulatorfx;

public class EnigmaDevice {
    private Rotor leftRotor;
    private Rotor midRotor;
    private Rotor rightRotor;
    private Plugboard plugboard;

    public EnigmaDevice() {

        leftRotor = new Rotor("HKMFLGDQVZNTOWYEXUSPAIBRCJ");
        midRotor = new Rotor("AJDKSIRUXBWHPTMCQGZNLYFVOE");
        rightRotor = new Rotor("CDFHQLBPRTNVZXYEIMGAKWUSJO");
        plugboard = new Plugboard();
    }

    public String switchLetter(String letter) {

        String plugged = plugboard.switchLetter(letter);
        String one = rightRotor.pass(plugged);
        String two = midRotor.pass(one);
        String three = leftRotor.pass(two);
        String reflect = Reflector.reflect(three);
        String four = leftRotor.invPass(reflect);
        String five = midRotor.invPass(four);
        String six = rightRotor.invPass(five);
        plugged = plugboard.switchLetter(six);

        rightRotor.advance();
        if (rightRotor.isAt("W")) {
            midRotor.advance();
            if (midRotor.isAt("F")) {
                leftRotor.advance();
            }
        }
        return plugged;
    }

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
