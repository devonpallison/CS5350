package src.main.java.util;

import src.main.java.test.TestUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static byte[] getPixelsOfImage(final BufferedImage bufferedImage) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final byte[] pixels = new byte[width * height * 3];

        int i = 0;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb);
                byte red = (byte) color.getRed();
                byte green = (byte) color.getGreen();
                byte blue = (byte) color.getBlue();
                pixels[i] = red;
                pixels[i+1] = green;
                pixels[i+2] = blue;
                i += 3;
            }
        }

        return pixels;
    }

    public static BufferedImage changePixelsOfImage(final BufferedImage bufferedImage, final byte[] newPixels) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        int i = 0;
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                byte red = newPixels[i];
                byte green = newPixels[i+1];
                byte blue = newPixels[i+2];
                i += 3;
                Color newColor = new Color(Byte.toUnsignedInt(red), Byte.toUnsignedInt(green), Byte.toUnsignedInt(blue));
                int rgb = newColor.getRGB();
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        return bufferedImage;
    }
}
