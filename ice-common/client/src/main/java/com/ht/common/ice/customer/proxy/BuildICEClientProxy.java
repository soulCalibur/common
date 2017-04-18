/*
 * @Project Name: cmp-ice
 * @File Name: BuildICEClientProxy.java
 * @Package Name: com.hhly.common.components.ice.customer.proxy
 * @Date: 2016年11月28日下午12:17:47
 * @Creator: shenxiaoping
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.common.ice.customer.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.ht.common.ice.customer.mode.ClientMetadata;

/**
 * @description ICE client proxy
 * @author Allen Shen
 * @date 2016年11月28日下午12:17:47
 * @see
 */
@Component
public class BuildICEClientProxy  implements BeanPostProcessor {

	private final static Logger LOG = LoggerFactory.getLogger(BuildICEClientProxy.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		EndpointsFactory.loadIceConfig();
		ClientMetadata[] metadatas = EndpointsFactory.getClientMetadataFields(bean);
		if(metadatas!=null&&metadatas.length>0){
			for (ClientMetadata clientMetadata : metadatas) {
				try {
					clientMetadata.getFiled().setAccessible(true);
					clientMetadata.getFiled().set(bean, clientMetadata.getBean());
					LOG.info("%%%%%%%%%%% inject proxy into {}-----{}%%%%%%%%%%%", bean.getClass().getName(),clientMetadata.getMetadataName());
					clientMetadata.getFiled().setAccessible(false);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					LOG.error("fail to set field with proxy object[{}]",clientMetadata.getMetadataName(), e);
					System.exit(1);
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
