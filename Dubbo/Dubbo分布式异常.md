## springmvc异常

@ControllerAdvice注解



## dubbo业务异常

**provider自定义业务异常BusinessException**，继承extends RuntimeException

**provider抛出自定义业务异常，在custom端并不能正确的捕获**。消费端捕获的依然是RuntimeException。



### 原因

如果Dubbo的 **provider端** 抛出异常（Throwable），则会被 provider端 的ExceptionFilter拦截到，执行以下invoke方法，**里面有个实现Listener类，重写了onResponse。**

