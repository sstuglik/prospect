package prospect.config;

import java.util.Arrays;

/**
 * Object containing app config
 * 
 * @author Szymon Stuglik
 *
 */
public class Config {
	
	private String [] domains;
	
	private String [] recipientTels;
	
	private String smsOutgoingTelNumber;
	
	private String smsApiAuthKey;
	
	private int checkInterval;
	
	private int numberOfListenerWorkers;
	
	protected Config() {
		
	}
	
	protected Config(String[] domains, String smsOutgoingTelNumber, String smsApiAuthKey, int checkInterval,
			int numberOfListenerWorkers, String[] recipientTels) {
		super();
		this.domains = domains;
		this.smsOutgoingTelNumber = smsOutgoingTelNumber;
		this.smsApiAuthKey = smsApiAuthKey;
		this.checkInterval = checkInterval;
		this.numberOfListenerWorkers = numberOfListenerWorkers;
		this.recipientTels = recipientTels;
	}

	public String[] getDomains() {
		return domains;
	}

	protected void setDomains(String[] domains) {
		this.domains = domains;
	}

	public String getSmsOutgoingTelNumber() {
		return smsOutgoingTelNumber;
	}

	protected void setSmsOutgoingTelNumber(String smsOutgoingTelNumber) {
		this.smsOutgoingTelNumber = smsOutgoingTelNumber;
	}

	public String getSmsApiAuthKey() {
		return smsApiAuthKey;
	}

	protected void setSmsApiAuthKey(String smsApiAuthKey) {
		this.smsApiAuthKey = smsApiAuthKey;
	}

	public long getCheckInterval() {
		return checkInterval;
	}

	protected void setCheckInterval(int checkInterval) {
		this.checkInterval = checkInterval;
	}

	public int getNumberOfListenerWorkers() {
		return numberOfListenerWorkers;
	}

	protected void setNumberOfListenerWorkers(int numberOfListenerWorkers) {
		this.numberOfListenerWorkers = numberOfListenerWorkers;
	}
	
	public String[] getRecipientTels() {
		return recipientTels;
	}

	protected void setRecipientTels(String[] recipientTels) {
		this.recipientTels = recipientTels;
	}

	@Override
	public String toString() {
		return "\ndomains=" + Arrays.toString(domains) + "\nrecipients=" + Arrays.toString(recipientTels) +
				"\nsmsOutgoingTelNumber=" + smsOutgoingTelNumber
				+ "\nsmsApiAuthKey=" + smsApiAuthKey + "\ncheckInterval=" + checkInterval + "\nnumberOfListenerWorkers="
				+ numberOfListenerWorkers;
	}
}
