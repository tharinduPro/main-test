/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.dgie.xmltask;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "MAINTASKLIST", catalog = "", schema = "TEST")
@NamedQueries({@NamedQuery(name = "Maintasklist.findAll", query = "SELECT m FROM Maintasklist m"), @NamedQuery(name = "Maintasklist.findByTaskid", query = "SELECT m FROM Maintasklist m WHERE m.taskid = :taskid"), @NamedQuery(name = "Maintasklist.findByTaskname", query = "SELECT m FROM Maintasklist m WHERE m.taskname = :taskname"), @NamedQuery(name = "Maintasklist.findByRemoteurl", query = "SELECT m FROM Maintasklist m WHERE m.remoteurl = :remoteurl"), @NamedQuery(name = "Maintasklist.findByLocalxslt", query = "SELECT m FROM Maintasklist m WHERE m.localxslt = :localxslt"), @NamedQuery(name = "Maintasklist.findByType", query = "SELECT m FROM Maintasklist m WHERE m.type = :type"), @NamedQuery(name = "Maintasklist.findByLocaloutput", query = "SELECT m FROM Maintasklist m WHERE m.localoutput = :localoutput"), @NamedQuery(name = "Maintasklist.findByDecription", query = "SELECT m FROM Maintasklist m WHERE m.decription = :decription")})
public class Maintasklist implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TASKID")
    private BigDecimal taskid;
    @Column(name = "TASKNAME")
    private String taskname;
    @Column(name = "REMOTEURL")
    private String remoteurl;
    @Column(name = "LOCALXSLT")
    private String localxslt;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "LOCALOUTPUT")
    private String localoutput;
    @Column(name = "DECRIPTION")
    private String decription;

    public Maintasklist() {
    }

    public Maintasklist(BigDecimal taskid) {
        this.taskid = taskid;
    }

    public BigDecimal getTaskid() {
        return taskid;
    }

    public void setTaskid(BigDecimal taskid) {
        BigDecimal oldTaskid = this.taskid;
        this.taskid = taskid;
        changeSupport.firePropertyChange("taskid", oldTaskid, taskid);
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        String oldTaskname = this.taskname;
        this.taskname = taskname;
        changeSupport.firePropertyChange("taskname", oldTaskname, taskname);
    }

    public String getRemoteurl() {
        return remoteurl;
    }

    public void setRemoteurl(String remoteurl) {
        String oldRemoteurl = this.remoteurl;
        this.remoteurl = remoteurl;
        changeSupport.firePropertyChange("remoteurl", oldRemoteurl, remoteurl);
    }

    public String getLocalxslt() {
        return localxslt;
    }

    public void setLocalxslt(String localxslt) {
        String oldLocalxslt = this.localxslt;
        this.localxslt = localxslt;
        changeSupport.firePropertyChange("localxslt", oldLocalxslt, localxslt);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        String oldType = this.type;
        this.type = type;
        changeSupport.firePropertyChange("type", oldType, type);
    }

    public String getLocaloutput() {
        return localoutput;
    }

    public void setLocaloutput(String localoutput) {
        String oldLocaloutput = this.localoutput;
        this.localoutput = localoutput;
        changeSupport.firePropertyChange("localoutput", oldLocaloutput, localoutput);
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        String oldDecription = this.decription;
        this.decription = decription;
        changeSupport.firePropertyChange("decription", oldDecription, decription);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskid != null ? taskid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maintasklist)) {
            return false;
        }
        Maintasklist other = (Maintasklist) object;
        if ((this.taskid == null && other.taskid != null) || (this.taskid != null && !this.taskid.equals(other.taskid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.dgie.xmltask.Maintasklist[taskid=" + taskid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
