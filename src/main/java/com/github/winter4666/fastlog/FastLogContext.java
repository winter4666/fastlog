package com.github.winter4666.fastlog;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志上下文
 * @author wutian
 */
public class FastLogContext {
	
	private static final ThreadLocal<Map<String, String>> contextMapHolder = new ThreadLocal<>();
	
	/**
	 * 向当前线程的上下文中放入值，放入的所有值最终会以{@link Map}的形式传回实现{@link FastLogListener}接口的类
	 * @param key
	 * @param value
	 */
	public static void put(String key,String value) {
		Map<String, String> contextMap = contextMapHolder.get();
		if(contextMap == null) {
			contextMap = new HashMap<String, String>();
			contextMapHolder.set(contextMap);
		}
		contextMap.put(key, value);
	}
	
	/**
	 * 清除当前线程的上下文
	 */
	public static void clear() {
		contextMapHolder.remove();
	}
	
	static Map<String, String> getContextMap() {
		Map<String, String> contextMap = contextMapHolder.get();
		if(contextMap == null) {
			return new HashMap<String, String>();
		} else {
			return contextMap;
		}
	}
}
