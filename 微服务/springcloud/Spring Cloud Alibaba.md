

## Spring Cloud Alibaba简介 

Spring Cloud Alibaba 是由阿里推出的， 致力于提供微服务开发的一站式解决方案。 2018年7月正式开源， 进入 Spring Cloud 孵化器;2019年7月, 从Spring Cloud迁出, 独立维护。 阿里巴巴推出 Spring Cloud Alibaba，很大程度上市希望通过抢占开发者生态，来帮助推广自家的 云产品。
 依赖于阿里巴巴强大的技术影响力， 逐渐成为主流互联网公司， 微服务解决方案的重要选择之 一。 

## Spring Cloud Alibaba主要功能: 

- 服务限流降级
- 服务注册与发现
- 分布式配置管理
- 消息驱动能力
- 分布式事务
- 分布式任务调度
- 阿里云对象存储、短信服务等

## Spring Cloud Alibaba主要组件: 

**Sentinel**:把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务 的稳定性。 

**Nacos**:一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。 

**RocketMQ**:一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高 可靠的消息发布与订阅服务。 

**Dubbo**:Apache Dubbo? 是一款高性能 Java RPC 框架。 

**Seata**:阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。

 **Alibaba Cloud ACM**:一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置 中心产品。

 **Alibaba Cloud OSS**: 阿里云对象存储服务(Object Storage Service，简称 OSS)，是阿里云 提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地 点存储和访问任意类型的数据。

 **Alibaba Cloud SchedulerX**: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精 准、高可靠、高可用的定时(基于 Cron 表达式)任务调度服务。

 **Alibaba Cloud SMS**: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业 迅速搭建客户触达通道。 



## Spring Cloud vs Spring Cloud Alibaba 

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



