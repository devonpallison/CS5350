package src.main.java.test;

import src.main.java.Encryptor;
import src.main.java.encryptors.*;
import src.main.java.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestEndToEnd {

    public static void main(String[] args) throws IOException {
        testAlteredSineLogisticBasedTentMap();
        testCubicTentMap();
        testLogisticMap();
        testTentMap();
    }

    public static void testAlteredSineLogisticBasedTentMap() throws IOException {
        testEncryptionAndDecryption(AlteredSineLogisticBasedTentMap.getInstance());
    }

    public static void testCubicTentMap() throws IOException {
        testEncryptionAndDecryption(CubicTentMap.getInstance());
    }

    public static void testLogisticMap() throws IOException {
        testEncryptionAndDecryption(LogisticMap.getInstance());
    }

    public static void testTentMap() throws IOException {
        testEncryptionAndDecryption(TentMap.getInstance());
    }

    public static void testEncryptionAndDecryption(final EncryptionAlgorithm encryptionAlgorithm) throws IOException {
        final String inputFile = "Lenna";
        final String inputFileType = ".png";
        final String inputFilePath = Encryptor.INPUT_IMAGE_FILE_PREFIX + inputFile + inputFileType;

        System.out.println("Reading image file " + inputFilePath);
        final BufferedImage plainText = ImageIO.read(new File(inputFilePath));
        final BufferedImage cypherText = encryptionAlgorithm.encrypt(plainText);

        final String encryptedPath = "encrypted" + encryptionAlgorithm.getDescription() + "." + Encryptor.OUTPUT_FORMAT;
        System.out.println("Writing encrypted image to file " + encryptedPath);
        ImageIO.write(cypherText, Encryptor.OUTPUT_FORMAT, new File(encryptedPath));


        final BufferedImage plainText2 = encryptionAlgorithm.decrypt(cypherText);
        TestUtils.testEquals(ImageUtils.getPixelsOfImage(plainText), ImageUtils.getPixelsOfImage(plainText2));

        final String decryptedPath = "decrypted" + encryptionAlgorithm.getDescription() + "." + Encryptor.OUTPUT_FORMAT;
        System.out.println("Writing decrypted image to file " + decryptedPath);
        ImageIO.write(plainText2, Encryptor.OUTPUT_FORMAT, new File(decryptedPath));
    }
}
