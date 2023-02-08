package com.housaire.security.utils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Zhang Kai
 */
public final class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	public static boolean isReferenceType(Class<?> clazz) {
		return null != clazz
				&& !clazz.isPrimitive()
				&& !isPrimitive(clazz)
				&& clazz != String.class
				&& clazz != Date.class
				&& clazz != BigDecimal.class
				&& clazz != java.sql.Date.class
				&& clazz != Class.class
				&& clazz != Void.class
				&& clazz != Object.class;
	}

	public static boolean isPrimitive(Class<?> typeClass) {
		if (typeClass.isPrimitive()) {
			return true;
		}
		try {
			Class<?> clazz = (Class<?>) typeClass.getField("TYPE").get(null);
			return clazz.isPrimitive();
		} catch (NoSuchFieldException | IllegalAccessException e) {
			// ignore
		}
		return false;
	}

}
