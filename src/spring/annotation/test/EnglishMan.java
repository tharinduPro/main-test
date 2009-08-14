package spring.annotation.test; 

import org.springframework.stereotype.Service;
@Service("enMan")
public class EnglishMan implements IMan { 
    public String sayHello() {
        return  "i am english!";
    } 
} 
