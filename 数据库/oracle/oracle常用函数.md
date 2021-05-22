









# oracle常用函数

**1、字符函数**

字符函数接受==字符参数==。可以是表中的列，也可以是一个字符串表达式。

```sql

ASCII(X)         --返回字符X的ASCII码  
                 --如：ASCII('A') 结果：65
CONCAT(X,Y)      --连接字符串X和Y  
                 --如：CONCAT('SQL','数据库开发')  结果：SQL数据库开发
CHR(X)           --返回X所指代的字符   如：CHR(65)  结果：A
INITCAP(X)       --返回X每个单词首字母大写的格式  
                 --如 INITCAP('hello world') 结果：Hello World
INSTR(X,STR[,START[,N]])  
                 --从X中查找str，并返回它出现的位置，可以指定从start开始，也可以指定从n开始
                 --如：INSTR('ABCDABCD','AB',4,1) 结果：5
LENGTH(X)        --返回X的长度  如：LENGTH('ABC') 结果：3
LOWER(X)         --X转换成小写
UPPER(X)         --X转换成大写
LTRIM(X[,TRIM_STR])                
                 --把X的左边截去trim_str中任一字符串，缺省截去空格
RTRIM(X[,TRIM_STR])              
                 --把X的右边截去trim_str中任一字符串，缺省截去空格
TRIM([TRIM_STR  FROM]X)      
                 --把X的两边截去trim_str字符串，缺省截去空格
REPLACE(X,old,new)                
                 --在X中查找old字符串，并替换成new字符串
                 --如：REPLACE('ABCDABCD','AB','X') 结果：XCDXCD
SUBSTR(X,start[,length])          
                 --返回X的字串，从start处开始，截取length个字符，缺省length，默认到结尾
                 --如：SUBSTR('ABCDEFGH',3,5) 结果：CDEFG
```

**2、数字函数**

数字函数接受==数字参数==。可以是表中的一列，也可以是一个数字表达式。

```sql
ABS(X)           --X的绝对值
ACOS(X)          --X的反余弦
COS(X)           --X的余弦
CEIL(X)          --大于或等于X的最小值
FLOOR(X)         --小于或等于X的最大值
LOG(X,Y)         --X为底Y的对数
MOD(X,Y)         --X除以Y的余数
POWER(X,Y)       --X的Y次幂
ROUND(X[,Y])     --X在第Y位四舍五入
SQRT(X)          --X的平方根
TRUNC(X[,Y])     --X在第Y位截断

说明：
a.  ROUND(X[,Y])，四舍五入。
在缺省 y 时，默认 y=0；比如：ROUND(3.56)=4。
y 是正整数，就是四舍五入到小数点后 y 位。ROUND(5.654,2)=5.65。
y 是负整数，四舍五入到小数点左边|y|位。ROUND(351.654,-2)=400。

b.  TRUNC(x[,y])，直接截取，不四舍五入。
在缺省 y 时，默认 y=0；比如：TRUNC (3.56)=3。
Y是正整数，就是四舍五入到小数点后 y 位。TRUNC (5.654,2)=5.65。
y 是负整数，四舍五入到小数点左边|y|位。TRUNC (351.654,-2)=300。
```

**3、日期函数**

日期函数对日期进行运算。

