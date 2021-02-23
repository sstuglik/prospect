package prospect.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Factory class for building the application config object
 * 
 * TODO NOTE: whenever/wherever user input is required, like i.e. here with the prop file, i prefer to 
 * extract the logic to designated factory
 * 
 * @author Szymon Stuglik
 *
 */
public class ConfigFactory {

	// object for loading properties from file
	private static Properties prop = new Properties();

	// dont want this instantiated
	private ConfigFactory() {};

	/**
	 * Load the property file 
	 * 
	 * @param propFilePath path to property file
	 * @param loadDefaults if <b>true</b> loads a default property value if the file does not contain value for given key,
	 * 	if <b>false</b> throws {@link IllegalArgumentException}
	 * @throws IllegalArgumentException
	 * @return
	 */
	public static Config loadConfig(String propFilePath, boolean loadDefaults) throws IllegalArgumentException, IOException {

		try (InputStream input = new FileInputStream(propFilePath)) {		
			prop.load(input);
		} catch (IOException ex) {
			if (!loadDefaults) {
				// add some logging here, re-throw exception
				throw ex;
			}
		}

		Config config = new Config();

		// domains
		setValue(
				ConfigConstants.HTTP_MONITOR_DOMAINS,
				ConfigConstants.DEFAULT_HTTP_MONITOR_DOMAINS,
				loadDefaults,
				(domains) -> {
					if (domains instanceof String) {
						config.setDomains(domains.toString().split(","));
					} else {
						config.setDomains((String[]) domains);
					}
				});
		
		// check interval
		setValue(
				ConfigConstants.HTTP_MONITOR_CHECK_INTERVAL,
				ConfigConstants.DEFAULT_HTTP_MONITOR_CHECK_INTERVAL,
				loadDefaults,
				(intervalObj) -> {
					config.setCheckInterval(parseInteger(intervalObj));
				});
		
		// listener workers
		setValue(
				ConfigConstants.HTTP_MONITOR_LISTENER_WORKERS,
				ConfigConstants.DEFAULT_HTTP_MONITOR_LISTENER_WORKERS,
				loadDefaults,
				(workersObj) -> {
					config.setNumberOfListenerWorkers(parseInteger(workersObj));
				});
		
		// sms outgoing tel
		setValue(
				ConfigConstants.SMS_SERVICE_TEL,
				ConfigConstants.DEFAULT_SMS_SERVICE_TEL,
				loadDefaults, 
				(smsTel) -> {
					config.setSmsOutgoingTelNumber(smsTel.toString());
				});
		
		// sms api auth
		setValue(
				ConfigConstants.SMS_SERVICE_AUTH_KEY,
				ConfigConstants.DEFAULT_SMS_SERVICE_AUTH_KEY,
				loadDefaults,
				(smsAuth) -> {
					config.setSmsApiAuthKey(smsAuth.toString());
				});
		
		// recipients
		setValue(
				ConfigConstants.SMS_SERVICE_RECIPIENTS_TEL,
				ConfigConstants.DEFAULT_SMS_SERVICE_RECIPIENTS_TEL,
				loadDefaults,
				(tels) -> {
					if (tels instanceof String) {
						config.setRecipientTels(tels.toString().split(","));
					} else {
						config.setRecipientTels((String[]) tels);
					}
				});

		return config;
	}
	
	/**
	 * Util method for attempting a parse to int operation on given object
	 * 
	 * @param intObj
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static int parseInteger(Object intObj) throws IllegalArgumentException {
		if (intObj instanceof Integer) {
			return (int) intObj;
		} else {
			try {
				int i = Integer.parseInt(intObj.toString());
				return i;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Illegal integer value: '" + intObj + "'.");
			}
		}
	}

	/**
	 * Utils method for setting config value to that defined in prop file or default (if requested)
	 * 
	 * @param key
	 * @param defaultValue
	 * @param useDefaults
	 * @param valueSeter
	 * @throws IllegalArgumentException
	 */
	private static void setValue(String key, Object defaultValue, boolean useDefaults, ConfigValueSetter valueSetter) throws IllegalArgumentException {
		String propValue = prop.getProperty(key);
		if (!useDefaults && (propValue == null || propValue.trim().isEmpty())) {
			throw new IllegalArgumentException("Property '" + key + "' not found in properties file, not using defaults.");
		}
		
		Object value = propValue == null ? defaultValue : propValue;		
		valueSetter.setValue(value);
	}

	@FunctionalInterface
	private static interface ConfigValueSetter {
		void setValue(Object s) throws IllegalArgumentException;
	}
}
