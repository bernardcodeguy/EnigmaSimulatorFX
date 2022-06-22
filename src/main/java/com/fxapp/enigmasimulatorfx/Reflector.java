package com.fxapp.enigmasimulatorfx;

public class Reflector {
    public static String alphabet = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    private static String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String reflect(String letter) {
        int i = alpha.indexOf(letter);
        return alphabet.substring(i, i+1);
    }
}
