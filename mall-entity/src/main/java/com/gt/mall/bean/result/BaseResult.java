package com.gt.mall.bean.result;

import java.io.Serializable;

/**
 * 返回结果父类
 * 
 * @author Administrator
 *
 */
public class BaseResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 状态码：0:正常 ,其他为错误代码
	 */
	private Integer code;

	/**
	 * 返回描述
	 */
	private String msg;
	
	/**
	 * 返回业务数据
	 */
	private T data;
	
	public BaseResult(){
		
	}
	
	public BaseResult(Integer code,String msg){
		this.code=code;
		this.msg=msg;
	}
	
	/**
	 * 状态码：0:正常 ,其他为错误代码
	 */
	public Integer getCode() {
		return code;
	}

	
	/**
	 * 返回描述
	 */
	public String getMsg() {
		return msg;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
