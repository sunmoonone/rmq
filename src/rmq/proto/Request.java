package rmq.proto;

import java.io.Serializable;
import java.util.UUID;

public class Request  implements Serializable{
	private static final long serialVersionUID = 3119218788302620905L;
	private String version;
	private long createdTime;
	private String type;
	
	public Request(String type){
		init();
		this.setType(type);
	}
	
	private void init(){
		this.version=UUID.randomUUID().toString();
		this.setCreatedTime(System.currentTimeMillis());
	}

	public String getBodyKey() {
			return version;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
