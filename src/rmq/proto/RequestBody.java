package rmq.proto;

import java.io.Serializable;

public class RequestBody implements Serializable{
	private static final long serialVersionUID = -5847943350524556431L;
	
	private String beanName;
	private String methodName;
	private String daoKey;
	private Object[] params;
	
	public RequestBody(){}
	
	public RequestBody(String beanName,String methodName,Object... params){
		this.beanName=beanName;
		this.methodName=methodName;
		this.setParams(params);
	}
	
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	public Class<?>[] getParamTypes() {
		if(params==null)return null;
		Class<?>[] types=new Class<?>[params.length];
		int i=0;
		for(Object p:params){
			types[i]=p.getClass();
			i++;
		}
		return types;
	}

	public String getDaoKey() {
		return daoKey;
	}

	public void setDaoKey(String daoKey) {
		this.daoKey = daoKey;
	}
	
	public String toString(){
		return String.format("daokey:%s  beanName:%s methodName:%s params:%s", daoKey,beanName,methodName,params);
	}
}
