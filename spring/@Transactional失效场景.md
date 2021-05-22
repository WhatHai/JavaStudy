



# @Transactional失效场景

## 修饰非public方法

```java
@Component
public class TestServiceImpl {
    @Resource
    TestMapper testMapper;
    @Transactional
    void insertTestWrongModifier() {
        int re = testMapper.insert(new Test(10,20,30));
        if (re > 0) {
            throw new NeedToInterceptException("need intercept");
        }
        testMapper.insert(new Test(210,20,30));
    }
}
```



##在类内部调用@Transactional方法

```
@Component
public class TestServiceImpl implements TestService {
    @Resource
    TestMapper testMapper;
    @Transactional
    public void insertTestInnerInvoke() {
        //正常public修饰符的事务方法
        int re = testMapper.insert(new Test(10,20,30));
        if (re > 0) {
            throw new NeedToInterceptException("need intercept");
        }
        testMapper.insert(new Test(210,20,30));
    }

    public void testInnerInvoke(){
        //类内部调用@Transactional标注的方法。
        insertTestInnerInvoke();
    }
}
```

##事务方法内部捕捉了异常，没有抛出新的异常，导致事务操作不会进行回滚

