/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author wayneonn
 */
@Entity
public class ReservationRecord implements Serializable { //reservation record

    @ManyToOne
    private Customer customer;
    
    @ManyToMany
    @JoinColumn(nullable = false)
    private List<RentalRate> rentalRates;

    public ReservationRecord() {
    }
    
    @OneToOne
    private Cars car; 
    
    @OneToOne(mappedBy = "reservationRecord")
    private TransitDriverDispatchRecord transitDriverDispatchRecord;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private LocalDateTime pickupDateTime;
    private LocalDateTime returnDateTime;
    private Double amtPaid;
    private String creditCardNum;
    private Double refundAmount;

    /**
     * @return the reservationId
     */
    public Long getReservationId() {
        return reservationId;
    }

    /**
     * @param reservationId the reservationId to set
     */
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationRecord)) {
            return false;
        }
        ReservationRecord other = (ReservationRecord) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "entity.Employee[ id=" + reservationId + " ]";
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
     * @return the amtPaid
     */
    public Double getAmtPaid() {
        return amtPaid;
    }

    /**
     * @param amtPaid the amtPaid to set
     */
    public void setAmtPaid(Double amtPaid) {
        this.amtPaid = amtPaid;
    }

    /**
     * @return the creditCardNum
     */
    public String getCreditCardNum() {
        return creditCardNum;
    }

    /**
     * @param creditCardNum the creditCardNum to set
     */
    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    /**
     * @return the refundAmount
     */
    public Double getRefundAmount() {
        return refundAmount;
    }

    /**
     * @param refundAmount the refundAmount to set
     */
    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    
}
