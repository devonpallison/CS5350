package src.main.java;

import src.main.java.encryptors.AlteredSineLogisticBasedTentMap;
import src.main.java.encryptors.CubicTentMap;
import src.main.java.encryptors.EncryptionAlgorithm;
import src.main.java.util.FileUtil;

import java.io.*;

public class Encryptor {
    public static final String INPUT_IMAGE_FILE_PREFIX = "testImages/";
    public static final String OUTPUT_IMAGE_FILE_PREFIX = "outputImages/";

    public enum Algorithm {
        ASLBTM,
        LM,
        CTM;

        public EncryptionAlgorithm getEncryptionAlgorithm() {
            switch (this) {
                case ASLBTM:
                    return AlteredSineLogisticBasedTentMap.getInstance();
                case CTM:
                    return CubicTentMap.getInstance();
                case LM:
                    return src.main.java.encryptors.LogisticMap.getInstance();
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Algorithm chosenAlg = Algorithm.CTM;
        final String inputFile = "Lenna";
        final String inputFileType = ".png";
        final String inputFilePath = INPUT_IMAGE_FILE_PREFIX + inputFile + inputFileType;
        final String encryptionOutputFilePath = OUTPUT_IMAGE_FILE_PREFIX + inputFile + "_Encrypted_" + chosenAlg + inputFileType;
        final String decryptionOutputFilePath = OUTPUT_IMAGE_FILE_PREFIX + inputFile + "_Decrypted_" + chosenAlg + inputFileType;

        System.out.println("Reading image file " + inputFilePath);
        final byte[] plainText = FileUtil.imageToByteArray(inputFilePath);

        final byte[] cypherText = encrypt(plainText, chosenAlg);

        System.out.println("Writing image to file " + encryptionOutputFilePath);
        FileUtil.writeByteArrayToImage(cypherText, encryptionOutputFilePath);

        final byte[] plainText2 = decrypt(cypherText, chosenAlg);

        System.out.println("Writing decrypted image to file " + decryptionOutputFilePath);
        FileUtil.writeByteArrayToImage(plainText2, decryptionOutputFilePath);
    }

    private static byte[] encrypt(final byte[] plainText, final Algorithm chosenAlg) {
        final EncryptionAlgorithm encryptionAlgorithm = chosenAlg.getEncryptionAlgorithm();

        System.out.println("Performing encryption using algorithm " + chosenAlg);
        final long millis = System.currentTimeMillis();

        final byte[] cypherText = encryptionAlgorithm.encrypt(plainText);

        final long timedMillis = System.currentTimeMillis() - millis;
        System.out.println("Encryption finished in " + timedMillis + "ms");

        return cypherText;
    }

    private static byte[] decrypt(final byte[] cypherText, final Algorithm chosenAlg) {
        final EncryptionAlgorithm encryptionAlgorithm = chosenAlg.getEncryptionAlgorithm();

        System.out.println("Performing decryption using algorithm " + chosenAlg);
        final long millis = System.currentTimeMillis();

        final byte[] plainText = encryptionAlgorithm.decrypt(cypherText);

        final long timedMillis = System.currentTimeMillis() - millis;
        System.out.println("Decryption finished in " + timedMillis + "ms");

        return plainText;
    }
}