package src.main.java.test;

import src.main.java.Encryptor;
import src.main.java.encryptors.*;
import src.main.java.util.FileUtil;

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
        final byte[] plainText = FileUtil.imageToByteArray(inputFilePath);
        final byte[] cypherText = encryptionAlgorithm.encrypt(plainText);

        try {
            FileUtil.writeByteArrayToImage(cypherText, "test.png");
            TestUtils.fail();
        } catch(IllegalArgumentException e) {
            //do nothing- the above method should fail with a corrupted image
        }

        final byte[] plainText2 = encryptionAlgorithm.decrypt(cypherText);
        TestUtils.testEquals(plainText, plainText2);
    }
}
