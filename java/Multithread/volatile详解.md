## 问题：

1+1 ！= 2

 a + 1 = ？

线程操作变量是先读取存入缓存，操作后再写回内存，多线程会导致缓存不一致

### 解决：

#### 1.在总线加LOCK#锁的方式

​	缺点：锁住总线期间，其他CPU无法访问内存，导致效率低下

#### 2.缓存一致性协议

​	如果操作的变量是共享变量，则通知其他CPU缓存无效，操作时先从内存重新读取

## volatile作用

​	volatile可以禁止指令的重排序优化，

​	保证多线程访问共享变量的内存可见性

## volatile使用场景

​	状态标记量

​	double check 单例双重检查

## volatile原理

### java内存模型：

规定所有的变量都是**存在主存**当中，线程对变量的**操作在缓存**中进行，每个线程不能访问其他线程的缓存

### 1.内存可见性

对共享变量操作立即可见性，线程修改变量，立即同步到内存，并**通知其他线程**缓存中的值无效，其他线程需要重新到内存中读取

volatile底层通过插入内存屏障实现：

1. 每个volatile写操作前面插入一个StoreStore屏障，保证在写之前，普通写操作已经刷新到主存
2. 每个volatile写操作后面插入一个StoreLoad屏障，避免volatile写与后面的volatile读/写操作重排序
3. 每个volatile读操作前面插入一个LoadLoad屏障，禁止处理器把volatile读与普通读操作重排序
4. 每个volatile读操作后面插入一个LoadStore屏障，禁止处理器把volatile读与普通写重排序

如何通知：

> 实现缓存一致性协议，每个处理器通过嗅探在总线上传播的数据来检查自己缓存的值是不是过期了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，当处理器对这个数据进行修改操作的时候，会重新从系统内存中把数据读到处理器缓存里



### 2.禁止指令重排序

如何禁止指令重排序：

1. 加入volatile关键字会多出一个Lock指令前缀
2. volatile使用成本比synchronized低，不会引起线程上下文的切换和调度
3. 内存可见性，某个线程对volatile变量更新，其他线程立刻可见



## 补充：

### volatile无法保证原子性

原子性：一个操作或者多个操作 要么全部执行并且执行的过程不会被任何因素打断，要么就都不执行。例子：转账两个操作，A减100，B加100

volatile只保证可见性，要原子性可以通过：synchronized，Lock，AtomicInteger

​	atomic是利用CAS来实现原子性操作的（CAS实际上是利用处理器提供的CMPXCHG指令实现的，而处理器执行CMPXCHG指令是一个原子性操作）

### 可见性

​	除了volatile，synchronized和锁都可以保证内存可见性



### 有序性：程序按代码先后顺序执行

​	由于volatile 能禁止指令重排序，所以能一定程度保证有序性

java内存模型，允许编译器在保证执行结果不变对指令重排序，多线程要禁止。

那么jvm内部如何保证有序性？happens-before原则！由happens-before推导出的操作不允许重排序

### happens-before原则

1、如果一个操作happens-before另一个操作，那第一个操作执行结果将对第二个操作可见

而且第一个操作执行顺序在第二个之前

2、两个操作存在happens-before关系，并不一定按照happens-before原则的顺序执行。如果重排序结果一致，这种重排序并不非法

> 程序次序规则：单线程按照代码顺序执行
>
> 锁定规则：对于同一个锁，必须先执行unlock才能进行lock
>
> volatile变量规则：volatile变量，一个线程写，另一个线程读，写操作一点happens-before读操作
>
> 传递规则：A操作先行于B，B操作先行于C，则A先行于C
>
> 线程启动规则：Thread对象的start（）方法，先行于此线程所有动作
>
> 线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
>
> 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
>
> 对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始



## 单利模式

单例七种写法 <https://blog.csdn.net/xlgen157387/article/details/78310385>

懒汉式：无法保住线程安全

```java
public class Singleton {
    private static Singleton singleton;
    private Singleton(){}
    public static Singleton getInstance(){
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }}
```

那如何解决呢？加锁！

```java
public class Singleton {
    private static Singleton singleton;
    private Singleton(){}
    public static synchronized Singleton getInstance(){
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }}
```

synchronized 是重量级锁，如此会影响程序性能，怎么办？**双重校验**？

这看起来没毛病，深入分析看看

```java
public class Singleton {
    private static Singleton singleton;
    private Singleton(){}
    public static Singleton getInstance(){
        if(singleton == null){                              
            synchronized (Singleton.class){                 
                if(singleton == null){                      
                    singleton = new Singleton();            
                }
            }
        }
        return singleton;
    }}
```

对象创建过程：1.分配内存，2.初始化对象，3.对象引用指向内存地址

如果发生重排序，可能步骤3先于2发生。那么在第7行代码，判断就会出错



所以，我们需要**禁止重排序**

**1 volatile：禁止重排序**

```java
public class Singleton {
    //通过volatile 避免创建和赋值重排序
    private volatile static Singleton singleton;
    private Singleton(){}
    public static Singleton getInstance(){
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }}
```

**2 JVM 类加载器**

Java语言规定，对于每一个类或者接口C,都有一个唯一的初始化锁LC与之相对应。从C到LC的映射，由JVM的具体实现去自由实现。JVM在类初始化阶段期间会获取这个初始化锁，并且每一个线程至少获取一次锁来确保这个类已经被初始化过了。

利用**classloder机制，**JVM在类初始化阶段会获取一个锁，这个锁可以同步多个线程对同一个类的初始化。

```java
public class Singleton {
    private static class SingletonHolder{
        public static Singleton singleton = new Singleton();
    }
    public static Singleton getInstance(){
        return SingletonHolder.singleton;
    }}
```

























