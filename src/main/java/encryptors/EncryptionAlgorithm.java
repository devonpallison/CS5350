package src.main.java.encryptors;

import src.main.java.util.BitOperations;
import src.main.java.util.ImageUtils;
import src.main.java.util.MathOperations;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

/*
Encryption algorithm taken from
 https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.88.5203&rep=rep1&type=pdf
 */
public abstract class EncryptionAlgorithm {
    //modify the key string every 16 pixels
    private static final int MODIFY_KEYS_COUNTER = 16;

    private static final boolean DEBUG = false;

    public static boolean ALTER_KEY = false;

    //initial key is 80 bits
    protected static final int[] INITIAL_KEY1 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY1_ALTERED = {1, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY2 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY3 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY4 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY5 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY6 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY7 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY8 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY9 = {0, 1, 1, 0, 1, 1, 0, 1};
    protected static final int[] INITIAL_KEY10 = {0, 1, 1, 0, 1, 1, 0, 1};

    //keys are modified as we progress through the encryption
    protected static int[] key1 = ALTER_KEY ? INITIAL_KEY1_ALTERED : INITIAL_KEY1;
    protected static int[] key2 = INITIAL_KEY2;
    protected static int[] key3 = INITIAL_KEY3;
    protected static int[] key4 = INITIAL_KEY4;
    protected static int[] key5 = INITIAL_KEY5;
    protected static int[] key6 = INITIAL_KEY6;
    protected static int[] key7 = INITIAL_KEY7;
    protected static int[] key8 = INITIAL_KEY8;
    protected static int[] key9 = INITIAL_KEY9;
    protected static int[] key10 = INITIAL_KEY10;

    /**
     * Apply the chaotic transformation.
     *
     * @param x input
     */
    public abstract double applyMap(final double x);

    /**
     * Return the description of this algorithm.
     */
    public abstract String getDescription();

    /**
     * Encrypts the entry byte array representation of an image in chunks of RGB values.
     *
     * @param plainText byte array representation of an image. Assume to be in order
     *                  R1,G1,B1,G2,G2,B2,...,RnGnBn
     * @return encrypted image
     */
    public BufferedImage encrypt(BufferedImage plainText) {
        final byte[] pixels = ImageUtils.getPixelsOfImage(plainText);
        final byte[] encryptedPixels = encrypt(pixels);
        return ImageUtils.changePixelsOfImage(plainText, encryptedPixels);
    }

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
        System.out.println("Encrypting image with " + plainText.length + " pixels.");
        long oldTime = System.currentTimeMillis();

        resetKeys();
        final byte[] cypherText = new byte[plainText.length];
        double y0 = applyMap(initialConditionY());
        int modifyKeysCounter = MODIFY_KEYS_COUNTER;
        for (int i = 0; i < plainText.length; i += 3) {
            byte byteRed = plainText[i];
            byte byteGreen = plainText[i + 1];
            byte byteBlue = plainText[i + 2];
            debug("Encrypting red=" + byteRed + ", green=" + byteGreen + ", blue=" + byteBlue);

            for(int j = 0; j < BitOperations.toBase10(key10); j++) {
                double y = applyMap(y0);
                y0 = y;
                debug("Decrypting with y=" + y);
                if (inRange(y, 0.1, 0.13) || inRange(y, 0.34, 0.37) || inRange(y, 0.58, 0.62)) {
                    byteRed = (byte) ~byteRed;
                    byteGreen = (byte) ~byteGreen;
                    byteBlue = (byte) ~byteBlue;
                } else if (inRange(y, 0.13, 0.16) || inRange(y, 0.37, 0.40) || inRange(y, 0.62, 0.66)) {
                    byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key4));
                    byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key5));
                    byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key6));
                } else if (inRange(y, 0.16, 0.19) || inRange(y, 0.40, 0.43) || inRange(y, 0.66, 0.70)) {
                    byteRed = (byte) (BitOperations.toBase10(BitOperations.toBitString(byteRed)) + ((int) BitOperations.convertToByte(key4)) + ((int) BitOperations.convertToByte(key5)));
                    byteGreen = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteGreen))) + ((int) BitOperations.convertToByte(key5)) + ((int) BitOperations.convertToByte(key6)));
                    byteBlue = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteBlue))) + ((int) BitOperations.convertToByte(key6)) + ((int) BitOperations.convertToByte(key4)));
                } else if (inRange(y, 0.19, 0.22) || inRange(y, 0.43, 0.46) || inRange(y, 0.70, 0.74)) {
                    byteRed = (byte) (~(byteRed ^ BitOperations.convertToByte(key4)));
                    byteGreen = (byte) (~(byteGreen ^ BitOperations.convertToByte(key5)));
                    byteBlue = (byte) (~(byteBlue ^ BitOperations.convertToByte(key6)));
                } else if (inRange(y, 0.22, 0.25) || inRange(y, 0.46, 0.49) || inRange(y, 0.74, 0.78)) {
                    byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key7));
                    byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key8));
                    byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key9));
                } else if (inRange(y, 0.25, 0.28) || inRange(y, 0.49, 0.52) || inRange(y, 0.78, 0.82)) {
                    byteRed = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteRed))) + ((int) BitOperations.convertToByte(key7)) + ((int) BitOperations.convertToByte(key8)));
                    byteGreen = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteGreen))) + ((int) BitOperations.convertToByte(key8)) + ((int) BitOperations.convertToByte(key9)));
                    byteBlue = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteBlue))) + ((int) BitOperations.convertToByte(key9)) + ((int) BitOperations.convertToByte(key7)));
                } else if (inRange(y, 0.28, 0.31) || inRange(y, 0.52, 0.55) || inRange(y, 0.82, 0.86)) {
                    byteRed = (byte) (~(byteRed ^ BitOperations.convertToByte(key7)));
                    byteGreen = (byte) (~(byteGreen ^ BitOperations.convertToByte(key8)));
                    byteBlue = (byte) (~(byteBlue ^ BitOperations.convertToByte(key9)));
                } else {
                    //do nothing
                }
            }

            cypherText[i] = byteRed;
            cypherText[i + 1] = byteGreen;
            cypherText[i + 2] = byteBlue;

            modifyKeysCounter--;
            if(modifyKeysCounter == 0) {
                modifyKeys();
                modifyKeysCounter = MODIFY_KEYS_COUNTER;
            }
        }
        System.out.println("Finished encryption in " + (System.currentTimeMillis() - oldTime));

        return cypherText;
    }

    /**
     * Decrypts the input byte array into a representation of an image in chunks of RGB values.
     *
     * @param cypherText encrypted image
     * @return decrypted image
     */
    public BufferedImage decrypt(BufferedImage cypherText) {
        final byte[] pixels = ImageUtils.getPixelsOfImage(cypherText);
        final byte[] decryptedPixels = decrypt(pixels);
        return ImageUtils.changePixelsOfImage(cypherText, decryptedPixels);
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
        System.out.println("Decrypting image with " + cypherText.length + " pixels.");
        long oldTime = System.currentTimeMillis();

        resetKeys();
        final byte[] plainText = new byte[cypherText.length];
        double y0 = applyMap(initialConditionY());
        int modifyKeysCounter = MODIFY_KEYS_COUNTER;
        for (int i = 0; i < cypherText.length; i += 3) {
            byte byteRed = cypherText[i];
            byte byteGreen = cypherText[i + 1];
            byte byteBlue = cypherText[i + 2];
            debug("Decrypting red=" + byteRed + ", green=" + byteGreen + ", blue=" + byteBlue);

            final int base10Key10 = BitOperations.toBase10(key10);
            double[] ys = new double[base10Key10];
            double y = y0;
            for(int j = 0; j < base10Key10; j++) {
                y = applyMap(y);
                ys[j] = y;
            }
            y0 = y;

            for(int j = 0; j < base10Key10; j++) {
                y = ys[ys.length - j - 1];
                debug("Decrypting with y=" + y);
                if (inRange(y, 0.1, 0.13) || inRange(y, 0.34, 0.37) || inRange(y, 0.58, 0.62)) {
                    byteRed = (byte) ~byteRed;
                    byteGreen = (byte) ~byteGreen;
                    byteBlue = (byte) ~byteBlue;
                } else if (inRange(y, 0.13, 0.16) || inRange(y, 0.37, 0.40) || inRange(y, 0.62, 0.66)) {
                    byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key4));
                    byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key5));
                    byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key6));
                } else if (inRange(y, 0.16, 0.19) || inRange(y, 0.40, 0.43) || inRange(y, 0.66, 0.70)) {
                    byteRed = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteRed))) + 256 - ((int) BitOperations.convertToByte(key4)) - ((int) BitOperations.convertToByte(key5)));
                    byteGreen = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteGreen))) + 256 - ((int) BitOperations.convertToByte(key5)) - ((int) BitOperations.convertToByte(key6)));
                    byteBlue = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteBlue))) + 256 - ((int) BitOperations.convertToByte(key6)) - ((int) BitOperations.convertToByte(key4)));
                } else if (inRange(y, 0.19, 0.22) || inRange(y, 0.43, 0.46) || inRange(y, 0.70, 0.74)) {
                    byteRed = (byte) ((~byteRed) ^ BitOperations.convertToByte(key4));
                    byteGreen = (byte) ((~byteGreen) ^ BitOperations.convertToByte(key5));
                    byteBlue = (byte) ((~byteBlue) ^ BitOperations.convertToByte(key6));
                } else if (inRange(y, 0.22, 0.25) || inRange(y, 0.46, 0.49) || inRange(y, 0.74, 0.78)) {
                    byteRed = (byte) (byteRed ^ BitOperations.convertToByte(key7));
                    byteGreen = (byte) (byteGreen ^ BitOperations.convertToByte(key8));
                    byteBlue = (byte) (byteBlue ^ BitOperations.convertToByte(key9));
                } else if (inRange(y, 0.25, 0.28) || inRange(y, 0.49, 0.52) || inRange(y, 0.78, 0.82)) {
                    byteRed = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteRed))) + 256 - ((int) BitOperations.convertToByte(key7)) - ((int) BitOperations.convertToByte(key8)));
                    byteGreen = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteGreen))) + 256 - ((int) BitOperations.convertToByte(key8)) - ((int) BitOperations.convertToByte(key9)));
                    byteBlue = (byte) ((BitOperations.toBase10(BitOperations.toBitString(byteBlue))) + 256 - ((int) BitOperations.convertToByte(key9)) - ((int) BitOperations.convertToByte(key7)));
                } else if (inRange(y, 0.28, 0.31) || inRange(y, 0.52, 0.55) || inRange(y, 0.82, 0.86)) {
                    byteRed = (byte) ((~byteRed) ^ BitOperations.convertToByte(key7));
                    byteGreen = (byte) ((~byteGreen) ^ BitOperations.convertToByte(key8));
                    byteBlue = (byte) ((~byteBlue) ^ BitOperations.convertToByte(key9));
                } else {
                    //do nothing
                }
            }

            plainText[i] = byteRed;
            plainText[i + 1] = byteGreen;
            plainText[i + 2] = byteBlue;

            modifyKeysCounter--;
            if(modifyKeysCounter == 0) {
                modifyKeys();
                modifyKeysCounter = MODIFY_KEYS_COUNTER;
            }
        }
        System.out.println("Finished decryption in " + (System.currentTimeMillis() - oldTime));

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

    public void resetKeys() {
        key1 = ALTER_KEY ? INITIAL_KEY1_ALTERED : INITIAL_KEY1;
        key2 = INITIAL_KEY2;
        key3 = INITIAL_KEY3;
        key4 = INITIAL_KEY4;
        key5 = INITIAL_KEY5;
        key6 = INITIAL_KEY6;
        key7 = INITIAL_KEY7;
        key8 = INITIAL_KEY8;
        key9 = INITIAL_KEY9;
    }
    /*
    Every 16 pixels, we modify the keys with
    Ki = (Ki + K10) mod 256 for 1 <= i <= 9
     */
    private void modifyKeys() {
        key1 = BitOperations.toBitString((BitOperations.toBase10(key1) + BitOperations.toBase10(key10)) % 256);
        key2 = BitOperations.toBitString((BitOperations.toBase10(key2) + BitOperations.toBase10(key10)) % 256);
        key3 = BitOperations.toBitString((BitOperations.toBase10(key3) + BitOperations.toBase10(key10)) % 256);
        key4 = BitOperations.toBitString((BitOperations.toBase10(key4) + BitOperations.toBase10(key10)) % 256);
        key5 = BitOperations.toBitString((BitOperations.toBase10(key5) + BitOperations.toBase10(key10)) % 256);
        key6 = BitOperations.toBitString((BitOperations.toBase10(key6) + BitOperations.toBase10(key10)) % 256);
        key7 = BitOperations.toBitString((BitOperations.toBase10(key7) + BitOperations.toBase10(key10)) % 256);
        key8 = BitOperations.toBitString((BitOperations.toBase10(key8) + BitOperations.toBase10(key10)) % 256);
        key9 = BitOperations.toBitString((BitOperations.toBase10(key9) + BitOperations.toBase10(key10)) % 256);
    }

    private void debug(final String s) {
        if(DEBUG) {
            System.out.println(s);
        }
    }
}
