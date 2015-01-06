package rmq.impl;

import java.util.UUID;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringBeanJobFactory implements JobFactory, ApplicationContextAware {
	private static ApplicationContext ctx;
	
	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
			throws SchedulerException {
		//TODO 根据最后修改时间，重新加载类文件
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(bundle.getJobDetail().getJobClass());

		String beanId = UUID.randomUUID().toString().replaceAll("-", "");
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ctx
				.getAutowireCapableBeanFactory();
		acf.registerBeanDefinition(beanId, builder.getBeanDefinition());

		Job job = (Job) ctx.getBean(beanId);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(job);
		MutablePropertyValues pvs = new MutablePropertyValues();

		pvs.addPropertyValues(bundle.getJobDetail().getJobDataMap());
		pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());

		bw.setPropertyValues(pvs, true);

		return job;
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
			ctx=appContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) ctx.getBean(name);
	}

}
