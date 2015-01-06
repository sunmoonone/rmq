package rmq.dispatcher;

import rmq.consumer.MessageConsumer;

public interface MessageDispatcher {

	MessageConsumer getConsumer(String type);
	void setConsumer(String type , MessageConsumer consumer);
}
