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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
    private String licenseNumber;
    private CarStateEnumeration carState;
    private String make;
    private double quantity;
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

    public Cars() {
    }

    public Long getCarId() {
        return carId;
    }

    public Cars(String licenseNumber, CarStateEnumeration carState, String make, double quantity) {
        this.licenseNumber = licenseNumber;
        this.carState = carState;
        this.make = make;
        this.quantity = quantity;
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
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make the make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
}