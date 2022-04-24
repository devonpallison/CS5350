package src.main.java.test;

import src.main.java.encryptors.LogisticMap;

import java.util.Arrays;
import java.util.Random;

public class TestEncryptionAlgorithm {
    public static void main(String[] args) {
        testSpecificEncryptDecrypt();
    }

    private static void testEncryptDecrypt() {
        final Random randy = new Random();
        int x = 100000;
        while(x -- > 0) {
            int b1 = randy.nextInt(256);
            int b2 = randy.nextInt(256);
            int b3 = randy.nextInt(256);
            final byte[] plainText = new byte[]{(byte) b1, (byte) b2, (byte) b3};
            final LogisticMap logisticMap = LogisticMap.getInstance();
            final byte[] cypherText = logisticMap.encrypt(plainText);
            final byte[] plainText2 = logisticMap.decrypt(cypherText);
            try {
                TestUtils.testEquals(plainText, plainText2);
            } catch (Exception e) {
                System.out.println("Input: " + Arrays.toString(plainText));
                throw e;
            }
        }
    }

    private static void testSpecificEncryptDecrypt() {
        final byte[] plainText = new byte[]{(byte) -73, (byte) 105, (byte) 127};
        final LogisticMap logisticMap = LogisticMap.getInstance();
        final byte[] cypherText = logisticMap.encrypt(plainText);
        final byte[] plainText2 = logisticMap.decrypt(cypherText);
        TestUtils.testEquals(plainText, plainText2);
    }
}
