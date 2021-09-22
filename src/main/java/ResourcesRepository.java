import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourcesRepository {


    private static final String RESOURCES_PATH = "resources_path";
    private static final String JAR_PATH = System.getProperty("user.dir");

    public static final String C_RESOURCES = new ResourcesRepository().getProjectResourcesPath();
    public static final String HOME_PAGE_ADDRESS = "\\index.html";
    public static final String ERROR_404_PAGE_ADDRESS = "\\404error.html";
    public static final String ERROR_405_PAGE_ADDRESS = "\\405error.html";

    private static final Logger logger = Logger.getLogger(ResourcesRepository.class.getName());

    public String getProjectResourcesPath() {
        try {
            return PropertyFileReader.getInstance().getProperty(RESOURCES_PATH);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Couldnt find resources_path property in property file" , e);
        }
        return null;
    }
}
