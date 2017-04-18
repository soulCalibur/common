//
//package com.ht.common.util;
//
//import java.beans.BeanInfo;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//
///**
// * 类说明：提供bean和vo之间的映射复制
// * <p>
// * @author guoya-420
// * @author xchd 从sns-api中拷贝出来
// * @create_date 2016-10-09
// */
//public class BeanMapUtil extends BeanUtils {
//
//	private static final Logger logger = LoggerFactory.getLogger(BeanMapUtil.class);
//
//	/**
//	 * @方法功能描述 单个实体类转换为指定VO
//	 * @创建时间 2016年10月9日下午4:20:10
//	 * @param obj 需要转换的实体类对象
//	 * @param clazz VO的类对象，如：User.class
//	 * @return
//	 */
//	public static <T> T objToVO(Object obj, Class<T> clazz) {
//		T target = null;
//		try {
//			target = clazz.newInstance();
//			if (null != obj) {
//				BeanUtils.copyProperties(obj, target);
//			}
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		return target;
//	}
//
//	/**
//	 * @方法功能描述 List对象实体类转换为List<VO>对象
//	 * @创建时间 2016年10月9日下午4:20:51
//	 * @param list 需转换的对象集
//	 * @param clazz VO的类对象，如：User.class
//	 * @return
//	 */
//	public static <T> List<T> listToListVO(List<?> list, Class<T> clazz) {
//		List<T> result = new ArrayList<>();
//		for (Object obj : list) {
//			T target = objToVO(obj, clazz);
//			result.add(target);
//		}
//		return result;
//	}
//
//	/**
//	 * @description Object转Map
//	 * @date 2017年3月7日下午3:28:15
//	 * @author xuchuandi-394
//	 * @since 1.0.0
//	 * @param obj
//	 * @return
//	 * @throws Exception
//	 */
//	public static Map<String, String> objectToMap(Object obj) {
//		if (obj == null) {
//			return null;
//		}
//		try {
//			Map<String, String> map = new HashMap<String, String>();
//			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//			for (PropertyDescriptor property : propertyDescriptors) {
//				String key = property.getName();
//				if (key.compareToIgnoreCase("class") == 0) {
//					continue;
//				}
//				Method getter = property.getReadMethod();
//				Object value = getter != null ? getter.invoke(obj) : null;
//				if (value != null) {
//					map.put(key, value.toString());
//				} else {
//					map.put(key, null);
//				}
//			}
//			return map;
//		} catch (Exception e) {
//			logger.error("BEAN 转 MAP异常", e);
//			return null;
//		}
//	}
//}
