package src.main.java.analysis;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class AnalysisTools {

    public static List<double[]> getPixelsAsDoubles(final byte[] pixels) {
        final double[] redValues = new double[pixels.length / 3];
        final double[] greenValues = new double[redValues.length];
        final double[] blueValues = new double[redValues.length];

        int j = 0;
        for(int i = 0; i < pixels.length; i+=3) {
            redValues[j] = Byte.toUnsignedInt(pixels[i]);
            greenValues[j] = Byte.toUnsignedInt(pixels[i + 1]);
            blueValues[j] = Byte.toUnsignedInt(pixels[i + 2]);
            j++;
        }

        return Arrays.asList(redValues, greenValues, blueValues);
    }

    public static List<double[]> getPixelsAsRGBDoublesV(final byte[] pixels, int width, int height) {
        final double[] RGBDoubles1 = new double[(width * height * 3) / 2];
        final double[] RGBDoubles2 = new double[RGBDoubles1.length];

        int j = 0;
        for(int i = 0; i < width - 1; i++) {
            for(int k = 0; k < height - 1; k+=2) {
                int w = i * (height * 3);
                int h = k * 3;
                int red = Byte.toUnsignedInt(pixels[w + h]);
                int green = Byte.toUnsignedInt(pixels[w + h + 1]);
                int blue = Byte.toUnsignedInt(pixels[w + h + 2]);
                int rgb = red;
                rgb = rgb << 8;
                rgb = rgb | green;
                rgb = rgb << 8;
                rgb = rgb | blue;
                RGBDoubles1[j] = rgb;

                int red2 = Byte.toUnsignedInt(pixels[w + h + 3]);
                int green2 = Byte.toUnsignedInt(pixels[w + h + 4]);
                int blue2 = Byte.toUnsignedInt(pixels[w + h + 5]);
                int rgb2 = red2;
                rgb2 = rgb2 << 8;
                rgb2 = rgb2 | green2;
                rgb2 = rgb2 << 8;
                rgb2 = rgb2 | blue2;
                RGBDoubles2[j] = rgb2;
                j++;
            }
        }

        return Arrays.asList(RGBDoubles1, RGBDoubles2);
    }

    public static List<double[]> getPixelsAsRGBDoublesH(final byte[] pixels, int width, int height) {
        final double[] RGBDoubles1 = new double[(width * height * 3) / 2];
        final double[] RGBDoubles2 = new double[RGBDoubles1.length];

        int j = 0;
        for(int i = 0; i < width - 1; i+=2) {
            for(int k = 0; k < height - 1; k++) {
                int w = i * (height * 3);
                int h = k * 3;
                int red = Byte.toUnsignedInt(pixels[w + h]);
                int green = Byte.toUnsignedInt(pixels[w + h + 1]);
                int blue = Byte.toUnsignedInt(pixels[w + h + 2]);
                int rgb = red;
                rgb = rgb << 8;
                rgb = rgb | green;
                rgb = rgb << 8;
                rgb = rgb | blue;
                RGBDoubles1[j] = rgb;

                int w2 = (i + 1) * (height * 3);
                int red2 = Byte.toUnsignedInt(pixels[w2 + h + 3]);
                int green2 = Byte.toUnsignedInt(pixels[w2 + h + 4]);
                int blue2 = Byte.toUnsignedInt(pixels[w2 + h + 5]);
                int rgb2 = red2;
                rgb2 = rgb2 << 8;
                rgb2 = rgb2 | green2;
                rgb2 = rgb2 << 8;
                rgb2 = rgb2 | blue2;
                RGBDoubles2[j] = rgb2;
                j++;
            }
        }

        return Arrays.asList(RGBDoubles1, RGBDoubles2);
    }
}
