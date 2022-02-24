package src.main.java.encryptors;

import java.io.*;
import java.util.Arrays;

/**
 * Encrypts plaintext using the AES cypher.
 *
 * Program input is of the form [inputFile, outputFile] e.g.
 * /Users/devonallison/Documents/CS5920/input.txt
 * /Users/devonallison/Documents/CS5920/output.txt
 *
 * The input file consists of 2 lines. The first line is the plaintext, the second is the key.
 * Both must consist of only 16 hexadecimal integers. No spaces.
 *
 * The result written to the output file.
 */
public class AES implements EncryptionAlgorithm {

    private static final boolean DEBUG = true;

    private static final int[] RCON = new int[] {1,2,4,8,16,32,64,128,27,54};
    private static final int[][] SBOX = new int[16][16];
    private static final int[][] MIX_COLUMNS = new int[4][4];
    private static final int ROUNDS = 10;
    static {
        SBOX[0] = new int[]{0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76};
        SBOX[1] = new int[]{0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0};
        SBOX[2] = new int[]{0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15};
        SBOX[3] = new int[]{0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75};
        SBOX[4] = new int[]{0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84};
        SBOX[5] = new int[]{0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF};
        SBOX[6] = new int[]{0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8};
        SBOX[7] = new int[]{0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2};
        SBOX[8] = new int[]{0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73};
        SBOX[9] = new int[]{0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB};
        SBOX[10] = new int[]{0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79};
        SBOX[11] = new int[]{0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08};
        SBOX[12] = new int[]{0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A};
        SBOX[13] = new int[]{0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E};
        SBOX[14] = new int[]{0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF};
        SBOX[15] = new int[]{0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16};

        MIX_COLUMNS[0] = new int[]{2,3,1,1};
        MIX_COLUMNS[1] = new int[]{1,2,3,1};
        MIX_COLUMNS[2] = new int[]{1,1,2,3};
        MIX_COLUMNS[3] = new int[]{3,1,1,2};
    }

    private static final AES instance = new AES();

    private AES() {
    }

    public static AES getInstance() {
        return instance;
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            throw new IllegalArgumentException("Input should be of the form [inputFile, outputFile]");
        }

        final String inputFile = args[0];
        final String outputFile = args[1];

        final int[][] inputs = parseInputFile(inputFile);
        final Word[] output = encrypt(inputs[0], inputs[1]);

