### javaConfig

用纯java方式配置spring容器，避免xml的使用

优点：

​	面向对象，配置类可以继承另一个，并重写@Bean方法

​	减少XML使用



### 怎么禁用某些自动配置特性？

如果我们想禁用某些自动配置特性，可以使用 ==@EnableAutoConfiguration==注解的==exclude==属性来指明。例如，下面的代码段是使 *DataSourceAutoConfiguration* 无效：

```
// other annotations
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class MyConfiguration { }
复制代码
```

如果我们使用 *@SpringBootApplication* 注解 — 那个将 *@EnableAutoConfiguration* 作为元注解的项，来启用自动化配置，我们能够使用相同名字的属性来禁用自动化配置：

```
// other annotations
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MyConfiguration { }
复制代码
```

我们也能够使用 *spring.autoconfigure.exclude* 环境属性来禁用自动化配置。*application.properties* 中的这项配置能够像以前那样做同样的事情：

```
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
复制代码
```

### 怎么注册一个定制的自动化配置？

为了注册一个自动化配置类，我们必须在 *META-INF/spring.factories* 文件中的    *EnableAutoConfiguration* 键下列出它的全限定名：

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.baeldung.autoconfigure.CustomAutoConfiguration
```

如果我们使用 Maven 构建项目，这个文件需要放置在在 package 阶段被写入完成的 *resources/META-INF* 目录中。

### 当 bean 存在的时候怎么置后执行自动配置？

为了当 bean 已存在的时候通知自动配置类置后执行，我们可以使用 ==@ConditionalOnMissingBean==注解。这个注解中最值得注意的属性是：

- value：被检查的 beans 的类型
- name：被检查的 beans 的名字

当将 *@Bean* 修饰到方法时，目标类型默认为方法的返回类型：

```
@Configuration
public class CustomConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CustomService service() { ... }
}
```

### 怎么将 SpringBoot web 应用程序部署为 JAR 或 WAR 文件？

Spring 通过提供插件来解决这个问题，也就是 *spring-boot-maven-plugin* 来打包 web 应用程序到一个额外的 JAR 文件当中。为了引入这个插件，只需要在 *pom.xml* 中添加一个 *plugin* 属性：

```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
复制代码
```

有了这个插件，我们会在执行 *package* 步骤后得到一个 JAR 包。这个 JAR 包包含所需的所有依赖以及一个嵌入的服务器。因此，我们不再需要担心去配置一个额外的服务器了。

我们能够通过运行一个普通的 JAR 包来启动应用程序。

注意一点，为了打包成 JAR 文件，*pom.xml* 中的 *packgaing*  属性必须定义为 *jar*：

```
<packaging>jar</packaging>
复制代码
```

如果我们不定义这个元素，它的默认值也为 *jar*。

如果我们想构建一个 WAR 文件，将 *packaging* 元素修改为 *war*：

```
<packaging>war</packaging>
复制代码
```

并且将容器依赖从打包文件中移除：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```

执行 Maven 的 *package* 步骤之后，我们得到一个可部署的 WAR 文件。

### 有什么外部配置的可能来源？

SpringBoot 对外部配置提供了支持，允许我们在不同环境中运行相同的应用。我们可以使用 properties 文件、YAML 文件、环境变量、系统参数和命令行选项参数来声明配置属性。

然后我们可以通过 *@Value* 这个通过 *@ConfigurationProperties* 绑定的对象的注解或者实现 *Enviroment* 来访问这些属性。

以下是最常用的外部配置来源：

- 命令行属性：命令行选项参数是以双连字符（例如，=）开头的程序参数，例如 *–server.port=8080*。SpringBoot将所有参数转换为属性并且添加到环境属性当中。
- 应用属性：应用属性是指那些从 *application.properties* 文件或者其 YAML 副本中获得的属性。默认情况下，SpringBoot会从当前目录、classpath 根目录或者它们自身的 *config* 子目录下搜索该文件。
- 特定 *profile* 配置：特殊概要配置是从 *application-{profile}.properties* 文件或者自身的 YAML 副本。*{profile}* 占位符引用一个在用的 *profile*。这些文件与非特定配置文件位于相同的位置，并且优先于它们。

