##RPC与RESTful



RPC 远程调用图解：

![](images/RPC远程调用.png)

| 比较项   | RESTful    | RPC         |
| -------- | ---------- | ----------- |
| 通讯协议 | HTTP       | 一般使用TCP |
| 性能     | 略低       | 较高        |
| 灵活度   | 高         | 低          |
| 应用     | 微服务架构 | SOA架构     |



## CAP理论

![](images/CAP理论.png)

Consistency(一致性):数据一致更新，所有数据的变化都是同步的
Availability(可用性):在集群中一部分节点故障后，集群整体是否还能响应客户端的读写请求
Partition tolerance(分区容忍性):某个节点的故障，并不影响整个系统的运行

| 选 择 | 说明                                                         |
| ----- | ------------------------------------------------------------ |
| CA    | `放弃分区容错性，加强一致性和可用性，其实就是传统的关系型数据库的选择 ` |
| AP    | 放弃一致性(这里说的一致性是强一致性)，追求分区容错性和可用性，这是很多分布式 系统设计时的选择，例如很多NoSQL系统就是如此 |
| CP    | `放弃可用性，追求一致性和分区容错性，基本不会选择，网络问题会直接让整个系统不可 用 ` |



##eureka

![](images/eureka.png)

- Eureka Client是一个Java客户端，用于简化与Eureka Server的交互;
- Eureka Server提供服务发现的能力，各个微服务启动时，会通过Eureka Client向Eureka Server
  进行注册自己的信息(例如网络信息)，Eureka Server会存储该服务的信息;
- 微服务启动后，会周期性地向Eureka Server发送心跳(默认周期为30秒)以续约自己的信息。如
  果Eureka Server在一定时间内没有接收到某个微服务节点的心跳，Eureka Server将会注销该微服
  务节点(默认90秒);
- 每个Eureka Server同时也是Eureka Client，多个Eureka Server之间通过复制的方式完成服务注
  册表的同步;
- Eureka Client会缓存Eureka Server中的信息。即使所有的Eureka Server节点都宕掉，服务消费
  者依然可以使用缓存中的信息找到服务提供者。

###eureka注册中心

引入依赖 spring-cloud-starter-netflix-eureka-server
配置EurekaServer
通过 @EnableEurekaServer 激活Eureka Server端配置

###eureka服务注册流程

pom文件中添加eureka client的相关坐标

配置文件中添加Eureka Server的主机地址 eureka.client.serviceUrl.defaultZone 

启动类添加服务注册注解 @EnableDiscoveryClient 或 @EnableEurekaClient 

###eureka续约流程



###Eureka中的自我保护 

微服务第一次注册成功之后，每**30秒**会发送一次**心跳**将服务的实例信息注册到注册中心。通知 Eureka Server 该实例仍然存在。如果超过**90秒**没有发送更新，则服务器将从注册信息中将此服务**移除**。 

Eureka Server在运行期间，会统计心跳失败的比例在15分钟之内是否低于85%，如果出现低于的情况 (在单机调试的时候很容易满足，实际在生产环境上通常是由于网络不稳定导致)，Eureka Server会 将当前的实例注册信息保护起来，同时提示这个警告。保护模式主要用于一组客户端和Eureka Server 之间存在网络分区场景下的保护。一旦进入保护模式，Eureka Server将会尝试保护其服务注册表中的 信息，不再删除服务注册表中的数据(也就是**不会注销任何微服务**)

### eureka启动流程

![eureka启动流程](images/eureka%E5%90%AF%E5%8A%A8%E6%B5%81%E7%A8%8B.png)

### eureka server启动流程

![eureka启动流程](images/eurekaserver%E5%90%AF%E5%8A%A8%E6%B5%81%E7%A8%8B.png)

### eureka client自动装载

通过@springbootapplication里面的@EnableAutoconfigration找到starter的factories文件，定位里的一些autoConfigration**自动配置类**，



### eureka元数据

Eureka的元数据有两种:标准元数据和自定义元数据。 

标准元数据:主机名、IP地址、端口号、状态页和健康检查等信息，这些信息都会被发布在服务注 册表中，用于服务之间的调用。 自定义元数据:可以使用eureka.instance.metadata-map配置，符合KEY/VALUE的存储格式。这 些元数据可以在远程客户端中访问。 



##Eureka中的常见问题 

###服务注册慢
 默认情况下，服务注册到Eureka Server的过程较慢。SpringCloud官方文档中给出了详细的原因 

