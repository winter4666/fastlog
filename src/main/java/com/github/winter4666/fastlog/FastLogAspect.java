package com.github.winter4666.fastlog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 日志切面
 * @author wutian
 */
@Aspect
public class FastLogAspect  {
	
	private static Logger logger = LoggerFactory.getLogger(FastLogAspect.class);
	
	private FastLogListener fastLogListener;
	
	public FastLogAspect(FastLogListener fastLogListener) {
		
	}

	@AfterReturning("@annotation(fastLog)")
    public void log(final JoinPoint joinPoint, final FastLog fastLog) throws Throwable {
		try {
	    	List<LogField> logFields = new ArrayList<>();
	    	if(fastLog.fieldNames().length > 0) {
	    		if(fastLog.fieldValues().length != fastLog.fieldNames().length) {
	    			throw new RuntimeException("length of fieldNames is not equal to length of fieldValues");
	    		}
	    		
	    		ExpressionParser parser = new SpelExpressionParser();
				EvaluationContext context = new StandardEvaluationContext();
				for (int i = 0; i < joinPoint.getArgs().length; i++) {
					context.setVariable("a" + i, joinPoint.getArgs()[i]);
					context.setVariable("p" + i, joinPoint.getArgs()[i]);
				}
	    		for(int i = 0;i < fastLog.fieldNames().length;i++) {
	    			LogField logField = new LogField();
	    			logField.setFieldName(fastLog.fieldNames()[i]);
	    			
	    			Expression exp = parser.parseExpression(fastLog.fieldValues()[i]);
	    			Object fieldValue = exp.getValue(context);
	    			String fieldValueStr = null;
	    			if(fieldValue == null) {
	    				fieldValueStr = String.valueOf(fieldValue);
	    			} else if(fieldValue.getClass().isArray()) {
	    				StringBuilder fieldValueStrBuilder = new StringBuilder("{");
	    				int length = Array.getLength(fieldValue);
	    				for(int j = 0;j < length;j++) {
	    					fieldValueStrBuilder.append(Array.get(fieldValue, j));
	    					if(j != length - 1) {
	    						fieldValueStrBuilder.append(",");
	    					}
	    				}
	    				fieldValueStrBuilder.append("}");
	    				fieldValueStr = fieldValueStrBuilder.toString();
	    			} else {
	    				fieldValueStr = String.valueOf(fieldValue);
	    			}
	    			logField.setFieldValue(fieldValueStr);
	    			logFields.add(logField);
	    		}
				
	    	}
    		fastLogListener.log(fastLog.value(), logFields);
		} catch(Throwable t) {
			logger.error("writeOperLog error", t);
    	}

    }
	
}
