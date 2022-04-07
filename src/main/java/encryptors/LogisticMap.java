package src.main.java.encryptors;

import src.main.java.util.BitOperations;
import src.main.java.util.MathOperations;

/**
 * Implementation of a chaotic logistic map image encryption algorithm.
 * This system is most chaotic for r values near 4.
 * Map is the function
 * f(x) = rx(1-x) where 0 <= x <= 1
 */
public class LogisticMap extends EncryptionAlgorithm {
    private static final double R = 3.9999d; //to be between 3.56994 and 4- most chaotic near 4

    private static final LogisticMap instance = new LogisticMap();

    private LogisticMap() {
    }

    public static LogisticMap getInstance() {
        return instance;
    }

    @Override
    public double applyMap(final double x) {
        return R * x * (1 - x);
    }

    @Override
    public String getDescription() {
        return "LM";
    }
}
