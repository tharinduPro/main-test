package net.dgie.xmltask;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;

public class MainTimer {
    
    public static void main(String[] args) {
        PropertyReader pr = new PropertyReader();
        long period = Long.valueOf( pr.getProperty( "updateIntervalTime" ) ) * 1000 * 60;
        Timer timer = new Timer();
        //timer.scheduleAtFixedRate( new MainTimerTask(), new Date(), period );
    }
}
class MainTimerTask extends TimerTask {
    private String intervalTime;
    public MainTimerTask( String intervalTime ) {
        this.intervalTime = intervalTime;
    }
    @Override
        public void run() {
            PropertyReader pr = new PropertyReader();
            String totalPath = pr.getProperty( "totalPath" );
            String[] allPathKeys = allPathKeys = pr.getProperty( "totalPath" ).split( "," );
            List<XMLUpdateTask> taskList = new ArrayList<XMLUpdateTask>();
            for( TaskBean tb: new TaskDAO().queryTask( intervalTime ) ) {
                List<String> paths = new ArrayList<String>();
                for( String filePathKey: allPathKeys ) {
                    paths.add( pr.getProperty( filePathKey ) + tb.getTaskId() + ".xml" );
                }
                XMLUpdateTask task= new XMLUpdateTask( paths, tb );
                taskList.add( task );
            }
            new XMLUpdateTimer().start( taskList );
        }
}
