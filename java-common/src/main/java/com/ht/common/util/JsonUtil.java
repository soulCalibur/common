
package com.ht.common.util;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具类
 * @author Administrator
 */
public class JsonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * bean转json数据
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object) {
		if (object == null)
			return null;
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error("BEAN转JSON异常", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * json转bean
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object jsonToBean(String json, Class<?> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			logger.error("Json转Bean异常", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param json
	 * @param typeReference
	 * @return
	 * @description json 转泛型bean
	 * @date 2016-12-7下午4:24:44
	 * @author bb.h
	 * @since 1.0.0
	 */
	public static <T> T jsonToBean(String json, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(json, typeReference);
		} catch (Exception e) {
			logger.error("Json转Bean异常", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json转换成Collection<T>
	 * @param json
	 * @param collectionClass
	 * @param elementClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T jsonToCollection(final String json, Class<? extends Collection> collectionClass,
			Class<?> elementClass) {
		try {
			if (StringUtils.isNotEmpty(json)) {
				JavaType javaType = mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
				return (T) mapper.readValue(json, javaType);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Json转集合异常", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param jsonString
	 * @param nodeName
	 * @return
	 * @方法功能描述 从json字符串中取指定的值，增加对json数组的支持
	 * @创建时间 2016年10月25日下午2:04:08
	 * @作者 wangjian-358
	 */
	public static String getValue(String jsonString, String nodeName) {
		JsonNode rootNode;
		String val;
		try {
			rootNode = mapper.readTree(jsonString);
			val = getValue(rootNode, nodeName);
		} catch (JsonProcessingException e) {
			logger.error("json取值异常", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("json取值异常", e);
			throw new RuntimeException(e);
		}
		return val;
	}

	/**
	 * @param json
	 * @return JsonNode
	 * @author: wangjian-358
	 * @date: 2017/1/18 11:39
	 * @description: 获取 json 的根节点，用于从一个 json 内容多次取值
	 */
	public static JsonNode getRootNode(String json) {
		JsonNode rootNode;
		try {
			rootNode = mapper.readTree(json);
		} catch (IOException e) {
			logger.error("json获取 RootNode 异常：", e);
			throw new RuntimeException(e);
		}
		return rootNode;
	}

	/**
	 * @param rootNode
	 * @param nodeName
	 * @return
	 * @author: wangjian-358
	 * @date: 2017/1/18 11:39
	 * @description: 从 json 的 RootNode 取值
	 */
	public static String getValue(JsonNode rootNode, String nodeName) {
		if (null == rootNode || StringUtils.isBlank(nodeName)) {
			return null;
		}
		String val;
		JsonNode node = rootNode.findValue(nodeName);
		if (null == node) {
			return null;
		}
		// 是否为数组
		if (node.isArray()) {
			val = node.toString();
		} else {
			val = node.asText();
		}
		return val;
	}
}
