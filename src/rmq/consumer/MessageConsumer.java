package rmq.consumer;

import rmq.proto.RequestBody;

public interface MessageConsumer {

	boolean consume(RequestBody request) throws Exception;

}
