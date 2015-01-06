package rmp.impl;

import rmq.impl.RedisMQ;
import rmq.impl.RmqConfig;
import rmq.mq.MessageQueue;
import junit.framework.TestCase;

public class TestMonitor extends TestCase{
	private MessageQueue q;
	
	public void testQInfo(){
		q=new RedisMQ();
		System.out.println("requests count:"+q.llen(RmqConfig.REQUEST_Q));
		System.out.println("working count:"+q.llen(RmqConfig.WORKING_Q));
		
		System.out.println("re-enq count:"+q.get(RmqConfig.RE_ENQ_COUNT_K));
		System.out.println("work failed count:"+q.get(RmqConfig.WORK_FAILED_COUNT_K));
		System.out.println("work error count:"+q.get(RmqConfig.WORK_ERROR_COUNT_K));
		System.out.println("last error:"+q.getObject(RmqConfig.LAST_WORK_ERROR_K));
	}
}