```sql
ADD_MONTHS(d,n)       --在某一个日期 d 上，加上指定的月数 n，返回计算后的新日期。
                      --d 表示日期，n 表示要加的月数。
CURRENT_DATE          --返回当前日期，包含时间部分
LAST_DAY(d)           --返回指定日期当月的最后一天。
MONTHS_BETWEEN(d1,d2) --返回日期d1月d2之间的相差的月数
NEXT_DAY(date,char)   --从日期参数中返回char参数所指定 下一天
                      --如：NEXT_DAY(DATE'2020-6-27', '星期二') 结果：2020/6/30
ROUND(d[,fmt])        --返回一个以 fmt 为格式的四舍五入日期值， d 是日期， fmt 是格式模型。
                      --默认 fmt 为 DDD，即月中的某一天。
1、如果 fmt 为“YEAR”则舍入到某年的 1 月 1 日，即前半年舍去，后半年作为下一年。
2、如果 fmt 为“MONTH”则舍入到某月的 1 日，即前月舍去，后半月作为下一月。
3、默认为“DDD”，即月中的某一天，最靠近的天，前半天舍去，后半天作为第二天。
4、如果 fmt 为“DAY”则舍入到最近的周的周日，即上半周舍去，下半周作为下一周周日。

例如
SELECT SYSDATE,ROUND(SYSDATE),ROUND(SYSDATE,'day'),
ROUND(SYSDATE,'month'),ROUND(SYSDATE,'year') FROM dual;
与 ROUND 对应的函数时 TRUNC(d[,fmt])对日期的操作， TRUNC 与 ROUND 非常相似，
只是不对日期进行舍入，直接截取到对应格式的第一天。

EXTRACT(fmt FROM d)    --提取日期中的特定部分。
fmt 为：YEAR、MONTH、DAY、HOUR、MINUTE、SECOND。
其中 YEAR、MONTH、DAY可以为 DATE 类型匹配，也可以与 TIMESTAMP 类型匹配；
但是 HOUR、MINUTE、SECOND 必须与 TIMESTAMP 类型匹配。
HOUR 匹配的结果中没有加上时区，因此在中国运行的结果小 8 小时。

例：SELECT SYSDATE "date",
       EXTRACT(YEAR FROM SYSDATE) "year",
       EXTRACT(MONTH FROM SYSDATE) "month",
       EXTRACT(DAY FROM SYSDATE) "day",
       EXTRACT(HOUR FROM SYSTIMESTAMP) "hour",
       EXTRACT(MINUTE FROM SYSTIMESTAMP) "minute",
       EXTRACT(SECOND FROM SYSTIMESTAMP) "second"
FROM dual;
```

**4、转换函数**

转换函数将值从一种数据类型转换为另外一种数据类型。

```sql

TO_CHAR(d|n[,fmt])       --把日期和数字转换为制定格式的字符串。Fmt是格式化字符串
例如：
SELECT TO_CHAR(SYSDATE,'YYYY"年"MM"月"DD"日" HH24:MI:SS') "date" FROM dual;
代码解析：
在格式化字符串中，使用双引号对非格式化字符进行引用
针对数字的格式化，格式化字符有：
9      --指定位置处显示数字
.      --指定位置返回小数点
,      --指定位置返回一个逗号
$      --数字开头返回一个美元符号
EEEE   --科学计数法表示
L      --数字前加一个本地货币符号
PR     --如果数字式负数则用尖括号进行表示 

TO_DATE(X [,fmt])        --把一个字符串以fmt格式转换成一个日期类型

TO_NUMBER(X [,fmt])      --把一个字符串以fmt格式转换为一个数字
```

**5、聚合函数**

统计函数(聚合函数)

```sql
AVG()                   --求平均值
COUNT()                 --统计数目
MAX()                   --求最大值
MIN()                   --求最小值
SUM()                   --求和
```

**6、分析函数**

```sql
分析函数语法
function_name(<argument>,<argument>...) over(<partition_Clause><order by_Clause>);
function_name()：函数名称
argument：参数
over( )：开窗函数
partition_Clause：分区子句，数据记录集分组，partition by...
order by_Clause：排序子句，数据记录集排序，order by...

COUNT() OVER()  --统计分区中各组的行数，partition by 可选，order by 可选
SUM() OVER()    --统计分区中记录的总和，partition by 可选，order by 可选
AVG() OVER()    --统计分区中记录的平均值，partition by 可选，order by 可选
MIN() OVER()    --统计分区中记录的最小值，partition by 可选，order by 可选
MAX() OVER()    --统计分区中记录的最大值，partition by 可选，order by 可选
RANK() OVER()   --跳跃排序，partition by 可选，order by 必选
DENSE_RANK() OVER()    --连续排序，partition by 可选，order by 必选
ROW_NUMBER() OVER()  --排序，无重复值，partition by 可选，order by 必选
NTILE(n) OVER()      --partition by 可选，order by 必选
    n表示将分区内记录平均分成n份，多出的按照顺序依次分给前面的组
FIRST_VALUE() OVER() --取出分区中第一条记录的字段值，partition by 可选，order by 可选
LAST_VALUE() OVER()  --取出分区中最后一条记录的字段值，partition by 可选，order by 可选
LAG() OVER()         --取出前n行数据，partition by 可选，order by 必选
LEAD() OVER()        --取出后n行数据，partition by 可选，order by 必选
PERCENT_RANK() OVER()  --partition by 可选，order by 必选
     所在组排名序号-1除以该组所有的行数-1，排名跳跃排序
```

