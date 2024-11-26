package connection.properties;

import exception.propertiesLoadException.LoadPropertiesException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream resource = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resource);
        } catch (IOException e) {
            throw new LoadPropertiesException(e, " Exception in loadProperties method in PropertiesUtil class.");
        }
    }

    private PropertiesUtil() {}

}
