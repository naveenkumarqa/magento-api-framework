package api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	private static ConfigReader reader;
	private final Properties prop;
	
	private ConfigReader() {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
			prop.load(fis);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load property file" + e.getMessage());
		}
	}
	
	public static ConfigReader getInstance() {
		if (reader == null) {
			reader = new ConfigReader();
		}
		return reader;		
	}
	
	   public String getProperty(String key) {
	        return prop.getProperty(key);
	    }
}