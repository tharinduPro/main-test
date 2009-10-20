package spring.aop;

import java.util.Date;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		DaoRegistry dr = DaoRegistry.getDaoRegistry();
		EventSpringDao eDao = dr.getEventDao();
		Event e = new Event();
		e.setDuration( 12 );
		e.setName( "test3" );
		e.setStartDate( new Date() );
		eDao.save( e );
		List<Event> el = eDao.findAll();
		for( Event evt: el ) {
			System.out.println( evt );
		}
	}

}
