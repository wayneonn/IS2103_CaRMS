/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumerations.EmployeeEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author wayneonn
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    
    @OneToOne
    @JoinColumn(nullable = false)
    private ReservationRecord reservationRecord;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Employee employee;

    public TransitDriverDispatchRecord() {
    }
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dispatchedId;
    private LocalDateTime pickupDateTime;
    private LocalDateTime returnDateTime;
    private LocalTime closingHour;
    private Date transitDate;
    private boolean isComplete;

    /**
     * @return the dispatchedId
     */
    public Long getDispatchedId() {
        return dispatchedId;
    }

    /**
     * @param dispatchedId the dispatchedId to set
     */
    public void setDispatchedId(Long dispatchedId) {
        this.dispatchedId = dispatchedId;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.dispatchedId == null && other.dispatchedId != null) || (this.dispatchedId != null && !this.dispatchedId.equals(other.dispatchedId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dispatchedId != null ? dispatchedId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "entity.Employee[ id=" + dispatchedId + " ]";
    }

    /**
     * @return the pickupDateTime
     */
    public LocalDateTime getPickupDateTime() {
        return pickupDateTime;
    }

    /**
     * @param pickupDateTime the pickupDateTime to set
     */
    public void setPickupDateTime(LocalDateTime pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    /**
     * @return the returnDateTime
     */
    public LocalDateTime getReturnDateTime() {
        return returnDateTime;
    }

    /**
     * @param returnDateTime the returnDateTime to set
     */
    public void setReturnDateTime(LocalDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    /**
     * @return the closingHour
     */
    public LocalTime getClosingHour() {
        return closingHour;
    }

    /**
     * @param closingHour the closingHour to set
     */
    public void setClosingHour(LocalTime closingHour) {
        this.closingHour = closingHour;
    }

    /**
     * @return the transitDate
     */
    public Date getTransitDate() {
        return transitDate;
    }

    /**
     * @param transitDate the transitDate to set
     */
    public void setTransitDate(Date transitDate) {
        this.transitDate = transitDate;
    }

    /**
     * @return the isComplete
     */
    public boolean isIsComplete() {
        return isComplete;
    }

    /**
     * @param isComplete the isComplete to set
     */
    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

}
