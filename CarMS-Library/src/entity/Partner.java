/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumerations.CarStateEnumeration;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 *
 * @author wayneonn
 */
@Entity
public class Partner implements Serializable {

    @OneToMany (mappedBy = "partner")
    @JoinColumn(nullable = false)
    private List<Customer> customers;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    private String partnerUsername;
    private String partnerPassword;

    public Partner() {
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

}
