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
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

// RentalRate
/**
 *
 * @author wayneonn
 */
@Entity
public class RentalRate implements Serializable {

    @ManyToMany(mappedBy="rentalRates")
    private List<ReservationRecord> reservationRecords;
    
    @ManyToMany(mappedBy="rentalRates")
    private List<Cars> cars;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    private String rentalRateType;
    private Double rateCost;

    public RentalRate() {
    }


    /**
     * @return the rentalRateId
     */
    public Long getRentalRateId() {
        return rentalRateId;
    }

    /**
     * @param rentalRateId the rentalRateId to set
     */
    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "entity.Employee[ id=" + rentalRateId + " ]";
    }

    /**
     * @return the rentalRateType
     */
    public String getRentalRateType() {
        return rentalRateType;
    }

    /**
     * @param rentalRateType the rentalRateType to set
     */
    public void setRentalRateType(String rentalRateType) {
        this.rentalRateType = rentalRateType;
    }

    /**
     * @return the rateCost
     */
    public Double getRateCost() {
        return rateCost;
    }

    /**
     * @param rateCost the rateCost to set
     */
    public void setRateCost(Double rateCost) {
        this.rateCost = rateCost;
    }

    
}
