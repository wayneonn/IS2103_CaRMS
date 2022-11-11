/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumerations.CarStateEnumeration;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
public class Partner implements Serializable {

    @OneToMany (mappedBy = "partner")
    @JoinColumn(nullable = false)
    private List<Customer> customers;
    @OneToMany(mappedBy = "partner")
    private List<ReservationRecord> reservationRecords;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(nullable = false, unique = true, length = 30)
    @NotNull
    @Size(min = 1, max = 30)
    private String partnerUsername;
    @Column(nullable = false, length = 30)
    @NotNull
    @Size(min = 6, max = 30)
    private String partnerPassword;
    @Column(nullable = false, unique = true, length = 128)
    @NotNull
    @Size(max = 128)
    private String partnerName;

    public Partner() {
    }

    public Partner(String partnerName, String partnerUsername, String partnerPassword) {
        this.partnerUsername = partnerUsername;
        this.partnerPassword = partnerPassword;
        this.partnerName = partnerName;
    }

    public Partner(String partnerName) {
        this.partnerName = partnerName;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cars[ id=" + partnerId + " ]";
    }

    /**
     * @return the partnerUsername
     */
    public String getPartnerUsername() {
        return partnerUsername;
    }

    /**
     * @param partnerUsername the partnerUsername to set
     */
    public void setPartnerUsername(String partnerUsername) {
        this.partnerUsername = partnerUsername;
    }

    /**
     * @return the partnerPassword
     */
    public String getPartnerPassword() {
        return partnerPassword;
    }

    /**
     * @param partnerPassword the partnerPassword to set
     */
    public void setPartnerPassword(String partnerPassword) {
        this.partnerPassword = partnerPassword;
    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * @return the reservationRecords
     */
    public List<ReservationRecord> getReservationRecords() {
        return reservationRecords;
    }

    /**
     * @param reservationRecords the reservationRecords to set
     */
    public void setReservationRecords(List<ReservationRecord> reservationRecords) {
        this.reservationRecords = reservationRecords;
    }

    /**
     * @return the partnerName
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * @param partnerName the partnerName to set
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

}
