package net.dgie.xmltask;

import java.util.Date;

public class TaskBean {
    private String taskId;
    private String taskName;
    private String remoteURL;
    private String localXSLT;
    private String localOutput;
    private String type;
    private Date scheduleDate; 

	/**
	 * get the value of localOutput
	 * @return the value of localOutput
	 */
	public String getLocalOutput(){
		return this.localOutput;
	}
	/**
	 * set a new value to localOutput
	 * @param localOutput the new value to be used
	 */
	public void setLocalOutput(String localOutput) {
		this.localOutput=localOutput;
	}
	/**
	 * get the value of taskId
	 * @return the value of taskId
	 */
	public String getTaskId(){
		return this.taskId;
	}
	/**
	 * set a new value to taskId
	 * @param taskId the new value to be used
	 */
	public void setTaskId(String taskId) {
		this.taskId=taskId;
	}

	/**
	 * get the value of taskName
	 * @return the value of taskName
	 */
	public String getTaskName(){
		return this.taskName;
	}
	/**
	 * set a new value to taskName
	 * @param taskName the new value to be used
	 */
	public void setTaskName(String taskName) {
		this.taskName=taskName;
	}

	/**
	 * get the value of remoteURL
	 * @return the value of remoteURL
	 */
	public String getRemoteURL(){
		return this.remoteURL;
	}
	/**
	 * set a new value to remoteURL
	 * @param remoteURL the new value to be used
	 */
	public void setRemoteURL(String remoteURL) {
		this.remoteURL=remoteURL;
	}

	/**
	 * get the value of localXSLT
	 * @return the value of localXSLT
	 */
	public String getLocalXSLT(){
		return this.localXSLT;
	}
	/**
	 * set a new value to localXSLT
	 * @param localXSLT the new value to be used
	 */
	public void setLocalXSLT(String localXSLT) {
		this.localXSLT=localXSLT;
	}

	/**
	 * get the value of type
	 * @return the value of type
	 */
	public String getType(){
		return this.type;
	}
	/**
	 * set a new value to type
	 * @param type the new value to be used
	 */
	public void setType(String type) {
		this.type=type;
	}

	/**
	 * get the value of scheduleDate
	 * @return the value of scheduleDate
	 */
	public Date getScheduleDate(){
		return this.scheduleDate;
	}
	/**
	 * set a new value to scheduleDate
	 * @param scheduleDate the new value to be used
	 */
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate=scheduleDate;
	}
    
    public String toString() {
       return  taskId + ", " +
               taskName + ", " +
               remoteURL + ", " +
               localXSLT + ", " +
               type + ", " +
               scheduleDate;
    }
}