服务的注册涉及到心跳，默认心跳间隔为30s。在实例、服务器、客户端都在本地缓存中具 有相同的元数据之前，服务不可用于客户端发现(所以可能需要3次心跳)。可以通过配置 

eureka.instance.leaseRenewalIntervalInSeconds (心跳频率)加快客户端连接到其他服务的过 程。在生产中，最好坚持使用默认值，因为在服务器内部有一些计算，他们对续约做出假设。 

###服务节点剔除问题
 默认情况下，由于Eureka Server剔除失效服务间隔时间为90s且存在自我保护的机制。所以不能有效而 迅速的剔除失效节点，这对开发或测试会造成困扰。

解决方案如下: **Eureka Server: 配置关闭自我保护，设置剔除无效节点的时间间隔 ；Eureka Client:配置开启健康检查，并设置续约时间**







##RestTemplate服务调用

Spring框架提供的RestTemplate类可用于在应用中调用rest服务

RestTemplate默认依赖JDK提供http连接的能力(HttpURLConnection)，如果有需要的话也可以通过 setRequestFactory方法替换为例如 Apache HttpComponents、Netty或OkHttp等其它HTTP library。 

HTTP协议的方法:HEAD、GET、POST、PUT、DELETE和OPTIONS。对应的， RestTemplate类具有headForHeaders()、getForObject()、postForObject()、put()和delete()等方法。 



##Ribbon

1、服务调用

基于Ribbon实现服务调用， 是通过拉取到的所有服务列表组成(服务名-请求路径的)映射关系。借助
RestTemplate 最终进行调用

```
创建RestTemplate时候，声明@LoadBalance注解

使用RestTemplate调用微服务，以服务名称替换IP地址
```

2、负载均衡

客户端负载均衡工具

负载均衡策略：轮询，随机，重试，权重策略（会计算每个服务的权重，越高的被调用的可能性越大），

最佳策略（遍历所有的服务实例，过滤掉故障实例，并返回请求数最小的实例返回） 

可用过滤策略（过滤掉故障和请求数超过阈值的服务实例，再从剩下的实力中轮询调用）

![ribbon](images/ribbon.png)

##ribbon源码

![](images/ribbon源码.png)

关键组件：

- ServerList:可以响应客户端的特定服务的服务器列表。
- ServerListFilter:可以动态获得的具有所需特征的候选服务器列表的过滤器。
- ServerListUpdater:用于执行动态服务器列表更新。
- Rule:负载均衡策略，用于确定从服务器列表返回哪个服务器。
- Ping:客户端用于快速检查服务器当时是否处于活动状态。
- LoadBalancer:负载均衡器，负责负载均衡调度的管理



##feign

服务消费者引入 spring-cloud-starter-openfeign 依赖
通过 @FeignClient 声明一个调用远程微服务接口
启动类上通过 @EnableFeignClients 激活Feign



**请求压缩：**

Spring Cloud Feign 支持对请求和响应进行GZIP压缩，以减少通信过程中的性能损耗。通过下面的参数



### 源码

![](images/feign源码.png)

动态代理对象调用接口



## 微服务高并发

请求积压问题

![](images/请求积压问题.png)

解决：线程池服务隔离隔离



##Hystrix断路器

### 服务雪崩

客户端访问A服务，而A服务需要调用B服务，B服务需要调用C服务，由于网络原因或者自身的原因，如果B服务或者C服务不能及时响应，A服务将处于阻塞状态，直到B服务C服务响应。此时若有大量的请求涌入，容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，造成连锁反应，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应



服务隔离：模块独立，无强依赖

熔断降级：当下游服务因访问压力过大而响应变慢或失败，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用

降级：是当某个服务熔断之后，服务器将不再被调用，此时客户端可以自己准备一个本地的fallback回调，返回一个缺省值

限流：可以认为服务降级的一种，限流就是限制系统的输入和输出流量已达到保护系统的目的

###实现：

**ribbon：**

1. 添加Hystrix的相关依赖
2. 启动类 添加 @EnableCircuitBreaker 注解开启对熔断器的支持
3. 定义降级方法，并在@HystrixCommand 中配置



**支持Feign**

1、引入依赖（Feign已经继承Hystrix）

2、在Feign配置中开启Hystrix

3、配置FeignClient接口实现类，里面有熔断触发降级逻辑

4、修改feignClient接口，添加降级方法支持，@FeignClient注解中添加降级方法



###短路

![](images/Hystrix断路器.png)



###熔断器隔离策略

