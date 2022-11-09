/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.ReservationRecord;
import exception.RentalReservationNotFoundException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface ReservationRecordSessionBeanRemote {

    public Long createNewReservationRecord(ReservationRecord reservationRecord);

    public void returnCar(Long rentalReservationId) throws RentalReservationNotFoundException;

    public void pickupCar(Long rentalReservationId) throws RentalReservationNotFoundException;

    public ReservationRecord retrieveReservationRecordById(Long reservationRecordId) throws RentalReservationNotFoundException;

    public List<ReservationRecord> retrieveCustomerReservationsRecordsByPickupOutletId(Long outletId);

    public List<ReservationRecord> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId);

    public List<ReservationRecord> retrieveReservationRecords();

    public List<ReservationRecord> retrieveReservationsByUsername(String username);

    public List<ReservationRecord> retrieveReservationsByCustId(Long custId);

}
