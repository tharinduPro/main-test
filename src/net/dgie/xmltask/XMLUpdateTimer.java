package net.dgie.xmltask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.dgie.xmltask.util.Constants;


public class XMLUpdateTimer{
    private final Timer timer = new Timer();

    public void start( List<XMLUpdateTask> taskList ) {
       for( XMLUpdateTask xut: taskList ) {
           this.timer.schedule( xut,  xut.getTaskBean().getScheduleDate() );
       }
    }

}

class XMLUpdateTask extends TimerTask {

    private String URL;

    private List<String> filePaths;

    private Date date;

    private TaskBean taskBean;

	public TaskBean getTaskBean(){
		return this.taskBean;
	}

	public void setTaskBean(TaskBean taskBean) {
		this.taskBean=taskBean;
	}

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

    public XMLUpdateTask( List<String> filePaths, TaskBean taskBean) {
       this.filePaths = filePaths;
       this.taskBean = taskBean;
    }
    @Override
    public void run() {
        try {
        XMLFixer xf = new XMLFixer( taskBean.getRemoteURL() );
        XMLToHTML xth = new XMLToHTML();
        PropertyReader pr = new PropertyReader();
            for( String fp: filePaths ) {
                File xhtmlFile = new File( fp );
                if( "html".equals( taskBean.getType() ) ) {
                    File tempFile = new File( fp + ".temp" );
                    //把xhtml保存到本地
                    xf.getXMLFromURL( tempFile );
                    //修正xhtml文件
                    xf.fixXMLFile( tempFile, xhtmlFile ); 
                    tempFile.delete(); 
                } 
                else {
                    xth.saveRssToLocale( taskBean.getRemoteURL(), xhtmlFile ); 
                }
               String outputFile = pr.getProperty( pr.getProperty( "htmlPathConfig" ) ) + taskBean.getTaskId() +  ".html";
               xth.transformToHtml( outputFile, fp, pr.getProperty( pr.getProperty( "xsltPathConfig" ) ) + taskBean.getLocalXSLT() ); 
               //是否保存生成的xml
               if( !Constants.XML_KEEP.equals( pr.getProperty( "xmlKeep" ) ) ) {
                   xhtmlFile.delete(); 
               }
               //如果是第一次生成
               if( Constants.LOCAL_OUTPUT_NO.equals( taskBean.getLocalOutput() ) ) {
                   new TaskDAO().htmlCreated( taskBean.getTaskId() );
               }
            }
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
