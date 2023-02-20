package com.alibaba.sls.demo.utils;

public class EnvUtils {

	public static String getEnv(String key) {
		return getEnv(key, null);
	}

	public static String getEnv(String key, String defaultValue) {
		String value = System.getenv(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

}
