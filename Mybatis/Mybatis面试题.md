## 1、#{}和${}的区别是什么？

$

```
${}`将parameterType 传入的内容拼接在sql中且不进行jdbc类型转换，会引发SQL注入
$可以接收简单类型值或属性值，如果传输单个简单类型值，{}可以接收简单类型值或pojo属性值，如果parameterType传输单个简单类型值，${}括号中只能是value
如表名，字段名要动态替换，用$
```

#

```
#{}表示一个占位符号，通过#{}可以实现preparedStatement向占位符中设置值，自动进行java类型和jdbc类型转换，
#{}可以有效防止sql注入，替换的值会自动加上引号。 
井{} 是预编译的，预编译的sql可以重复利用
#{}可以接收简单类型值或pojo属性值。 如果parameterType传输单个简单类型值，#{}括号中可以是value或其它名称。
```

## 2、OGNL表达式

Object Graphic Navigation Language 对象图导航语言，它是按照 `#{对象.对象}` 的语法格式来获取数据的。

例如：#{user.username}它会先去找 user 对象，然后在 user 对象中找到 username 属性，并调用 getUsername()方法把值取出来。

但是我们在 parameterType 属性上指定了实体类名称，所以可以省略 user. 而直接写 #{username}。



## 2、映射文件常见标签中

select|insert|updae|delete；`<resultMap>`、`<parameterMap>`、`<sql>`、`<include>`、`<selectKey>`，加上动态 sql 的 9 个标签，`trim|where|set|foreach|if|choose|when|otherwise|bind`等，

其中`<sql>`为 sql 片段标签，通过`<include>`标签引入 sql 片段，`<selectKey>`为不支持自增的主键生成策略标签。

## 3、Dao 接口

Dao 接口，就是人们常说的 `Mapper`接口，接口的全限名，就是映射文件中的 namespace 的值，接口的方法名，就是映射文件中`MappedStatement`的 id 值，接口方法内的参数，就是传递给 sql 的参数。`Mapper`接口是没有实现类的，当调用接口方法时，接口全限名+方法名拼接字符串作为 key 值，可唯一定位一个`MappedStatement`

举例：`com.mybatis3.mappers.StudentDao.findStudentById`，可以唯一找到 namespace 为`com.mybatis3.mappers.StudentDao`下面`id = findStudentById`的`MappedStatement`。在 Mybatis 中，每一个`<select>`、`<insert>`、`<update>`、`<delete>`标签，都会被解析为一个`MappedStatement`对象。

Dao 接口里的方法不能重载的，因为是通过全限名+方法名寻找映射文件中的sql。

Dao 接口的工作原理

​	是 JDK 动态代理，Mybatis 运行时会使用 JDK 动态代理为 Dao 接口生成代理 proxy 对象，代理对象 proxy 会拦截接口方法，转而执行`MappedStatement`所代表的 sql，然后将 sql 执行结果返回。

## 4、Mybatis 分页

Mybatis 使用 RowBounds 对象进行分页，它是针对 ResultSet 结果集执行的内存分页，而非物理分页，

可以在 sql 内直接书写带有物理分页的参数来完成物理分页功能，

也可以使用分页插件来完成物理分页。

分页插件的基本原理是使用 Mybatis 提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的 sql，然后重写 sql，根据 dialect 方言，添加对应的物理分页语句和物理分页参数。

举例：`select _ from student`，拦截 sql 后重写为：`select t._ from （select \* from student）t limit 0，10`

## 5、Mybatis 的插件原理

答：Mybatis 仅可以编写针对 `ParameterHandler`、`ResultSetHandler`、`StatementHandler`、`Executor` 这 4 种接口的插件，Mybatis 使用 JDK 的动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这 4 种接口对象的方法时，就会进入拦截方法，具体就是 `InvocationHandler` 的 `invoke()`方法，当然，只会拦截那些你指定需要拦截的方法。

自定义插件：

​	实现 Mybatis 的 Interceptor 接口并复写` intercept()`方法，然后在给插件编写注解，指定要拦截哪一个接口的哪些方法即可

​	最后在配置文件中配置你编写的插件。

