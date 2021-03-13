

![spring架构](images/spring架构.jpeg)

## 一、bean实例化

### 1、代码debug

创建spring应用，debug，F7一步步来

![debug](源码/debug.png)

setConfigLocations()：加载外部配置文件



![关键方法refresh](源码/关键方法refresh.png)

关键方法refresh（）：





## springboot

![springboot架构](源码/springboot架构.jpeg)

springbootApplication debug

springApplication构造方法：

通过堆栈信息匹配main方法，推断主类的class

![springApplication构造方法](源码/springApplication构造方法.png)

执行run()方法：

1、准备工作

- 设置启动时间；
- 创建上下文对象；
- 创建异常报告器；
- 获取监听器并启动；
- 设置环境参数对象ApplicationArguments；



![springApplication的run方法](源码/springApplication的run方法.png)

2、prepareContext方法：准备上下文

给上下文设置具体属性值：

- 环境对象；
- 初始化参数；
- 发布监听事件
- 创建对象工厂



![prepareContext方法](源码/prepareContext方法.png)



load方法：

加载bean到上下文。

![load方法](源码/load方法.png)



3、refreshContex方法：刷新容器

调用spring的refresh方法















