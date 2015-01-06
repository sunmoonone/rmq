package rmq.mq;

import java.util.List;

public interface MessageQueue {

	Object brpoplpush(String requestQ, String workingQ, int popTimeout);

	Object getObject(String bodyKey);

	Long del(String bodyKey);

	Long lremFromTail(String workingQ, Object request);

	<T> List<T> lrange(String workingQ, int start, int stop);

	Long llen(String workingQ);

	Long lpush(String requestQ, Object request);

	String setObject(String bodyKey, Object body);
	
	Long incr(String key);
	String get(String key);

}
