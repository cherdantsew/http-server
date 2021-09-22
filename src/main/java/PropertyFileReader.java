import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    private static final String JAR_LOCATION_PROPERTY = "user_dir";
    private static final String CONFIG_FILE_NAME = "\\config.properties";
    private static String property_file_path;

    private static PropertyFileReader instance = null;

    public static PropertyFileReader getInstance(String[] args){
        if (instance == null){
            instance = new PropertyFileReader(args[0]);
        }
        return instance;
    }

    public static PropertyFileReader getInstance(){
        return instance;
    }
    public String getProperty(String name) throws IOException {
        try (InputStream inputStream = new FileInputStream(property_file_path)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return prop.getProperty(name);
        }
    }

    private String buildDefaultPropertyFilePath(){
        return System.getProperty(JAR_LOCATION_PROPERTY + CONFIG_FILE_NAME);
    }

    private PropertyFileReader(String arg) {
        if (arg != null) {
            property_file_path = arg;
            return;
        }
        property_file_path = buildDefaultPropertyFilePath();
    }
}