## 6、Mybatis 执行批量插入，能返回数据库主键列表吗？

答：能，JDBC 都能，Mybatis 当然也能。

​     如何获取自动生成的主键值：配置文件设置  usegeneratedkeys  为 true



**返回新增数据的id**：

​	使用 <selectKey 标签，将数据库id和实体类id关联，标签内写 select last_insert_id() 语句

## 7、动态 sql 

在 Xml 映射文件内，以标签的形式编写动态 sql，完成逻辑判断和动态拼接 sql 的功能

Mybatis 提供了 9 种动态 sql 标签 `trim|where|set|foreach|if|choose|when|otherwise|bind`。

foreach 标签：collection为要遍历的集合；open为语句开始部分；close为语句结束部分；item为每个元素变量名；sperator为分隔符

其执行原理为，使用 OGNL 从 sql 参数对象中计算表达式的值，根据表达式的值动态拼接 sql，以此来完成动态 sql 的功能。

## 8、封装sql 结果为目标对象

第一种是使用`<resultMap>`标签，逐一定义列名和pojo对象属性名之间的映射关系。

第二种是使用 sql 列的别名功能，将列别名书写为对象属性名实现映射。比如 T_NAME AS NAME，对象属性名一般是 name，小写，但是列名不区分大小写，Mybatis 会忽略列名大小写，智能找到与之对应对象属性名，你甚至可以写成 T_NAME AS NaMe，Mybatis 一样可以正常工作。

有了列名与属性名的映射关系后，Mybatis 通过反射创建对象，同时使用反射给对象的属性逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的。

## 8、一对一查询

可以用联和查询和嵌套查询

联合查询：联合多个表，只查询一次，在resultMap中配置association节点，配置从表对应pojo类的属性，

嵌套查询：先查询一个表，根据查询结果的外键id，去查另一个表的数据

## 8、一对多查询

在主表pojo添加从表pojo的集合属性， resultMap中配置 collection 标签节点，配置从表对应pojo类的属性。

property 表示关联查询的结果集存储在 主表pojo对象的上哪个属性。

ofType 表示List属性中的对象类型

## 8、多对多

多对多可以看成双向一对多

需要添加中间表。



## 9、Mybatis 一对多的关联查询

答：能，Mybatis 不仅可以执行一对一、一对多的关联查询，还可以执行多对一，多对多的关联查询，多对一查询，其实就是一对一查询，只需要把 `selectOne()`修改为 `selectList()`即可；多对多查询，其实就是一对多查询，只需要把 `selectOne()`修改为 `selectList()`即可。

关联对象查询，有两种实现方式，一种是单独发送一个 sql 去查询关联对象，赋给主对象，然后返回主对象。另一种是使用嵌套查询，嵌套查询的含义为使用 join 查询，一部分列是 A 对象的属性值，另外一部分列是关联对象 B 的属性值，好处是只发一个 sql 查询，就可以把主对象和其关联对象查出来。

那么问题来了，join 查询出来 100 条记录，如何确定主对象是 5 个，而不是 100 个？其去重复的原理是`<resultMap>`标签内的`<id>`子标签，指定了唯一确定一条记录的 id 列，Mybatis 根据列值来完成 100 条记录的去重复功能，`<id>`可以有多个，代表了联合主键的语意。

同样主对象的关联对象，也是根据这个原理去重复的，尽管一般情况下，只有主对象会有重复记录，关联对象一般不会重复。

举例：下面 join 查询出来 6 条记录，一、二列是 Teacher 对象列，第三列为 Student 对象列，Mybatis 去重复处理后，结果为 1 个老师 6 个学生，而不是 6 个老师 6 个学生。

t_id t_name s_id

| 1 | teacher | 38 | | 1 | teacher | 39 | | 1 | teacher | 40 | | 1 | teacher | 41 | | 1 | teacher | 42 | | 1 | teacher | 43 |

## 10、Mybatis 延迟加载

先从单表查询，需要时再从关联表去关联查询，大大提高数据库性能

Mybatis支持 association 关联对象和 collection 关联集合对象的延迟加载，association 指的就是一对一，collection 指的就是一对多查询

**association**：

