package rmq.mq;

public interface ConsumerQueue extends MessageQueue {

	Long hset(String key, String field, String value);

	String hget(String key, String field);

	void lempty(String key);

}
