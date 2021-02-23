package prospect.config;

/**
 * Util class containing keys of properties extracted from properties file
 * 
 * @author Szymon Stuglik
 */
public class ConfigConstants {

	// ------------ below keys mapped to properties file ------------

	// key for comma separated string with domains to check
	public final static String HTTP_MONITOR_DOMAINS = "http.monitor.domains";
	
	// key for number of worker threads to process listeners
	public final static String HTTP_MONITOR_LISTENER_WORKERS = "http.monitor.listener.workers";
	
	// key for http monitor check interval, value is in [ms]
	public final static String HTTP_MONITOR_CHECK_INTERVAL = "http.monitor.check.interval";

	// key for outgoing sms tel
	public final static String SMS_SERVICE_TEL = "sms.service.tel";
	
	// key for comma separated string with sms receipient numbers
	public final static String SMS_SERVICE_RECIPIENTS_TEL = "sms.service.recipients.tel";

	// key for sms service auth key
	public final static String SMS_SERVICE_AUTH_KEY = "sms.service.auth.key";

	
	
	// ------------ below default values -----------------------------
	
	// default http monitor domains
	public final static String [] DEFAULT_HTTP_MONITOR_DOMAINS = new String[] {
			"googleapis.com",
			"salesforce.com",
			"close.com",
			"zapier.com",
			"hubspot.com",
			"sendgrid.com",
			"gmail.com",
			"outlook.com",
	}; 

	// default sms receipient numbers
	public final static String [] DEFAULT_SMS_SERVICE_RECIPIENTS_TEL = new String[] { 
			"1234567",
			"7654321"
	};
	
	// default outgoing sms tel
	public final static String DEFAULT_SMS_SERVICE_TEL = "7777771";

	// default sms service auth key
	public final static String DEFAULT_SMS_SERVICE_AUTH_KEY = "abcasd!@#12qda";
	
	// default config file path
	public final static String DEFAULT_CONFIG_FILE_PATH = "./config.properties";
	
	// default check interval
	public final static Integer DEFAULT_HTTP_MONITOR_CHECK_INTERVAL = 30_000;
	
	// default listener worker number
	public final static Integer DEFAULT_HTTP_MONITOR_LISTENER_WORKERS = 5;

	// dont wanna have this class instantiated
	private ConfigConstants() {}
}
