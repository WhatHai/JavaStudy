

https://www.toutiao.com/a7008020808962441761/?log_from=9a9d61214b693_1634174727187

# 前言

在实际项目开发中很多业务场景需要使用异步去完成，比如消息通知，日志记录，等非常常用的都可以通过异步去执行，提高效率，那么在Spring框架中应该如何去使用异步呢

# 使用步骤

> 完成异步操作一般有两种，消息队列MQ，和线程池处理ThreadPoolExecutor

而在Spring4中提供的对ThreadPoolExecutor封装的线程池ThreadPoolTaskExecutor，直接使用注解启用@Async，这个注解让我们在使用Spring完成异步操作变得非常方便

# 配置线程池类参数配置

自定义常量类

```
public class ConstantFiledUtil {
    public static final String AUTHORIZATION_TOKEN = "authorizationToken";
    /**
     * kmall线程池名称
     */
    public static final String KMALL_THREAD_POOL = "KmallThreadPool";

    /**
     * kmall线程名称前缀
     */
    public static final String KMALL_THREAD_NAME_PREFIX = "kmall-thread-";
}
```

配置线程池

```
@Configuration(proxyBeanMethods = false)
@EnableAsync //开启注解
public class KmallConfig {
  
  @Bean(ConstantFiledUtil.KMALL_THREAD_POOL)
    public ThreadPoolTaskExecutor FebsShiroThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
         //配置核心线程数
        executor.setCorePoolSize(5);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(200);
        //线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(30);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(ConstantFiledUtil.KMALL_THREAD_NAME_PREFIX);
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(60);
          // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
   
}
```

> 注意这里需要通过@EnableAsync开启异步否则无效

# 自定义线程任务#

```
public interface ILogService extends IService<Log> {

    /**
     * 分页搜索查询日志信息
     * @param logParams
     * @return
     */
    IPage getSearchLogByPage(SearchLogParams logParams);

    /**
     * 保存日志
     *
     * @param logSubject
     */
    @Async
    void saveLog(LogSubject logSubject);
}
```

在需要异步执行的接口，或者方法上加上@Async注解此方法就是异步方法，在主程序中调用的化，就是异步方式单独线程执行

此注解表明saveLog方法进入的线程池是KmallThreadPool方法创建的。

我们也可以单独指定方法名称@Async("saveLogs")

这样在进行日记记录的时候就是单独线程执行每次请求都快速响应了，而耗时的操作都留给线程池中的线程去异步执行

# 总结

Spring中用@Async注解标记的方法，称为异步方法。在spring boot应用中使用@Async很简单：

1. 调用异步方法类上或者启动类加上注解@EnableAsync
2. 在需要被异步调用的方法外加上@Async
3. 所使用的@Async注解方法的类对象应该是Spring容器管理的bean对象；

> 注意同一个类里面调用异步方法不生效：原因默认类内的方法调用不会被aop拦截，即调用方和被调用方是在同一个类中，是无法产生切面的，该对象没有被Spring容器管理。即@Async方法不生效

解决办法：

如果要使同一个类中的方法之间调用也被拦截，需要使用spring容器中的实例对象，而不是使用默认的this，因为通过bean实例的调用才会被spring的aop拦截
本例使用方法：AsyncService asyncService = context.getBean(AsyncService.class); 然后使用这个引用调用本地的方法即可达到被拦截的目的
备注：这种方法只能拦截protected，default，public方法，private方法无法拦截。这个是spring aop的一个机制。

# 原理刨析

异步方法返回类型只能有两种:

1. 当返回类型为void的时候，方法调用过程产生的异常不会抛到调用者层面，可以通过注AsyncUncaughtExceptionHandler来捕获此类异常
2. 当返回类型为Future的时候，方法调用过程产生的异常会抛到调用者层面

> 注意如果不自定义异步方法的线程池默认使用SimpleAsyncTaskExecutor。SimpleAsyncTaskExecutor：不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。并发大的时候会产生严重的性能问题。

Spring异步线程池接口 TaskExecutor

看源码可知

```
@FunctionalInterface
public interface TaskExecutor extends Executor {
    void execute(Runnable var1);
}
```

它的实先类有很多如下：





1. SimpleAsyncTaskExecutor：不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。
2. SyncTaskExecutor：这个类没有实现异步调用，只是一个同步操作。只适用于不需要多线程的地方
3. ConcurrentTaskExecutor：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类
4. SimpleThreadPoolTaskExecutor：是Quartz的SimpleThreadPool的类。线程池同时被quartz和非quartz使用，才需要使用此类
5. ThreadPoolTaskExecutor ：最常使用，推荐。 其实质是对java.util.concurrent.ThreadPoolExecutor的包装