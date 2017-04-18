
package com.ht.web.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**	
 * <plugin interceptor="com.hhly.sns.dal.interceptor.MybatisPageInterceptor">
		    <!-- 分页查询数据-每页数据最大条数 -->
		    <property name="pageMaxSize" value="1000" />
	</plugin>
 * @author bb.h @时间：2016-11-14 下午3:12:49
 * @说明: 提供分页查询限制
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }))
public class MybatisPageInterceptor implements Interceptor {

	private static Logger logger = LoggerFactory.getLogger(MybatisPageInterceptor.class);
	private int pageMaxSize = 0;// 分页查询数据-每页数据最大条数

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		// 分页范围限制
		if (args[2] != null && args[2] != RowBounds.DEFAULT) {
			RowBounds rowBounds = (RowBounds) args[2];
			if (pageMaxSize > 0 && rowBounds.getLimit() > pageMaxSize) {
				// 重置分页范围
				StringBuffer errMsg = new StringBuffer();
				errMsg.append("分页请求超限[" + rowBounds.getLimit() + "] 自动重置[" + pageMaxSize + "]");
				StackTraceElement stack[] = Thread.currentThread().getStackTrace();
				for (StackTraceElement stackTraceElement : stack) {
					errMsg.append("\n");
					errMsg.append(stackTraceElement.toString());
				}
				logger.warn(errMsg.toString());
				rowBounds = new RowBounds(rowBounds.getOffset(), pageMaxSize);
				invocation.getArgs()[2] = rowBounds;
				// 置空销毁
				rowBounds = null;
			}
		}
		// 继续执行后续逻辑 终止逻辑可以抛出异常 拦截后统一处理
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		String pageMaxSizeStr = (String) properties.get("pageMaxSize");
		if (null != pageMaxSizeStr) {
			try {
				pageMaxSize = Integer.valueOf(pageMaxSizeStr);
			} catch (NumberFormatException e) {
				logger.error(
						"pageMaxSize 配置必须为数字[MybatisPageInterceptor:propertie]@[pageMaxSize=" + pageMaxSizeStr + "]");
			}
		}
	}
}
