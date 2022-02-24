package src.main.java.encryptors;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Capable of performing addition, subtraction, multiplication, and division in GF(2^8) with
 * irreducible polynomial 100011011.
 *
 * Program input is of the form [inputFile, outputFile] e.g.
 * /Users/devonallison/Documents/CS5920/input.txt
 * /Users/devonallison/Documents/CS5920/output.txt
 *
 * The input file consists of 3 lines. The first line is the first polynomial, and must consist only up to 8 integers either 0 or 1.
 * The second line is the operator. It must be one of [+,-,*,/]
 *
 * The result written to the output file.
 */
public class GFCalculator {

    private static final boolean DEBUG = false;
    private final static BitField AES = new BitField(new int[]{1,0,0,0,1,1,0,1,1});
    private final static BitField MOD_FIELD = new BitField(new int[]{0,0,0,1,1,0,1,1});

    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            throw new IllegalArgumentException("Input should be of the form [inputFile, outputFile]");
        }

        final String inputFile = args[0];
        final String outputFile = args[1];

        final Object[] inputs = parseInputFile(inputFile);
        final BitField output = processInputs((BitField) inputs[0], (Operator) inputs[1], (BitField) inputs[2]);

        writeToFile(output, outputFile);
    }

    /**
     * Determines which subroutine to run based on the given Operator.
     *
     * @param x leftside polynomial
     * @param operator which function to run
     * @param y right side polynomial
     *
     * @return result of operator(x,y)
     */
    private static BitField processInputs(final BitField x, final Operator operator, final BitField y) {
        switch(operator) {
            case ADDITION:
                return addition(x, y);
            case SUBTRACTION:
                return subtraction(x, y);
            case MULTIPLICATION:
                return multiplication(x, y);
            case DIVISION:
                return division(x, y);
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Performs x + y
     */
    public static BitField addition(final BitField x, final BitField y) {
        return x.xor(y);
    }

    /**
     * Performs x - y
     */
    public static BitField subtraction(final BitField x, final BitField y) {
        return addition(x,y);
    }

    /**
     * Performs x * y
     */
    public static BitField multiplication(final BitField x, final BitField y) {
        final List<BitField> partialProducts = new ArrayList<>();



        for(int i = 0; i < y.getBits().length; i++) {
            final int bit = y.getBits()[i];
            if(bit == 1) {
                final BitField partialProduct = multiplicationAux(x, 7 - i);
                if(DEBUG) System.out.println("Power " + (7 - i) + " gives product " + partialProduct);
                partialProducts.add(partialProduct);
            }
        }

        if(partialProducts.size() == 0) {
            return new BitField("00000000");
        }

        BitField sum = partialProducts.get(0);
        for(int i = 1; i < partialProducts.size(); i++) {
            sum = addition(sum, partialProducts.get(i));
        }

        return sum;
    }

    /**
     * Performs x / y, or x * (y^-1)
     */
    public static BitField division(final BitField x, final BitField y) {
        return multiplication(x, multiplicativeInverse(y));
    }

    private static BitField multiplicationAux(final BitField x, final int power) {
        if(power > 7 || power < 0) {
            throw new IllegalStateException();
        }

        BitField postMod = x;
        for(int i = 0; i < power; i++) {
            final int[] preChop = new int[postMod.getBits().length + 1];
            System.arraycopy(postMod.getBits(), 0, preChop, 0, postMod.getBits().length);

            final int[] postChop = new int[postMod.getBits().length];
            System.arraycopy(preChop, preChop.length - 8, postChop, 0, postChop.length);
            final BitField postChopField = new BitField(postChop);

            //if b7==1, xor by 00011011
            if (preChop[preChop.length - 9] == 1) {
                postMod = addition(postChopField, MOD_FIELD);
            } else {
                postMod = postChopField;
            }
        }

        return postMod;
    }

    /**
     * Performs brute force search for the multiplicative inverse of field.
     */
    private static BitField multiplicativeInverse(final BitField field) {
        final BitField one = new BitField("00000001");

        for(int i = 0; i < 256; i++) {
            final BitField possibleInverse = getBitField(i);
            if(multiplication(field, possibleInverse).equals(one)) {
                return possibleInverse;
            }
        }

        throw new RuntimeException("Could not perform division; no multiplicative inverse found"); //should not happen
    }

    private static BitField getBitField(final int i) {
        return new BitField(Integer.toBinaryString(i));
    }

    public enum Operator {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION
    }

    public static Operator determineOperator(String operator) {
        switch(operator) {
            case "+":
                return Operator.ADDITION;
            case "-":
                return Operator.SUBTRACTION;
            case "x":
                return Operator.MULTIPLICATION;
            case "/":
                return Operator.DIVISION;
            default:
                throw new IllegalStateException();
        }
    }


    public static class BitField {
        private final int[] bits;

        public BitField(String bitString) {
            final int stringLength = bitString.length();
            final int[] bits = new int[8];

            if(stringLength > bits.length) {
                throw new IllegalArgumentException("Bit string can not be longer than length 8");
            }

            for(int i = 0; i < bits.length - stringLength; i++) {
                bits[i] = 0;
            }

            for(int i = 0; i < bitString.length(); i++) {
                bits[i + (bits.length - stringLength)] = Integer.parseInt(bitString.substring(i, i + 1));
            }

            this.bits = bits;
        }

        //input here can be length 8 for AES string
        public BitField(int[] bits) {
            this.bits = bits;
        }

        public int[] getBits() {
            return bits;
        }

        public BitField xor(final BitField other) {
            final int[] result = new int[bits.length];

            for(int i = 0; i < bits.length; i++) {
                result[i] = (bits[i] + other.bits[i]) % 2;
            }

            return new BitField(result);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            for(final int i : bits) {
                result.append(i);
            }

            return result.toString();
        }

        @Override
        public boolean equals(final Object other) {
            if(other == null || other.getClass() != this.getClass()) {
                return false;
            }

            return Arrays.equals(bits, ((BitField) other).bits);
        }
    }

    /**
     * Parses the input file for the functions and the operator.
     * @param filePath input file location
     * @return two functions and operator as an Object array
     * @throws IOException if the input file can not be read
     */
    private static Object[] parseInputFile(String filePath) throws IOException {
        final Object[] inputFileParts = new Object[3];

        try(BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            final String function1 = br.readLine();
            final String operator = br.readLine();
            final String function2 = br.readLine();
            inputFileParts[0] = new BitField(function1);
            inputFileParts[1] = determineOperator(operator);
            inputFileParts[2] = new BitField(function2);
        }

        return inputFileParts;
    }

    /**
     * Writes text to file.
     *
     * @param bits bits
     * @param fileLocation output file pat
     * @throws IOException if the output file can not be written to
     */
    private static void writeToFile(BitField bits, final String fileLocation) throws IOException {
        File fout = new File(fileLocation);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(bits.toString());
        bw.close();
    }
}