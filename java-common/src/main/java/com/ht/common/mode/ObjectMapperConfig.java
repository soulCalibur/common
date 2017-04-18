package com.ht.common.mode;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
/**
 * 
 * @类描述 处理输出 json 数据时候的 null 值
 * @作者 wangjian-358
 * @创建时间 2016年8月24日上午11:24:25
 */
public class ObjectMapperConfig extends ObjectMapper {

	private static final long serialVersionUID = -813661394135988505L;

	public ObjectMapperConfig() {
		super();
		// 允许单引号
		this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
		this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 为 null 时不参与序列化，注意：只对VO起作用，Map List不起作用
		this.setSerializationInclusion(Include.NON_NULL);
		// null 替换为 空字符串
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				if( value instanceof Collection ) {
					gen.writeString("[]");
				} else if ( value instanceof Map) {
					gen.writeString("{}");
				} else {
					gen.writeString("");
				}
			}
		});
	}
}