**线程池隔离策略**:使用一个线程池来存储当前的请求，线程池对请求作处理，设置任务返回处理超
时时间，堆积的请求堆积入线程池队列。这种方式需要为每个依赖的服务申请线程池，有一定的资
源消耗，好处是可以应对突发流量(流量洪峰来临时，处理不完可将数据存储到线程池队里慢慢处
理)
**信号量隔离策略**:使用一个原子计数器(或信号量)来记录当前有多少个线程在运行，请求来先判
断计数器的数值，若超过设置的最大线程个数则丢弃改类型的新请求，若不超过则执行计数操作请
求来计数器+1，请求返回计数器-1。这种方式是严格的控制线程且立即返回模式，无法应对突发
流量(流量洪峰来临时，处理的线程超过数量，其他的请求会直接返回，不继续去请求依赖的服
务)





##Spring Cloud Alibaba 

###Spring Cloud Alibaba简介 

Spring Cloud Alibaba 是由阿里推出的， 致力于提供微服务开发的一站式解决方案。 2018年7月正式开源， 进入 Spring Cloud 孵化器;2019年7月, 从Spring Cloud迁出, 独立维护。 阿里巴巴推出 Spring Cloud Alibaba，很大程度上市希望通过抢占开发者生态，来帮助推广自家的 云产品。
 依赖于阿里巴巴强大的技术影响力， 逐渐成为主流互联网公司， 微服务解决方案的重要选择之 一。 

###Spring Cloud Alibaba主要功能: 

- 服务限流降级

- 服务注册与发现
- 分布式配置管理
-  消息驱动能力

- 分布式事务

- 分布式任务调度

- 阿里云对象存储、短信服务等

###Spring Cloud Alibaba主要组件: 

**Sentinel**:把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务 的稳定性。 

**Nacos**:一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。 

**RocketMQ**:一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高 可靠的消息发布与订阅服务。 

**Dubbo**:Apache Dubbo? 是一款高性能 Java RPC 框架。 

**Seata**:阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。

 **Alibaba Cloud ACM**:一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置 中心产品。

 **Alibaba Cloud OSS**: 阿里云对象存储服务(Object Storage Service，简称 OSS)，是阿里云 提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地 点存储和访问任意类型的数据。

 **Alibaba Cloud SchedulerX**: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精 准、高可靠、高可用的定时(基于 Cron 表达式)任务调度服务。

 **Alibaba Cloud SMS**: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业 迅速搭建客户触达通道。 



###Spring Cloud vs Spring Cloud Alibaba 

Spring Cloud Alibaba与Spring Cloud并非两种对立的技术, Spring Cloud Alibaba是基于Spring Cloud 基础上做了改进与扩展, 增加了许多功能, 提供了完善的微服务解决方案。 

| 对比           | Spring Cloud Alibaba | Spring Cloud       |
| -------------- | -------------------- | ------------------ |
| 注册中心       | Nacos                | Eureka/Consul      |
| 熔断降级       | Sentinel             | Hystrix            |
| 服务通讯       | Rest/Rpc(Dubbo)      | Rest(Ribbon/Feign) |
| 服务网关       | Spring Cloud Gateway | Zuul               |
| 分布式事务     | Seata                | -                  |
| 消息队列       | RocketMQ             | -                  |
| 对象存储       | OSS                  | -                  |
| 分布式任务调度 | SchedulerX           | -                  |
| 日志分析       | SLS                  | -                  |
| 推送服务       | SMS短信/VMS语音      | -                  |



##面试要点

### 微服务概述



微服务架构设计，优势，存在问题，服务雪崩，穿透，并发，可用性等



深度：，CAP机制，BASE理论，TCC两阶段，补偿



### 设计模式

聚合模式



代理模式



分支模式



异步模式



### 熔断降级原理





###springcloud和springcloud alibaba区别



**springcloud alibaba的改进**



###springcloud核心组件

打的全点，



### springcloud和dubbo区别

rest，RPC



###**Spring Cloud Eureka ⭐**

​        服务注册发现，由 Eureka Server 注册中心和

Eureka Client 服务注册组成，支持心跳机制、健康检查、负载均衡等；



###服务注册 Register

​        当 Eureka Client（服务提供者）向 Eureka Server 注册时，它提供自身的元数据 ，比如 IP 地址、端口，运行状况指示符 URL，主页等；



###服务续约 Renew

​        Eureka Client 会每隔30秒（默认情况下）发送一次心跳来续约；通过续约来告知 Eureka Server 该 Eureka Client 仍然存在，没有出现问题；正常情况下，如果 Eureka Server 在90秒没有收到 Eureka Client 的续约，它会将实例从其注册表中删除；



