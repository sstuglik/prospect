package prospect.api;

/**
 * Simple interface enabling user to define and implement listener called when service is down  
 * TODO you could have another interface that would only trigger on specific service being down, that would be also easy to implement
 * would only require small changes to the config so that the user can define these without recompiling the code. Thought it may be too much
 * for this exercise :)
 * 
 * @author Szymon Stuglik
 */
public interface IOnServiceDown {

	/**
	 * Called whenever service is down
	 * 
	 * @param info
	 */
	void reportServiceDown(ServiceDownInfo info);
}
