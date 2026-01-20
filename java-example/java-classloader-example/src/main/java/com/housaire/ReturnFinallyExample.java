package com.housaire;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/2/9 0009
 * @Since 1.0
 */
public class ReturnFinallyExample {

    public static void main(String[] args) throws InterruptedException {
        Function function = new Function();
        System.out.println("开始调用方法");
        String result = function.sayHello();
        System.err.println("方法调用完成，处理返回结果");
//        Thread.sleep(50L);
        System.err.println(result);
        System.err.println("返回结果处理完成");
    }

    public static class Function {
        public String sayHello() {
            try {
                System.out.println("方法执行完毕，返回结果");
                return "1";
            } finally {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("执行finally");
            }
        }
    }

}
