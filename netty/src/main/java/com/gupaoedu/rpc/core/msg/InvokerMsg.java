package com.gupaoedu.rpc.core.msg;

import java.io.Serializable;

public class InvokerMsg implements Serializable {
	  
    private static final long serialVersionUID = -8970942815543515064L;

    
    private String className;//调用的服务名称，这里使用的是接口名称
    private String methodName;//调用接口即服务中的哪个方法
    private Class<?>[] parames;//由于方法有重载，所以将参数传递过来
    private Object[] values;//对应参数值
    
    public String getClassName() {
        return className;  
    }  
    public void setClassName(String className) {
        this.className = className;
    }  
    public String getMethodName() {
        return methodName;
    }  
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
	public Class<?>[] getParames() {
		return parames;
	}
	public void setParames(Class<?>[] parames) {
		this.parames = parames;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}

}