###获取注册列表信息 Fetch Registries

​        Eureka Client 从 Eureka Server 获取注册表信息，并将其缓存在本地；Eureka Client 会使用该信息查找其他服务，从而进行远程调用；该注册列表信息定期（每30秒钟）更新一次；每次返回注册列表信息如果与 Eureka Client 的缓存信息不同，Eureka Client 自动处理；Eureka Client 和 Eureka Server 可以使用 JSON / XML 格式进行通讯；



###服务下线 Cancel**

​        Eureka Client 在程序关闭时向 Eureka Server 发送取消请求；发送请求后，该客户端实例信息将从 Eureka Server 的实例注册表中删除；该下线请求不会自动完成，它需要调用以下内容：

DiscoveryManager.getInstance().shutdownComponent();



###服务剔除 Eviction**

​        在默认的情况下，当 Eureka Client 连续90秒（3个续约周期）没有向 Eureka Server 发送服务续约，即心跳， Eureka Server 会将该服务实例从服务注册列表删除，即服务剔除；



###Eureka 高可用**

​        Eureka Server 互相注册，Eureka Client 配置所有的 Eureka Server 连接，逗号隔开；



###Eureka 服务保护**

​        Eureka 通过心跳来判断服务健康，同时会定期删除超过90秒没有发送心跳的服务；导致 Eureka Server 接收不到心跳包的可能：一是微服务自身的原因（个别服务出现故障），二是微服务与 Eureka 之间的网络故障（大面积故障）；

​        Eureka 设置了一个阀值，当判断挂掉的服务的数量超过阀值时，Eureka Server 认为很大程度上出现了网络故障，将不再剔除心跳过期的服务，会将所有好的数据（有效服务数据）和坏的数据（无效服务数据）都返回给 Eureka Client；当网络故障恢复后，Eureka Server 会退出服务保护模式；

​        通过全局配置文件

（eureka.server.enableSelfPreservation）来关闭服务保护模式，商业项目中不推荐关闭服务保护，因为网络不可靠很容易造成网络波动、延迟、断线的可能，如果关闭了，可能导致大量的服务反复注册、删除、再注册，导致效率降低；



###Eureka 优雅停服**

​        在全局配置文件中配置

（endpoints.shutdown.enabled），通过 HTTP POST 请求的方式（http://ip:port/shutdown），通知 Eureka Client 优雅停服，这个请求一旦发送到 Eureka Client，那么 Eureka Client 会发送一个 shutdown 请求到 Eureka Server，Eureka Server 接收到这个 shutdown 请求后，会在服务列表中标记这个服务的状态为 down，同时 Eureka Client 应用自动关闭；

​        如果使用了优雅停服，则不需要再关闭

Eureka Server 的服务保护模式；









###Spring Cloud Ribbon ⭐**

​        客户端负载均衡器，属于软负载，运行在消费者端；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXGSbB91PbCia9SLkVFyh25x9SBMkiaHoXcwIdLGs7icSkBIyD1uGN3lqvw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



###负载均衡的核心**

- 服务发现：LoadBalancerClient

  （RibbonLoadBalancerClient 是实现类）在初始化的时候（execute 方法），会通过 ILoadBalance（BaseLoadBalancer 是实现类）向 Eureka 注册中心获取服务注册列表；

- 服务选择规则：通过 LoadBalancerInterceptor 进行拦截请求，并根据具体的 IRule 实现来进行负载均衡，默认轮询算法，并且可以更换默认的负载均衡算法，只需要在配置文件中做出修改；

- - RandomRule 随机策略；
  - RoundRobinRule 轮询策略；
  - WeightedResponseTimeRule 加权策略；
  - BestAvailableRule 请求数最少策略；

