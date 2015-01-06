package rmq.impl.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import rmq.impl.AbstractJob;
import rmq.impl.RedisMQ;
import rmq.impl.RmqConfig;
import rmq.mq.ConsumerQueue;
import rmq.mq.MessageQueue;
import rmq.proto.Request;

/**
 * NOTE: must run in single thread mode
 * @author Sunmoonone
 *
 */
@DisallowConcurrentExecution
public class RmqMonitor extends AbstractJob{
	private final static Logger logger=Logger.getLogger(RmqMonitor.class);
	private ConsumerQueue q;
	
	private static AtomicInteger stop= new AtomicInteger(-1);
	private static final int count=20;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		

		if(q==null){
			q=new RedisMQ();
		}
		
		List<Request> ws= q.lrange(RmqConfig.WORKING_Q,stop.get()-count+1,stop.get());
		
		if(ws==null || ws.size()==0){
			Long len=q.llen(RmqConfig.WORKING_Q);
			if(len==null) return;
			if(len == 0 || len < -(stop.get())){
				stop.set(-1);
			}
			return;
		}
		for(Request req:ws){
			long starttime=this.getRequestWorkStartTime(req);
			boolean timeout=false;
			
			if(starttime>0){
				if( (System.currentTimeMillis() - starttime) > RmqConfig.getConfig().getRequestWorkingTimeout() ){
					timeout=true;
				}
			}else if((System.currentTimeMillis() - req.getCreatedTime())> RmqConfig.getConfig().getRequestWorkingTimeout()){
				timeout=true;
			}
			if(timeout){
				
				if(q.lremFromTail(RmqConfig.WORKING_Q, req)>0){
					logger.info("re-en-q request:"+req.getBodyKey());
					q.lpush(RmqConfig.REQUEST_Q, req);
					q.incr(RmqConfig.RE_ENQ_COUNT_K);
				}else{
					logger.info("re-en-q failed:"+req.getBodyKey());
					q.incr(RmqConfig.RE_ENQ_FAILED_COUNT_K);
				}
				//q.brpoplpush(requestQ, workingQ, popTimeout);
			}
		}
		
		//set progress
		stop.set(stop.get()-ws.size());
	}

	private long getRequestWorkStartTime(Request header) {
		String time = q.hget(RmqConfig.WORK_START_TIME_HASH,header.getBodyKey());
		if(time==null || time.equals(""))return 0l;
		return Long.valueOf(time);
	}

}
