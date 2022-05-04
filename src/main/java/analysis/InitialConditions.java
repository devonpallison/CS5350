package src.main.java.analysis;

import src.main.java.Encryptor;
import src.main.java.encryptors.*;
import src.main.java.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class InitialConditions {

    public static final int SAMPLE_SIZE = 100;

    public static void main(String[] args) throws IOException {
        final String myDirectoryPath = Encryptor.INPUT_IMAGE_FILE_PREFIX + "/mod3paintings/";
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();
        final CsvWriter csvWriter = new CsvWriter();
        csvWriter.addWords("File", "Encryption Method", "NPCR");

        final java.util.List<Double> npcrsASLBTM = new ArrayList<>();
        final java.util.List<Double> npcrsCTM = new ArrayList<>();
        final java.util.List<Double> npcrsLM = new ArrayList<>();
        final java.util.List<Double> npcrsTM = new ArrayList<>();

        final EncryptionAlgorithm alteredSineLogisticBasedTentMap = AlteredSineLogisticBasedTentMap.getInstance();
        final EncryptionAlgorithm cubicTentMap = CubicTentMap.getInstance();
        final EncryptionAlgorithm logisticMap = LogisticMap.getInstance();
        final EncryptionAlgorithm tentMap = TentMap.getInstance();

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        if (directoryListing != null) {
            for (File child : directoryListing) {
                final String inputFilePath = child.getAbsolutePath();
                final String fileName = child.getName();

                System.out.println("Reading image file " + inputFilePath);
                final BufferedImage plainText = ImageIO.read(new File(inputFilePath));
                final byte[] imageBytes = getRandomSample(plainText);

                EncryptionAlgorithm.ALTER_KEY = false;
                alteredSineLogisticBasedTentMap.resetKeys();
                cubicTentMap.resetKeys();
                logisticMap.resetKeys();
                tentMap.resetKeys();
                final byte[] encryptedImageASLBTM = alteredSineLogisticBasedTentMap.encrypt(imageBytes);
                final byte[] encryptedImageCTM = cubicTentMap.encrypt(imageBytes);
                final byte[] encryptedImageLM = logisticMap.encrypt(imageBytes);
                final byte[] encryptedImageTM = tentMap.encrypt(imageBytes);

                EncryptionAlgorithm.ALTER_KEY = true;
                EncryptionAlgorithm instance = AlteredSineLogisticBasedTentMap.getInstance();
                instance.resetKeys();
                final byte[] encryptedImageASLBTM2 = instance.encrypt(imageBytes);
                String map = "ASLBTM";
                double npcr = comparePixelChangeRate(csvWriter, df, fileName, map, encryptedImageASLBTM, encryptedImageASLBTM2);
                npcrsASLBTM.add(npcr);

                instance = CubicTentMap.getInstance();
                instance.resetKeys();
                final byte[] encryptedImageCTM2 = instance.encrypt(imageBytes);
                npcr = comparePixelChangeRate(csvWriter, df, fileName, "CTM", encryptedImageCTM, encryptedImageCTM2);
                npcrsCTM.add(npcr);

                instance = LogisticMap.getInstance();
                instance.resetKeys();
                final byte[] encryptedImageLM2 = LogisticMap.getInstance().encrypt(imageBytes);
                npcr = comparePixelChangeRate(csvWriter, df, fileName, "LM", encryptedImageLM, encryptedImageLM2);
                npcrsLM.add(npcr);

                instance = TentMap.getInstance();
                instance.resetKeys();
                final byte[] encryptedImageTM2 = TentMap.getInstance().encrypt(imageBytes);
                npcr = comparePixelChangeRate(csvWriter, df, fileName, "TM", encryptedImageTM, encryptedImageTM2);
                npcrsTM.add(npcr);
            }

            csvWriter.finish("initial_conditions.csv");
        }

        getMean("ASLBTM", npcrsASLBTM);
        getMean("CTM", npcrsCTM);
        getMean("LM", npcrsLM);
        getMean("TM", npcrsTM);
    }

    private static byte[] getRandomSample(final BufferedImage plainText) {
        int startPixelWidth = new Random().nextInt(plainText.getWidth() - SAMPLE_SIZE);
        int startPixelHeight = new Random().nextInt(plainText.getHeight() - SAMPLE_SIZE);
        byte[] bytes = new byte[SAMPLE_SIZE*SAMPLE_SIZE*3];
        int k = 0;
        for(int i = startPixelWidth; i < startPixelWidth + SAMPLE_SIZE; i++) {
            for (int j = startPixelHeight; j < startPixelHeight + SAMPLE_SIZE; j++) {
                Color color = new Color(plainText.getRGB(i, j));
                byte red = (byte) color.getRed();
                byte green = (byte) color.getGreen();
                byte blue = (byte) color.getBlue();
                bytes[k] = red;
                bytes[k+1] = green;
                bytes[k+2] = blue;
                k+=3;
            }
        }

        return bytes;
    }

    private static double comparePixelChangeRate(CsvWriter csvWriter,DecimalFormat df, String image, String map, byte[] encrypted1, byte[] encrypted2) {
        if(encrypted1.length != encrypted2.length) {
            throw new IllegalStateException();
        }
        double npcr = 0.0;
        for(int i = 0; i < encrypted1.length; i++) {
            npcr += encrypted1[i] == encrypted2[i] ? 1 : 0;
        }

        npcr = 1 - (npcr / (encrypted1.length));
        csvWriter.addWords(image, map, df.format(npcr));
        return npcr;
    }

    private static byte[] changeOnePixel(byte[] imageBytes1) {
        final byte[] copy = new byte[imageBytes1.length];
        System.arraycopy(imageBytes1, 0, copy, 0, imageBytes1.length);

        Random randy = new Random();
        int toChange = randy.nextInt(copy.length);
        byte b = imageBytes1[toChange];
        copy[toChange] = (byte) (b + randy.nextInt(256));
        return copy;
    }

    private static void getMean(final String map, final java.util.List<Double> results) {
        double sum = 0.0;
        for(double d : results) {
            sum += d;
        }
        System.out.println("Map: " + map + ", " + (sum / results.size()));
    }
}