### SpringBoot 支持松绑定

SpringBoot中的松绑定适用于配置属性的类型安全绑定。使用松绑定，环境属性的键不需要与属性名完全匹配。这样就可以用驼峰式、短横线式、蛇形式或者下划线分割来命名。

例如，在一个有 ==@ConfigurationProperties== 声明的 bean 类中带有一个名为 *myProp* 的属性，它可以绑定到以下任何一个参数中，*myProp*、 *my-prop*、*my_prop* 或者  *MY_PROP*。



### 怎么编写一个集成测试？

当我们使用 Spring 应用去跑一个集成测试时，我们需要一个 ==ApplicationContext==。

为了使我们开发更简单，SpringBoot 为测试提供一个注解 – *@SpringBootTest*。这个注释由其 classes 属性指示的配置类创建一个 *ApplicationContext*。

**如果没有配置 classes 属性，SpringBoot 将会搜索主配置类**。搜索会从包含测试类的包开始直到找到一个使用 *@SpringBootApplication* 或者 *@SpringBootConfiguration* 的类为止。

注意如果使用 JUnit4，我们必须使用 *@RunWith(SpringRunner.class)* 来修饰这个测试类。



### spring-boot-starter-parent 有什么用 ?

我们都知道，新创建一个 Spring Boot 项目，默认都是有 parent 的，这个 parent 就是 spring-boot-starter-parent ，spring-boot-starter-parent 主要有如下作用：

1. 定义了 Java 编译版本为 1.8 。
2. 使用 UTF-8 格式编码。
3. 继承自 spring-boot-dependencies，这个里边定义了依赖的版本，也正是因为继承了这个依赖，所以我们在写依赖时才不需要写版本号。
4. 执行打包操作的配置。
5. 自动化的资源过滤。
6. 自动化的插件配置。
7. 针对 application.properties 和 application.yml 的资源过滤，包括通过 profile 定义的不同环境的配置文件，例如 application-dev.properties 和 application-dev.yml。

