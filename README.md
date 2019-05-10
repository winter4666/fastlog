# fastlog
基于spring的aop特性，提供了一种通过注解打日志的方式，可以方便而且没有侵入性地记录程序运行过程中的各种重要日志

## 使用方法
1. 修改spring配置，启用`@AspectJ`支持
```xml
<aop:aspectj-autoproxy/>
```

2. 实现`FastLogListener`接口
```java
public class OperLogRecorder implements FastLogListener {
	
	private static Logger logger = LoggerFactory.getLogger(OperLogRecorder.class);

	@Override
	public void log(String logType, List<LogField> logFields) {
		StringBuilder logFieldsStr = new StringBuilder();
		for(LogField logField : logFields) {
			logFieldsStr.append(logField.getFieldName())
						.append(":")
						.append(logField.getFieldValue())
						.append(" ");
		}
		logger.info("logType:{},logFields:{}",logType,logFieldsStr);
	}

}
```

3. `FastLogAspect`注入spring
```xml
<bean id="operLogRecorder" class="com.github.winter4666.test.OperLogRecorder"/>
	
<bean class="com.github.winter4666.fastlog.FastLogAspect">
    <constructor-arg name="fastLogListener" ref="operLogRecorder"/>
</bean>
```

4. 在需要记录日志的方法上面加上`@FastLog`注解，`fieldValues`里面填写的字符串为[SpEL](https://docs.spring.io/spring/docs/3.0.x/reference/expressions.html)
```java
@FastLog(value="userLogin",			
		fieldNames={"用户名","密码"},
		fieldValues={"#a0","#a1"})
@Override
public void login(String username, String password) {
	
}
```

5. 当上面注解的方法被调用时，`FastLogListener`的`log`方法被回调，打印出下面的信息
```
2019-05-08 17:31:32.478 [INFO] #pool-2-thread-1# com.github.winter4666.test.OperLogRecorder - logType:userLogin,logFields:用户名:test 密码:1234 
```
