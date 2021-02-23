package prospect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import prospect.api.AbstractService;
import prospect.config.Config;
import prospect.config.ConfigConstants;
import prospect.config.ConfigFactory;
import prospect.services.HttpMonitorService;
import prospect.services.SmsNotificationService;

public class Main {

	public static void main(String[] args) {

		boolean loadDefaults = true;
		String configPath = ConfigConstants.DEFAULT_CONFIG_FILE_PATH;

		if (args.length == 0) {
			System.out.println("NOTE: No args detected, you can optionally provide following arguments:");
			System.out.println("1. <config_file_path>:String, absolute or relative path to config file, " + 
					" i.e. /home/user/config.properties, if not provided default path will be used");
			System.out.println("2. <load_defaults>:boolean, wether internal config defaults " + 
					"shall be loaded in absence of required properties, true by default\n");
		}	else if (args.length < 2) {
			System.out.println("Both path and default mode is needed");
			System.exit(1);
		} else {
			configPath = args[0];

			// TODO Boolean parse doesn't throw exception as long as *any* String is provided
			// the parse is case insensitive so any form of 'true' will result with true boolean, 
			// any other string with false
			loadDefaults = Boolean.parseBoolean(args[1]);		  
		}

		Config c = null;
		try {
			c = ConfigFactory.loadConfig(configPath, loadDefaults);
		} catch (IllegalArgumentException | IOException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
		

		System.out.println("Loaded config: " + c.toString());
		System.out.println("Starting HttpMonitor...");
		
		// instantiate services
		List<AbstractService> services = new ArrayList<>();
		SmsNotificationService smsService = new SmsNotificationService(c);
		services.add(smsService);
		
		HttpMonitorService hmService = new HttpMonitorService(c);
		hmService.addOnServiceDownListener(smsService);
		services.add(hmService);
		
		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread()
	    {
	        @Override
	        public void run()
	        {
	        	for (AbstractService service : services) {
	        		System.out.println("Shuting down service '" + service.getName() + "'.");
	        		service.gracefullShutdown(false);
	        	}
	        }
	    });	
		
		// start services
		for (AbstractService service : services) {
			System.out.println("Starting service '" + service.getName() + "'.");
			service.startService();
		}
	}
}