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
import exception.OutletNotFoundException;
import exception.RentalReservationNotFoundException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
    public List<ReservationRecord> retrieveReservationRecord() {
        Query query = em.createQuery("SELECT r FROM ReservationRecord r");

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
            Outlet returnOutlet = rentalReservation.getReturnOutlet();
            Cars car = rentalReservation.getCar();
            car.setCarState(CarStateEnumeration.AVAILABLE);
            car.setOutlet(rentalReservation.getReturnOutlet());
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
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.pickedUp = FALSE AND r.car IS NOT NULL AND r.pickupOutlet.outletId = :inOutletId ");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }

    @Override
    public List<ReservationRecord> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId) {
        Query query = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.completed = FALSE AND r.pickedUp = TRUE AND r.car IS NOT NULL AND r.returnOutlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }
}
