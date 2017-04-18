
package com.ht.common.exception;

/**
 * @author LiuPing 业务处理异常
 */
public class ServiceException extends BaseException {

	private String code;
	private String message;
	private Object data;
	private Object params[];
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 错误响应-动态参数格式化日志输出
	 * @param code
	 * @param params
	 */
	public ServiceException(String code, Object... params) {
		super();
		this.code = code;
		this.params = params;
	}

	public void changeCode(String code) {
		this.code = code;
	}

	/**
	 * 错误响应-日志输出
	 * @param code
	 * @param message
	 */
	public ServiceException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	/**
	 * 通用错误响应
	 * @param code
	 */
	public ServiceException(String code) {
		super();
		this.code = code;
	}

	/**
	 * 错误响应 绑定到数据
	 * @param code
	 * @param data
	 */
	public ServiceException(String code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}

	/**
	 * 错误响应 日志输出 绑定到数据
	 * @param code
	 * @param message
	 * @param data
	 */
	public ServiceException(String code, String message, Object data) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public Object[] getParams() {
		return params;
	}

	public String getMessage() {
		return message;
	}
}
