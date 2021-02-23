package prospect.services;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import prospect.api.AbstractService;
import prospect.api.IOnServiceDown;
import prospect.api.ServiceDownInfo;
import prospect.config.Config;

/**
 * Sample implementation of sms delivery service
 * TODO not using an actual api to send the sms, however the service stub is here
 * @author Szymon Stuglik
 *
 */
public class SmsNotificationService extends AbstractService implements IOnServiceDown {
	
	// config data
	private Config cfg;
	
	// run flag
	private AtomicBoolean run = new AtomicBoolean(true);
	
	// TODO probably good idea to restrict the size and implement scenario for when queue's full
	private BlockingQueue<ServiceDownInfo> notificationQueue = new LinkedBlockingQueue<>();
	
	private Thread smsNotificationThread;
	
	public SmsNotificationService(Config cfg) {
		super("SmsNotificationService");
		this.cfg = cfg;	
	}

	@Override
	public void reportServiceDown(ServiceDownInfo info) {
		// TODO again, should add checking for whether the item was added or not and react accordingly
		boolean inserted = this.notificationQueue.offer(info);
	}


	@Override
	public void gracefullShutdown(boolean wait) {
		this.run.set(false);
		this.smsNotificationThread.interrupt();
		
		if (wait) {
			try {
				this.smsNotificationThread.join(10_000);
			} catch (InterruptedException e) {
				System.out.println(getName() + " Service termination timed out.");
			}
		}
	}

	@Override
	public void startService() {
		// TODO you could also create an thread pool here as a future improvement
		this.smsNotificationThread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Starting SMS Notification Service");
				while (run.get() && !Thread.currentThread().isInterrupted()) {
					try {
						ServiceDownInfo info = notificationQueue.take();
						
						// TODO send sms via any publicly available provider (i.e. Twilio, Nexmo) or any other from RapidAPI
						// most of these apis are REST based so use any Java REST libs - i like unirest
						// alternatively hook usb sms gateway / modem and send sms via that - though that's more work
						System.out.println("[SMS NOTIFICATION SERVICE] Service down, sms message: " + info.toString());
						System.out.println("[SMS NOTIFICATION SERVICE] Sending sms to : " + Arrays.toString(cfg.getRecipientTels()));
						
					} catch (InterruptedException e) {
						System.out.println("SMS Notification Thread interrupted.");
					}
				}
			}
			
		});		
		
		// set as deamon, there's no gracefull shutdown so
		this.smsNotificationThread.setDaemon(true);
		this.smsNotificationThread.start();
	}
}
