package utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;

public class ConfigUtils {

    private static final String FILE_NAME = "config.txt";

    private ConfigUtils() {
    }

    public static String getValue() {
        try {
            FileReader reader = new FileReader(FILE_NAME);
            CharBuffer buffer = CharBuffer.allocate(20);
            int count = reader.read(buffer);
            if(count == -1) {
                updateValue("0");
                return "0";
            }
            buffer.flip();
            return buffer.toString();
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
