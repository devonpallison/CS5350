package src.main.java.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FileUtil {
    public static final String IMAGE_FORMAT = "jpg";

    public static byte[] imageToByteArray(String file) throws IOException {
        BufferedImage bImage = ImageIO.read(new File(file));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, IMAGE_FORMAT, bos);
        return bos.toByteArray();
    }

    public static void writeByteArrayToImage(final byte[] data, final String outputFile) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        final File outputFileObj = new File(outputFile);
        ImageIO.write(bImage2, IMAGE_FORMAT, outputFileObj);
        System.out.println("image created at " + outputFileObj.getAbsolutePath());
    }
}
