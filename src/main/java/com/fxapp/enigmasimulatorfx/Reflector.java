/*
    Reflector of the enigma machine emulator
 */

package com.fxapp.enigmasimulatorfx;

public class Reflector {
    // Configuration of the reflector
    public static String alphabet = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    private static String alphabet2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // A method that reflect letters when called
    public static String reflect(String letter) {
        int i = alphabet2.indexOf(letter);
        return alphabet.substring(i, i+1);
    }
}
