/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class MCRCustomer extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = true, unique = true, length = 32)
    @Size(max = 32)
    private String custUsername;
    
    @Column(nullable = true, length = 32)
    @Size(max = 32)
    private String custPassword;
    
    @Column(nullable = true, length = 32)
    @Size(max = 32)
    private String idNumber;
    
    @Column(nullable = true)
    @Min(1)
    private String phoneNumber;

    public MCRCustomer(String custUsername, String custPassword, String idNumber, String phoneNumber, String firstName, String lastName, String email, String creditCardNumber) {
        super(firstName, lastName, email, creditCardNumber);
        this.custUsername = custUsername;
        this.custPassword = custPassword;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
    }

    public MCRCustomer() {
    }

    /**
     * @return the custUsername
     */
    public String getCustUsername() {
        return custUsername;
    }

    /**
     * @param custUsername the custUsername to set
     */
    public void setCustUsername(String custUsername) {
        this.custUsername = custUsername;
    }

    /**
     * @return the custPassword
     */
    public String getCustPassword() {
        return custPassword;
    }

    /**
     * @param custPassword the custPassword to set
     */
    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }

    /**
     * @return the idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
}
