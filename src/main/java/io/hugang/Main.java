package io.hugang;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        // read spring config file named applicationContext.xml
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        // get the bean from spring container named executor
        BasicExecutor executor = context.getBean("executor", BasicExecutor.class);
        executor.execute();
    }
}
