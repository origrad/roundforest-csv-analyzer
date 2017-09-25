package service;

public class MonitoringService implements IMonitoringService {

	public long bytes;

	@Override
	public void addString(String str) {
		int numBytes = str.getBytes().length;
		bytes = bytes + numBytes;
		if (bytes > 400000000) {
			System.err.println("Memory alert");
		}
	}

}
