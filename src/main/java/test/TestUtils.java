package src.main.java.test;

import java.util.Arrays;

public class TestUtils {

    public static void testEquals(final byte[] b1, final byte[] b2) {
        if(b1 == null) {
            if(b2 != null) {
                throw new RuntimeException("b1!=b2, b1=" + b1 + " b2=" + b2);
            }
            return;
        }

        if(b2 == null) {
            throw new RuntimeException("b1!=b2, b1=" + b1 + " b2=" + b2);
        }

        if(b1.length != b2.length) {
            throw new RuntimeException("b1!=b2, b1=" + Arrays.toString(b1) + " b2=" + Arrays.toString(b2));
        }

        for(int i = 0; i < b1.length; i++) {
            if(b1[i] != b2[i]) {
                throw new RuntimeException("b1!=b2, b1=" + Arrays.toString(b1) + " b2=" + Arrays.toString(b2));
            }
        }
    }

    public static void testEquals(final int[] b1, final int[] b2) {
        if(b1 == null) {
            if(b2 != null) {
                throw new RuntimeException("b1!=b2, b1=" + b1 + " b2=" + b2);
            }
            return;
        }

        if(b2 == null) {
            throw new RuntimeException("b1!=b2, b1=" + b1 + " b2=" + b2);
        }

        if(b1.length != b2.length) {
            throw new RuntimeException("b1!=b2, b1=" + Arrays.toString(b1) + " b2=" + Arrays.toString(b2));
        }

        for(int i = 0; i < b1.length; i++) {
            if(b1[i] != b2[i]) {
                throw new RuntimeException("b1!=b2, b1=" + Arrays.toString(b1) + " b2=" + Arrays.toString(b2));
            }
        }
    }

    public static void fail() {
        throw new RuntimeException("Test failed");
    }
}
