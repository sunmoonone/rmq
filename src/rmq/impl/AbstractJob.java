package rmq.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class AbstractJob implements Job{

	public abstract void execute(JobExecutionContext context) throws JobExecutionException ;
	
}
