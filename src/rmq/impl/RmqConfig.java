package rmq.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 * @author Sunmoonone
 *
 */
public class RmqConfig {
	private static RmqConfig _config;
	
	public final static String WORKING_Q="dana_working_q";
	public static final String REQUEST_Q="dana_request_q";
	public static final String RE_ENQ_COUNT_K = "dana_re_enq_count";

	public static final String WORK_FAILED_COUNT_K = "dana_work_failed_count";
	public static final String WORK_ERROR_COUNT_K = "dana_work_error_count";

	public static final String LAST_WORK_ERROR_K = "dana_last_work_error";

	public static final String WORK_SUCCESS_COUNT_K = "dana_work_success_count";

	public static final String WORK_SKIP_COUNT_K = "dana_work_skip_count";

	public static final String RE_ENQ_FAILED_COUNT_K = "dana_work_skip_count";

	public static final String WORK_START_TIME_HASH = "dana_work_start_time_hash";
	
	private int requestWorkingTimeout=1800;
	private int workingMonitorInterval=10;
	private int requestPullInterval=2;
	private int workerThreadCount=3;
	private boolean skipRequestOnError=false;
	
	public static RmqConfig getConfig() {
		if(_config==null){
			try {
				_config=load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _config;
	}
	
	private static RmqConfig load() throws IOException{
		Properties props = new Properties();
		InputStream st = RmqConfig.class.getResourceAsStream("/rmq/proto/rmq-default.properties");
		props.load(st);
		
		try {
			Properties props2 = new Properties();
			props2.load(RmqConfig.class
					.getResourceAsStream("/rmq.properties"));

			Enumeration<?> names = props2.propertyNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement().toString();
				props.put(name, props2.getProperty(name));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		RmqConfig config = new RmqConfig();

		config.requestWorkingTimeout= Integer.valueOf(props.getProperty("rmq.requestWorkingTimeout"));
		config.workingMonitorInterval= Integer.valueOf(props.getProperty("rmq.workingMonitorInterval"));
		config.requestPullInterval= Integer.valueOf(props.getProperty("rmq.requestPullInterval"));
		config.workerThreadCount= Integer.valueOf(props.getProperty("rmq.workerThreadCount"));
		config.skipRequestOnError= Boolean.valueOf(props.getProperty("rmq.skipRequestOnError"));
		
		return config;
	
	}


	public int getRequestWorkingTimeout() {
		return requestWorkingTimeout;
	}

	public int getWorkingMonitorInterval() {
		return workingMonitorInterval;
	}

	public int getRequestPullInterval() {
		return requestPullInterval;
	}


	public int getWorkerThreadCount() {
		return workerThreadCount;
	}

	public boolean isSkipRequestOnError() {
		return skipRequestOnError;
	}

	public void setSkipRequestOnError(boolean skipRequestOnError) {
		this.skipRequestOnError = skipRequestOnError;
	}

	
}
