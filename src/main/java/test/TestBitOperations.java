package src.main.java.test;

import src.main.java.util.BitOperations;

public class TestBitOperations {

    public static void main(String[] args) {
        testToBinaryString();
    }

    private static void testToBinaryString() {
        //integer conversion
        TestUtils.testEquals(new int[]{0,0,0,0,1,1,1,1}, BitOperations.toBitString(15));
        TestUtils.testEquals(new int[]{0,0,0,0,1,1,0,0}, BitOperations.toBitString(12));
        TestUtils.testEquals(new int[]{0,1,1,1,1,0,1,1}, BitOperations.toBitString(123));

        //byte conversion
        TestUtils.testEquals(new int[]{0,0,0,0,1,1,1,1}, BitOperations.toBitString((byte) 15));
        TestUtils.testEquals(new int[]{0,0,0,0,1,1,0,0}, BitOperations.toBitString((byte) 12));
        TestUtils.testEquals(new int[]{0,1,1,1,1,0,1,1}, BitOperations.toBitString((byte) 123));
    }
}
