package com.example;

public class Calculator {

    public static void main(String[] args) {
        System.out.println("Calculator app started");
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public boolean isPositive(int num) {
        return num > 0;
    }
}
