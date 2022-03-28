package src.main.java.test;

import src.main.java.encryptors.LogisticMap;

import java.util.Arrays;

public class TestLogisticMap {

    public static void main(String[] args) {
        testLogisticMap();
    }

    public static void testLogisticMap() {
        final byte[] plainText = {10,10,10,10,10,10};
        final LogisticMap logisticMap = LogisticMap.getInstance();
        final byte[] cypherText = logisticMap.encrypt(plainText);
        System.out.println(Arrays.toString(cypherText));
        final byte[] plainText2 = logisticMap.decrypt(cypherText);
        System.out.println(Arrays.toString(plainText2));
        TestUtils.testEquals(plainText, plainText2);
    }
}
