import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourcesRepository {

    public static final String RESOURCES_PATH = "resources_path";
    public static final String SERVER_CONFIG_PROPERTIES = "C:\\Users\\nvson\\IdeaProjects\\http-server\\config.properties";
    public static final String C_RESOURCES = new ResourcesRepository().getProjectResourcesPath();
    public static final String HOME_PAGE_ADDRESS = "\\index.html";
    public static final String ERROR_404_PAGE_ADDRESS = "\\404error.html";
    public static final String ERROR_405_PAGE_ADDRESS = "\\405error.html";

    private static final Logger logger = Logger.getLogger(ResourcesRepository.class.getName());

    public String getProjectResourcesPath() {
        try (InputStream inputStream = new FileInputStream(SERVER_CONFIG_PROPERTIES)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return prop.getProperty(RESOURCES_PATH);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while getting project resources path.", e);
        }
        return null;
    }
}
