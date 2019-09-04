package com.github.winter4666.fastlog;

import java.util.List;
import java.util.Map;

/**
 * 日志监听器
 * @author wutian
 */
public interface FastLogListener {
	
	/**
	 * 被{@linkplain FastLog @FastLog}注解的方法执行完毕后，该方法会被回调
	 * @param logType 日志类型
	 * @param logFields 记录的字段
	 * @param contextMap 上下文map，里面的key,value值源于{@link FastLogContext#put(String, Object)}
	 */
	void log(String logType,List<LogField> logFields,Map<String, Object> contextMap);

}
