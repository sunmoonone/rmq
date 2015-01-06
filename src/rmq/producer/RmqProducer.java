package rmq.producer;

import rmq.mq.MessageQueue;
import rmq.proto.Request;
import rmq.proto.RequestBody;

public class RmqProducer {
	private MessageQueue mq;

	public RmqProducer() {
		this.mq = new ProducerQ();
	}

	public RmqProducer(MessageQueue mq) {
		this.mq = mq;
	}

	public boolean produce(Request header, RequestBody body) throws Exception {
		String ret = mq.setObject(header.getBodyKey(), body);

		if (ret != null && ret.equals("OK")) {
			return mq.lpush("dana_request_q", header) > 0;
		}
		throw new Exception("write request to queue failed");
	}

}
