package rmq.impl;
import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import rmq.dispatcher.MessageDispatcher;
import rmq.impl.service.MessagePuller;
import rmq.impl.service.RmqMonitor;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class SchedulerService {
	private JobFactory jobFactory;
	private static MessageDispatcher messageDispatcher;
	
	public SchedulerService(MessageDispatcher messageDispatcher){
		SchedulerService.messageDispatcher=messageDispatcher;
	}
	
	@PostConstruct 
	public void start() throws SchedulerException{
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.setJobFactory(jobFactory);
	    scheduler.start();
	    
	    JobDetail job = newJob(MessagePuller.class)
	        .withIdentity("myJob", "group1")
	        .build();
	    
	    
	    Trigger trigger = newTrigger()
	        .withIdentity("myTrigger", "group1")
	        .startNow()
	        .withSchedule(simpleSchedule()
	            .withIntervalInSeconds(RmqConfig.getConfig().getRequestPullInterval())
	            .repeatForever())           
	        .build();
	    scheduler.scheduleJob(job, trigger);
	    
	    
	    //schedule rmq monitor
	    JobDetail jobMonitor = newJob(RmqMonitor.class)
	        .withIdentity("jobMonitor", "jobMonitor") 
	        .build();
	          
	    Trigger triggerMonitor = newTrigger()
	        .withIdentity("triggerMonitor", "jobMonitor")
	        .startNow()
	        .withSchedule(simpleSchedule()
	            .withIntervalInSeconds(RmqConfig.getConfig().getWorkingMonitorInterval())
	            .repeatForever())           
	        .build();
	    scheduler.scheduleJob(jobMonitor, triggerMonitor);
	}

	public JobFactory getJobFactory() {
		return jobFactory;
	}

	public void setJobFactory(JobFactory jobFactory) {
		this.jobFactory = jobFactory;
	}

	public static MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	public static void setMessageDispatcher(MessageDispatcher messageDispatcher) {
		SchedulerService.messageDispatcher = messageDispatcher;
	}
	
}
