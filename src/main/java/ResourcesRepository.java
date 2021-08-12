import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesRepository {

    public static final String RESOURCES_PATH_KEY = "resources_path";
    public static final String SERVER_CONFIG_PROPERTIES = "C:\\Users\\nvson\\IdeaProjects\\http-server\\config.properties";
    public static final String C_RESOURCES = new ResourcesRepository().getProjectResourcesPath();
    public static final String HOME_PAGE_ADDRESS = "\\index.html";
    public static final String ERROR_404_PAGE_ADDRESS = "\\404error.html";

    public String getProjectResourcesPath() {
        try (InputStream inputStream = new FileInputStream(SERVER_CONFIG_PROPERTIES)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return prop.getProperty(RESOURCES_PATH_KEY);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
