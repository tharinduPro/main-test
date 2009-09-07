package spring.annotation.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SimpleMen {
    // 自动注入名称为 Man 的 Bean
    @Autowired(required = false)
    @Qualifier("cnMan")
    private IMan chineseMan;   
    
    @Autowired(required = false)
    @Qualifier("enMan")
    private IMan englishMan;
    
    public void hello() {
    	System.out.println( chineseMan.sayHello() );
    	System.out.println( englishMan.sayHello() );
    }
    
} 

