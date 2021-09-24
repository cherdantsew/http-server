import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourcesRepository {

    public static final String C_RESOURCES = new ResourcesRepository().getProjectResourcesPath();

    private static final String RESOURCES_PATH = "resources_path";
    private static final Logger logger = Logger.getLogger(ResourcesRepository.class.getName());

    public String getProjectResourcesPath() {
        try {
            return PropertyFileReader.getInstance().getProperty(RESOURCES_PATH);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Couldn't find resources_path property in property file" , e);
        }
        return null;
    }
}
