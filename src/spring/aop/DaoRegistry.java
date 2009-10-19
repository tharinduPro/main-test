package spring.aop;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoRegistry {
    private static ApplicationContext ctx;

    static {
        ctx = new ClassPathXmlApplicationContext("spring\\aop\\context.xml");
    }

    /**
     * Private to make this a singleton.
     */
    private DaoRegistry(){
    }

    public static DaoRegistry getDaoRegistry() {
    	return new DaoRegistry();
    }

    public  EventSpringDao getEventDao(){
        return (EventSpringDao)ctx.getBean("eventDao", EventSpringDao.class);
    }
}