**7、其他函数**

```sql
NVL(X,VALUE)             --如果X为空，返回value，否则返回X
NVL2(x,value1,value2)    --如果x非空，返回value1，否则返回value2
```

# 函数对比

> Oracle 11.2.0.4 MySQL 5.7.18

[TOC]

## 字符串函数

### 字符串函数汇总

| 功能       | 函数名       | 函数用法                                                   |
| ---------- | ------------ | ---------------------------------------------------------- |
| 大写       | upper()      | select upper('booboo') from dual;                          |
| 小写       | lower()      | select lower('Booboo') from dual;                          |
| 首字母大写 | initcap()    | select initcap('booboo wei') from dual;                    |
| 字符连接   | concat()     | select concat('select ','a',' from dual') from dual;       |
| 字符索引   | instr()      | select instr('booboo','o',2) from dual;                    |
| 字符截取   | substr()     | select substr('booboo',instr('booboo','o',2),4) from dual; |
| 字符扩充   | lpad()rpad() | select lpad('booboo',8,'*') from dual;                     |
| 首尾去除   | trim()       | select trim('&' from ''&&booboo&') from dual;              |
| 字符替换   | replace()    | select replace('booboo','o','O') from dual;                |

### 学习中的难点

#### MySQL和Oracle的区别

| 数据库           | Oracle                       | MySQL              |
| ---------------- | ---------------------------- | ------------------ |
| 单词首字母       | initcatp()                   | 没有该函数         |
| 获取关键字索引位 | instr() 四个参数             | 只有两个参数       |
| 执行函数的区别   | select upper('a') from dual; | select upper('a'); |
| 连接字符串       | \|\|=concat()                | 求或运算           |

oracle 必须要从一个dual的虚表中进行查询；mysql不需要。

```
mysql> select length('booboowei') length,instr('booboowei','w') windex,substr('booboowei',7) sub7,trim('o' from 'ooboo') otrim,lpad('boo',6,'A') lpad,rpad('boo',6,'A') rpad ,replace('booboowei','o','O') replaceo,'Q'||'P',concat('Q','P');
+--------+--------+------+-------+--------+--------+-----------+----------+-----------------+
| length | windex | sub7 | otrim | lpad   | rpad   | replaceo  | 'Q'||'P' | concat('Q','P') |
+--------+--------+------+-------+--------+--------+-----------+----------+-----------------+
|      9 |      7 | wei  | b     | AAAboo | booAAA | bOObOOwei |        0 | QP              |
+--------+--------+------+-------+--------+--------+-----------+----------+-----------------+
mysql> select instr('sdsp','s') ;
+-------------------+
| instr('sdsp','s') |
+-------------------+
|                 1 |
+-------------------+
1 row in set (0.00 sec)

mysql> select instr('sdsp','s',1) ;
ERROR 1582 (42000): Incorrect parameter count in the call to native function 'instr'
```

#### 容易忘记的函数用法

| 函数      | 用法                                    | 举例子                     |
| --------- | --------------------------------------- | -------------------------- |
| trim()    | trim('remove_string' from 'Obj_string') | trim('&&' from '&&abc&&&') |
| replace() | replace('Obj_string','old','new')       | replace('booboo','o','O')  |

> #### 为什么`trim()`用法记不住呢？
>
> 因为python中有个功能同样为去除首尾字符的字符串函数`strim()`用法为`strim('&&abc&&','&&')`，所以你懂的，容易搞混淆了，另外python中还有`lstrim() rstrim()`replace()·

> #### 为什么replace()函数难记呢？
>
> 用法是会用，但是容易将该函数的功能想多了，比如replace('booboo','o','O',1) 只想替换第一个出现的o，但是oracle或mysql中的replace函数没有该功能，只能全部替换。
>
> python中对字符串可以调用replace()函数，该函数可以选择只替换第一个出现的指定字符串。
>
> ```
> In [3]: a_str='booboowei'
> 
> In [4]: a_str.replace('o','A')
> Out[4]: 'bAAbAAwei'
> 
> In [7]: a_str.replace('o','A',1)
> Out[7]: 'bAoboowei'
> 
> In [8]: a_str.replace('o','A',2)
> Out[8]: 'bAAboowei'
> 
> In [9]: a_str.replace('o','A',4)
> Out[9]: 'bAAbAAwei'
> ```
>
> string的replace方法中，第三个参数可以指定替换的字符的个数

## 数字函数

### 数字函数汇总

| 功能                | 函数名  | 函数用法                          |
| ------------------- | ------- | --------------------------------- |
| 四舍五入            | round() | select round(3.1415,3) from dual; |
| 截断                | trunc() | select trunc(3.1415,3) from dual; |
| 取余数              | mod()   | select mod(3,2) from dual;        |
| 绝对值              | abs()   | select abs(-10) from dual;        |
| 取>= X 的最小整数   | ceil()  | select ceil(3.14) from dual;      |
| 取<= X 的最大整数值 | floor() | select floor(3.14) from dual;     |
| 乘方                | power() | select power(3,2) from emp;       |

### 学习中的难点

#### MySQL和Oracle的区别

| 数据库            | Oracle  | MySQL             |
| ----------------- | ------- | ----------------- |
| 截断              | trunc() | 没有trunc()函数   |
| 取余              | mod()   | mod()和%          |
| 取>= X 的最小整数 | ceil()  | ceil()和ceiling() |

#### MySQL实例

```
mysql> select round(3.1415,3) round,substr(3.1415,1,5) trunc,mod(3,2) modf, 3%2,abs(-1) abs ,ceil(3.14) ceil,ceiling(3.14) ceiling,floor(3.14) floor,power(3,2);
+-------+-------+------+------+-----+------+---------+-------+------------+
| round | trunc | modf | 3%2  | abs | ceil | ceiling | floor | power(3,2) |
+-------+-------+------+------+-----+------+---------+-------+------------+
| 3.142 | 3.141 |    1 |    1 |   1 |    4 |       4 |     3 |          9 |
+-------+-------+------+------+-----+------+---------+-------+------------+
```

## 日期函数

### 日期函数汇总

| 函数分类 | 功能         | 函数名           | 函数用法                                          |
| -------- | ------------ | ---------------- | ------------------------------------------------- |
|          | 现在的时间   | sysdate          | select sysdate from dual;                         |
|          | 月减         | months_between() | select months_between(sysdate,hiredate) from emp; |
|          | 月加         | add_months()     | select add_months(hiredate,3) from emp;           |
|          | 天减         | D1-D2            | select sysdate-hiredate from emp;                 |
|          | 天加         | D1+D2            | select sysdate+3 from emp;                        |
|          | 下一个周几   | next_day()       | select next_day(sysdate,'friday') from dual;      |
|          | 当月最后一天 | last_day()       | select last_day(sysdate) from dual;               |
|          | 日期四舍五入 | round()          | select round(sysdate,'year') from dual;           |
|          | 日期截断     | trunc()          | select truncate(sysdate,'year') from dual;        |

### 学习中的难点

#### MySQL和Oracle的区别

| 数据库             | Oracle           | MySQL                                 |
| ------------------ | ---------------- | ------------------------------------- |
| 返回现在的系统时间 | sysdate          | sysdate()、now()、curdate()等         |
| 月份相减           | months_between() | 无                                    |
| 月份相加           | add_months()     | date_add(sysdate(), interval 2 month) |
| 日期相减（天）     | D1-D2            | datediff(D1,D2)                       |
| 日期相加（天）     | D1+Days          | date_add(sysdate(), interval 2 day)   |

#### MySQL实例

- Oracle的日期可以直接相加减，而MySQL的日期必须使用date_add()来进行加运算
- MySQL中没有对日期的round()和trunc()
- Oracle通过YY年和RR年来记录和读取二位年；MySQL通过对比70来确定二位年

```
mysql> select sysdate(),sysdate()+30,date_add(sysdate(),interval + 30 day) dateaddday,date_add(sysdate(),interval + 1 month) dateaddmonth;
+---------------------+----------------+---------------------+---------------------+
| sysdate()           | sysdate()+30   | dateaddday          | dateaddmonth        |
+---------------------+----------------+---------------------+---------------------+
| 2017-10-10 14:48:19 | 20171010144849 | 2017-11-09 14:48:19 | 2017-11-10 14:48:19 |
+---------------------+----------------+---------------------+---------------------+
1 row in set (0.00 sec)

mysql> select round(sysdate(),'year');
+-------------------------+
| round(sysdate(),'year') |
+-------------------------+
|          20171010105032 |
+-------------------------+
1 row in set, 2 warnings (0.00 sec)

mysql> select round(sysdate(),3);
+--------------------+
| round(sysdate(),3) |
+--------------------+
| 20171010105040.000 |
+--------------------+

mysql> select convert('91-01-09',date),convert('01-01-09',date);
+--------------------------+--------------------------+
| convert('91-01-09',date) | convert('01-01-09',date) |
+--------------------------+--------------------------+
| 1991-01-09               | 2001-01-09               |
+--------------------------+--------------------------+
```

## 转换函数

### 转换函数汇总

| 函数分类 | 功能         | 函数名    | 函数用法                                        |
| -------- | ------------ | --------- | ----------------------------------------------- |
| 转字符串 | 数字转字符串 | to_char() | select to_char(3.14,'$09.99') from dual;        |
|          | 日期转字符串 | to_char() | select to_char(sysdate,'rrrr-dd-mm') from dual; |
| 转数字   | 字符串转数字 | to_num()  | select to_num('3.14','$09.99') from dual;       |
| 转日期   | 字符串转日期 | to_date() | select to_date('2017','rr') from dual;          |

### 学习中的难点

#### MySQL和Oracle的区别

| 数据库       | Oracle                        | MySQL                                                        |
| ------------ | ----------------------------- | ------------------------------------------------------------ |
| 数字转字符串 | to_char(3.14,'$09.99')        | convert('3.14',char) 或cast('3.14' as char)                  |
| 日期转字符串 | to_char(sysdate,'rrrr-dd-mm') | conver(sysdate(),char) 或cast(sysdate as char)或 date_format(now(),'%Y-%m-%d') 或 time_format(now(),'%H-%i-%S') |
| 字符串转数字 | to_num('3.14','$09.99')       | convert('3.14',decimal(6,4)) 或 cast('30' as signed)         |
| 字符串转日期 | to_date('2017','rr')          | convert('2017-09-08',date) 或cast('2017-09-08' as date) 或STR_TO_DATE('2004-03-01', '%Y-%m-%d') |

- MySQL中通过convert()和cast()函数实现数据类型的转换
- MySQL中没有to_char()、to_num()、to_date()函数

#### MySQL实例

```
mysql> select convert('30.01',signed) convet_num ,cast('30.01' as signed) cast_num;
+------------+----------+
| convet_num | cast_num |
+------------+----------+
|         30 |       30 |
+------------+----------+
```

## 常规函数（null）

### 常规函数汇总

| 函数分类   | 功能             | 函数名     | 函数用法                               |
| ---------- | ---------------- | ---------- | -------------------------------------- |
| 化空为有   | 1空则2，1不空则1 | nvl()      | select nvl(null,2) from dual;          |
|            | 1空则3，1不空则2 | nvl2()     | select nvl2(null,2,3) from dual;       |
| 等值判断   | 1=2则null，否则1 | nullif()   | select nullif(1,2) from dual;          |
| 枚举非空值 | 返回第一个非空值 | coalesce() | select coalesce(null,1,2,3) from dual; |

### 学习中的难点

#### MySQL和Oracle的区别

| 数据库     | Oracle       | MySQL      |
| ---------- | ------------ | ---------- |
| 化空为有   | nvl() nvl2() | ifnull()   |
| 等值判断   | nullif()     | nullif()   |
| 枚举非空值 | coalesce()   | coalesce() |

MySQL没有类似nvl2()的函数

#### MySQL实例

```
mysql> select ifnull(null,2),ifnull(1,2),isnull(null),coalesce(null,null,3);
+----------------+-------------+--------------+-----------------------+
| ifnull(null,2) | ifnull(1,2) | isnull(null) | coalesce(null,null,3) |
+----------------+-------------+--------------+-----------------------+
|              2 |           1 |            1 |                     3 |
+----------------+-------------+--------------+-----------------------+
```

## 条件表达式

### 条件表达式汇总

| 条件表达式分类 | 功能     | 函数字             | 用法                                                         |
| -------------- | -------- | ------------------ | ------------------------------------------------------------ |
| case表达式     | 条件判断 | case when esle end | select case job when 'SALESMAN' then sal*1.15 else sal end from emp; |
| decode函数     | 条件判断 | decode()           | select decode(job,'SALESMAN',sal*1.15,sal) from emp;         |

### 学习中的难点

#### MySQL和Oracle的区别

| 数据库   | Oracle     | MySQL                                     |
| -------- | ---------- | ----------------------------------------- |
| 条件判断 | case表达式 | case表达式                                |
|          | decode()   | IF(job='SALESMAN',sal*1.15,sal),if(),if() |

MySQL没有decode()的函数，但是可以用多个if()代替

#### MySQL实例

```
mysql> select case 1 when 1 then 'ok' when 2 then 'okk' else '0kkk' end casecol,coalesce(if(1=1,'ok',null),if(1=2,'okk',null)) ifcol;
+---------+-------+
| casecol | ifcol |
+---------+-------+
| ok      | ok    |
+---------+-------+
```

## rank()

> rank() 可用于分组排序，获取以A列分组后，按照B列从高到低的排序后获取每组前3名

```
# oracle
select id,md,qy,yj,rank () over (partition by qy order by yj desc) Ord from booboo;
# mysql
select id,qy,yj,rank from 
(select b.id,b.qy,b.yj,@rownum:=@rownum+1 , if(@pdept=b.qy,@rank:=@rank+1,@rank:=1) as rank,  @pdept:=b.qy  
from 
(select id,qy,yj from booboo order by qy,yj desc ) b ,
(select @rownum :=0 , @pdept := null ,@rank:=0) c ) result  
where rank <=3 ; 
# mysql
mysql> desc booboo;
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| id    | int(11)     | NO   | PRI | NULL    | auto_increment |
| md    | varchar(10) | YES  |     | NULL    |                |
| qy    | varchar(10) | YES  |     | NULL    |                |
| yj    | int(11)     | YES  |     | NULL    |                |
+-------+-------------+------+-----+---------+----------------+
4 rows in set (0.00 sec)

mysql> select * from booboo;
+----+------+----------+------+
| id | md   | qy       | yj   |
+----+------+----------+------+
|  1 | a    | shanghai |   10 |
|  2 | b    | beijing  |    8 |
|  3 | c    | shanghai |    9 |
|  4 | d    | beijing  |   10 |
+----+------+----------+------+
4 rows in set (0.00 sec)

mysql> select qy,rank,md,yj from (select b.id,b.md,b.qy,b.yj,@rownum:=@rownum+1 , if(@pdept=b.qy,@rank:=@rank+1,@rank:=1) as rank,  @pdept:=b.qy from (select id,md,qy,yj from booboo order by qy,yj desc ) b ,(select @rownum :=0 , @pdept := null ,@rank:=0) c)re;
+----------+------+------+------+
| qy       | rank | md   | yj   |
+----------+------+------+------+
| beijing  |    1 | d    |   10 |
| beijing  |    2 | b    |    8 |
| shanghai |    1 | a    |   10 |
| shanghai |    2 | c    |    9 |
+----------+------+------+------+
4 rows in set (0.00 sec)

# 简化
mysql> select * from (select name,age,if(home=@hm,@rownum:=@rownum+1,@rownum:=1) rownum,@hm:=home from (select * from stu order by home,age desc )b,(select @rownum :=0 , @pdept := null ,@rank:=0)c) d  where rownum<=2;
+--------+------+--------+-----------+
| name   | age  | rownum | @hm:=home |
+--------+------+--------+-----------+
| kk     |   18 |      1 | beijing   |
| batman |   10 |      2 | beijing   |
| tom    |   10 |      1 | shanghai  |
| super  |    9 |      2 | shanghai  |
+--------+------+--------+-----------+
4 rows in set (0.00 sec)
```



