package spring.aop;

import java.util.Date;

public class Main {

	public static void main(String[] args) {
		DaoRegistry dr = DaoRegistry.getDaoRegistry();
		EventSpringDao eDao = dr.getEventDao();
		Event e = new Event();
		e.setId( 1L );
		e.setDuration( 3 );
		e.setName( "test" );
		e.setStartDate( new Date() );
		eDao.save( e );
	}

}
