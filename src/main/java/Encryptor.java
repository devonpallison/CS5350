package src.main.java;

import src.main.java.encryptors.*;
import src.main.java.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Encryptor {
    public static final String INPUT_IMAGE_FILE_PREFIX = "testImages/";
    public static final String OUTPUT_IMAGE_FILE_PREFIX = "outputImages/";
    public static final String OUTPUT_FORMAT = "png";

    public enum Algorithm {
        ASLBTM,
        LM,
        CTM,
        TM;

        public EncryptionAlgorithm getEncryptionAlgorithm() {
            switch (this) {
                case ASLBTM:
                    return AlteredSineLogisticBasedTentMap.getInstance();
                case CTM:
                    return CubicTentMap.getInstance();
                case LM:
                    return LogisticMap.getInstance();
                case TM:
                    return TentMap.getInstance();
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
        final BufferedImage plainText = ImageIO.read(new File(inputFilePath));

        final BufferedImage cypherText = encrypt(plainText, chosenAlg);

        try {
            System.out.println("Writing image to file " + encryptionOutputFilePath);
            ImageIO.write(cypherText, OUTPUT_FORMAT, new File(encryptionOutputFilePath));
        } catch(Exception e) {
            System.out.println("Could not write image to file");
            System.out.println("Exception: " + e.getMessage());
        }

        final BufferedImage plainText2 = decrypt(cypherText, chosenAlg);

        System.out.println("Writing decrypted image to file " + decryptionOutputFilePath);
        ImageIO.write(plainText2, OUTPUT_FORMAT, new File(decryptionOutputFilePath));
    }

    private static BufferedImage encrypt(final BufferedImage plainText, final Algorithm chosenAlg) {
        final EncryptionAlgorithm encryptionAlgorithm = chosenAlg.getEncryptionAlgorithm();

        System.out.println("Performing encryption using algorithm " + chosenAlg);
        final long millis = System.currentTimeMillis();

        final BufferedImage cypherText = encryptionAlgorithm.encrypt(plainText);

        final long timedMillis = System.currentTimeMillis() - millis;
        System.out.println("Encryption finished in " + timedMillis + "ms");

        return cypherText;
    }

    private static BufferedImage decrypt(final BufferedImage cypherText, final Algorithm chosenAlg) {
        final EncryptionAlgorithm encryptionAlgorithm = chosenAlg.getEncryptionAlgorithm();

        System.out.println("Performing decryption using algorithm " + chosenAlg);
        final long millis = System.currentTimeMillis();

        final BufferedImage plainText = encryptionAlgorithm.decrypt(cypherText);

        final long timedMillis = System.currentTimeMillis() - millis;
        System.out.println("Decryption finished in " + timedMillis + "ms");

        return plainText;
    }
}