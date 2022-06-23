/*
    Reflector of the enigma machine emulator
 */

package com.fxapp.enigmasimulatorfx;

public class Reflector {
    // Configuration of the reflector
    public static String alphabet = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    private static String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // A method that reflect letters when called
    public static String reflect(String letter) {
        int i = alpha.indexOf(letter);
        return alphabet.substring(i, i+1);
    }
}
