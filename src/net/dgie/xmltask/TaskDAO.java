package net.dgie.xmltask;

import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;

public class TaskDAO {

    public List<TaskBean> queryTask() {
        List<TaskBean> taskList = new ArrayList<TaskBean>();
        try {
            PropertyReader pr = new PropertyReader();
            Statement s = DBHelper.getConn().createStatement();
            ResultSet rs = s.executeQuery( "select mtl.taskid, taskname, remoteurl, localxslt, type, " + 
                    "to_date( to_char( sysdate, 'yyyy-mm-dd' ) || tasktime, 'yyyy-mm-dd hh24:mi' ) " + 
                    "from maintaskList mtl, subtasklist  stl where mtl.taskid = stl.taskid and " +
                    "to_date( to_char( sysdate, 'yyyy-mm-dd' ) || tasktime, 'yyyy-mm-dd hh24:mi' ) between " +
                    "sysdate and sysdate + " +  pr.getProperty( "updateIntervalTime" ) + "/(24*60)" );
            while(rs.next()) {
                TaskBean tb = new TaskBean();
                tb.setTaskId( rs.getString(1) );
                tb.setTaskName( rs.getString(2) );
                tb.setRemoteURL( rs.getString(3) );
                tb.setLocalXSLT( rs.getString(4) );
                tb.setType( rs.getString(5) );
                tb.setScheduleDate( rs.getTime(6) );
                taskList.add( tb );
            }
            return taskList;
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            return taskList;
        }
    }
}
