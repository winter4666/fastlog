package com.github.winter4666.fastlog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 * @author wutian
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FastLog {
	
	/**
	 * 日志类型
	 */
	String value();
	
	/**
	 * 需要记录的字段名
	 */
	String[] fieldNames() default {};
	
	/**
	 * 需要记录的字段值
	 */
	String[] fieldValues() default {};
	
}