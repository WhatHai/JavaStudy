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

   

##MySQL与Oracle区别

**1）应用方面：**MySQL是中小型应用的数据库，一般用于个人项目或中小型网站及论坛。Oracle属于大型数据库，一般在具有相当规模的企业应用。

**2）自动增长的数据类型方面：**MySQL有自动增长的数据类型。Oracle没有自动增长的数据类型，需要建立一个自增序列

**3）group by用法：**MySQL中group by在select语句中可以随意使用，但是在Oracle中如果查询语句中有组函数，那其他列名必须是组函数处理过的或者是group by子句中的列，否则报错

**4）引号方面：**MySQL中可以用单引号、双引号包起字符串，Oracle中只可以用单引号包起字符串





