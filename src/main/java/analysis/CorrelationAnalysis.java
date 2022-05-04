package src.main.java.analysis;

import src.main.java.Encryptor;
import src.main.java.encryptors.AlteredSineLogisticBasedTentMap;
import src.main.java.encryptors.CubicTentMap;
import src.main.java.encryptors.LogisticMap;
import src.main.java.encryptors.TentMap;
import src.main.java.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class CorrelationAnalysis {
    private static int SAMPLE_SIZE = 100;

    public static void main(String[] args) throws IOException {
        final String myDirectoryPath = Encryptor.INPUT_IMAGE_FILE_PREFIX + "/mod3paintings/";
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();
        final CsvWriter csvWriter = new CsvWriter();
        csvWriter.addWords("File", "Encryption Method", "Horizontal Correlation", "VerticalCorrelation");

        if (directoryListing != null) {
            for (File child : directoryListing) {
                final String inputFilePath = child.getAbsolutePath();

                System.out.println("Reading image file " + inputFilePath);
                final BufferedImage plainText = ImageIO.read(new File(inputFilePath));

                int startPixelWidth = new Random().nextInt(plainText.getWidth() - SAMPLE_SIZE);
                int startPixelHeight = new Random().nextInt(plainText.getHeight() - SAMPLE_SIZE);
                int[][] intArray = new int[SAMPLE_SIZE][SAMPLE_SIZE];
                byte[] bytes = new byte[SAMPLE_SIZE*SAMPLE_SIZE*3];
                int k = 0;
                int xC = 0;
                for(int i = startPixelWidth; i < startPixelWidth + SAMPLE_SIZE; i++) {
                    int yC = 0;
                    for (int j = startPixelHeight; j < startPixelHeight + SAMPLE_SIZE; j++) {
                        intArray[xC][yC] = plainText.getRGB(i, j);
                        Color color = new Color(intArray[xC][yC]);
                        byte red = (byte) color.getRed();
                        byte green = (byte) color.getGreen();
                        byte blue = (byte) color.getBlue();
                        bytes[k] = red;
                        bytes[k+1] = green;
                        bytes[k+2] = blue;
                        k+=3;
                        yC++;
                    }

                    xC++;
                }
                final String fileName = child.getName();
                String method = "Unencrypted";
                double horizontalCorrelation = calculateCorrelation(method, intArray, fileName, false);
                double verticalCorrelation = calculateCorrelation(method, intArray, fileName, true);
                csvWriter.addWords(fileName, method, horizontalCorrelation + "", verticalCorrelation + "");

                method = "ASLBTM";
                final byte[] encryptedImageASLBTM = AlteredSineLogisticBasedTentMap.getInstance().encrypt(bytes);
                horizontalCorrelation = calculateCorrelation(method, encryptedImageASLBTM, fileName, false);
                verticalCorrelation = calculateCorrelation(method, encryptedImageASLBTM, fileName, true);
                csvWriter.addWords(fileName, method, horizontalCorrelation + "", verticalCorrelation + "");

                method = "CTM";
                final byte[] encryptedImageCTM = CubicTentMap.getInstance().encrypt(bytes);
                horizontalCorrelation = calculateCorrelation(method, encryptedImageCTM, fileName, false);
                verticalCorrelation = calculateCorrelation(method, encryptedImageCTM, fileName, true);
                csvWriter.addWords(fileName, method, horizontalCorrelation + "", verticalCorrelation + "");

                method = "LM";
                final byte[] encryptedImageLM = LogisticMap.getInstance().encrypt(bytes);
                horizontalCorrelation = calculateCorrelation(method, encryptedImageLM, fileName, false);
                verticalCorrelation = calculateCorrelation(method, encryptedImageLM, fileName, true);
                csvWriter.addWords(fileName, method, horizontalCorrelation + "", verticalCorrelation + "");

                method = "TM";
                final byte[] encryptedImageTM = TentMap.getInstance().encrypt(bytes);
                horizontalCorrelation = calculateCorrelation(method, encryptedImageTM, fileName, false);
                verticalCorrelation = calculateCorrelation(method, encryptedImageTM, fileName, true);
                csvWriter.addWords(fileName, method, horizontalCorrelation + "", verticalCorrelation + "");
            }
        }

        csvWriter.finish("output.csv");
    }

    private static double calculateCorrelation(String map, byte[] rgbValues, String file, boolean vertical) {
        return calculateCorrelation(map, AnalysisTools.getPixelsAsIntArrayArray(rgbValues, SAMPLE_SIZE, SAMPLE_SIZE), file, vertical);
    }

    private static double calculateCorrelation(String map, int[][] rgbValues, String file, boolean vertical) {
        double correlation = vertical ? correlationCoefficientV(rgbValues) : correlationCoefficientH(rgbValues);
        System.out.println(map + " correlation between " + (vertical ? "vertical" : "horizontal") + " pixels for file" + file + " = " + correlation);
        return correlation;
    }

    private static double correlationCoefficientV(final int[][] values) {
        final int N = values.length * (values.length/2);
        double cov1 = 0;
        double xSum = 0;
        double ySum = 0;
        double x2sum = 0;
        double y2sum = 0;

        for(int i = 0; i < values[0].length - 1; i+=2) {
            final int x1 = i;
            final int y1 = i+1;

            for (int j = 0; j < values.length; j++) {
                cov1 += (values[j][x1] * values[j][y1]);
                xSum += values[j][x1];
                ySum += values[j][y1];
                x2sum += values[j][x1] * values[j][x1];
                y2sum += values[j][y1] * values[j][y1];
            }
        }

        return ((N * (cov1)) - (xSum * ySum)) /
                Math.sqrt(((N * x2sum) - (xSum * xSum)) * ((N * y2sum) - (ySum * ySum)));
    }

    private static double correlationCoefficientH(final int[][] values) {
        final int N = values.length * (values.length/2);
        double cov1 = 0;
        double xSum = 0;
        double ySum = 0;
        double x2sum = 0;
        double y2sum = 0;

        for(int i = 0; i < values[0].length - 1; i+=2) {
            final int[] x = values[i];
            final int[] y = values[i+1];

            for (int j = 0; j < values[0].length; j++) {
                cov1 += (x[j] * y[j]);
                xSum += x[j];
                ySum += y[j];
                x2sum += x[j] * x[j];
                y2sum += y[j] * y[j];
            }
        }

        return ((N * (cov1)) - (xSum * ySum)) /
                Math.sqrt(((N * x2sum) - (xSum * xSum)) * ((N * y2sum) - (ySum * ySum)));
    }
}
