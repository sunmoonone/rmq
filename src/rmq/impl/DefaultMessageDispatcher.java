package rmq.impl;

import java.util.HashMap;
import java.util.Map;

import rmq.consumer.MessageConsumer;
import rmq.dispatcher.MessageDispatcher;

public class DefaultMessageDispatcher implements MessageDispatcher{
	protected Map<String,MessageConsumer> consumers;
	
	public void setMessageConsumers(Map<String,MessageConsumer> consumers){
		this.consumers=consumers;
	}
	

	@Override
	public MessageConsumer getConsumer(String type) {
		if(consumers==null)return null;
		return consumers.get(type);
	}


	@Override
	public void setConsumer(String type, MessageConsumer consumer) {
		if(consumers==null)consumers=new HashMap<String,MessageConsumer>();
		consumers.put(type, consumer);
	}
}