关于这个问题，读者可以参考：[你真的理解 Spring Boot 项目中的 parent 吗？](https://mp.weixin.qq.com/s/2w6B4fMdbTK_mGjnaMG4BQ)

### springboot异常处理

实现ControlerAdvice 类来处理控制器抛出的所有异常



###Spring Boot 中如何解决跨域问题 ?

跨域可以在前端通过 JSONP 来解决，但是 JSONP 只可以发送 GET 请求，无法发送其他类型的请求，在 RESTful 风格的应用中，就显得非常鸡肋，因此我们推荐在后端通过 （CORS，Cross-origin resource sharing） 来解决跨域问题。这种解决方案并非 Spring Boot 特有的，在传统的 SSM 框架中，就可以通过 CORS 来解决跨域问题，只不过之前我们是在 XML 文件中配置 CORS ，现在则是通过 @CrossOrigin 注解来解决跨域问题。关于 CORS ，小伙伴们可以参考：[Spring Boot 中通过 CORS 解决跨域问题](https://mp.weixin.qq.com/s/ASEJwiswLu1UCRE-e2twYQ)

定义一个配置类，继承WebMvcConfigurer，配置CorsFilter过滤器，注入容器中

在启动类上添加

###微服务中如何实现 session 共享 ?

在微服务中，一个完整的项目被拆分成多个不相同的独立的服务，各个服务独立部署在不同的服务器上，各自的 session 被从物理空间上隔离开了，但是经常，我们需要在不同微服务之间共享 session 

常见的方案就是 ==Spring Session + Redis== 来实现 session 共享。将所有微服务的 session 统一保存在 Redis 上，当各个微服务对 session 有相关的读写操作时，都去操作 Redis 上的 session 。这样就实现了 session 共享，Spring Session 基于 Spring 中的代理过滤器实现，使得 session 的同步操作对开发人员而言是透明的，非常简便。 session 共享大家可以参考：[Spring Boot 一个依赖搞定 session 共享，没有比这更简单的方案了！](https://mp.weixin.qq.com/s/xs67SzSkMLz6-HgZVxTDFw)



### 微服务中数据库一致性

分布式事务

### ？？微服务同时调用多个接口，怎么支持事务

可以springboot集成Automikos，使用分布式事务会增加响应时间

消息补偿机制来处理分布式事务

### 无状态服务设计

如果一个数据要被多个服务共享才能完成，此数据被称为状态，依赖此数据的服务为有状态的服务

例如，将数据缓存，session缓存存储到分布式缓存中



### springcache缓存注解

@Cacheable，声明方法是可缓存的，将结果存到缓存中

@Cacheput，每次执行方法将结果存入缓存

@CacheEvict，清除缓存

### SpringBoot DevTools 的用途是什么？

SpringBoot 开发者工具，或者说 DevTools，是一系列可以让开发过程变得简便的工具。为了引入这些工具，我们只需要在 *POM.xml* 中添加如下依赖：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
复制代码
```

*spring-boot-devtools* 模块在生产环境中是默认禁用的，archives 的 repackage 在这个模块中默认也被排除。因此，它不会给我们的生产环境带来任何开销。

通常来说，DevTools 应用属性适合于开发环境。这些属性禁用模板缓存，启用 web 组的调试日志记录等等。最后，我们可以在不设置任何属性的情况下进行合理的开发环境配置。

每当 classpath 上的文件发生更改时，使用 DevTools 的应用程序都会重新启动。这在开发中非常有用，因为它可以为修改提供快速的反馈。

默认情况下，像视图模板这样的静态资源修改后是不会被重启的。相反，资源的更改会触发浏览器刷新。注意，只有在浏览器中安装了 LiveReload 扩展并以与 DevTools 所包含的嵌入式 LiveReload 服务器交互时，才会发生。



###Spring Boot 如何实现热部署 ?

引入 devtools 依赖即可，这样当编译文件发生变化时，Spring Boot 就会自动重启。

在 Eclipse 中，用户按下保存按键，就会自动编译进而重启 Spring Boot，IDEA 中由于是自动保存的，自动保存时并未编译，所以需要开发者按下 Ctrl+F9 进行编译，编译完成后，项目就自动重启了。

###Spring Boot 中如何实现定时任务 ?

使用 Spring 中的定时任务主要通过 ==@Scheduled== 注解来实现。

使用 Quartz ，则按照 Quartz 的方式，定义 Job 和 Trigger 即可。

关于定时任务这一块，大家可以参考：[Spring Boot 中实现定时任务的两种方式!](https://mp.weixin.qq.com/s/_20RYBkjKrB4tdpXI3hBOA)

###Spring Boot 使用 XML 配置 ?

Spring Boot 推荐使用 Java 配置而非 XML 配置，但是 Spring Boot 中也可以使用 XML 配置，通过 ==@ImportResource== 注解可以引入一个 XML 配置。

### 多切面如何指定优先级

`@Order(i)`注解来指定优先级，



###springboot自动配置

@springbootapplication注解，是有@Configuration、@ComponentScan、@EnableAutoConfiguration注解组成

@EnableAutoConfiguration注解是通过@Import注解导入配置信息到selector类

将配置信息类加载到ioc容器



### springboot stater的工作原理

启动时会去依赖的starter 包中寻找配置文件，根据配置文件扫描项目依赖的jar包

加载Autoconfig类

根据@conditional注解进行自动配置并注入bean

### 自定义starter

https://mp.weixin.qq.com/s/EZzfczJ6vL0yS36wJokV7A

### springboot监视器

Spring boot actuato，监视器可以访问生产环境应用程序的当前状态，



###   禁用Actuator 端点安全性

默认所有敏感的HTTP端点都是安全的，只有具有Actuator 角色的用户才能访问

management.security.enabled = false 可以禁用



###Spring Boot  集成 ActiveMQ

添加spring-boot-starter-activemq 的依赖



### 配置文件属性值映射到javabean

@ConfigurationProperties注解：将类中的所有属性和配置文件中相关的配置进行绑定，指定 prefix 为类名

@Component 加入spring容器

如果只需要注入某项的值，使用@Value注解