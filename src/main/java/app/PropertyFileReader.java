package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {

    private static final String DEFAULT_CONFIG_FILE_NAME = "config.properties";

    public static Properties getProps(String[] args) throws IOException {
        try (InputStream input = new FileInputStream(args.length > 0 ? args[0] : DEFAULT_CONFIG_FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        }
    }
}