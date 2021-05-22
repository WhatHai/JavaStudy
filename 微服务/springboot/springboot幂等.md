# 接口限流

## 防止恶意请求--redis控制访问次数

https://blog.csdn.net/zhouzhiwengang/article/details/113738736

总体思路：结合**redis控制单位时间内相同ip访问同一个接口的次数**，将恶意的请求拦截在系统的上游。
1.自定义一个注解，这样可以灵活的控制每个接口与访问限制次数
2.定义切面范围，结合redis记录每个Ip对每个接口的单位时间的访问次数
3.在需要拦截的接口中判断用户是否在单位时间内频繁调起请求

```java
@Retention(RetentionPolicy.RUNTIME)//注解的保留位置
@Target(ElementType.METHOD)//注解的作用目标
@Documented//说明该注解将被包含在javadoc中
@Order(Ordered.HIGHEST_PRECEDENCE)最高优先级
public @interface RequestTimes {
  //单位时间允许访问次数 - - -默认值是3
  int count() default 3;

  //设置单位时间为1秒钟 - - -即默认值1秒钟，1000 为毫秒。
  long time() default  1000;
}
```

@Aspect//将java类定义为切面类
@Component
public class RequestTimesAop {

```java
@Autowired
private RedisTemplate<String, String> redisTemplate;

private final static Logger log4j = LoggerFactory.getLogger(RequestTimesAop.class);

//切面范围
@Pointcut("execution(* com.yn.ynmanage.controller..*.*(..))")
public void WebPointCut() {
}

@Before("WebPointCut() && @annotation(times)")
/**
 * JoinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象.
 */
public void ifovertimes(final JoinPoint joinPoint, RequestTimes times) {
    try {

        /**
         * 获取request
         */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = HttpUtil.getIpByRequest(request);
        String url = request.getRequestURL().toString();
        String key = "ifOvertimes".concat(url).concat(ip);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        //访问次数加一
        int count = redisTemplate.opsForValue().increment(key, 1).intValue();//increment(K key, double delta),以增量的方式将double值存储在变量中。
        //如果是第一次，则设置过期时间
        if (count == 1) {
            redisTemplate.expire(key, times.time(), TimeUnit.MILLISECONDS);
        }
        if (count > times.count()) {
            request.setAttribute("ifOvertimes", "true");
            log4j.error(df.format(new Date()) +", ip :" +ip+",每 "+times.time() / 1000+" 秒访问接口:"+url+", "+count+" 次,拦截请求。");
        } else {
            request.setAttribute("ifOvertimes", "false");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```
```java
 @RequestTimes(count = 3, time = 60000)
    @RequestMapping("/limit")
    @OperLog("测试接口")
    public ResultVo hello(String username, HttpServletRequest request) {
        if (request.getAttribute("ifOvertimes").equals("false")) {
            return ResultVo.success("进入方法!");
        }
        return ResultVo.error("操作过于频繁!");
    }
```





# 接口幂等

在《分布式锁》中有解释

**1、token机制：**防止页面重复提交。采用token加redis或token加jvm内存。处理流程：1. 数据提交前要向服务的申请token，token放到redis或jvm内存，token有效时间；2. 提交后后台校验token，同时删除token，生成新的token返回。token特点：要申请，一次有效性，可以限流。注意：redis要用删除操作来判断token，删除成功代表token校验通过，如果用select+delete来校验token，存在并发问题，不建议使用；

**2、select + insert：**在设计单据相关的业务，或者是任务相关的业务，肯定会涉及到状态机（状态变更图）。简单理解，就是业务单据上面有个状态的字段，状态在不同的情况下会发生变更，一般情况下存在有限状态机。这时候，如果状态机已经处于下一个状态，这时候来了一个上一个状态的变更，理论上是不能够变更的，这样的话，保证了有限状态机的幂等。注意：订单等单据类业务，存在很长的状态流转，一定要深刻理解状态机，对业务系统设计能力提高有很大帮助。





