package src.main.java.analysis;

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

}
