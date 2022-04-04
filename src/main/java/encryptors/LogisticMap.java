package src.main.java.encryptors;

import src.main.java.util.BitOperations;
import src.main.java.util.MathOperations;

/**
 * Implementation of a chaotic logistic map image encryption algorithm.
 *
 */
public class LogisticMap extends EncryptionAlgorithm {
    private static final double R = 3.9999d; //to be between 3.56994 and 4- most chaotic near 4
    private static final double P0 = 0.5d;
    private static final int[] key1 = {0,1,1,0,1,1,0,1};
    private static final int[] key2 = {0,1,1,0,1,1,0,1};
    private static final int[] key3 = {0,1,1,0,1,1,0,1};
    private static final int[] key4 = {0,1,1,0,1,1,0,1};
    private static final int[] key5 = {0,1,1,0,1,1,0,1};
    private static final int[] key6 = {0,1,1,0,1,1,0,1};
    private static final int[] key7 = {0,1,1,0,1,1,0,1};
    private static final int[] key8 = {0,1,1,0,1,1,0,1};
    private static final int[] key9 = {0,1,1,0,1,1,0,1};
    private static final int[] key10 = {0,1,1,0,1,1,0,1};


    private static final LogisticMap instance = new LogisticMap();

    private LogisticMap() {
    }

    public static LogisticMap getInstance() {
        return instance;
    }

    private static boolean inRange(double y, double inclusiveStart, double exclusiveEnd) {
        return y >= inclusiveStart && y < exclusiveEnd;
    }

    @Override
    public double applyMap(final double x) {
        return R * x * (1 - x);
    }

}