​	在resultMap中，添加association 节点

​	property 为关联的属性， javaType为参数类型，select 填要调用的 select 映射的 id，column : 填要传递给 select 映射的参数

```
<association property="user" javaType="user" 
	select="com.itheima.dao.IUserDao.findById" 
	column="uid">
</association>
```

**collection：**

​	collection是用于建立一对多中集合属性的对应关系 

​	ofType 用于指定集合元素的数据类型 

​	select 是用于指定查询账户的唯一标识(账户的 dao 全限定类名加上方法名称) 

​	column 是用于指定使用哪个字段的值作为条件查询 --> 

**配置：**

​	在 Mybatis 配置文件中，可以配置是否启用延迟加载 `lazyLoadingEnabled=true|false。`

------

**注解：**

配置@Results 注解，在@Result中配置 @One或者@Many，然后select的值为要调用的映射的id，最后fetchType 开启延迟加载





它的原理是，使用` CGLIB` 创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用 `a.getB().getName()`，拦截器 `invoke()`方法发现 `a.getB()`是 null 值，那么就会单独发送事先保存好的查询关联 B 对象的 sql，把 B 查询上来，然后调用 a.setB(b)，于是 a 的对象 b 属性就有值了，接着完成 `a.getB().getName()`方法的调用。这就是延迟加载的基本原理。

## 11、不同的 Xml 映射文件，id 是否可以重复？

不同的 Xml 映射文件，如果配置了 namespace，那么 id 可以重复；

如果没有配置 namespace，那么 id 不能重复；毕竟 namespace 不是必须的，只是最佳实践而已。

因为映射文件的statement是存在一个map中， namespace+id 是作为 `Map<String, MappedStatement>`的 key 使用的，如果没有 namespace，就剩下 id，那么，id 重复会导致数据互相覆盖。有了 namespace，自然 id 就可以重复，namespace 不同，namespace+id 自然也就不同。

## 12、Mybatis 批处理

使用 BatchExecutor 完成批处理。

## 13、Mybatis 的 Executor 执行器们

答：Mybatis 有三种基本的 Executor 执行器，**SimpleExecutor、ReuseExecutor、BatchExecutor。**

**`SimpleExecutor`：**每执行一次 update 或 select，就开启一个 Statement 对象，用完立刻关闭 Statement 对象。

