package org.cy;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    public int minus(int a, int b) {
        return a - b;
    }
    public int multi(int a, int b) {
        return a * b;
    }
    public int divd(int a, int b) {
        if (b==0){
            throw new ArithmeticException("除数不能为0");
        }
        return a / b;
    }
    public double sqrt(double x) {
        return Math.sqrt(x);
    }

    public int abs(int x) {
        return Math.abs(x);
    }

    public double inverse(double x) {
        return 1.0 / x;
    }
}
