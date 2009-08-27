package spring.services;
public class HelloWorldImpl implements HelloWorld{
    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello( String hello ) {
        this.hello = hello;
    }
     
    public void sayHello() {
        System.out.println( hello );
    }
}
