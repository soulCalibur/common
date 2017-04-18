///*
// * Project Name: lib File Name: ValidatorUtil.java Package Name:
// * com.hhly.base.util Date: 2017-1-19上午10:13:57 Creator: bb.h
// * ------------------------------ 修改人: 修改时间: 修改内容:
// */
//
//package com.ht.common.util;
//
//import java.util.Set;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//
//import org.hibernate.validator.HibernateValidator;
//import org.hibernate.validator.internal.engine.ConfigurationImpl;
//import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;
//
///**
// * @description 验证类工具
// * @author bb.h
// * @date 2017-1-19上午10:13:57
// * @see
// */
//public class ValidatorUtil {
//
//	public static <T> boolean valid(T bean) {
//		// ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		HibernateValidator provider = new HibernateValidator();
//		ConfigurationImpl configurationState = new ConfigurationImpl(provider);
//		ValidatorFactoryImpl factoryImpl = new ValidatorFactoryImpl(configurationState);
//		Validator validator = factoryImpl.getValidator();
//		Set<ConstraintViolation<T>> aasdas = validator.validate(bean);
//		System.out.println(aasdas.size());
//		return false;
//	}
//	// public static void main(String[] args) {
//	// ReqFightBallVOAPI fightBallVOAPI = new ReqFightBallVO();
//	// fightBallVOAPI.setLongitude(new BigDecimal(21312312));
//	// fightBallVOAPI.setLatitude(new BigDecimal(21312312));
//	// ValidatorUtil.valid(fightBallVOAPI);
//	// }
//}
