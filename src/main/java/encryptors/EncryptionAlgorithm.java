package src.main.java.encryptors;

import src.main.java.util.BitOperations;
import src.main.java.util.MathOperations;

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

    public abstract double applyMap(final double x);

    public abstract double initialConditionY();
}
