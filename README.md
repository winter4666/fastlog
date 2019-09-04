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

	@Override
	public void log(String logType, List<LogField> logFields,Map<String, String> contextMap) {
		StringBuilder logFieldsStr = new StringBuilder();
		for(LogField logField : logFields) {
			logFieldsStr.append(logField.getFieldName())
						.append(":")
						.append(logField.getFieldValue())
						.append(" ");
		}
		System.out.println("logType:" + logType + ",logFields:" + logFieldsStr + ",contextMap:" + contextMap);
	}
}
```

3. `FastLogAspect`注入spring
```xml
<bean id="operLogRecorder" class="xxx.xxx.OperLogRecorder"/>
	
<bean class="com.github.winter4666.fastlog.FastLogAspect">
    <constructor-arg name="fastLogListener" ref="operLogRecorder"/>
</bean>
```

4. 在需要记录日志的方法上面加上`@FastLog`注解，`fieldValues`里面填写的字符串为[SpEL](https://docs.spring.io/spring/docs/3.0.x/reference/expressions.html)
```java
@FastLog(value="userLogin",			
		fieldNames={"用户名","密码"},
		fieldValues={"#a0","#a1"})
public void login(String username, String password) {
	FastLogContext.put("测试", "任意值");
}
```

5. 当上面注解的方法被调用时，`FastLogListener`的`log`方法被回调，打印出下面的信息
```
logType:userLogin,logFields:用户名:test 密码:1234 ,contextMap:{测试=任意值}
```