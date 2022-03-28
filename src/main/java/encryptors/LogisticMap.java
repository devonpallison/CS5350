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

    private static double initialConditionX() {
        return MathOperations.nonNegativeMod(x01() - x02(), 1);
    }

    @Override
    public double initialConditionY() {
        final int[] keyString = keyStringY();
        return MathOperations.nonNegativeMod(y01(keyString) - y02(keyString), 1);
    }

    private static double x01() {
        final int[] keyString = new int[key1.length *3];
        System.arraycopy(key4, 0, keyString, 0, key1.length);
        System.arraycopy(key5, 0, keyString, key1.length, key1.length);
        System.arraycopy(key6, 0, keyString, key1.length * 2, key1.length);
        return x01(keyString);
    }

    private static double x01(final int[] keyString) {
        double x01 = 0;
        for(int i = 0; i < keyString.length; i++) {
            x01 += keyString[i] * Math.pow(2, i);
        }

        return x01 / Math.pow(2, 24);
    }

    private static double x02() {
        final int[] hexKey = getHexKey02();
        double x02 = 0;
        for(int i = 13; i < 18; i++) {
            x02 += hexKey[i] / 96.0;
        }

        return x02;
    }

    //need blocks 13-18
    private static int[] getHexKey02() {
        final int[] hexKey = new int[6];
        int block13 = 0;
        for(int i = 0; i < 4; i++) {
            block13 += key7[i] * Math.pow(2, 4 - i);
        }
        hexKey[0] = block13;
        int block14 = 0;
        for(int i = 4; i < 8; i++) {
            block14 += key7[i] * Math.pow(2, 8 - i);
        }
        hexKey[1] = block14;

        int block15 = 0;
        for(int i = 0; i < 4; i++) {
            block15 += key8[i] * Math.pow(2, 4 - i);
        }
        hexKey[2] = block15;

        int block16 = 0;
        for(int i = 4; i < 8; i++) {
            block16 += key8[i] * Math.pow(2, 8 - i);
        }
        hexKey[3] = block16;

        int block17 = 0;
        for(int i = 0; i < 4; i++) {
            block13 += key9[i] * Math.pow(2, 4 - i);
        }
        hexKey[4] = block13;

        int block18 = 0;
        for(int i = 4; i < 8; i++) {
            block18 += key9[i] * Math.pow(2, 8 - i);
        }
        hexKey[5] = block18;

        return hexKey;
    }

    private static int[] pValues(double initialCondition) {
        final int[] pValues = new int[24];
        double prev = initialCondition;

        for(int i = 0; i < 24; i++) {
            final double mappedValue = getInstance().applyMap(prev);
            //exclude values less than 0.1 or greater than 0.9
            if(mappedValue > 0.9 || mappedValue < 0.1) {
                i--;
            } else {
                pValues[i] = ((int) (23 * (mappedValue - 0.1) / 0.8)) + 1;
            }
            prev = mappedValue;
        }

        return pValues;
    }

    private static int[] keyStringY() {
        final int[] keyString = new int[key1.length *3];
        System.arraycopy(key1, 0, keyString, 0, key1.length);
        System.arraycopy(key2, 0, keyString, key1.length, key1.length);
        System.arraycopy(key3, 0, keyString, key1.length * 2, key1.length);
        return keyString;
    }

    private static double y01(final int[] keyString) {
        return BitOperations.toBase10(keyString) / Math.pow(2, 24);
    }

    private static double y02(final int[] keyString) {
        double y02 = 0;
        for(int i = 0; i < keyString.length; i++) {
            y02 += keyString[i] * Math.pow(2, i);
        }

        return y02 / Math.pow(2, 24);
    }
}
