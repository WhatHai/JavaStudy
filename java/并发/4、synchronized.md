

![Synchronized 关键字使用、底层原理、JDK1.6 之后的底层优化以及 和ReenTrantLock 的对比](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/Java%20%E7%A8%8B%E5%BA%8F%E5%91%98%E5%BF%85%E5%A4%87%EF%BC%9A%E5%B9%B6%E5%8F%91%E7%9F%A5%E8%AF%86%E7%B3%BB%E7%BB%9F%E6%80%BB%E7%BB%93/%E4%BA%8C%20%20Synchronized%20%E5%85%B3%E9%94%AE%E5%AD%97%E4%BD%BF%E7%94%A8%E3%80%81%E5%BA%95%E5%B1%82%E5%8E%9F%E7%90%86%E3%80%81JDK1.6%20%E4%B9%8B%E5%90%8E%E7%9A%84%E5%BA%95%E5%B1%82%E4%BC%98%E5%8C%96%E4%BB%A5%E5%8F%8A%20%E5%92%8CReenTrantLock%20%E7%9A%84%E5%AF%B9%E6%AF%94.png)

## 简介

一种锁，保证同一时刻只有一个方法进入到临界区，保证共享变量的内存可见性和排他性





##  用法

**1、修饰实例方法**

作用于**当前对象实例加锁**，进入同步代码前要获得当前对象实例的锁

**2、修饰静态方法**

作用于**当前类对象加锁**，进入同步代码前要获得当前类对象的锁 ，会作用于类的所有对象实例。

所以如果一个线程A调用一个实例对象的非静态 synchronized 方法，而线程B需要调用这个实例对象所属类的静态 synchronized 方法，是允许的，不会发生互斥现象，**因为访问静态 synchronized 方法占用的锁是当前类的锁，而访问非静态 synchronized 方法占用的锁是当前实例对象锁**。

**3、修饰代码块**

**指定加锁对象，对给定对象加锁，进入同步代码库前要获得给定对象的锁。** 和 synchronized 方法一样，synchronized(this)代码块也是锁定当前对象的。synchronized 关键字加到 static 静态方法和 synchronized(class)代码块上都是是给 Class 类上锁。这里再提一下：synchronized关键字加到非 static 静态方法上是给对象实例上锁。另外需要注意的是：尽量不要使用 synchronized(String a) 因为JVM中，字符串常量池具有缓冲功能！



**双重校验锁实现对象单例（线程安全）**

```java
public class Singleton {

    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
       //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {
            //类对象加锁
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
```
另外，需要注意 uniqueInstance 采用 **volatile** 关键字修饰也是很有必要。

uniqueInstance 采用 volatile 关键字修饰也是很有必要的， uniqueInstance = new Singleton(); 这段代码其实是分为三步执行：

1. **为 uniqueInstance 分配内存空间**
2. **初始化 uniqueInstance**
3. **将 uniqueInstance 指向分配的内存地址**

但是由于 JVM 具有指令重排的特性，执行顺序有可能变成 1->3->2。指令重排在单线程环境下不会出现问题，但是在多线程环境下会导致一个线程获得还没有初始化的实例。例如，线程 T1 执行了 1 和 3，此时 T2 调用 getUniqueInstance() 后发现 uniqueInstance 不为空，因此返回 uniqueInstance，但此时 uniqueInstance 还未被初始化。

使用 volatile 可以**禁止 JVM 的指令重排**，保证在多线程环境下也能正常运行。



## Synchronized锁重入

当一个线程已经得到一个对象的锁，执行代码的时候再次请求这个对象锁，如果是重入锁，可以请求成功，否则线程阻塞

例如：一个Synchronized修饰的方法或代码块的内部调用本类的其他Synchronized修饰的方法或代码块时，是永远可以得到锁的

可重入锁原理 http://www.cnblogs.com/pureEve/p/6421273.html 



## **JVM 层原理**

### **① 修饰同步语句块的情况**

```java
public class SynchronizedDemo {
	public void method() {
		synchronized (this) {
			System.out.println("synchronized 代码块");
		}
	}
}
```

通过 JDK 自带的 javap 命令查看 SynchronizedDemo 类的相关字节码信息：首先切换到类的对应目录执行 `javac SynchronizedDemo.java` 命令生成编译后的 .class 文件，然后执行`javap -c -s -v -l SynchronizedDemo.class`。

