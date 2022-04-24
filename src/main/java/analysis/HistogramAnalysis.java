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
import java.util.List;

public class HistogramAnalysis {

    public static void main(String[] args) throws IOException {
        final String inputFile = "Lenna";
        final String inputFileType = ".png";
        final String inputFilePath = Encryptor.INPUT_IMAGE_FILE_PREFIX + inputFile + inputFileType;

        System.out.println("Reading image file " + inputFilePath);
        final BufferedImage plainText = ImageIO.read(new File(inputFilePath));
        final byte[] imageBytes1 = ImageUtils.getPixelsOfImage(plainText);



        final java.util.List<double[]> RGBValuesUnencrypted = AnalysisTools.getPixelsAsDoubles(imageBytes1);
        saveRGBAsHistogram("Unencrypted", RGBValuesUnencrypted);

        final byte[] encryptedImageASLBTM = AlteredSineLogisticBasedTentMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesASLBTM = AnalysisTools.getPixelsAsDoubles(encryptedImageASLBTM);
        saveRGBAsHistogram("ASLBTM", RGBValuesASLBTM);

        final byte[] encryptedImageCTM = CubicTentMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesCTM = AnalysisTools.getPixelsAsDoubles(encryptedImageCTM);
        saveRGBAsHistogram("CTM", RGBValuesCTM);

        final byte[] encryptedImageLM = LogisticMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesLM = AnalysisTools.getPixelsAsDoubles(encryptedImageLM);
        saveRGBAsHistogram("LM", RGBValuesLM);

        final byte[] encryptedImageTM = TentMap.getInstance().encrypt(imageBytes1);
        final java.util.List<double[]> RGBValuesTM = AnalysisTools.getPixelsAsDoubles(encryptedImageTM);
        saveRGBAsHistogram("TM", RGBValuesTM);
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
