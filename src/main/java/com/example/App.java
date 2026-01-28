package com.example;

public class App {
    public static void main(String[] args) {
        System.out.println("String Utils Application Started");

        String input = "DevOps";
        System.out.println("Uppercase: " + toUpper(input));
        System.out.println("Reverse: " + reverse(input));
    }

    public static String toUpper(String input) {
        if (input == null) {
            return "";
        }
        return input.toUpperCase();
    }

    public static String reverse(String input) {
        if (input == null) {
            return "";
        }
        return new StringBuilder(input).reverse().toString();
    }
}