- ![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXDC1ibRtfCTS8ibIGicqdEIZ9Lnwq8DwYC0buftpdqA1IIYyh1E1Xbqj6w/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

- 服务监听：每10s一次向 EurekaClient 发送 Ping，来判断服务的可用性，如果服务的可用性发生了改变或者服务数量和之前的不一致，则从注册中心更新或者重新拉取；









###Spring Cloud Feign ⭐**

​        声明式REST客户端（伪 RPC），采用基于接口的注解，完全代理HTTP 请求，用 @FeignClient(value = "微服务名") 来映射服务调用，像调用方法一样调用它就可以完成服务请求及相关处理，并且 Feign 整合了 Ribbon 和 Hystrix：

- 可插拔的注解支持，包括 Feign 注解和 JAX-RS 注解；
- 支持可插拔的 HTTP 编码器和解码器；
- 支持 Hystrix 和它的 Fallback；
- 支持Ribbon的负载均衡；
- 支持HTTP请求和响应的压缩；



###Feign 原理**

1. 启动时，程序会进行包扫描，扫描所有包下所有 @FeignClient 注解的类，并将这些类注入到 Spring IOC 容器中，当定义的 Feign 中的接口被调用时，通过 JDK 动态代理来生成 RequestTemplate；
2. RequestTemplate 中包含请求的所有信息，如请求参数，请求 URL 等；
3. RequestTemplate 生产 Request，然后将 Request 交给 Client 处理，这个 Client 默认是 JDK HTTPUrlConnection，也可以是 OKhttp、Apache HTTPClient 等；
4. 最后 Client 封装成 LoadBaLanceClient，结合 Ribbon 负载均衡地发起调用；



###Feign 组成**

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuX7CImicJxHYSJn52Py1ibTicUJR2YYSyyRfgovNY5wFj9J1Hz6xic1htC6A/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



###Feign 配置方式**

- 代码配置；
- 属性配置文件；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuX27HucrSfXbxXjYxc1Zqq6KMQWOUCDRD6fwm7pxbwckJoJYEVxZA5Fw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXygkwlanskia5d0usqSuufRFAmgBCdoiaQRIexz8aEF4VXaRicEAMcKbNA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



###RestTemplate 和 Feign 对比**

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXBMkEPY9Z9SicdHPKdQI1iayABiaIbnpcQZpTTO6F2bCoribxBYOeeDtajg/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)









###Spring Cloud Feign 优化（加分项）⭐**

**Feign 日志级别**

​        优化：生产日志级别设置：BASIC；

- NONE（默认值）：不记录任何日志；
- BASIC：仅记录请求方法、URL、响应状态代码以及执行时间；
- HEADERS：记录请求和响应的 header；
- FULL：记录请求和响应的 header、body 和元数据；



**GET 多参数请求构造**

​        Feign 默认不支持 GET 方法直接绑定 POJO 的，目前解决方式如下：

- 方法一：通过 Feign 的 RequestInterceptor 中的 apply 方法，统一拦截转换处理 Feign 中的 GET 方法多参数；
- 方法二：Feign 接口方法入参使用 @SpringQueryMap User user；
- 方法三：Feign 接口方法入参使用 
- @RequestParam("id") Long id, @RequestParam("username") String username；



**配置请求连接池（使用 OKhttp）**

​        Feign 底层默认是使用 JDK 中的 

HttpURLConnection 发送 HTTP 请求（没有连接池，保持长连接），Feign 也提供了 OKhttp 来发送请求；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuX1pxzicdlwCbUnn7xtJts3R4AOrphLntAxgyFicAPJPGYLibZmaEfqhZfA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



**Feign 开启 GZIP 压缩**

​        支持对请求和响应进行 GZIP 压缩，以提高通信效率；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXcl9SloPEsXmfQ7OvAKN7Sia4a9gib3LE1WVgMgyTVaJ76Abib4IrhzR4g/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

​        注意：由于开启 GZIP 压缩之后，Feign 之间的调用数据通过二进制协议进行传输，返回值需要修改为 ResponseEntity<byte[]> 才可以正常显示，否则会导致服务之间的调用乱码；



**Feign 超时时间**

​        Feign 的调用分为两层，Ribbon 和 Hystrix（默认集成），默认情况下，Hystrix 是关闭的，所以当 Ribbon 发生超时异常时，可以如下配置调整 Ribbon 超时时间：

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXu3XxRo5ibkRxcqK3YKDfTlHL1t3KW9LrHVib0ChbricHnVPQuIeLTJJqQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

​        如果开启了 Hystrix，此时 Ribbon 的超时时间和 Hystrix 的超时时间的结合就是 Feign 的超时时间，当 Hystrix 发生了超时异常时，可以如下配置调整 Hystrix 的超时时间：

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXc6IqxvEwFIzhgYG4AoREVDpHKgqr7cia30xicXQHI8F0rgCUMvwSbiaicA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

​        注 : 当开启 Ribbon 之后，可能会出现首次调用失败的情况，原因 : 因为 Hystrix 的默认超时时间是1s，而 Feign 首次的请求都会比较慢，如果 Feign 响应时间（Ribbon 响应时间）大于了1s，就会出现调用失败的问题；



​        解决方法 :

1. 方法一：将 Hystrix 的超时时间尽量修改得长一点（有时 Feign 进行文件上传的时候，如果时间太短，可能文件还没有上传完就超时异常了，这个配置很有必要）；

