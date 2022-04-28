package src.main.java.analysis;

import src.main.java.Encryptor;
import src.main.java.encryptors.AlteredSineLogisticBasedTentMap;
import src.main.java.encryptors.CubicTentMap;
import src.main.java.encryptors.LogisticMap;
import src.main.java.encryptors.TentMap;
import src.main.java.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CorrelationAnalysis {

    public static void main(String[] args) throws IOException {
        final String myDirectoryPath = Encryptor.INPUT_IMAGE_FILE_PREFIX + "/mod3paintings/";
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                final String inputFilePath = child.getAbsolutePath();

                System.out.println("Reading image file " + inputFilePath);
                final BufferedImage plainText = ImageIO.read(new File(inputFilePath));
                final byte[] imageBytes1 = ImageUtils.getPixelsOfImage(plainText);

                final List<double[]> RGBValuesUnencrypted = AnalysisTools.getPixelsAsRGBDoublesH(imageBytes1, plainText.getWidth(), plainText.getHeight());
                final List<double[]> RGBValuesUnencryptedV = AnalysisTools.getPixelsAsRGBDoublesV(imageBytes1, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("Unencrypted", RGBValuesUnencrypted, child.getName(), false);
                calculateCorrelation("Unencrypted", RGBValuesUnencryptedV, child.getName(), true);

                final byte[] encryptedImageASLBTM = AlteredSineLogisticBasedTentMap.getInstance().encrypt(imageBytes1);
                final List<double[]> RGBValuesASLBTM = AnalysisTools.getPixelsAsRGBDoublesH(encryptedImageASLBTM, plainText.getWidth(), plainText.getHeight());
                final List<double[]> RGBValuesASLBTMV = AnalysisTools.getPixelsAsRGBDoublesH(encryptedImageASLBTM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("ASLBTM", RGBValuesASLBTM, child.getName(), false);
                calculateCorrelation("ASLBTM", RGBValuesASLBTMV, child.getName(), true);

                final byte[] encryptedImageCTM = CubicTentMap.getInstance().encrypt(imageBytes1);
                final List<double[]> RGBValuesCTM = AnalysisTools.getPixelsAsRGBDoublesH(encryptedImageCTM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("CTM", RGBValuesCTM, child.getName(), false);
                final List<double[]> RGBValuesCTMV = AnalysisTools.getPixelsAsRGBDoublesV(encryptedImageCTM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("CTM", RGBValuesCTMV, child.getName(), true);

                final byte[] encryptedImageLM = LogisticMap.getInstance().encrypt(imageBytes1);
                final List<double[]> RGBValuesLM = AnalysisTools.getPixelsAsRGBDoublesH(encryptedImageLM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("LM", RGBValuesLM, child.getName(), false);
                final List<double[]> RGBValuesLMV = AnalysisTools.getPixelsAsRGBDoublesV(encryptedImageLM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("LM", RGBValuesLMV, child.getName(), true);

                final byte[] encryptedImageTM = TentMap.getInstance().encrypt(imageBytes1);
                final List<double[]> RGBValuesTM = AnalysisTools.getPixelsAsRGBDoublesH(encryptedImageTM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("TM", RGBValuesTM, child.getName(), false);
                final List<double[]> RGBValuesTMV = AnalysisTools.getPixelsAsRGBDoublesV(encryptedImageTM, plainText.getWidth(), plainText.getHeight());
                calculateCorrelation("TM", RGBValuesTMV, child.getName(), true);
            }
        }
    }

    private static void calculateCorrelation(String map, List<double[]> rgbValues, String file, boolean vertical) {
        double correlation = correlationCoefficient(rgbValues.get(0), rgbValues.get(1));
        System.out.println(map + " correlation between " + (vertical ? "vertical" : "horizontal") + " pixels for file" + file + " = " + correlation);
    }

    private static double correlationCoefficient(final double[] x, final double[]y) {
        if(x.length != y.length) {
            throw new IllegalArgumentException();
        }
        final int N = x.length;

        double cov1 = 0;
        double xSum = 0;
        double ySum = 0;
        double x2sum = 0;
        double y2sum = 0;
        for(int i = 0; i < N; i++) {
            cov1 += (x[i] * y[i]);
            xSum += x[i];
            ySum += y[i];
            x2sum += x[i] * x[i];
            y2sum += y[i] * y[i];
        }

        return ((N * (cov1)) - (xSum * ySum)) /
                Math.sqrt(((N * x2sum) - (xSum * xSum)) * ((N * y2sum) - (ySum * ySum)));
    }
}
