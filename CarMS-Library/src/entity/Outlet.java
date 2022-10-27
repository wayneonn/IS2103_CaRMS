/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, unique = true, length = 128)
    @NotNull
    @Size(min = 1, max = 128)
    private String address;
    @Column(nullable = false, unique = true, length = 128)
    @NotNull
    @Size(min = 1, max = 128)
    private String outletName;
    //@Temporal(TemporalType.TIME) 
    private Time openingHour;
    //@Temporal(TemporalType.TIME) 
    private Time closingHour;
    @OneToMany(mappedBy = "outlet")
    private List<Cars> cars;
    @OneToMany(mappedBy = "outlet")
    private List<Employee> employees;

    public Outlet() {
    }
    
    public Long getOutletId() {
        return outletId;
    }

    /**
     * @param outletId the outletId to set
     */
    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the outletName
     */
    public String getOutletName() {
        return outletName;
    }

    /**
     * @param outletName the outletName to set
     */
    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    /**
     * @return the openingHour
     */
    public Time getOpeningHour() {
        return openingHour;
    }

    /**
     * @param openingHour the openingHour to set
     */
    public void setOpeningHour(Time openingHour) {
        this.openingHour = openingHour;
    }

    /**
     * @return the closingHour
     */
    public Time getClosingHour() {
        return closingHour;
    }

    /**
     * @param closingHour the closingHour to set
     */
    public void setClosingHour(Time closingHour) {
        this.closingHour = closingHour;
    }
    
}