2. 方法二：禁用 Hystirx 超时时间 :

   hystrix.command.default.execution.timeout.enabled=false；

3. 方法三：Feign 直接禁用 Hystrix（不推荐) : feign.hystrix.enabled=false；

4. 方法四：Ribbon 配置饥饿加载（最佳）：ribbon.eager-load.enabled: true，支持配置 eager load 实现在启动时就初始化 Ribbon 相关类；



**Feign 调用传递 headers 信息内容**

​        默认情况下，当通过 Feign 调用其他的服务时，Feign 不会带上当前请求的 headers 信息的；

​        如果需要调用其他服务进行鉴权时，需要从 headers 中获取鉴权信息，则可以通过实现 Feign 的拦截 RequestInterceptor 接口，进行获取 headers，然后手动配置到 Feign 请求的 headers 中去；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXsv9fiaIfF55KMhqXraSdYHV3sEQmotibBGl1GWMaRbGsiaGbGHEgjS7KA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



###产生 Hystrix Stream 监控信息**

​        Feign 本身已经整合 Hystrix，可直接使用

@FeignClient(value = "microservice-provider-user", fallback = XXX.class) 来指定 fallback 类，fallback 类继承 @FeignClient 所标注的接口即可；

​        但如需使用 Hystrix Stream 进行监控，默认情况下，访问

http://IP:PORT/actuator/hystrix.stream 是会返回404，这是因为 Feign 虽然整合了 Hystrix，但并没有整合 Hystrix 的监控，添加监控支持：

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXLn8ptY2CeibxSkQETNTpRJIYwUiblM11JDcsPPGDLcGPUS0mufib0cM3Q/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



###Feign 自定义处理返回的异常**

​        自定义好异常处理类后，要在 @Configuration 修饰的配置类中声明此类；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuX4A5kzbzuFTR2r5ewm5GL0iaCFnCb84fytZmSo3Q0SQVkbGsuGpia9uUw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



###Feign 上传文件

​        早期的 Spring Cloud 中，Feign 本身是没有上传文件的能力的，要实现需要自己去编写 Encoder 去实现上传，现在 Feign 官方提供了子项目 feign-form，其中实现了上传所需的 Encoder ；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXDkOa4o6QgcrraCR2jG4OD7ed3PnmUjUib2c6ia7hkcrCRGydNGJVal8A/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)









**Spring Cloud Config**

​        统一配置中心，集中管理所有微服务的配置，方便维护，支持配置内容安全与权限，支持无需重启更新项目配置；









**Spring Cloud Bus**

​        消息总线，自动刷新配置，实现流程：config-server 对外提供  /bus-refresh接口，pom 引入 RabbitMQ 后默认添加两个队列，在客户端加上 @RefreshScope，在 WebHooks 配置对应的 URL/monitor 实现自动刷新；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXLhtpQyv4osicOZcXFH0IsRBdr5aNiaCUJ4kjKzqAegBBo3GAq2JmozfQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)









**Spring Cloud Stream**

​        消息中间件的封装，目前支持 RabbitMQ 和 Kafka；









###Spring Cloud Zuul ⭐**

​        服务网关，由路由+过滤器组成，其核心是一系列过滤器；



**4种标准过滤器**

- 前置（Pre）:限流（令牌桶限流 Guava实现或开源的个人 ratelimit 实现）、鉴权、参数校验调整；
- 路由（Route）
- 后置（Post）：统计、日志；
- 错误（Error）

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXxaAWxWZTiaxPCK3ZvfFpbRekoWqkp2LOYFgj0XOwKJj0UC38jNZIsOA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXJ1q0Et69xMXxnhic38jdHuwvrer72MFiaJLA8IoETf8DbCMXm6ssI9SA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



**Zuul 高可用**

- 多个 Zuul 节点注册到 Eureka Server；
- Nginx 和 Zuul 混搭；



**跨域**

​        在 Zuul 里增加 CorsFilter 过滤器；



**敏感请求头屏蔽**

​        默认情况下，像 Cookie、Set-Cookie 等敏感请求头信息会被 Zuul 屏蔽掉，可以将这些默认屏蔽去掉，当然，也可以添加要屏蔽的请求头；









###Spring Cloud Gateway ⭐**

​        SpringCloud 第二代网关，未来会取代第一代网关 Zuul；基于 Netty、Reactor 以及 WebFlux 构建；



**功能特性**

