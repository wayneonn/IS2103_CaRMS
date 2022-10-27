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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
public class Cars implements Serializable {
    //c
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    
    @Column(nullable = false, unique = true, length = 20)
    @NotNull
    @Size(min = 1, max = 20)
    private String licenseNumber;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CarStateEnumeration carState;
    
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String location;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String colour;
    
    @ManyToMany
    @JoinColumn(nullable = false)
    private List<RentalRate> rentalRates;
    @OneToOne(mappedBy = "car")
    private ReservationRecord reservationRecord; 
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    @OneToOne
    @JoinColumn(nullable = false)
    private Model model;
    @OneToOne
    @JoinColumn(nullable = false)
    private Category category;

    @OneToOne
    private Customer customer; 
    
    public Cars() {
    }

    public Long getCarId() {
        return carId;
    }

    public Cars(String licenseNumber, CarStateEnumeration carState) {
        this.licenseNumber = licenseNumber;
        this.carState = carState;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Cars)) {
            return false;
        }
        Cars other = (Cars) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cars[ id=" + carId + " ]";
    }

    /**
     * @return the licenseNumber
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * @param licenseNumber the licenseNumber to set
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    /**
     * @return the carState
     */
    public CarStateEnumeration getCarState() {
        return carState;
    }

    /**
     * @param carState the carState to set
     */
    public void setCarState(CarStateEnumeration carState) {
        this.carState = carState;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * @param colour the colour to set
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    
}