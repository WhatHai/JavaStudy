package com.tcl.jr.ebank;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 多线程并发测试，对系统性能影响较大，请勿随便运行
 */
public class test {
    private static int getNum() {
        int max = 100;
        int min = 0;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;

    }

    public static void main(String[] ar) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // 创建存储任务的容器
        List<Callable<String>> tasks = new ArrayList<>();
        String test=UUID.randomUUID().toString().substring(0,20);

        // 提交10个任务
        for (int i = 0; i < 1; i++) {
            Callable<String> task = new Callable<String>() {
                public String call() {
                    EbankErpDemo demo = new EbankErpDemo();

                    JSONObject header = new JSONObject();
                    JSONObject body = new JSONObject();
                    String ip = "127.0.0.1";
                    Date reqDate = new Date();
                    SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");

                    header.put("REQ_TYPE", "foreignPayment");
                    header.put("REQ_TIME", reqForamt.format(reqDate));
                    header.put("REQ_IP", ip);
                    header.put("VERSION", 2);
                    header.put("APPNO", "auge");
                    header.put("USER_NAME", "jr08981");
                    header.put("CHANNEL", "EAI");
                    header.put("REQ_INDEX", ip + System.currentTimeMillis());
 
                   /* body.put("AGTBKACCTNO", "53543545455");
                    body.put("AGTBKADDR", "sdfdfsdf");

                    body.put("AGTBKNAME", "cdf");
                    body.put("AGTBKSWCD", "aabbdf");
                    body.put("AGTSTATE", "dfff");
                    body.put("AGTSTATECODE", "23");
                    body.put("CHARGEBEARS", "sfff");*/
                    body.put("CHANNEL", "EAI");
                    body.put("PAYACCTNO", "8010016397100001");

                    body.put("RECACCTNAME", "深圳市百利铭科技有限公司");
                    body.put("RECACCTNO", "44201628800052506869");
                    body.put("RECORGNAME", "中国建设银行股份有限公司深圳石岩支行");
                    body.put("RECBANKNAME", "中国建设银行");
                    body.put("PROVINCENAME", "");
                    body.put("CITYNAME", "");

                    body.put("TRANAMT", getNum());
                    body.put("PAYMODE", "1");
                    body.put("TRANTYPE", "201");
                    body.put("URGFLAG", "0");
                    body.put("EXPLMODE", "2");
                    body.put("TRANDATE", "2018-01-15");
                    body.put("DIGEST", "金单到期托收");
                    body.put("DBTYPE", "mysql");
                    body.put("ADDRESS", ip);
                    body.put("VOUCHER_ID", "");
                    body.put("APPROVALPROCESS", "0");
                    try {
                        return demo.eaiTest(header, body, "foreignPayment");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "error";
                }
            };
            // 将task添加进任务队列
            tasks.add(task);
        }

        List<Future<String>> results = executorService.invokeAll(tasks);

        for (int i = 0; i < results.size(); i++) {
            // 获取包含返回结果的future对象
            Future<String> future = results.get(i);
            // 从future中取出执行结果（若尚未返回结果，则get方法被阻塞，直到结果被返回为止）
            String result = future.get();
            System.out.println(i + result);
        }
        System.exit(0);
    }


}
