package src.main.java.encryptors;

import src.main.java.Encryptor;

public class ASLBTM implements EncryptionAlgorithm {

    private static final ASLBTM instance = new ASLBTM();

    private ASLBTM() {
    }

    public static ASLBTM getInstance() {
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
            ret = (int) (4 - Encryptor.r / 4 * Math.sin(3.14159 * b) + Encryptor.r / 2 * b);
        } else {
            ret = (int) ((4 - Encryptor.r) * b * (1 - b) + Encryptor.r / 2 * (1 - b));
        }
        return ret;
    }
}
