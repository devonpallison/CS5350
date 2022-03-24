package src.main.java.encryptors;

import src.main.java.util.BitOperations;

/**
 * Implementation of a chaotic logistic map image encryption algorithm.
 *
 */
public class LogisticMap implements EncryptionAlgorithm {
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

    @Override
    public byte[] encrypt(byte[] plainText) {
        if(plainText.length % 3 != 0) {
            throw new RuntimeException("Expect R,G,B pattern for byte array here");
        }

        final byte[] cypherText = new byte[plainText.length];
        double y0 = logisticMap(initialConditionY());
        for(int i = 0; i < plainText.length; i+=3) {
            byte byteRed = plainText[i];
            byte byteGreen = plainText[i + 1];
            byte byteBlue = plainText[i + 2];

            double y = logisticMap(y0);
            y0 = y;
            if(inRange(y, 0.1, 0.13) || inRange(y, 0.34, 0.37) || inRange(y, 0.58, 0.62)) {
                byteRed = (byte) ~byteRed;
                byteGreen = (byte) ~byteGreen;
                byteBlue = (byte) ~byteBlue;
            } else if(inRange(y, 0.13, 0.16) || inRange(y, 0.37, 0.40) || inRange(y, 0.62, 0.66)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key4));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key5));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key6));
            } else if(inRange(y, 0.16, 0.19) || inRange(y, 0.40, 0.43) || inRange(y, 0.66, 0.70)) {
                byteRed = (byte) (((int) byteRed) + ((int) BitOperations.convertToByte(key4)) + ((int) BitOperations.convertToByte(key5)));
                byteGreen = (byte) (((int) byteGreen) + ((int) BitOperations.convertToByte(key5)) + ((int) BitOperations.convertToByte(key6)));
                byteBlue = (byte) (((int) byteBlue) + ((int) BitOperations.convertToByte(key6)) + ((int) BitOperations.convertToByte(key4)));
            } else if(inRange(y, 0.19, 0.22) || inRange(y, 0.43, 0.46) || inRange(y, 0.70, 0.74)) {
                byteRed = (byte) (~(byteRed ^ BitOperations.convertToByte(key4)));
                byteGreen = (byte) (~(byteGreen ^ BitOperations.convertToByte(key5)));
                byteBlue = (byte) (~(byteBlue ^ BitOperations.convertToByte(key6)));
            } else if(inRange(y, 0.22, 0.25) || inRange(y, 0.46, 0.49) || inRange(y, 0.74, 0.78)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key7));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key8));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key9));
            } else if(inRange(y, 0.25, 0.28) || inRange(y, 0.49, 0.52) || inRange(y, 0.78, 0.82)) {
                byteRed = (byte) (((int) byteRed) + ((int) BitOperations.convertToByte(key7)) + ((int) BitOperations.convertToByte(key8)));
                byteGreen = (byte) (((int) byteGreen) + ((int) BitOperations.convertToByte(key8)) + ((int) BitOperations.convertToByte(key9)));
                byteBlue = (byte) (((int) byteBlue) + ((int) BitOperations.convertToByte(key9)) + ((int) BitOperations.convertToByte(key7)));
            } else if(inRange(y, 0.28, 0.31) || inRange(y, 0.52, 0.55) || inRange(y, 0.82, 0.86)) {
                byteRed = (byte) (~(byteRed ^ BitOperations.convertToByte(key7)));
                byteGreen = (byte) (~(byteGreen ^ BitOperations.convertToByte(key8)));
                byteBlue = (byte) (~(byteBlue ^ BitOperations.convertToByte(key9)));
            } else {
                //do nothing
            }

            cypherText[i] = byteRed;
            cypherText[i+1] = byteGreen;
            cypherText[i+2] = byteBlue;
        }

        return cypherText;
    }

    @Override
    public byte[] decrypt(byte[] cypherText) {
        if(cypherText.length % 3 != 0) {
            throw new RuntimeException("Expect R,G,B pattern for byte array here");
        }

        final byte[] plainText = new byte[cypherText.length];
        double y0 = logisticMap(initialConditionY());
        for(int i = 0; i < cypherText.length; i+=3) {
            byte byteRed = cypherText[i];
            byte byteGreen = cypherText[i + 1];
            byte byteBlue = cypherText[i + 2];

            double y = logisticMap(y0);
            y0 = y;
            if(inRange(y, 0.1, 0.13) || inRange(y, 0.34, 0.37) || inRange(y, 0.58, 0.62)) {
                byteRed = (byte) ~byteRed;
                byteGreen = (byte) ~byteGreen;
                byteBlue = (byte) ~byteBlue;
            } else if(inRange(y, 0.13, 0.16) || inRange(y, 0.37, 0.40) || inRange(y, 0.62, 0.66)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key4));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key5));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key6));
            } else if(inRange(y, 0.16, 0.19) || inRange(y, 0.40, 0.43) || inRange(y, 0.66, 0.70)) {
                byteRed = (byte) (((int) byteRed) + 256 - ((int) BitOperations.convertToByte(key4)) - ((int) BitOperations.convertToByte(key5)));
                byteGreen = (byte) (((int) byteGreen) + 256 - ((int) BitOperations.convertToByte(key5)) - ((int) BitOperations.convertToByte(key6)));
                byteBlue = (byte) (((int) byteBlue) + 256 - ((int) BitOperations.convertToByte(key6)) - ((int) BitOperations.convertToByte(key4)));
            } else if(inRange(y, 0.19, 0.22) || inRange(y, 0.43, 0.46) || inRange(y, 0.70, 0.74)) {
                byteRed = (byte) ((~byteRed) ^ BitOperations.convertToByte(key4));
                byteGreen = (byte) ((~byteGreen) ^ BitOperations.convertToByte(key5));
                byteBlue = (byte) ((~byteBlue) ^ BitOperations.convertToByte(key6));
            } else if(inRange(y, 0.22, 0.25) || inRange(y, 0.46, 0.49) || inRange(y, 0.74, 0.78)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key7));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key8));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key9));
            } else if(inRange(y, 0.25, 0.28) || inRange(y, 0.49, 0.52) || inRange(y, 0.78, 0.82)) {
                byteRed = (byte) (((int) byteRed) + 256 - ((int) BitOperations.convertToByte(key7)) - ((int) BitOperations.convertToByte(key8)));
                byteGreen = (byte) (((int) byteGreen) + 256 - ((int) BitOperations.convertToByte(key8)) - ((int) BitOperations.convertToByte(key9)));
                byteBlue = (byte) (((int) byteBlue) + 256 - ((int) BitOperations.convertToByte(key9)) - ((int) BitOperations.convertToByte(key7)));
            } else if(inRange(y, 0.28, 0.31) || inRange(y, 0.52, 0.55) || inRange(y, 0.82, 0.86)) {
                byteRed = (byte) ((~byteRed) ^ BitOperations.convertToByte(key7));
                byteGreen = (byte) ((~byteGreen) ^ BitOperations.convertToByte(key8));
                byteBlue = (byte) ((~byteBlue) ^ BitOperations.convertToByte(key9));
            } else {
                //do nothing
            }

            plainText[i] = byteRed;
            plainText[i+1] = byteGreen;
            plainText[i+2] = byteBlue;
        }

        return plainText;
    }


    private static boolean inRange(double y, double inclusiveStart, double exclusiveEnd) {
        return y >= inclusiveStart && y < exclusiveEnd;
    }

    private static double logisticMap(final double x) {
        return R * x * (1 - x);
    }

    private static double initialConditionX() {
        return (x01() - x02()) % 1; //todo I think java allows negatives
    }

    private static double initialConditionY() {
        final int[] keyString = keyStringY();
        return (y01(keyString) - y02(keyString)) % 1; //todo I think java allows negatives
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
            final double mappedValue = logisticMap(prev);
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
