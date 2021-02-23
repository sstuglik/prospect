package prospect.api;

/**
 * Service down info class, used for sending notifications to consumers
 * 
 * @author Szymon Stuglik
 * 
 */
public class ServiceDownInfo {
	
	private String domain;
	
	private String ip;
	
	private boolean httpsUp;
	
	private boolean httpUp;
	
	private long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isHttpsUp() {
		return httpsUp;
	}

	public void setHttpsUp(boolean httpsUp) {
		this.httpsUp = httpsUp;
	}

	public boolean isHttpUp() {
		return httpUp;
	}

	public void setHttpUp(boolean httpUp) {
		this.httpUp = httpUp;
	}

	public ServiceDownInfo() {
		super();
	}

	public ServiceDownInfo(String domain, String ip, boolean httpsUp, boolean httpUp, long timestamp) {
		super();
		this.domain = domain;
		this.ip = ip;
		this.httpsUp = httpsUp;
		this.httpUp = httpUp;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ServiceDownInfo [domain=" + domain + ", ip=" + ip + ", httpsUp=" + httpsUp + ", httpUp=" + httpUp
				+ ", timestamp=" + timestamp + "]";
	}

}
