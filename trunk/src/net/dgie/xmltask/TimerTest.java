package net.dgie.xmltask;

import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
    private final Timer timer = new Timer();
    private final int minutes;

    public TimerTest(int minutes) {
        this.minutes = minutes;
    }

    public void start() {
           this.timer.schedule( new MyTask(), this.minutes, 200 );
    }

    public static void main(String[] args) {
        TimerTest eggTimer = new TimerTest(2);
        eggTimer.start();
    }

}

class MyTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Your egg is ready!");
    }
}
        //String inputURL = "http://www.google.com";
        //File  originalFile = new File( "F:\\TestWork\\Test\\original.xml" );
        //File  fixedFile = new File( "F:\\TestWork\\Test\\fixed.xml" );
        //XMLFixer xf = new XMLFixer( inputURL );
        ////把xhtml保存到本地
        //xf.getXMLFromURL( originalFile );
        ////修正xhtml文件
        //xf.fixXMLFile( originalFile, fixedFile ); 
