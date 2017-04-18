
package com.ht.common.exception;

/**
 * @author LiuPing 数据存取异常基类
 */
@SuppressWarnings("serial")
public class DaoException extends BaseException {

	public DaoException() {
		super();
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}
}
