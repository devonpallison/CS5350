package src.main.java.analysis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
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
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistogramAnalysis {

    public static void main(String[] args) throws IOException {
        final String inputFile = "Lenna";
        final String inputFileType = ".png";
        final String inputFilePath = Encryptor.INPUT_IMAGE_FILE_PREFIX + inputFile + inputFileType;

        System.out.println("Reading image file " + inputFilePath);
        final BufferedImage plainText = ImageIO.read(new File(inputFilePath));
        final byte[] imageBytes1 = ImageUtils.getPixelsOfImage(plainText);



        final java.util.List<double[]> RGBValuesUnencrypted = AnalysisTools.getPixelsAsDoubles(imageBytes1);
//        saveRGBAsHistogram("Unencrypted", RGBValuesUnencrypted);
        printMeansAndStandardDeviations(new CsvWriter(), "Unencrypted", RGBValuesUnencrypted);

        final byte[] encryptedImageASLBTM = AlteredSineLogisticBasedTentMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesASLBTM = AnalysisTools.getPixelsAsDoubles(encryptedImageASLBTM);
//        saveRGBAsHistogram("ASLBTM", RGBValuesASLBTM);
       printMeansAndStandardDeviations(new CsvWriter(), "ASLBTM",RGBValuesASLBTM);

        final byte[] encryptedImageCTM = CubicTentMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesCTM = AnalysisTools.getPixelsAsDoubles(encryptedImageCTM);
//        saveRGBAsHistogram("CTM", RGBValuesCTM);
       printMeansAndStandardDeviations(new CsvWriter(), "CTM",RGBValuesCTM);

        final byte[] encryptedImageLM = LogisticMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesLM = AnalysisTools.getPixelsAsDoubles(encryptedImageLM);
//        saveRGBAsHistogram("LM", RGBValuesLM);
       printMeansAndStandardDeviations(new CsvWriter(), "LM",RGBValuesLM);

        final byte[] encryptedImageTM = TentMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesTM = AnalysisTools.getPixelsAsDoubles(encryptedImageTM);
//        saveRGBAsHistogram("TM", RGBValuesTM);
       printMeansAndStandardDeviations(new CsvWriter(), "TM",RGBValuesTM);
    }

    private static void printMeansAndStandardDeviations(CsvWriter csvWriter, final String map, List<double[]> rgbValuesTM) {
        List<Double> red = printMeansAndStandardDeviations(rgbValuesTM.get(0));
        List<Double> green = printMeansAndStandardDeviations(rgbValuesTM.get(1));
        List<Double> blue = printMeansAndStandardDeviations(rgbValuesTM.get(2));

        csvWriter.addWords("Color", "std");
        csvWriter.addWords("Red", red.get(1).toString());
        csvWriter.addWords("Green", green.get(1).toString());
        csvWriter.addWords("Blue", blue.get(1).toString());
        csvWriter.finish(map + "_hist.csv");
    }


    private static List<Double> printMeansAndStandardDeviations(double[] values) {
        final int[] frequencies = new int[256];
        for(double value : values) {
            frequencies[(int) value] = frequencies[(int) value] + 1;
        }

        return printMeansAndStandardDeviations(frequencies);
    }

    private static List<Double> printMeansAndStandardDeviations(int[] frequencies) {
        final int sum = Arrays.stream(frequencies).sum();
        final double mean = ((double) sum) / frequencies.length;
        double std = 0.0;
        for(final int frequency : frequencies) {
            std += Math.pow(frequency - mean, 2);
        }
        std = Math.sqrt(std / frequencies.length);
       return Arrays.asList(mean, std);
    }

    private static void saveRGBAsHistogram(String title, List<double[]> rgbValues) {
        saveRGBAsHistogram(title, "Red", rgbValues.get(0));
        saveRGBAsHistogram(title, "Green", rgbValues.get(1));
        saveRGBAsHistogram(title, "Blue", rgbValues.get(2));
    }

    private static void saveRGBAsHistogram(String title, String color, double[] rgbValues) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries(color + " Values", rgbValues, 256);

        JFreeChart histogram = ChartFactory.createHistogram(title + " " + color + " Values for Lenna.png",
                "RGB Value (0-256)", "Frequency", dataset, PlotOrientation.VERTICAL, false, false, false);


        XYBarRenderer r = (XYBarRenderer)histogram.getXYPlot().getRenderer();
        r.setSeriesPaint(0, Color.black);
        try {
            ChartUtilities.saveChartAsPNG(new File("outputImages/Histogram" + title + color + ".png"), histogram, 400,300);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        };
    }


}
