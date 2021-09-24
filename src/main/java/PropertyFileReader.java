import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    private static final String JAR_LOCATION_PROPERTY = "user.dir";
    private static final String CONFIG_FILE_NAME = "\\config.properties";
    private static String PROPERTY_FILE_PATH;

    private static PropertyFileReader instance = null;

    public static PropertyFileReader getInstance(String[] args) {
        if (instance == null) {
            if (args.length > 0) {
                instance = new PropertyFileReader(args[0]);
            } else {
                instance = new PropertyFileReader(null);
            }
        }
        return instance;
    }

    public static PropertyFileReader getInstance() {
        return instance;
    }

    public String getProperty(String name) throws IOException {
        System.out.println(PROPERTY_FILE_PATH);
        try (InputStream inputStream = new FileInputStream(PROPERTY_FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return prop.getProperty(name);
        }
    }

    private PropertyFileReader(String arg) {
        if (arg != null) {
            PROPERTY_FILE_PATH = arg;
            return;
        }
        PROPERTY_FILE_PATH = buildDefaultPropertyFilePath();
    }

    private String buildDefaultPropertyFilePath() {
        return System.getProperty(JAR_LOCATION_PROPERTY) + CONFIG_FILE_NAME;
    }
}