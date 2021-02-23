package prospect.api;

/**
 * Util abstract class for easier app lifecycle management 
 *  
 * @author Szymon Stuglik
 */
public abstract class AbstractService {
	
	private String serviceName;
	
	protected AbstractService(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getName() {
		return this.serviceName;
	}
	
	public abstract void startService();
	
	/**
	 * requestes shutdown service
	 * 
	 * @param wait whether to join any underlying threads and wait for them to finish
	 * 
	 * 
	 */
	// TODO timeout and asyn call shall be added here, again to much for this exercise probably
	public abstract void gracefullShutdown(boolean wait);
}
