package spring.annotation.test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/annotation/test/applicationContext.xml");
		SimpleMen simpleMen = (SimpleMen) context.getBean("simpleMen");
		simpleMen.hello();
    }
} 

