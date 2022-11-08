/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationRecord;
import exception.RentalReservationNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface ReservationRecordSessionBeanLocal {

    public Long createNewReservationRecord(ReservationRecord reservationRecord);

    public List<ReservationRecord> retrieveReservationRecord();

    public void returnCar(Long rentalReservationId) throws RentalReservationNotFoundException;

    public void pickupCar(Long rentalReservationId) throws RentalReservationNotFoundException;
    
    public List<ReservationRecord> retrieveCustomerReservationsRecordsByPickupOutletId(Long outletId);

    public ReservationRecord retrieveReservationRecordById(Long reservationRecordId) throws RentalReservationNotFoundException;
    
     public List<ReservationRecord> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId);
}
