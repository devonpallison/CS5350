package src.main.java.encryptors;

import src.main.java.util.BitOperations;
import src.main.java.util.MathOperations;

/*
Encryption algorithm taken from
 https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.88.5203&rep=rep1&type=pdf
 */
public abstract class EncryptionAlgorithm {
    protected static final int[] key1 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key2 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key3 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key4 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key5 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key6 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key7 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key8 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key9 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] key10 = {0, 1, 1, 0, 1, 1, 0, 1};

    /**
     * Apply the chaotic transformation.
     *
     * @param x input
     */
    public abstract double applyMap(final double x);

    /**
     * Encrypts the entry byte array representation of an image in chunks of RGB values.
     *
     * @param plainText byte array representation of an image. Assume to be in order
     *                  R1,G1,B1,G2,G2,B2,...,RnGnBn
     * @return encrypted image
     */
    public byte[] encrypt(byte[] plainText) {
        if (plainText.length % 3 != 0) {
            throw new RuntimeException("Expect R,G,B pattern for byte array here");
        }

        final byte[] cypherText = new byte[plainText.length];
        double y0 = applyMap(initialConditionY());
        for (int i = 0; i < plainText.length; i += 3) {
            byte byteRed = plainText[i];
            byte byteGreen = plainText[i + 1];
            byte byteBlue = plainText[i + 2];

            double y = applyMap(y0);
            y0 = y;
            if (inRange(y, 0.1, 0.13) || inRange(y, 0.34, 0.37) || inRange(y, 0.58, 0.62)) {
                byteRed = (byte) ~byteRed;
                byteGreen = (byte) ~byteGreen;
                byteBlue = (byte) ~byteBlue;
            } else if (inRange(y, 0.13, 0.16) || inRange(y, 0.37, 0.40) || inRange(y, 0.62, 0.66)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key4));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key5));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key6));
            } else if (inRange(y, 0.16, 0.19) || inRange(y, 0.40, 0.43) || inRange(y, 0.66, 0.70)) {
                byteRed = (byte) (((int) byteRed) + ((int) BitOperations.convertToByte(key4)) + ((int) BitOperations.convertToByte(key5)));
                byteGreen = (byte) (((int) byteGreen) + ((int) BitOperations.convertToByte(key5)) + ((int) BitOperations.convertToByte(key6)));
                byteBlue = (byte) (((int) byteBlue) + ((int) BitOperations.convertToByte(key6)) + ((int) BitOperations.convertToByte(key4)));
            } else if (inRange(y, 0.19, 0.22) || inRange(y, 0.43, 0.46) || inRange(y, 0.70, 0.74)) {
                byteRed = (byte) (~(byteRed ^ BitOperations.convertToByte(key4)));
                byteGreen = (byte) (~(byteGreen ^ BitOperations.convertToByte(key5)));
                byteBlue = (byte) (~(byteBlue ^ BitOperations.convertToByte(key6)));
            } else if (inRange(y, 0.22, 0.25) || inRange(y, 0.46, 0.49) || inRange(y, 0.74, 0.78)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key7));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key8));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key9));
            } else if (inRange(y, 0.25, 0.28) || inRange(y, 0.49, 0.52) || inRange(y, 0.78, 0.82)) {
                byteRed = (byte) (((int) byteRed) + ((int) BitOperations.convertToByte(key7)) + ((int) BitOperations.convertToByte(key8)));
                byteGreen = (byte) (((int) byteGreen) + ((int) BitOperations.convertToByte(key8)) + ((int) BitOperations.convertToByte(key9)));
                byteBlue = (byte) (((int) byteBlue) + ((int) BitOperations.convertToByte(key9)) + ((int) BitOperations.convertToByte(key7)));
            } else if (inRange(y, 0.28, 0.31) || inRange(y, 0.52, 0.55) || inRange(y, 0.82, 0.86)) {
                byteRed = (byte) (~(byteRed ^ BitOperations.convertToByte(key7)));
                byteGreen = (byte) (~(byteGreen ^ BitOperations.convertToByte(key8)));
                byteBlue = (byte) (~(byteBlue ^ BitOperations.convertToByte(key9)));
            } else {
                //do nothing
            }

            cypherText[i] = byteRed;
            cypherText[i + 1] = byteGreen;
            cypherText[i + 2] = byteBlue;
        }

        return cypherText;
    }

    /**
     * Decrypts the input byte array into a representation of an image in chunks of RGB values.
     *
     * @param cypherText encrypted image
     * @return decrypted image
     */
    public byte[] decrypt(byte[] cypherText) {
        if (cypherText.length % 3 != 0) {
            throw new RuntimeException("Expect R,G,B pattern for byte array here");
        }

        final byte[] plainText = new byte[cypherText.length];
        double y0 = applyMap(initialConditionY());
        for (int i = 0; i < cypherText.length; i += 3) {
            byte byteRed = cypherText[i];
            byte byteGreen = cypherText[i + 1];
            byte byteBlue = cypherText[i + 2];

            double y = applyMap(y0);
            y0 = y;
            if (inRange(y, 0.1, 0.13) || inRange(y, 0.34, 0.37) || inRange(y, 0.58, 0.62)) {
                byteRed = (byte) ~byteRed;
                byteGreen = (byte) ~byteGreen;
                byteBlue = (byte) ~byteBlue;
            } else if (inRange(y, 0.13, 0.16) || inRange(y, 0.37, 0.40) || inRange(y, 0.62, 0.66)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key4));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key5));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key6));
            } else if (inRange(y, 0.16, 0.19) || inRange(y, 0.40, 0.43) || inRange(y, 0.66, 0.70)) {
                byteRed = (byte) (((int) byteRed) + 256 - ((int) BitOperations.convertToByte(key4)) - ((int) BitOperations.convertToByte(key5)));
                byteGreen = (byte) (((int) byteGreen) + 256 - ((int) BitOperations.convertToByte(key5)) - ((int) BitOperations.convertToByte(key6)));
                byteBlue = (byte) (((int) byteBlue) + 256 - ((int) BitOperations.convertToByte(key6)) - ((int) BitOperations.convertToByte(key4)));
            } else if (inRange(y, 0.19, 0.22) || inRange(y, 0.43, 0.46) || inRange(y, 0.70, 0.74)) {
                byteRed = (byte) ((~byteRed) ^ BitOperations.convertToByte(key4));
                byteGreen = (byte) ((~byteGreen) ^ BitOperations.convertToByte(key5));
                byteBlue = (byte) ((~byteBlue) ^ BitOperations.convertToByte(key6));
            } else if (inRange(y, 0.22, 0.25) || inRange(y, 0.46, 0.49) || inRange(y, 0.74, 0.78)) {
                byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key7));
                byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key8));
                byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key9));
            } else if (inRange(y, 0.25, 0.28) || inRange(y, 0.49, 0.52) || inRange(y, 0.78, 0.82)) {
                byteRed = (byte) (((int) byteRed) + 256 - ((int) BitOperations.convertToByte(key7)) - ((int) BitOperations.convertToByte(key8)));
                byteGreen = (byte) (((int) byteGreen) + 256 - ((int) BitOperations.convertToByte(key8)) - ((int) BitOperations.convertToByte(key9)));
                byteBlue = (byte) (((int) byteBlue) + 256 - ((int) BitOperations.convertToByte(key9)) - ((int) BitOperations.convertToByte(key7)));
            } else if (inRange(y, 0.28, 0.31) || inRange(y, 0.52, 0.55) || inRange(y, 0.82, 0.86)) {
                byteRed = (byte) ((~byteRed) ^ BitOperations.convertToByte(key7));
                byteGreen = (byte) ((~byteGreen) ^ BitOperations.convertToByte(key8));
                byteBlue = (byte) ((~byteBlue) ^ BitOperations.convertToByte(key9));
            } else {
                //do nothing
            }

            plainText[i] = byteRed;
            plainText[i + 1] = byteGreen;
            plainText[i + 2] = byteBlue;
        }

        return plainText;
    }

    private static boolean inRange(double y, double inclusiveStart, double exclusiveEnd) {
        return y >= inclusiveStart && y < exclusiveEnd;
    }

    public double initialConditionY() {
        final int[] keyString = keyStringY();
        return MathOperations.nonNegativeMod(y01(keyString) - y02(keyString), 1);
    }

    private int[] keyStringY() {
        final int[] keyString = new int[key1.length *3];
        System.arraycopy(key1, 0, keyString, 0, key1.length);
        System.arraycopy(key2, 0, keyString, key1.length, key1.length);
        System.arraycopy(key3, 0, keyString, key1.length * 2, key1.length);
        return keyString;
    }

    private double y01(final int[] keyString) {
        return BitOperations.toBase10(keyString) / Math.pow(2, 24);
    }

    private double y02(final int[] keyString) {
        double y02 = 0;
        final int[] pValues = pValues(initialConditionX());
        for(int i = 0; i < keyString.length; i++) {
            y02 += keyString[pValues[i]] * Math.pow(2, i);
        }

        return y02 / Math.pow(2, 24);
    }

    private double initialConditionX() {
        return MathOperations.nonNegativeMod(x01() - x02(), 1);
    }

    private double x01() {
        final int[] keyString = new int[key1.length *3];
        System.arraycopy(key4, 0, keyString, 0, key1.length);
        System.arraycopy(key5, 0, keyString, key1.length, key1.length);
        System.arraycopy(key6, 0, keyString, key1.length * 2, key1.length);
        return x01(keyString);
    }

    private double x01(final int[] keyString) {
        double x01 = 0;
        for(int i = 0; i < keyString.length; i++) {
            x01 += keyString[i] * Math.pow(2, i);
        }

        return x01 / Math.pow(2, 24);
    }

    private double x02() {
        final int[] hexKey = getHexKey02();
        double x02 = 0;
        for(int i = 0; i < 6; i++) {
            x02 += hexKey[i] / 96.0;
        }

        return x02;
    }

    //need blocks 13-18
    private int[] getHexKey02() {
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

    private int[] pValues(double initialCondition) {
        final int[] pValues = new int[24];
        double prev = initialCondition;

        for(int i = 0; i < 24; i++) {
            final double mappedValue = applyMap(prev);
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

}
