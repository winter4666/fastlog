package com.github.winter4666.fastlog;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志上下文
 * @author wutian
 */
public class FastLogContext {
	
	private static final ThreadLocal<Map<String, Object>> contextMapHolder = new ThreadLocal<>();
	
	/**
	 * 向当前线程的上下文中放入值，放入的所有值最终会以{@link Map}的形式传回实现{@link FastLogListener}接口的类
	 * @param key
	 * @param value
	 */
	public static void put(String key,Object value) {
		Map<String, Object> contextMap = contextMapHolder.get();
		if(contextMap == null) {
			contextMap = new HashMap<>();
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
	
	static Map<String, Object> getContextMap() {
		Map<String, Object> contextMap = contextMapHolder.get();
		if(contextMap == null) {
			return new HashMap<>();
		} else {
			return contextMap;
		}
	}
}
