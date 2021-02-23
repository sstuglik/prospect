package prospect.services;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import prospect.api.IOnServiceDown;
import prospect.api.AbstractService;
import prospect.api.ServiceDownInfo;
import prospect.config.Config;

public class HttpMonitorService extends AbstractService {

	// app config
	private Config cfg;

	// Executor to run the check and run reports on a service
	private ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

	// Thread pool for calling registered listeners
	private ExecutorService threadPool;

	// Set of listeners triggered when system is down
	private Set<IOnServiceDown> onServiceDownListeners = new HashSet<>();

	public HttpMonitorService(Config config) {
		super("HttpMonitorService");
		this.cfg = config;
		this.threadPool = Executors.newFixedThreadPool(this.cfg.getNumberOfListenerWorkers());		
	}

	public void addOnServiceDownListener(IOnServiceDown listener) {
		this.onServiceDownListeners.add(listener);
	}

	private boolean isReachable(String host, int port) {
		// Ping the provided IP and port
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 1000);
			return true;
		// TODO Socket throws multiple exceptions, capture them to prevent the scheduled executor service
	    // from unexpectedly terminating due to uncaught exception
		} catch (Exception e) {
			return false;
		}
	}

	private void check(String domain) throws Exception {
		final StringBuilder ip = new StringBuilder();
		StringBuilder log = new StringBuilder();
		// Get the domain's IP so we can ping it
		try {
			ip.append(InetAddress.getByName(domain).getHostAddress());
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("Invalid domain provided");
		}

		// Are HTTP and HTTPS reachable?
		// TODO 443 is standard https port, these also could be added to config file
		boolean http = isReachable(domain, 80);
		boolean https = isReachable(domain, 443);
		
		if (!http || !https) {
			for (IOnServiceDown listener : this.onServiceDownListeners) {
				threadPool.submit(() -> {
					
					// TODO this is simply adding the object to the queue on the other side, but the queue may be full, blocking etc
					// thus safer to do this in a thread
					listener.reportServiceDown(new ServiceDownInfo(domain, ip.toString(), https, http, System.currentTimeMillis()));
				});
			}			
		}

		// Log result to console
		log.append(String.format("%20s", domain));
		log.append(String.format("%18s", ip));
		log.append("\t\tHTTP: ");
		log.append(http ? "UP" : "DOWN!");
		log.append(" | HTTPS: ");
		log.append(https ? "UP" : "DOWN!");

		System.out.println(log.toString());
	}

	@Override
	public void gracefullShutdown(boolean wait) {
		this.ses.shutdown();
		if (wait) {
			try {
				this.ses.awaitTermination(10l, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				System.out.println(getName() + " Service termination timedout.");
			}
		}
	}

	@Override
	public void startService() {
		// Check every domain at an interval
		ses.scheduleAtFixedRate(() -> {		
			// Clear previously displayed report
			System.out.print("\033[H\033[2J");  
			System.out.flush(); 
			
			for (String domain : this.cfg.getDomains()) {
				try {
					check(domain);
				} catch (Exception e) {
					// log the exception
					System.err.println("Exception during service check: ");
					System.err.println(e);
				}
			}			
		}, 10_000, this.cfg.getCheckInterval(), TimeUnit.MILLISECONDS);		
	}
}