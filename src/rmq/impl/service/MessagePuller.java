package rmq.impl.service;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import rmq.dispatcher.MessageDispatcher;
import rmq.impl.AbstractJob;
import rmq.impl.RedisMQ;
import rmq.impl.RmqConfig;
import rmq.impl.SchedulerService;
import rmq.mq.ConsumerQueue;
import rmq.proto.RequestBody;
import rmq.proto.Request;

public class MessagePuller extends AbstractJob {
	private final static Logger logger = Logger
			.getLogger(MessagePuller.class);
	private final static int POP_TIMEOUT = 1;

	private ConsumerQueue mq;

	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.debug("running PullRequestService");
		
		if(mq==null){
			mq=new RedisMQ();
		}
		
		Request header = this.popRequest();
		RequestBody request = null;
		if(header!=null){
			request=(RequestBody)mq.getObject(header.getBodyKey());
		}
		
		if (header != null && request!=null) {
			MessageDispatcher dispatcher = SchedulerService.getMessageDispatcher();
			
			String type=header.getType();
			if(type==null || type.equals("")){
				type="invoke-bean";
			}
			
			try{
				
				boolean result = dispatcher.getConsumer(type).consume(request);
				
				if(result){
					mq.del(header.getBodyKey());
					mq.lremFromTail(RmqConfig.WORKING_Q, header);
					mq.incr(RmqConfig.WORK_SUCCESS_COUNT_K);
					logger.info("work success:"+request.toString());
				}else{
					logger.warn("work failed:"+request.toString());
					mq.incr(RmqConfig.WORK_FAILED_COUNT_K);
				}
				
			}catch(Exception e){
				if(RmqConfig.getConfig().isSkipRequestOnError()){
					logger.warn("skip request:"+ (request !=null ?request.toString():"null"));
					mq.del(header.getBodyKey());
					mq.lremFromTail(RmqConfig.WORKING_Q, header);
					mq.incr(RmqConfig.WORK_SKIP_COUNT_K);
				}else{
					mq.incr(RmqConfig.WORK_ERROR_COUNT_K);
					mq.setObject(RmqConfig.LAST_WORK_ERROR_K, new WorkError(header,e.getMessage()));
					logger.error(
							"error when consuming request:" + (request !=null ? request.toString():"null"), e);
				}
			}
		}
	}


	private Request popRequest() {
		
		Request header = (Request) mq.brpoplpush(
				RmqConfig.REQUEST_Q, RmqConfig.WORKING_Q, POP_TIMEOUT);
		if(header!=null){
			mq.hset(RmqConfig.WORK_START_TIME_HASH,header.getBodyKey(),String.valueOf(System.currentTimeMillis()));
		}
		return header;
	}
}
