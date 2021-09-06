import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourcesRepository {

    private final String CONFIG_FILE_NAME = "\\config.properties";
    private static final String RESOURCES_PATH = "resources_path";
    private static final String JAR_PATH = System.getProperty("user.dir");

    public static final String C_RESOURCES = new ResourcesRepository().getProjectResourcesPath();
    public static final String HOME_PAGE_ADDRESS = "\\index.html";
    public static final String ERROR_404_PAGE_ADDRESS = "\\404error.html";
    public static final String ERROR_405_PAGE_ADDRESS = "\\405error.html";

    private static final Logger logger = Logger.getLogger(ResourcesRepository.class.getName());

    public String getProjectResourcesPath() {
        try {
            if (Main.pathToPropertyFile != null) {
                return getResourcesPathProperty(Main.pathToPropertyFile);
            }
            return getResourcesPathProperty(JAR_PATH + CONFIG_FILE_NAME);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while trying to read property file.", e);
        }
        return null;
    }

    private String getResourcesPathProperty(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return prop.getProperty(RESOURCES_PATH);
        }
    }
}
