/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Customer;
import entity.Outlet;
import entity.ReservationRecord;
import enumerations.CarStateEnumeration;
import exception.CustomerNotFoundException;
import exception.RentalReservationNotFoundException;
import exception.ReservationNotFoundException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Wayne
 */
@Stateless
public class ReservationRecordSessionBean implements ReservationRecordSessionBeanRemote, ReservationRecordSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewReservationRecord(ReservationRecord reservationRecord) {
        em.persist(reservationRecord);
        em.flush();

        return reservationRecord.getReservationId();
    }

    @Override
    public List<ReservationRecord> retrieveReservationRecords() {
        Query query = em.createQuery("SELECT R FROM ReservationRecord r");

        return query.getResultList();
    }

    @Override
    public ReservationRecord retrieveReservationRecordById(Long reservationRecordId) throws RentalReservationNotFoundException {

        ReservationRecord reservationRecord = em.find(ReservationRecord.class, reservationRecordId);

        if (reservationRecord != null) {
            return reservationRecord;
        } else {
            throw new RentalReservationNotFoundException("ReservationRecord ID " + reservationRecordId + " does not exist!");
        }
    }
    

    @Override
    public void pickupCar(Long rentalReservationId) throws RentalReservationNotFoundException {

        try {
            ReservationRecord rentalReservation = retrieveReservationRecordById(rentalReservationId);
            Customer customer = rentalReservation.getCustomer();
            Cars car = rentalReservation.getCar();
            car.setCarState(CarStateEnumeration.ONRENTAL);
            car.setLocation("Car is with customer name: " + customer.getFirstName() + " " + customer.getLastName());
            car.setReservationRecord(rentalReservation);
            rentalReservation.setPaid(true);
            rentalReservation.setPickedUp(true);
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
        }
    }

    @Override
    public void returnCar(Long rentalReservationId) throws RentalReservationNotFoundException {
        try {
            ReservationRecord rentalReservation = retrieveReservationRecordById(rentalReservationId);
            rentalReservation.getReturnOutlet();
            Outlet returnOutlet = rentalReservation.getReturnOutlet();
            System.out.println("returnOutlet" + returnOutlet);
            Cars car = rentalReservation.getCar();
            car.setCarState(CarStateEnumeration.AVAILABLE);
            car.setOutlet(returnOutlet);
            car.setLocation(car.getOutlet().getOutletName());
            car.setReservationRecord(null);
            returnOutlet.addCar(car);
            rentalReservation.setCompleted(true);
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
        }
    }

    @Override
    public List<ReservationRecord> retrieveCustomerReservationsRecordsByPickupOutletId(Long outletId) {
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.pickedUp = FALSE AND r.car IS NOT NULL AND r.pickupOutlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }

    @Override
    public List<ReservationRecord> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId) {
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.completed = FALSE AND r.pickedUp = TRUE AND r.car IS NOT NULL AND r.returnOutlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }
    
    @Override
    public List<ReservationRecord> retrieveReservationsByUsername(String username) {
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.c c = :inUsername");
        query.setParameter("inId", username);
        return query.getResultList();
    }
    
    @Override
    public List<ReservationRecord> retrieveReservationsByCustId(Long custId) {
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.customer.customerId = :inCustId");
        query.setParameter("inCustId", custId);
        return query.getResultList();
    }
    
}
