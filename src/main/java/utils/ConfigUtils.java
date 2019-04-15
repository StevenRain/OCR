package utils;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigUtils {

    private static final String FILE_NAME = "config.txt";

    private ConfigUtils() {
    }

    public static String getValue() {
        Path path = Paths.get(FILE_NAME);
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.get(0);
        }catch (Exception e) {
            updateValue("0");
            return "0";
        }
    }

    public static void updateValue(String value) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(value);
            writer.close();
        }catch (Exception e) {

        }
    }
}
