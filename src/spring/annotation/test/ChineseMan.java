package spring.annotation.test; 

import org.springframework.stereotype.Service;

@Service("cnMan")
public class ChineseMan implements IMan { 
    public String sayHello() {
        return "我是中国人";
    } 

} 