**``ReuseExecutor`：**执行 update 或 select，以 sql 作为 key 查找 Statement 对象，存在就使用，不存在就创建，用完后，不关闭 Statement 对象，而是放置于 Map<String, Statement>内，供下一次使用。简言之，就是重复使用 Statement 对象。

**`BatchExecutor`：**执行 update（没有 select，JDBC 批处理不支持 select），将所有 sql 都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个 Statement 对象，每个 Statement 对象都是 addBatch()完毕后，等待逐一执行 executeBatch()批处理。与 JDBC 批处理相同。

作用范围：Executor 的这些特点，都严格限制在 **SqlSession** 生命周期范围内。

## 14、Mybatis 中如何指定使用哪一种 Executor 执行器？

注：我出的

答：在 Mybatis 配置文件中，可以指定默认的 ExecutorType 执行器类型，也可以手动给 `DefaultSqlSessionFactory` 的创建 SqlSession 的方法传递 ExecutorType 类型参数。

## 15、Mybatis 映射 Enum 枚举类

Mybatis 可以映射枚举类，不单可以映射枚举类，Mybatis 可以映射任何对象到表的一列上。

映射方式为自定义一个 `TypeHandler`，实现 `TypeHandler` 的 `setParameter()`和 `getResult()`接口方法。

`TypeHandler` 有两个作用，一是完成从 javaType 至 jdbcType 的转换，二是完成 jdbcType 至 javaType 的转换，体现为 `setParameter()`和 `getResult()`两个方法，分别代表设置 sql 问号占位符参数和获取列查询结果。

## 16、Mybatis 映射文件中，如果 A 标签通过 include 引用了 B 标签的内容，请问，B 标签能否定义在 A 标签的后面，还是说必须定义在 A 标签的前面？

虽然 Mybatis 解析 Xml 映射文件是按照顺序解析的，但是，被引用的 B 标签依然可以定义在任何地方，Mybatis 都可以正确识别。

原理是，Mybatis 解析 A 标签，发现 A 标签引用了 B 标签，但是 B 标签尚未解析到，尚不存在，此时，Mybatis 会将 A 标签标记为未解析状态，然后继续解析余下的标签，包含 B 标签，待所有标签解析完毕，Mybatis 会重新解析那些被标记为未解析的标签，此时再解析 A 标签时，B 标签已经存在，A 标签也就可以正常解析完成了。

## 17、Xml 映射文件和内部数据结构之间的映射关系？

Mybatis 将所有 **Xml 配置信息**都封装到 All-In-One 重量级对象 **Configuration** 内部。

在 Xml 映射文件中，`<parameterMap>`标签会被解析为 `ParameterMap` 对象，其每个子元素会被解析为 ParameterMapping 对象。`<resultMap>`标签会被解析为 `ResultMap` 对象，其每个子元素会被解析为 `ResultMapping` 对象。每一个`<select>、<insert>、<update>、<delete>`标签均会被解析为 `MappedStatement` 对象，标签内的 sql 会被解析为 BoundSql 对象。

## 18、为什么说 Mybatis 是半自动 ORM 映射工具？它与全自动的区别在哪里？

注：我出的

答：Hibernate 属于全自动 ORM 映射工具，使用 Hibernate 查询关联对象或者关联集合对象时，可以根据对象关系模型直接获取，所以它是全自动的。而 Mybatis 在查询关联对象或关联集合对象时，需要手动编写 sql 来完成，所以，称之为半自动 ORM 映射工具。

面试题看似都很简单，但是想要能正确回答上来，必定是研究过源码且深入的人，而不是仅会使用的人或者用的很熟的人，以上所有面试题及其答案所涉及的内容，在我的 Mybatis 系列博客中都有详细讲解和原理分析。

##19、Mybatis缓存

一级缓存放在`SqlSession`里，默认就有，当调用 SqlSession 的修改，添加，删除，commit()，close()



二级缓存存在于命名空间里，使用时需要属性类实现 Serializable 序列化接口

二级缓存跨SqlSession，多个 SqlSession 去操作同一个 Mapper 映射的 sql 语句，可以共享二级缓存。

**配置**：

​	SqlMapConfig.xml文件的setting 标签配置cacheEnabled，开启二级缓存，可省略

```
<setting name="cacheEnabled" value="true"/>
```

​	然后配置映射文件，cache 标签开启二级缓存

```
    <cache></cache>
```

​	最后在statement上配置 useCache 属性，开启或关闭二级缓存

```
<select id="findById" resultType="user" parameterType="int" useCache="true">
```



**注解方式：**

​	在Dao接口上添加注解，@CacheNamespace(blocking=true) //mybatis 基于注解方式实现配置二级缓存

##20、parameterType和resultType

parameterType：指定输入参数类型，mybatis通过ognl从输入对象中获取参数值拼接在sql中。

resultType：指定输出结果类型，mybatis将sql查询结果的一行记录数据映射为resultType指定类型的对象。

##21、selectOne和selectList

selectOne查询一条记录，如果使用selectOne查询多条记录则抛出异常：



##22、spring中一级缓存失效



## 23、JDBC流程

加载数据库驱动：Class.forName("");

获取数据库连接类：DriverManager.getConnection

定义sql

获取预处理对象：preparedStatement

执行查询：preparedStatement.executeQuery();

处理结果集

释放资源





## 23、MyBatis 如何解决JDBC 的不足

1、数据库链接创建、释放频繁造成系统资源浪费从而影响系统性能，如果使用数据库链接池可解决此问题。 

解决:在 SqlMapConfig.xml 中配置数据链接池，使用连接池管理数据库链接。 

2、Sql 语句写在代码中造成代码不易维护，实际应用 sql 变化的可能较大，sql 变动需要改变 java 代码。 

解决:将 Sql 语句配置在 XXXXmapper.xml 文件中与 java 代码分离。 

3、向 sql 语句传参数麻烦，因为 sql 语句的 where 条件不一定，可能多也可能少，占位符需要和参数一一对应。 

解决: Mybatis 自动将 java 对象映射至 sql 语句，通过 statement 中的 parameterType 定义输入参数的类型

4、对结果集解析麻烦，sql 变化导致解析代码变化，且解析前需要遍历，如果能将数据库记录封装成 pojo 对象解 析比较方便。 

解决:Mybatis 自动将 sql 执行结果映射至 java 对象。通过 statement 中的 resultType 定义输出结果类型



## 24、Mybatis接口绑定

在Mybatis中任意定义接口，把接口方法和SQL语句绑定，然后直接调用接口方法就可以

实现方法：

​	 注解  @Select@Update 里写SQL

​	或者xml文件里面写SQL，映射文件namespace 为接口全限定名     



## 25、模糊查询

sql使用like语句，用# 赋值，`select * from user where username like #{username}`



