package src.main.java.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CsvWriter {

    private final StringBuilder stringBuilder = new StringBuilder();

    public void addWords(final String... words) {
        for(int i=0; i < words.length - 1; i++) {
            stringBuilder.append(words[i]);
            stringBuilder.append(",");
        }
        stringBuilder.append(words[words.length - 1]);
        stringBuilder.append('\n');
    }

    public void startNewLine() {
        stringBuilder.append('\n');
    }
    public void addLine(final String line) {
        stringBuilder.append(line);
        stringBuilder.append('\n');
    }

    public void finish(final String fileName) {
        writeToCsv(fileName, stringBuilder.toString());
    }

    public static void writeToCsv(final String fileName, final String string) {
        System.out.println("Writing to csv " + fileName);

        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.write(string);
            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
