package net.dgie.xmltask;

import java.io.IOException;
import java.io.File;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;

public class XMLUpdateTimer{
    private final Timer timer = new Timer();

    public void start( List<XMLUpdateTask> taskList ) {
       for( XMLUpdateTask xut: taskList ) {
           this.timer.schedule( xut,  xut.getDate() );
       }
    }

}

class XMLUpdateTask extends TimerTask {

    private String URL;

    private List<String> filePaths;

    private Date date;

	public Date getDate(){
		return this.date;
	}

	public void setDate( Date date ) {
		this.date=date;
	}


	public List<String> getFilePaths(){
		return this.filePaths;
	}

	public void setFilePaths( List<String> filePaths) {
		this.filePaths=filePaths;
	}

	public String getURL(){
		return this.URL;
	}

	public void setURL(String URL) {
		this.URL=URL;
	}

    public XMLUpdateTask( List<String> filePaths, String URL, Date date) {
       this.filePaths = filePaths;
       this.URL = URL;
       this.date = date;
    }
    @Override
    public void run() {
        XMLFixer xf = new XMLFixer( URL );
        for( String fp: filePaths ) {
            File tempFile = new File( fp + ".temp" );
            //把xhtml保存到本地
            xf.getXMLFromURL( tempFile );
            //修正xhtml文件
            xf.fixXMLFile( tempFile, new File( fp ) ); 
            tempFile.delete(); 

            //如果是xslt类型的把xml结合xslt输出成html
            
        }
    }
}
