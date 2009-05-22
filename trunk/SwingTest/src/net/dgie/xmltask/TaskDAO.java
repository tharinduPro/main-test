package net.dgie.xmltask;

import net.dgie.xmltask.util.Constants;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDAO {
    public List<TaskBean> queryTask( String intervalTime ) throws SQLException {
        List<TaskBean> taskList = new ArrayList<TaskBean>();
        Statement s = null;
        try {
            PropertyReader pr = new PropertyReader();
            s = DBHelper.getConn().createStatement();
            ResultSet rs = s.executeQuery( "select mtl.taskid, taskname, remoteurl, localxslt, type, " + 
                    "to_date( to_char( sysdate, 'yyyy-mm-dd' ) || tasktime, 'yyyy-mm-dd hh24:mi' ), " + "localoutput " +
                    "from maintaskList mtl, subtasklist  stl where mtl.taskid = stl.taskid and " +
                    "to_date( to_char( sysdate, 'yyyy-mm-dd' ) || tasktime, 'yyyy-mm-dd hh24:mi' ) between " +
                    "sysdate and sysdate + " +  intervalTime + "/(24*60) or localoutput = " + Constants.LOCAL_OUTPUT_NO  );
            while(rs.next()) {
                TaskBean tb = new TaskBean();
                tb.setTaskId( rs.getString(1) );
                tb.setTaskName( rs.getString(2) );
                tb.setRemoteURL( rs.getString(3) );
                tb.setLocalXSLT( rs.getString(4) );
                tb.setType( rs.getString(5) );
                tb.setScheduleDate( rs.getTime(6) );
                tb.setLocalOutput( rs.getString(7) );
                taskList.add( tb );
            }
            return taskList;
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            try {
                s.close();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
            return taskList;
        }
    }

    public List<TaskBean> queryManualTask( List<String> taskIdList) {
        List<TaskBean> taskList = new ArrayList<TaskBean>();
        Statement sta = null;
        try {
            String taskIdSet = new String();
            for( String s: taskIdList ) {
                taskIdSet += s;
            }
            PropertyReader pr = new PropertyReader();
            sta = DBHelper.getConn().createStatement();
            if( !taskIdSet.isEmpty() ) {
                ResultSet rs = sta.executeQuery( "select mtl.taskid, taskname, remoteurl, localxslt, type, " +
                        "to_date( to_char( sysdate, 'yyyy-mm-dd' ) || tasktime, 'yyyy-mm-dd hh24:mi' ), " + "localoutput " +
                        "from maintaskList mtl, subtasklist  stl where mtl.taskid = stl.taskid and " +
                        "mtl.taskid in( " + taskIdSet  + " )" );
                while(rs.next()) {
                    TaskBean tb = new TaskBean();
                    tb.setTaskId( rs.getString(1) );
                    tb.setTaskName( rs.getString(2) );
                    tb.setRemoteURL( rs.getString(3) );
                    tb.setLocalXSLT( rs.getString(4) );
                    tb.setType( rs.getString(5) );
                    tb.setScheduleDate( rs.getTime(6) );
                    tb.setLocalOutput( rs.getString(7) );
                    taskList.add( tb );
                }
            }
            return taskList;
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
             try {
                sta.close();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
            return taskList;
        }
    }

   public List<TaskBean> queryAllTask() {
        List<TaskBean> taskList = new ArrayList<TaskBean>();
        Statement s = null;
        try {
            PropertyReader pr = new PropertyReader();
            s = DBHelper.getConn().createStatement();
            ResultSet rs = s.executeQuery( "select mtl.taskid, taskname, remoteurl, localxslt, type, " +
                    "to_date( to_char( sysdate, 'yyyy-mm-dd' ) || tasktime, 'yyyy-mm-dd hh24:mi' ), " + "localoutput " +
                    "from maintaskList mtl, subtasklist  stl where mtl.taskid = stl.taskid " );
            while(rs.next()) {
                TaskBean tb = new TaskBean();
                tb.setTaskId( rs.getString(1) );
                tb.setTaskName( rs.getString(2) );
                tb.setRemoteURL( rs.getString(3) );
                tb.setLocalXSLT( rs.getString(4) );
                tb.setType( rs.getString(5) );
                tb.setScheduleDate( rs.getTime(6) );
                tb.setLocalOutput( rs.getString(7) );
                taskList.add( tb );
            }
            return taskList;
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
             try {
                s.close();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
            return taskList;
        }
    }


    public void updateTask( String sql ) {
        Statement s = null;
        try {
            s = DBHelper.getConn().createStatement();
            s.execute( sql );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            try {
                s.close();
            }
            catch ( SQLException se ) {
               se.printStackTrace(); 
            }
        }
    }

    public void htmlCreated( String taskId ) {
       updateTask( "update maintasklist set localoutput = " + Constants.LOCAL_OUTPUT_YES + " where taskid = " + taskId ); 
    }
}
