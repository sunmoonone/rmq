package rmq.impl;

import java.lang.reflect.Method;

import rmq.consumer.MessageConsumer;
import rmq.proto.RequestBody;


public class InvokeBeanConsumer implements MessageConsumer {

	@Override
	public boolean consume(RequestBody request) throws Exception{
		Object bean = SpringBeanJobFactory.getBean(request.getBeanName());
		Class<?> cls = bean.getClass();
		Method m;
		m = cls.getMethod(request.getMethodName(),
				request.getParamTypes());
		m.invoke(bean, request.getParams());
		return true;
	}

}
