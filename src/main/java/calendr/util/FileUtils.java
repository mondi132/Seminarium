package calendr.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeToFile(File file, String content) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Couldn't create new file");
        }

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        System.out.println("Content: " + content);

        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(content);
        } catch (IOException e) {
            System.err.println("Couldn't open FileWriter");
        } finally {
            try {
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (IOException e) {
                System.err.println("Couldn't close BufferedWriter");
            }

            try {
                if (fileWriter != null) fileWriter.close();
            } catch (IOException e) {
                System.err.println("Couldn't close FileWriter");
            }
        }
    }
}