        writeToFile(output, outputFile);
    }

    /**
     * Parses the input file for the functions and the operator.
     * @param filePath input file location
     * @return two functions and operator as an Object array
     * @throws IOException if the input file can not be read
     */
    private static int[][] parseInputFile(String filePath) throws IOException {
        final int[][] inputFileParts = new int[2][16];

        try(BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            final String plainText = br.readLine();
            final String key = br.readLine();
            inputFileParts[0] = parseStringToArray(plainText.trim());
            inputFileParts[1] = parseStringToArray(key.trim());
        }

        return inputFileParts;
    }

    /**
     * Parses the input plain text into the corresponding integer array.
     */
    private static int[] parseStringToArray(final String string) {
        if(string.length() != 32) {
            throw new IllegalStateException();
        }

        int[] intArray = new int[16];
        for(int i = 0; i < 16; i++) {
            intArray[i] = Integer.parseInt(string.substring(2 * i, (2 * i) + 2), 16);
        }

        return intArray;
    }

    //utility for easy copy/paste out of the book for testing
    private static void printMe(String x) {
        System.out.println("new int[]{0x" + x.replace(" ", ", 0x") + "};");
    }

    private static void printMe2(String x) {

        System.out.print("new int[]{");

        for(int i = 0; i < x.length(); i+=2) {
            System.out.print("0x" + x.substring(i, i+2) + ", ");
        }

        System.out.println("};");
    }


    /**
     * Encrypts the plaintext using the key.
     * @return cyphertext
     */
    public static Word[] encrypt(final int[] plainText, final int[] key) {
        final Word[] expandedKey = keyExpansion(key); //creates 44 words
        if(DEBUG) System.out.println(Arrays.toString(expandedKey));

        Word[] state = convertPlainTextToWordArray(plainText);
        Word[] roundKey = getRoundKey(expandedKey, 0);
        if(DEBUG) System.out.println("Start of Round " + 0 + " = " + Arrays.toString(state));
        if(DEBUG) System.out.println("RoundKey " + 0 + " = " + Arrays.toString(roundKey));

        state = addRoundKey(state, roundKey);

        for(int i = 1; i <= ROUNDS; i++) {
            if(DEBUG) System.out.println("Start of Round " + i + " = " + Arrays.toString(state));

            final Word[] subbedBytes = substituteBytes(state);
            if(DEBUG) System.out.println("After Sub Bytes " + Arrays.toString(subbedBytes));

            final Word[] shiftedRows = shiftRows(subbedBytes);
            if(DEBUG) System.out.println("After ShiftRows " + Arrays.toString(shiftedRows));

            final Word[] mixedColumns;
            if(i != ROUNDS) {
                mixedColumns = mixColumns(shiftedRows);
                if (DEBUG) System.out.println("After Mix Columns " + Arrays.toString(mixedColumns));
            } else {
                mixedColumns = shiftedRows;
            }

            roundKey = getRoundKey(expandedKey, i);
            if(DEBUG) System.out.println("RoundKey = " + Arrays.toString(roundKey));

            state = addRoundKey(mixedColumns, roundKey);
        }

        if(DEBUG) System.out.println("Final result " + Arrays.toString(state));
        return state;
    }

    /**
     * Gets the key for the given round.
     * @param expandedKey array of 44 words
     * @param round which AES round
     */
    private static Word[] getRoundKey(final Word[] expandedKey, final int round) {
        final Word[] roundKey = new Word[4];
        final int[][] convertedExpandedKey = convertWordArrayTo2DIntArray(expandedKey);

        for(int i = 0; i < 4; i++) {
            final int[] col = new int[4];

            for(int j = 0; j < 4; j++) {
                col[j] = convertedExpandedKey[(4 * round) + j][i];
            }

            roundKey[i] = new Word(col);
        }

        return roundKey;
    }

    /**
     * Converts plaintext int[] to corresponding Word[]
     */
    private static Word[] convertPlainTextToWordArray(int[] plainText) {
        if(plainText.length != 16) {
            throw new IllegalStateException();
        }

        final Word[] result = new Word[4];
        for(int i = 0; i < 4; i++) {
            final int[] col = new int[4];

            for(int j = 0; j < 4; j++) {
                col[j] = plainText[i + (4 * j)];
            }

            result[i] = new Word(col);
        }

        return result;
    }

    /**
     * addRoundKey subroutine. See design document for further details.
     */
    public static Word[] addRoundKey(final Word[] state, final Word[] key) {
        if(state.length != 4 || key.length != 4) {
            throw new IllegalStateException();
        }

        final Word[] result = new Word[4];
        for(int i = 0; i < 4; i++) {
            final Word stateWord = state[i];
            final Word keyWord = key[i];

            result[i] = xor(stateWord, keyWord);
        }

        return result;
    }

    /**
     * substituteBytes subroutine. See design document for further details.
     */
    public static Word[] substituteBytes(final Word[] words) {
        final Word[] result = new Word[words.length];

        for(int i = 0; i < words.length; i++) {
            result[i] = subWord(words[i]);
        }

        return result;
    }

    /**
     * shiftRows subroutine. See design document for further details.
     */
    public static Word[] shiftRows(final Word[] bytes) {
        final Word[] result = new Word[4];

        for(int i = 0; i < result.length; i++) {
            result[i] = shiftRow(bytes[i], i);
        }

        return result;
    }

    private static Word shiftRow(final Word byte1, final int leftShift) {
        if(leftShift == 0) {
            return byte1;
        }

        return shiftRow(new Word(byte1.bytes[1], byte1.bytes[2], byte1.bytes[3], byte1.bytes[0]), leftShift - 1);
    }

    /**
     * mixColumns subroutine. See design document for further details.
     */
    public static Word[] mixColumns(final Word[] bytes) {
        return mixColumns(convertWordArrayTo2DIntArray(bytes));
    }

    private static Word[] mixColumns(final int[][] bytes) {
        return convert2DIntArrayToWordArray(matrixMultiplication(MIX_COLUMNS, bytes));
    }

    private static int[][] matrixMultiplication(final int[][] left, final int[][] right) {
        final int[][] result = new int[4][4];

        for(int i = 0; i < result.length; i++) {
            final int[] leftVector = left[i];

            for(int j = 0; j < 4; j++) {
                final int[] rightVector = new int[4];
                for (int k = 0; k < 4; k++) {
                    rightVector[k] = right[k][j];
                }

                result[i][j] = multiply(leftVector, rightVector);
            }
        }

        return result;
    }

    private static int multiply(final int[] left, final int[] right) {
        if(left.length < 4 && right.length < 4) {
            throw new IllegalStateException();
        }

        int sum = 0;

        for(int i = 0; i < left.length; i++) {
            final GFCalculator.BitField leftByte = new GFCalculator.BitField(Integer.toBinaryString(left[i]));
            final GFCalculator.BitField rightByte = new GFCalculator.BitField(Integer.toBinaryString(right[i]));

            //noinspection SuspiciousNameCombination
            sum = Integer.valueOf(GFCalculator.addition(new GFCalculator.BitField(Integer.toBinaryString(sum)), GFCalculator.multiplication(leftByte, rightByte)).toString(), 2);
        }

        return sum;
    }

    private static Word[] convert2DIntArrayToWordArray(final int[][] bytes) {
        final Word[] result = new Word[4];

        for(int i = 0; i < bytes.length; i++) {
            result[i] = new Word(bytes[i]);
        }

        return result;
    }

    private static int[][] convertWordArrayTo2DIntArray(Word[] bytes) {
        final int[][] result = new int[bytes.length][4];

        for(int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i].bytes;
        }

        return result;
    }

    /**
     * keyExpansion subroutine. See design document for further details.
     */
    public static Word[] keyExpansion(final int[] key) {
        System.out.println(Arrays.toString(key));
        if(key.length != 16) {
            throw new IllegalArgumentException("Key must be length 16");
        }

        final Word[] words = new Word[44];

        for(int i = 0; i < 4; i++) {
            words[i] = new Word(key[(4 * i)], key[(4 * i) + 1], key[(4 * i) + 2], key[(4 * i) + 3]);
            if(DEBUG) System.out.println("words[" + (i) + "] = " + words[i]);
        }


        for(int i = 4; i < 44; i++) {
            Word temp = words[i - 1];
            if(DEBUG) System.out.println("Temp = " + temp);

            if(i % 4 == 0) {
                final Word rotWord = rotWord(temp);
                if(DEBUG) System.out.println("rotWord = " + rotWord);

                final Word subWord = subWord(rotWord);
                if(DEBUG) System.out.println("subWord = " + subWord);

                final Word rcon = rCon(i / 4);
                if(DEBUG) System.out.println("rcon = " + rcon);

                final Word xored = xor(subWord, rcon);
                if(DEBUG) System.out.println("xored = " + xored);

                temp = xored;
            }

            words[i] = xor(words[i - 4], temp);

            if(DEBUG) System.out.println("words[" + i + "] = " + words[i]);
        }

        return words;
    }

    /**
     * Performs bitwise xor on input words.
     */
    public static Word xor(final Word w1, final Word w2) {
        final int[] result = new int[4];

        for(int i = 0; i < w1.bytes.length; i++) {
            result[i] = w1.bytes[i] ^ w2.bytes[i];
        }

        return new Word(result);
    }


    /**
     * rotWord subroutine. See design document for further details.
     */
    public static Word rotWord(final Word word) {
        final int[] result = new int[4];

        final int temp = word.bytes[0];
        System.arraycopy(word.bytes, 1, result, 0, 3);
        result[3] = temp;

        return new Word(result);
    }

    /**
     * rCon subroutine. See design document for further details.
     */
    public static Word rCon(final int round) {
        return new Word(RCON[round - 1], 0, 0, 0);
    }

    public static Word subWord(final Word word) {
        final int[] bytes = new int[4];

        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = subWord(word.bytes[i]);
        }

        return new Word(bytes);
    }

    //left most 4 bytes x, right most y
    private static int subWord(final int byte1) {
        final int rightBytes = byte1 % 16;
        final int leftBytes = (byte1 - rightBytes) / 16;

        return SBOX[leftBytes][rightBytes];
    }

    @Override
    public byte[] encrypt(byte[] plainText) {
        return new byte[0]; //todo
    }

    @Override
    public byte[] decrypt(byte[] cypherText) {
        return new byte[0]; //todo
    }

    //chunk of 4 bytes
    private static class Word {
        private final int[] bytes;

        private Word(final int... bytes) {
            if(bytes.length != 4) {
                throw new IllegalArgumentException("Words are 4 bytes long");
            }

            this.bytes = bytes;
        }

        @Override
        public String toString() {
            final String[] hexStrings = new String[4];
            for(int i = 0; i < bytes.length; i++) {
                hexStrings[i] = Integer.toHexString(bytes[i]);
            }
            return Arrays.toString(hexStrings);
        }
    }

    /**
     * Writes text to file.
     *
     * @param cypherText cypherText
     * @param fileLocation output file pat
     * @throws IOException if the output file can not be written to
     */
    private static void writeToFile(final Word[] cypherText, final String fileLocation) throws IOException {
        File fout = new File(fileLocation);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        StringBuilder output = new StringBuilder();
        final int[][] niceFormatting = convertWordArrayTo2DIntArray(cypherText);
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                String hex = Integer.toHexString(niceFormatting[j][i]);
                hex = hex.length() == 1 ? "0" + hex : hex;
                output.append(hex);
            }
        }
        bw.write(output.toString());
        bw.close();
    }
}