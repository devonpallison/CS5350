package src.main.java.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.BitSet;

public class BitOperations {
    public static int toBase10(final int[] bitString) {
        int base10 = 0;
        for(int i = 0; i < bitString.length; i++) {
            base10 += bitString[i] * Math.pow(2, bitString.length - i - 1);
        }

        return base10;
    }

    public static int[] toBitString(final byte b) {
        int[] bitString = new int[8];
        String rep = Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
        for(int i = 0; i < rep.length(); i++) {
            if(rep.charAt(i) == '0') {
                bitString[i] = 0;
            } else {
                bitString[i] = 1;
            }
        }

        return bitString;
    }

    public static byte invert(final byte b) {
        return convertToByte(invert(toBitString(b)));
    }

    public static int[] invert(final int[] bitString) {
        final int[] ret = new int[bitString.length];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = bitString[i] == 0 ? 1 : 0;
        }

        return ret;
    }

    //todo I think we need to not use java bytes
    public static byte convertToByte(final int[] bitString) {
        if(bitString.length != 8) {
            throw new IllegalArgumentException();
        }

        int base32 = toBase10(bitString);
        System.out.println(base32);
        return (byte) base32;
    }

    public static void main(String[] args) {
        byte b = 10;
        System.out.println(convertToByte(new int[]{1,0,0,0,0,0,1,0}));
    }
}
