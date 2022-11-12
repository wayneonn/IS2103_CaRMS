/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
public class ReservationRecord implements Serializable {

//reservation record
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
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet pickupOutlet;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet returnOutlet;
    @OneToOne(optional = true)
    private Category categoryCriteria;
    @OneToOne(optional = true)
    private Model modelCriteria;
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Partner partner;
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickupDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDateTime;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private Double amtPaid;
    @Column(nullable = false, length = 16)
    @NotNull
    @Size(min = 13, max = 16)
    private String creditCardNum;
    @Column(nullable = true, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private Double refundAmount;
    @Column(nullable = false)
    @NotNull
    private Boolean paid;
    @Column(nullable = false)
    @NotNull
    private Boolean pickedUp;
    @Column(nullable = false)
    @NotNull
    private Boolean completed;
    @Column(nullable = false)
    @NotNull
    private Boolean reservationCancelled;

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
    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    /**
     * @param pickupDateTime the pickupDateTime to set
     */
    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    /**
     * @return the returnDateTime
     */
    public Date getReturnDateTime() {
        return returnDateTime;
    }

    /**
     * @param returnDateTime the returnDateTime to set
     */
    public void setReturnDateTime(Date returnDateTime) {
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

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the rentalRates
     */
    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    /**
     * @return the car
     */
    public Cars getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Cars car) {
        this.car = car;
    }

    /**
     * @return the transitDriverDispatchRecord
     */
    public TransitDriverDispatchRecord getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    /**
     * @param transitDriverDispatchRecord the transitDriverDispatchRecord to set
     */
    public void setTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    /**
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the pickedUp
     */
    public Boolean getPickedUp() {
        return pickedUp;
    }

    /**
     * @param pickedUp the pickedUp to set
     */
    public void setPickedUp(Boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * @return the completed
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    /**
     * @return the pickupOutlet
     */
    public Outlet getPickupOutlet() {
        return pickupOutlet;
    }

    /**
     * @param pickupOutlet the pickupOutlet to set
     */
    public void setPickupOutlet(Outlet pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
    }

    /**
     * @return the returnOutlet
     */
    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    /**
     * @param returnOutlet the returnOutlet to set
     */
    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    /**
     * @return the categoryCriteria
     */
    public Category getCategoryCriteria() {
        return categoryCriteria;
    }

    /**
     * @param categoryCriteria the categoryCriteria to set
     */
    public void setCategoryCriteria(Category categoryCriteria) {
        this.categoryCriteria = categoryCriteria;
    }

    /**
     * @return the modelCriteria
     */
    public Model getModelCriteria() {
        return modelCriteria;
    }

    /**
     * @param modelCriteria the modelCriteria to set
     */
    public void setModelCriteria(Model modelCriteria) {
        this.modelCriteria = modelCriteria;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the reservationCancelled
     */
    public Boolean getReservationCancelled() {
        return reservationCancelled;
    }

    /**
     * @param reservationCancelled the reservationCancelled to set
     */
    public void setReservationCancelled(Boolean reservationCancelled) {
        this.reservationCancelled = reservationCancelled;
    }
}
