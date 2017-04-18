
package com.ht.web.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.ht.common.exception.ServiceException;
import com.ht.common.mode.ObjectMapperConfig;
import com.ht.common.util.StringUtil;
import com.ht.web.util.HttpUtil;
/**
 * @author bb.h @时间：2016-11-14 下午3:13:46
 * @说明: 1.提供所有请求日志记录 2.所有请求错误包装处理响应
 */
@Component
public class ExceptionInterceptor implements HandlerExceptionResolver{

	private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionInterceptor.class);
	private final static MappingJackson2JsonView responseView = new MappingJackson2JsonView();
	{
		responseView.setObjectMapper(new ObjectMapperConfig());
	}

	/**
	 * @函数功能 业务错误输出
	 * @创建时间 2016-11-17 上午10:57:14 @author bb.h
	 * @param exception
	 * @return
	 */
	private ModelAndView doServicesException(ServiceException exception) {
		// 业务错误输出
		if (!StringUtil.isBlank(exception.getMessage())) {
			LOGGER.info("[业务状态码:" + exception.getCode() + "][说明:" + exception.getMessage() + "]");
		}
		ModelMap model = new ModelMap();
		String code = exception.getCode();
		if (StringUtil.isBlank(code)) {
			//code = Constant.FAIL_SYSTEM_CODE;
		}
		model.addAttribute("result", code);
		//model.addAttribute("msg", apiResultUtil.getMessage(code));
		if (exception.getMessage() != null && exception.getMessage().trim().length() > 0) {
			model.addAttribute("msg", exception.getMessage());
		}
		if (exception.getData() != null) {
			model.addAttribute("data", exception.getData());
		}
		return new ModelAndView(responseView, model);
	}

	/**
	 * @函数功能 无法预测的异常处理
	 * @创建时间 2016-11-17 上午10:59:57 @author bb.h
	 * @param exception
	 * @param request
	 * @return
	 */
	private ModelAndView doException(Exception exception, HttpServletRequest request) {
		// 严重错误处理
		StringBuffer msg = new StringBuffer();
		msg.append("异常拦截日志");
		msg.append("[uri:").append(request.getRequestURI()).append("]");
		msg.append(HttpUtil.logBuffer(request));
		//msg.append(TokenUtil.logBuffer(request));
		Enumeration<?> names = request.getHeaderNames();
		msg.append("\n===================================================================");
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			msg.append(name + ":" + request.getHeader(name));
		}
		msg.append("\n===================================================================\n");
		LOGGER.error(msg.toString());
		ModelMap model = new ModelMap();
		model.addAttribute("result", "vvvvvvvvvvvvvvvvvvv");
		model.addAttribute("msg","aaaaaaaaaaaaaaaaaaaaaaaaaa");
		exception.printStackTrace();
		return new ModelAndView(responseView, model);
	}

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,	Exception ex) {
		if (ex instanceof BindException) {// 参数验证错误
			return doBindException((BindException)ex);
		
		} else if (ex instanceof ConstraintViolationException) {
			return doConstraintViolationException((ConstraintViolationException)ex);
			
		}else if (ex instanceof HttpRequestMethodNotSupportedException) {
			return doAddressException((HttpRequestMethodNotSupportedException)ex);
			
		} else if (ex instanceof MethodArgumentTypeMismatchException) {
			return doParamsTypeException((MethodArgumentTypeMismatchException)ex);
		} else if (ex instanceof MissingServletRequestParameterException) {
			return doParamsException((MissingServletRequestParameterException)ex);
		}else if (ex instanceof ServiceException) {
			return doServicesException((ServiceException) ex);
		} else if (ex instanceof InvocationTargetException) {
			//InvocationTargetException targetException = (InvocationTargetException) ex;
		}
		return doException(ex, request);
	}

	private ModelAndView doConstraintViolationException(ConstraintViolationException exception) {
		//1.组织错误提示信息
		 Set<ConstraintViolation<?>> objectError = exception.getConstraintViolations();
			String msg = objectError.iterator().next().getMessage();
			ModelMap model = new ModelMap();
			model.addAttribute("msg", msg);
			return new ModelAndView(responseView, model);
	}

	/**
	 *@函数功能 入参的类型错误处理
	 *@创建时间 2016-11-17 上午10:55:01 @author bb.h
	 *@param exception
	 *@return
	 */
	private final static ModelAndView doParamsTypeException(MethodArgumentTypeMismatchException exception){
		//入参的类型错误
		ModelMap model = new ModelMap();
		model.addAttribute("msg", "参数类型错误["+ exception.getRequiredType().getSimpleName() + "@"+ exception.getName() + ":" + exception.getValue() + "] ");
		return new ModelAndView(responseView, model);
	}

	private final static ModelAndView doParamsException(MissingServletRequestParameterException exception) {
		ModelMap model = new ModelMap();
//		model.addAttribute("result", Constant.RESULT_4002);
		model.addAttribute("msg", "参数错误["+ exception.getParameterType()+ "@"+ exception.getParameterName() +"]");
		return new ModelAndView(responseView, model);
	}
	
	/**
	 *@函数功能 请求方法找不到错误处理
	 *@创建时间 2016-11-17 上午10:51:40 @author bb.h
	 *@param exception
	 *@return
	 */
	private final static ModelAndView doAddressException(HttpRequestMethodNotSupportedException exception){
		ModelMap model = new ModelMap();
		StringBuffer buffer = new StringBuffer("[");
		for (String needMethodName : exception.getSupportedMethods()) {
			buffer.append(needMethodName + ",");
		}
		buffer.append("]");
		model.addAttribute("msg", "请求方式错误 [" + exception.getMethod()+ "]此接口支持以下模式 " + buffer);
		return new ModelAndView(responseView, model);
	}
	/**
	 *@函数功能 spring验证规则错误处理
	 *@创建时间 2016-11-17 上午10:47:40 @author bb.h
	 *@param exception
	 *@return
	 */
	private final static ModelAndView doBindException(BindException exception){
		//1.组织错误提示信息
		StringBuffer result=new StringBuffer("");
		ObjectError objectError=exception.getAllErrors().get(0);
		//for ( ObjectError objectError : exception.getAllErrors()) {
			if("typeMismatch".equals(objectError.getCode())){
				FieldError fieldError=(FieldError) objectError;
				String type=null;
				if(fieldError.getCodes().length>1){
					 type=fieldError.getCodes()[2];
					 int index=type.lastIndexOf(".")+1;
					if(index<type.length()){
						type=type.substring(index);
					}else {
						type="未知类型";
					}
				}
				result.append("参数类型错误["+type+"@"+fieldError.getField()+":"+fieldError.getRejectedValue()+"]");
			}else {
				result.append(objectError.getDefaultMessage());
			}
			//2.组织返回响应信息
			String msg = result.toString();
			if (!StringUtil.isBlank(msg)) {
				LOGGER.info("[参数验证错误:" + exception.getMessage() + "][说明:" + msg + "]");
			}
			ModelMap model = new ModelMap();
//			model.addAttribute("result", Constant.RESULT_4002);
			model.addAttribute("msg", msg);
			return new ModelAndView(responseView, model);
	}
	

}
