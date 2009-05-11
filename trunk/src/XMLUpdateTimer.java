import java.io.IOException;
import java.io.File;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;

public class XMLUpdateTimer{
    private final Timer timer = new Timer();

    public void start( List<XMLUpdateTask> taskList ) {
        int i = 1;
       for( XMLUpdateTask xut: taskList ) {
           this.timer.schedule( xut, i*1000 , xut.getTimeOut() );
           i++;
       }
    }

    public static void main(String[] args) {
        List<XMLUpdateTask> taskList = new ArrayList<XMLUpdateTask>();
        XMLUpdateTask taskOne = new XMLUpdateTask( "E:/TestWork/Test/show.xml", "http://www.google.cn", 2000 );
        taskList.add( taskOne );
        new XMLUpdateTimer().start( taskList );
    }

}

class XMLUpdateTask extends TimerTask {

    private String URL;

    private String filePath;

    private Integer timeOut;

	public Integer getTimeOut(){
		return this.timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut=timeOut;
	}

	public String getFilePath(){
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath=filePath;
	}

	public String getURL(){
		return this.URL;
	}

	public void setURL(String URL) {
		this.URL=URL;
	}

    public XMLUpdateTask( String filePath, String URL, Integer timeOut ) {
       this.filePath = filePath;
       this.URL = URL;
       this.timeOut = timeOut;
    }
    @Override
    public void run() {
        XMLFixer xf = new XMLFixer( URL );
        //把xhtml保存到本地
        xf.getXMLFromURL( new File( filePath + ".temp" ) );
        //修正xhtml文件
        xf.fixXMLFile( new File( filePath + ".temp" ), new File( filePath ) ); 
    }
}
