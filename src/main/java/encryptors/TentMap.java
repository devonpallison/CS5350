package src.main.java.encryptors;

public class TentMap extends EncryptionAlgorithm {
    private final double r = 1.99999d;

    @Override
    public double applyMap(double x) {
        if(x < 0.5) {
            return r * x;
        } else {
            return r * (1 - x);
        }
    }
}
