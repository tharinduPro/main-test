package spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import spring.services.HelloWorld;

public class SpringTest {
    public static void main( String args[] ) throws Exception {
        BeanFactory factory = new XmlBeanFactory( ( new FileSystemResource( "F:\\TestWork\\Test\\target\\classes\\spring\\hello.xml" ) ) );

        HelloWorld hi = (HelloWorld)factory.getBean( "hello" );

        hi.sayHello();
    }
}

