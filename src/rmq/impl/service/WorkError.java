package rmq.impl.service;

import java.io.Serializable;

import rmq.proto.Request;

public class WorkError implements Serializable {
	private static final long serialVersionUID = 7526121350533460379L;
	private String requestBodyKey;
	private String requestType;
	private String message;
	
	public WorkError(Request request, String message) {
		this.requestType=request.getType();
		this.requestBodyKey=request.getBodyKey();
		this.message=message;
	}


	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getRequestBodyKey() {
		return requestBodyKey;
	}


	public void setRequestBodyKey(String requestBodyKey) {
		this.requestBodyKey = requestBodyKey;
	}
	
	public String toString(){
		return String.format("request type:%s request body key:%s error:%s", this.requestType,this.requestBodyKey,this.message);
	}

}