- 基于 Spring Framework 5，Project Reactor 和 Spring Boot 2.0；
- 动态路由；
- Predicates 和 Filters 作用于特定路由；
- 集成 Hystrix 断路器；
- 集成 Spring Cloud DiscoveryClient；
- 易于编写的 Predicates 和 Filters；
- 限流；
- 路径重写；



**优点**

- 性能强劲：是 Zuul 1.x 的1.6倍；
- 功能强大：内置很多实用功能，如转发、监控、限流等；
- 设计优雅，易扩展；



**缺点**

- 依赖 Netty 与 Webflux，不是 Servlet 编程模型，有一定的适应成本；
- 不能在 Servlet 容器下工作，也不能构建 war 包；
- 不支持 SpringBoot 1.x；



**架构剖析**

​        客户端向 Spring Cloud Gateway 发出请求，然后在 Gateway Handler Mapping 中找到与请求相匹配的路由，将其发送到 Gateway Web Handler，Handler 再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑，然后返回；过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（pre）或之后（post）执行业务逻辑；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXzWW6W4TB01BtkOS8LqcsYjibicUfqpozfPCRbOaDNriaNJvPciauv74wtA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)



**概念**

- Route（路由）：转发规则，包含：ID、目标 URL、Predicate 集合以及 Filter 集合；
- Predicate（谓词）：即 java.util.function.Predicate，使用 Predicate 实现路由的匹配规则；

