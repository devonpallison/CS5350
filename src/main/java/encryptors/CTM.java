package src.main.java.encryptors;

import src.main.java.Encryptor;

/**
 * Implementation of a complex tent map image encryption
 */
public class CTM implements EncryptionAlgorithm {
    public static final float r = 3;//r is the control parameter in the range [0,4

    private static final CTM instance = new CTM();

    private CTM() {
    }

    public static CTM getInstance() {
        return instance;
    }

    public byte[] encrypt(final byte[] plainText) {
        final byte[] cypherText = new byte[plainText.length];

        // Performing an XOR operation on each value of
        // byte array due to which every value of Image
        // will change.
        for (int i = 0; i < plainText.length; i++) {
            final byte b = plainText[i];
            final int key = key(b);
            cypherText[i] = (byte) (b ^ key);
            i++;
        }

        return cypherText;
    }

    public byte[] decrypt(final byte[] cypherText) {
        return cypherText; //todo
    }

    private static int key(byte b) {
        int ret;
        if (b < 0.5) {
            ret = (int) (4 - 3 / 4 * r * b * (1 - b ^ 2) + r / 2 * b % 1);
        } else {
            ret = (int) (4 - 3 / 4 * r * b * (1 - b ^ 2) + r / 2 * (1 - b) % 1);
        }
        return ret;
    }
}