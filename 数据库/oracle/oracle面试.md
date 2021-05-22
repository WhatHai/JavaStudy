## 1、分页

### Oracle用rownum进行分页

a.最内层sql，查询要分页的所有数据

b.第二层sql，通过rownum伪列确定显示数据的上限，并且给查询的数据添加rownum伪列的值

c.最外层sql，设置显示数据的下限

select*from

(select a.*,rownum r from

(select*from表名 where条件 orderby列) a

whererownum<=页数*条数) b where r>（页数-1）*条数

### mysql分页

LIMIT <M> OFFSET <N>子句实现

把结果集分页，每页3条记录。要获取第1页的记录。

`LIMIT 3 OFFSET 0`表示，对结果集从0号记录开始，最多取3条。注意SQL记录集的索引从0开始

```sql
SELECT id, name, gender, score FROM students ORDER BY score DESC LIMIT 3 OFFSET 0;
```

 如果要查询第2页，那么我们只需要“跳过”头3条记录，也就是对结果集从3号记录开始查询，把`OFFSET`设定为3

```sql
SELECT id, name, gender, score FROM students ORDER BY score DESC LIMIT 3 OFFSET 3;
```



## **2、truncate和delete区别**

1）Truncate和delete都可以将数据实体删掉，truncate操作不记录到rollback日志，同时数据不能恢复

2）Truncate是数据定义语言（DDL），delete是数据操作语言（DML）

3）Truncate不能对视图进行操作，delete操作不会腾出表空间的内存

4）truncate 是删除表再创建，delete 是逐条删除

 

## Oracle中常用的函数

length长度、lower小写、upper大写、to_date转化日期、to_char转化字符、to_number转化数字Ltrim去左边空格、rtrim去右边空格、substr截取字符串、add_month增加或减掉月份、

 

##创建索引，使用原则，优缺点

create index 索引名 on 表名（列名）

**原则：**

建议索引列建立not null约束

经常与其他表进行连接的表，在连接列上建立索引

**优缺点：**

创建索引能大大加快检索速度，加强表与表的连接，但是创建索引很占用空间

 

**7、使用oracle伪列删除表中的重复数据中的一条**

delete fromtable t where t.rowid!=(select max(t1.rowid) from table t1 wheret.name=t1.name)

 

**8、如何只显示重复数据**

select * fromtable group by id having count(*)>1

  

5.**数据库优化的方案**

建立主键，为数据库创建索引，建立存储过程，触发器，可提高查询速度。

6.**Oracle****中有哪几种索引**

1.单列索引与复合索引

一个索引可以由一个或多个列组成，用来创建索引的列被称为“索引列”。

单列索引是基于单列所创建的索引，复合索引是基于两列或者多列所创建的索引。

2.唯一索引与非唯一索引

唯一索引是索引列值不能重复的索引，非唯一索引是索引列可以重复的索引。

无论是唯一索引还是非唯一索引，索引列都允许取NULL值。默认情况下，Oracle创建的索引是不唯一索引。

3.B树索引

B树索引是按B树算法组织并存放索引数据的，所以B树索引主要依赖其组织并存放索引数据的算法来实现快速检索功能。

4.位图索引

位图索引在多列查询时，可以对两个列上的位图进行AND和OR操作，达到更好的查询效果。

5.函数索引

Oracle中不仅能够直接对表中的列创建索引，还可以对包含列的函数或表达式创建索引，这种索引称为“位图索引

7.**数据库索引的优点和缺点【首航财务】**

优点：

1、通过创建唯一性索引，可以保证数据库表中每一行数据的唯一性。

2、可以大大加快数据的检索速度，这也是创建索引的最主要的原因。

3、可以加速表和表之间的连接，特别是在实现数据的参考完整性方面特别有意义。

4、在使用分组和排序子句进行数据检索时，同样可以显著减少查询中分组和排序的时间。

5、通过使用索引，可以在查询的过程中，使用优化隐藏器，提高系统的性能。

缺点：

1、创建索引和维护索引要耗费时间，这种时间随着数据量的增加而增加。

2、索引需要占物理空间，除了数据表占数据空间之外，每一个索引还要占一定的物理空间，如果要建立聚簇索引，那么需要的空间就会更大。

3、当对表中的数据进行增加、删除和修改的时候，索引也要动态的维护，这样就降低了数据的维护速度。

8.**触发器有几种？**

共2种，一种DML触发，就是遇到DML事件时触发执行，像insert\update\delete。一种DDL触发，遇到DDL事件时触发，像Login Datatabase、更改数据库状态、create语句等。

9.**oracle****中除了数据库备份，还有什么方法备份？**

Oracle数据库有三种标准的备份方法，它们分别是导出/导入(EXP/IMP)、热备份和冷备份。导出备份是一种逻辑备份，冷备份和热备份是物理备份。

10G有几种新功能进行备份，像数据磅

10.**写出删除表中重复记录的语句****oracle**

delete from people

where peopleId in (select   peopleId from people group by   peopleId   having count(peopleId) > 1)  and rowid not in (select min(rowid) from   people group by

11.**数据库里面游标，索引是怎么用的？**

declare cur cursor keyset for

get返回null,load classnotfoundException

12.**.****在****Oracle****中数据库中的一个表中，这个表没有主键****id****也没有特定标示来查数据，怎么查？**

利用伪列标识进行查询。





##MySQL与Oracle区别

**1）应用方面：**MySQL是中小型应用的数据库，一般用于个人项目或中小型网站及论坛。Oracle属于大型数据库，一般在具有相当规模的企业应用。

**2）自动增长的数据类型方面：**MySQL有自动增长的数据类型。Oracle没有自动增长的数据类型，需要建立一个自增序列

**3）group by用法：**MySQL中group by在select语句中可以随意使用，但是在Oracle中如果查询语句中有组函数，那其他列名必须是组函数处理过的或者是group by子句中的列，否则报错

**4）引号方面：**MySQL中可以用单引号、双引号包起字符串，Oracle中只可以用单引号包起字符串