![synchronized 关键字原理](https://images.gitbook.cn/abc37c80-d21d-11e8-aab3-09d30029e0d5)

从上面我们可以看出：

**synchronized 同步语句块的实现使用的是 monitorenter 和 monitorexit 指令，其中 monitorenter 指令指向同步代码块的开始位置，monitorexit 指令则指明同步代码块的结束位置。** **当minitor被持有后，处于锁定状态**

当执行 monitorenter 指令时，线程试图获取锁也就是获取 monitor 的持有权.当计数器为0则可以成功获取，获取后将锁计数器设为1也就是加1。相应的在执行 monitorexit 指令后，将锁计数器设为0，表明锁被释放。如果获取对象锁失败，那当前线程就要阻塞等待，直到锁被另外一个线程释放为止。

> monitor对象存在于每个Java对象的对象头中，synchronized 锁便是通过这种方式获取锁的，也是为什么Java中任意对象可以作为锁的原因

#### 对象头

JVM创建对象时在对象前面添加，普通占2个机器码，数组对象占3个（32位的机器码为32bit），对象头包括MarkWord，KlassPointer，Fields

##### MarkWord：

对象储存自身运行时数据，实现轻量级锁和偏向锁的关键

在轻量级锁中，Mark Word被分成两部分，刚开始时**LockWord**为被设置为HashCode和对象分代年龄，最低三位表示锁的标志位，初始状态为001表示无锁状态

##### Monitor Record：

线程私有数据结构，每个线程都有一个可用的Monitor Record列表和一个全局可用列表。

作用：关联被锁住对象的对象头

##### 线程如何锁住对象：

1、通过膨胀一个处于无锁状态（状态位001）的对象获得该对象的锁；

2、对象已经处于膨胀状态（状态位00）但LockWord指向的monitor record的Owner字段为NULL，则可以直接通过CAS原子指令尝试将Owner设置为自己的标识来获得锁。



### **② 修饰方法的的情况**

```java
public class SynchronizedDemo2 {
	public synchronized void method() {
		System.out.println("synchronized 方法");
	}
}
```

![synchronized 关键字原理](https://images.gitbook.cn/7d407bf0-d21e-11e8-b2d6-1188c7e0dd7e)

synchronized 修饰方法用的是 ACC_SYNCHRONIZED 标识，该标识指明了该方法是一个同步方法，JVM 通过该 ACC_SYNCHRONIZED 访问标志来辨别一个方法是否声明为同步方法，从而执行相应的同步调用。

>
> 在 Java 早期版本中，synchronized 属于重量级锁，效率低下，因为监视器锁（monitor）是依赖于底层的操作系统的 Mutex Lock 来实现的，Java 的线程是映射到操作系统的原生线程之上的。如果要挂起或者唤醒一个线程，都需要操作系统帮忙完成，而操作系统实现线程之间的切换时需要从用户态转换到内核态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高，这也是为什么早期的 synchronized 效率低的原因。庆幸的是在 Java 6 之后 Java 官方对从 JVM 层面对synchronized 较大优化，所以现在的 synchronized 锁效率也优化得很不错了。JDK1.6对锁的实现引入了大量的优化，如自旋锁、适应性自旋锁、锁消除、锁粗化、偏向锁、轻量级锁等技术来减少锁操作的开销。
>


##  JDK1.6 之后的底层优化

JDK1.6 对锁的实现引入了大量的优化，如偏向锁、轻量级锁、自旋锁、适应性自旋锁、锁消除、锁粗化等技术来减少锁操作的开销。

锁主要存在四中状态，依次是：**无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态**，他们会随着竞争的激烈而逐渐升级。注意锁可以升级不可降级，这种策略是为了提高获得锁和释放锁的效率。

### **①偏向锁**

如果一个线程获得了锁，那么锁就进入了偏向模式。如果该锁没有被其他线程获取，当这个线程**再次请求锁**时，无需再做任何同步操作

是**为了在无锁竞争的情况下避免在锁获取过程中执行不必要的CAS原子指令**，因为CAS原子指令虽然相对于重量级锁来说开销比较小但还是存在非常可观的本地延迟

获取过程：

- 当一个线程访问同步块并获取锁时，会在**对象头和栈帧**中的锁记录里存储锁偏向的线程ID，以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，只需简单地测试一下对象头的**Mark Word**里是否存储着指向当前线程的偏向锁；

- 如果测试成功，表示线程已经获得了锁。如果测试失败，则需要再测试一下Mark Word中偏向锁的标识是否设置成1（表示当前是偏向锁）；

- 如果没有设置，则使用CAS竞争锁；

- 如果设置了，则尝试使用CAS将对象头的偏向锁指向当前线程。


**引入偏向锁的目的和引入轻量级锁的目的很像，他们都是为了没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗。**

**但是不同是：轻量级锁在无竞争的情况下使用 CAS 操作去代替使用互斥量。而偏向锁在无竞争的情况下会把整个同步都消除掉**。

在锁竞争较激烈的场景，可能每次不同的线程来请求相同的锁，偏向锁就会失效。这种情况可选择**关闭偏向锁**

如果偏向锁失败，虚拟机并不会立即挂起线程，而是使用**轻量级锁**进行操作

### **② 轻量级锁**

倘若偏向锁失败，虚拟机并不会立即升级为重量级锁，它还会尝试使用一种称为轻量级锁的优化手段(1.6之后加入的)。**轻量级锁不是为了代替重量级锁，它的本意是在没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗，因为使用轻量级锁时，不需要申请互斥量。另外，轻量级锁的加锁和解锁都用到了CAS操作。** 关于轻量级锁的加锁和解锁的原理可以查看《深入理解Java虚拟机：JVM高级特性与最佳实践》第二版的13章第三节锁优化。

**轻量级锁能够提升程序同步性能的依据是“对于绝大部分锁，在整个同步周期内都是不存在竞争的”，这是一个经验数据。如果没有竞争，轻量级锁使用 CAS 操作避免了使用互斥操作的开销。但如果存在锁竞争，除了互斥量开销外，还会额外发生CAS操作，因此在有锁竞争的情况下，轻量级锁比传统的重量级锁更慢！如果锁竞争激烈，那么轻量级将很快膨胀为重量级锁！**

### **③  自旋锁和自适应自旋**

轻量级锁就会膨胀为重量级锁后，虚拟机为了避免线程真实的在操作系统层面挂起，会有自旋锁

互斥同步对性能最大的影响就是阻塞的实现，因为挂起线程/恢复线程的操作都需要转入内核态中完成（用户态转换到内核态会耗费时间）。

背景：某些对象锁状态只会持续很短时间，频繁阻塞和唤醒线程很浪费资源。为了让一个线程等待，我们只需要让线程执行一个忙循环（自旋），这项技术就叫做自旋

适用场景：1.多核处理器，2.等所时间很短，比两次上下文切换时间还少

实现：**CAS机制**

并发时，互斥锁让其他线程阻塞；自旋锁让线程循环等待一段时间（jdk1.6默认自选10次），不会立刻挂起，属于轻量级乐观锁；自选锁是**不公平锁**，无法保证等待时间最长的线程优先获得锁，可能会导致线程饥饿

自旋锁在 JDK1.6 之前其实就已经引入了，不过是默认关闭的，需要通过`--XX:+UseSpinning`参数来开启。JDK1.6及1.6之后，就改为默认开启的了。

需要注意的是：自旋等待不能完全替代阻塞，因为它还是要占用处理器时间。如果锁被占用的时间短，那么效果当然就很好了！反之，相反！自旋等待的时间必须要有限度。如果自旋超过了限定次数任然没有获得锁，就应该挂起线程。**自旋次数的默认值是10次，用户可以修改`--XX:PreBlockSpin`来更改**。

另外,**在 JDK1.6 中引入了自适应的自旋锁。自适应的自旋锁带来的改进就是：自旋的时间不在固定了，而是和前一次同一个锁上的自旋时间以及锁的拥有者的状态来决定，虚拟机变得越来越“聪明”了**。

**适应自旋锁**

自旋次数不是固定的，由前一次在同一个锁上的自旋时间及锁的拥有者状态来决定。

如果上一次自旋成功，下一次自旋次数增加；如果某个锁很少自选成功，那之后需要这个锁的时候自选次数会减少

### **④ 锁消除**

当不存在共享数据竞争，JVM会对锁进行消除。消除依据是逃逸分析

场景如下：操作StringBuffer、Vector、HashTable等，会存在隐形的加锁操作。

JVM检测变量vector没有逃逸出vectorTest（）方法，就会消除vector内部加的锁

```java
public void test(){
    Vector<string> vector = new Vector<String>();
    for(int i = 0;i < 10;i++){
        vector.add(i + "");
    }
}
```



### **⑤ 锁粗化**

原则上，我们在编写代码的时候，总是推荐将同步块的作用范围限制得尽量小，——直在共享数据的实际作用域才进行同步，这样是为了使得需要同步的操作数量尽可能变小，如果存在锁竞争，那等待线程也能尽快拿到锁。

大部分情况下，上面的原则都是没有问题的，但是如果一系列的连续操作都对同一个对象**反复加锁和解锁**，那么会带来很多不必要的性能消耗。

##  Synchronized 和 ReenTrantLock 的对比


**① 两者都是可重入锁**

两者都是可重入锁。“可重入锁”概念是：自己可以再次获取自己的内部锁。比如一个线程获得了某个对象的锁，此时这个对象锁还没有释放，当其再次想要获取这个对象的锁的时候还是可以获取的，**如果不可锁重入的话，就会造成死锁**。同一个线程每次获取锁，锁的计数器都自增1，所以要等到锁的计数器下降为0时才能释放锁。

**② synchronized 依赖于 JVM 而 ReenTrantLock 依赖于 API**

synchronized 是依赖于 JVM 实现的，前面我们也讲到了 虚拟机团队在 JDK1.6 为 synchronized 关键字进行了很多优化，但是这些优化都是在虚拟机层面实现的，并没有直接暴露给我们。ReenTrantLock 是 JDK 层面实现的（也就是 API 层面，需要 lock() 和 unlock 方法配合 try/finally 语句块来完成），所以我们可以通过查看它的源代码，来看它是如何实现的。

**③ ReenTrantLock 比 synchronized 增加了一些高级功能**

相比synchronized，ReenTrantLock增加了一些高级功能。主要来说主要有三点：**①等待可中断；②可实现公平锁；③可实现选择性通知（锁可以绑定多个条件）**

- **ReenTrantLock提供了一种能够中断等待锁的线程的机制**，通过lock.lockInterruptibly()来实现这个机制。也就是说正在等待的线程可以选择放弃等待，改为处理其他事情。
- **ReenTrantLock可以指定是公平锁还是非公平锁。而synchronized只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。** ReenTrantLock默认情况是非公平的，可以通过 ReenTrantLock类的`ReentrantLock(boolean fair)`构造方法来制定是否是公平的。
- synchronized关键字与wait()和notify/notifyAll()方法相结合可以实现等待/通知机制，ReentrantLock类当然也可以实现，但是需要借助于**Condition**接口与newCondition() 方法。Condition是JDK1.5之后才有的，它具有很好的灵活性，比如可以实现多路通知功能也就是在一个Lock对象中可以创建多个Condition实例（即对象监视器），**线程对象可以注册在指定的Condition中，从而可以有选择性的进行线程通知，在调度线程上更加灵活。 在使用notify/notifyAll()方法进行通知时，被通知的线程是由 JVM 选择的，用ReentrantLock类结合Condition实例可以实现“选择性通知”** ，这个功能非常重要，而且是Condition接口默认提供的。而synchronized关键字就相当于整个Lock对象中只有一个Condition实例，所有的线程都注册在它一个身上。如果执行notifyAll()方法的话就会通知所有处于等待状态的线程这样会造成很大的效率问题，而Condition实例的signalAll()方法 只会唤醒注册在该Condition实例中的所有等待线程。

如果你想使用上述功能，那么选择ReenTrantLock是一个不错的选择。

**④ 性能已不是选择标准**

在JDK1.6之前，synchronized 的性能是比 ReenTrantLock 差很多。具体表示为：synchronized 关键字吞吐量随线程数的增加，下降得非常严重。而ReenTrantLock 基本保持一个比较稳定的水平。我觉得这也侧面反映了， synchronized 关键字还有非常大的优化余地。后续的技术发展也证明了这一点，我们上面也讲了在 JDK1.6 之后 JVM 团队对 synchronized 关键字做了很多优化。**JDK1.6 之后，synchronized 和 ReenTrantLock 的性能基本是持平了。所以网上那些说因为性能才选择 ReenTrantLock 的文章都是错的！JDK1.6之后，性能已经不是选择synchronized和ReenTrantLock的影响因素了！而且虚拟机在未来的性能改进中会更偏向于原生的synchronized，所以还是提倡在synchronized能满足你的需求的情况下，优先考虑使用synchronized关键字来进行同步！优化后的synchronized和ReenTrantLock一样，在很多地方都是用到了CAS操作**。
