/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    private String address;
    private String outletName;
    private LocalTime openingHour;
    private LocalTime closingHour;
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
    public LocalTime getOpeningHour() {
        return openingHour;
    }

    /**
     * @param openingHour the openingHour to set
     */
    public void setOpeningHour(LocalTime openingHour) {
        this.openingHour = openingHour;
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
    
}
