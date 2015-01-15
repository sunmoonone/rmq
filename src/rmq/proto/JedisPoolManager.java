package rmq.proto;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolManager {
	private JedisPool pool;
	
	public JedisPoolManager(){}
	
	public JedisPoolManager(JedisPool pool){
		this.pool=pool;
	}
	
	public JedisPoolManager(String ip,Integer port) {
		this.pool= JedisPoolManager.buildPool(ip, port);
	}

	public JedisPool getPool() throws IOException {
		if (pool == null) {
			Properties props = new Properties();
			props.load(JedisPoolManager.class
					.getResourceAsStream("/rmq/proto/rmq-default.properties"));
			
			try {
				Properties props2 = new Properties();
				props2.load(JedisPoolManager.class
						.getResourceAsStream("/rmq.properties"));

				Enumeration<?> names = props2.propertyNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement().toString();
					props.put(name, props2.getProperty(name));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Integer.valueOf(props
					.getProperty("jedis.pool.maxTotal")));
			config.setMaxIdle(Integer.valueOf(props
					.getProperty("jedis.pool.maxIdle")));
			config.setMaxWaitMillis(Long.valueOf(props
					.getProperty("jedis.pool.maxWait")));
			config.setTestOnBorrow(Boolean.valueOf(props
					.getProperty("jedis.pool.testOnBorrow")));
			config.setTestOnReturn(Boolean.valueOf(props
					.getProperty("jedis.pool.testOnReturn")));

			pool = new JedisPool(config, props.getProperty("redis.ip"),
					Integer.valueOf(props.getProperty("redis.port")));
		}
		return pool;
	}
	
	public static JedisPool buildPool(String ip,Integer port,
			Integer maxTotal,
			Integer maxIdle,
			Long maxWait,
			Boolean testOnBorrow,
			Boolean testOnReturn) {
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWait);
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);

		return new JedisPool(config,ip,port);
	}
	
	
	public static JedisPool buildPool(String ip,Integer port) {
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(1);
		config.setMaxIdle(1);
		config.setMaxWaitMillis(500);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

		return new JedisPool(config,ip,port);
	}

}
