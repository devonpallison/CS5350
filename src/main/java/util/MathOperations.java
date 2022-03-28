package src.main.java.util;

public class MathOperations {

    //return a%b, but no negatives unlike default javaa
    public static double nonNegativeMod(final double a, final double b) {
        return (a % b + b) % b;
    }
}