![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXBWTLKian2ia1W4AkNtMwOTfoApVA4n1ExJuiaMmXsGj9kt9BVqibVxL0iaA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

- Filter（过滤器）：修改请求和响应；过滤器执行

- - 在 Pre 类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等；在 Post 类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等；

  - Order 越小越靠前执行；

  - 过滤器工厂的 Order 按配置顺序从1开始递增（filters 下的配置顺序）；

  - 如果配置了默认过滤器，则先执行所有配置相同 Order 的过滤器；

  - 如需自行控制 Order，可返回 OrderGatewayFilter；

  - Spring Cloud Gateway 根据作用范围划分为 GatewayFilter 和 GlobalFilter，二者区别如下：

  - - GatewayFilter : 需要通过 spring.cloud.routes.filters 配置在具体路由下，只作用在当前路由上或通过 spring.cloud.default-filters 配置在全局，作用在所有路由上；
    - GlobalFilter : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过 GatewayFilterAdapter 包装成 GatewayFilterChain 可识别的过滤器，它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上；



**监控**

​        添加 Spring Boot Actuator（ spring-boot-starter-actuator ）的依赖，并将

Gateway 端点暴露，即可获得若干监控端点，监控 & 操作 Gateway 的方方面面；



**限流**

​        内置的 RequestRateLimiterGatewayFilterFactory

提供限流的能力，基于令牌桶算法实现，它内置的  RedisRateLimiter ，依赖 Redis 存储限流配置，以及统计数据，当然也可以实现自己的 RateLimiter，只需实现 Gateway 提供的的 RateLimiter 接口，或者继承 Gateway 的 AbstractRateLimiter；



​        漏桶算法：

- 有一个水桶，水桶以一定的速度出水（以一定速率消费请求），当水流速度过大水会溢出（访问速率超过响应速率，就直接拒绝）；

- 漏桶算法的两个变量：

- - 水桶漏洞的大小：rate；
  - 最多可以存多少的水：burst；



​        令牌桶算法：

- 系统按照恒定间隔向水桶里加入令牌（Token），如果桶满了的话，就不加了；
- 每个请求来的时候，会拿走1个令牌，如果没有令牌可拿，那么就拒绝服务；



**跨域**

​        CORS Configuration，Gateway 是支持 CORS 的配置，可以通过不同的 URL 规则匹配不同的 CORS 策略；









###Spring Cloud Hystrix ⭐**

​        服务保护，将服务调用进行隔离，用快速失败来代替排队，阻止级联调用失败（服务雪崩）；



**功能**

- 服务降级：超时降级、资源不足时(线程或信号量)降级，降级后可以配合降级接口返回托底数据，通过 HystrixCommand 注解指定，fallbackMethod 中具体实现降级逻辑，实现优先核心服务，非核心服务不可用或弱可用；

- 依赖隔离：包括线程池隔离和信号量隔离，限制调用分布式服务的资源使用，某一个调用的服务出现问题不会影响其他服务调用，Hystrix 自动实现依赖隔离；

- 服务熔断：CircuitBreaker 断路器，当失败率达到阀值自动触发降级(如因网络故障 / 超时造成的失败率高)，熔断器触发的快速失败会进行快速恢复；

  ![img](https://mmbiz.qpic.cn/mmbiz_png/JyO3eIhDia8PBQK5gdKic9Sq0hJ2LdGbuXMbGtI0CwUPPiaPicM3B4QibT0icAduSrvxF45INibYohvrZCoYzpqKtlQxw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

- - circuitBreaker.sleepWindowInMilliseconds：熔断时间窗口，结束后断路器将进入半开状态并尝试执行一次主逻辑，成功则断路器关闭，失败则断路器打开；

  - circuitBreaker.requestVolumeThreshold：计算失败率的最小请求数；

  - circuitBreaker.errorThresholdPercentage：失败率；

  - 熔断器处理流程：

  - - 开始时断路器处于关闭状态（Closed）；
    - 如果调用持续出错、超时或失败率超过一定限制，断路器打开进入熔断状态，后续一段时间内的所有请求都会被直接拒绝；
    - 一段时间以后，保护器会尝试进入半熔断状态（Half-Open），允许少量请求进来尝试；如果调用仍然失败，则回到熔断状态，如果调用成功，则回到电路闭合状态；

- 缓存：提供了请求缓存、请求合并实现；

- 监控（Hystrix Dashboard）









**Spring Cloud Sleuth**

​        链路监控，是一个分布式跟踪解决方案，在整个分布式系统中能跟踪一个用户请求的过程（包括数据采集，数据传输，数据存储，数据分析，数据可视化），捕获这些跟踪数据，就能构建微服务的整个调用链的视图，就能调试和监控微服务；



**概念**

- span（跨度）：基本工作单元，它用一个64位的 id 唯一标识，除 id 外，span 还包含其他数据，例如描述、时间戳事件、键值对的注解（标签）、spanID、span 父 ID 等；

- trace（跟踪）：一组 span 组成的树状结构；

- Annotation（标注）：

- - CS（Client Sent 客户端发送）：客户端发起一个请求，该 annotation 描述了 span 的开始；
  - SR（Server Received 服务器端接收）：服务器端获得请求并准备处理它；
  - SS（Server Sent 服务器端发送）：该 annotation 表明完成请求处理（当响应发回客户端时）；
  - CR（Client Received 客户端接收）：span 结束的标识，客户端成功接收到服务器端的响应；



**抽样收集**

​        默认情况下，Sleuth 会使用 

PercentageBasedSampler 实现的抽样策略，以请求百分比的方式配置和收集跟踪信息，默认值0.1（代表收集 10% 的请求跟踪信息），可以通过配置 spring.sleuth.sampler 来修改收集的百分比；



**与 ELK 整合**

​        由于日志文件都分布在各个服务实例的文件系统上，如果链路上服务比较多，查看日志文件定位问题是一件非常麻烦的事情，所以需要一些工具来帮忙集中收集、存储和搜素这些跟踪信息。引入基于日志的分析系统，比如ELK平台，SpringCloudSleuth 在与 ELK 平台整合使用时，实际上只需要与负责日志收集的 Logstash 完成数据对接即可，所以需要为 logstash 准备 JSON 格式的日志输出（SpringBoot 应用默认使用 logback 来记录日志，而 logstash 自身也有对 logback 日志工具支持）；

ELK 搭建可参考：

[        ELK+Kafka 日志采集分析平台（一）](http://mp.weixin.qq.com/s?__biz=MzU2ODU1NDA3Mg==&mid=2247484239&idx=1&sn=330a1f9a0c2822c1fdc9f7f4c41c8d7a&chksm=fc8d6478cbfaed6e58fa49f6b3d19ffc12ce1455fe177a432f801d97dc30079c1c763cba07ba&scene=21#wechat_redirect)

[        ELK+Kafka 日志采集分析平台（二）](http://mp.weixin.qq.com/s?__biz=MzU2ODU1NDA3Mg==&mid=2247484264&idx=1&sn=37e72d2b08d0f6642f9d534faf34e5a9&chksm=fc8d645fcbfaed49f4708a7581692e7d873f7b2cd21b83d19ddb2d55ed5cc1f22fda7be61881&scene=21#wechat_redirect)









###Zipkin**

​        Twitter 开源的分布式跟踪系统，主要用来收集系统的时序数据，从而跟踪系统的调用问题；

​        Sleuth 搭配 Zipkin 控制台使用构建分布式追踪系统，核心步骤：数据采集，数据存储，

查询展示；

​        持久化：

- 数据存储：MySQL、Elasticsearch、Cassandra；
- 持久化后需使用 Zipkin Dependencies 才能进行依赖关系的查看；