## 26、mapper传多个参数

第一中：直接在方法传多个参数，xml文件用#{0}、#{1} 获取

第二中：使用@param 注解，在xml文件通过#{name} 获取



## 27、数据源

在<dataSource 标签配置type属性，值有POOLED：MyBatis 会创建 PooledDataSource 实例；

​	UNPOOLED” : MyBatis 会创建 UnpooledDataSource 实例

​	JNDI ：MyBatis 会从 JNDI 服务上查找 DataSource 实例，然后返回使用



如何创建DataSource：

​	DataSourceFactory工厂类的 getDataSource 方法。

​	创建完 DataSource 实例后，放到 Configuration 对象内的 Environment 对象中



##28、 获取SqlSession连接过程

当我们需要创建 SqlSession 对象并需要执行 SQL 语句时，这时候 MyBatis 才会去调用 dataSource 对象来创建 java.sql.Connection 对象。

也就是说，java.sql.Connection 对象的创建一直延迟到执行 SQL 语句



## 29、常用注解

@Insert:实现新增 

@Update:实现更新

@Delete:实现删除 

@Select:实现查询 

@Result:实现结果集封装
@Results:可以与@Result 一起使用，封装多个结果集 

​	pojo属性与数据库列名不一样，需要手动映射

```
@Results(id="userMap", value= {
	@Result(id=true,column="id",property="userId"),
	@Result(column="username",property="userName"), 
	@Result(column="sex",property="userSex"), 
	@Result(column="address",property="userAddress"),
	@Result(column="birthday",property="userBirthday") })
```

@ResultMap:实现引用@Results 封装的结果集

@One:实现一对一结果集封装 

​	代替了<assocation 标签，是多表查询的关键，在注解中用来指定子查询返回单一对象。

​	select 指定用来多表查询的 sqlmapper 

​	fetchType 会覆盖全局的配置参数 lazyLoadingEnabled

```
@Result(column=" ",property="",one=@One(select="")) 
```

@Many:实现一对多结果集封装 

​	代替了<Collection 标签,是多表查询的关键，在注解中用来指定子查询返回对象集合。 

注意:聚集元素用来处理“一对多”的关系。需要指定映射的 Java 实体类的属性，属性的 javaType 

(一般为 ArrayList)但是注解中可以不定义; 

```
@Result(property="",column="",many=@Many(select="")) 
```

@SelectProvider: 实现动态SQL映射 

@CacheNamespace:实现注解二级缓存的使用

@SelectKey：用于返回数据库自增主键

```
@SelectKey(keyColumn="id",keyProperty="id",resultType=Integer.class,before =
false, statement = { "select last_insert_id()" })
```



## 30、Mybatis编程步骤

1、编写持久层接口

2、编写持久层映射文件（与持久层接口包相同，文件名相同）

​	mapper标签的命名空间为接口全限定名

​	每个statement 的id为方法名

3、编写Mybatis配置文件

​	配置事务类型、数据源，引入mapper映射文件

4、编码

​	1.读取配置文件

​	2.创建SqlSessionFactoryBuilder

​	3.使用SqlSessionFactoryBuilder和配置文件创建SqlSessionFactory

​	4.创建SqlSession

​	5.创建DAO接口代理对象

​	6.使用代理对象执行持久层方法



批量更新



复杂更新