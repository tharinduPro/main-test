/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.dgie.xmltask;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
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
@Table(name = "JFORUM_RANKS", catalog = "", schema = "TEST")
@NamedQueries({@NamedQuery(name = "JforumRanks.findAll", query = "SELECT j FROM JforumRanks j"), @NamedQuery(name = "JforumRanks.findByRankId", query = "SELECT j FROM JforumRanks j WHERE j.rankId = :rankId"), @NamedQuery(name = "JforumRanks.findByRankTitle", query = "SELECT j FROM JforumRanks j WHERE j.rankTitle = :rankTitle"), @NamedQuery(name = "JforumRanks.findByRankMin", query = "SELECT j FROM JforumRanks j WHERE j.rankMin = :rankMin"), @NamedQuery(name = "JforumRanks.findByRankSpecial", query = "SELECT j FROM JforumRanks j WHERE j.rankSpecial = :rankSpecial"), @NamedQuery(name = "JforumRanks.findByRankImage", query = "SELECT j FROM JforumRanks j WHERE j.rankImage = :rankImage")})
public class JforumRanks implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RANK_ID")
    private Long rankId;
    @Basic(optional = false)
    @Column(name = "RANK_TITLE")
    private String rankTitle;
    @Basic(optional = false)
    @Column(name = "RANK_MIN")
    private long rankMin;
    @Column(name = "RANK_SPECIAL")
    private Long rankSpecial;
    @Column(name = "RANK_IMAGE")
    private String rankImage;

    public JforumRanks() {
    }

    public JforumRanks(Long rankId) {
        this.rankId = rankId;
    }

    public JforumRanks(Long rankId, String rankTitle, long rankMin) {
        this.rankId = rankId;
        this.rankTitle = rankTitle;
        this.rankMin = rankMin;
    }

    public Long getRankId() {
        return rankId;
    }

    public void setRankId(Long rankId) {
        Long oldRankId = this.rankId;
        this.rankId = rankId;
        changeSupport.firePropertyChange("rankId", oldRankId, rankId);
    }

    public String getRankTitle() {
        return rankTitle;
    }

    public void setRankTitle(String rankTitle) {
        String oldRankTitle = this.rankTitle;
        this.rankTitle = rankTitle;
        changeSupport.firePropertyChange("rankTitle", oldRankTitle, rankTitle);
    }

    public long getRankMin() {
        return rankMin;
    }

    public void setRankMin(long rankMin) {
        long oldRankMin = this.rankMin;
        this.rankMin = rankMin;
        changeSupport.firePropertyChange("rankMin", oldRankMin, rankMin);
    }

    public Long getRankSpecial() {
        return rankSpecial;
    }

    public void setRankSpecial(Long rankSpecial) {
        Long oldRankSpecial = this.rankSpecial;
        this.rankSpecial = rankSpecial;
        changeSupport.firePropertyChange("rankSpecial", oldRankSpecial, rankSpecial);
    }

    public String getRankImage() {
        return rankImage;
    }

    public void setRankImage(String rankImage) {
        String oldRankImage = this.rankImage;
        this.rankImage = rankImage;
        changeSupport.firePropertyChange("rankImage", oldRankImage, rankImage);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rankId != null ? rankId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JforumRanks)) {
            return false;
        }
        JforumRanks other = (JforumRanks) object;
        if ((this.rankId == null && other.rankId != null) || (this.rankId != null && !this.rankId.equals(other.rankId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.dgie.testswing.JforumRanks[rankId=" + rankId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
