/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import exception.EmployeeIsNotFromAssignedOutletException;
import exception.EmployeeNotFoundException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.RentalReservationNotFoundException;
import exception.TransitAlreadyCompletedException;
import exception.TransitDriverDispatchRecordNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface TransitDriverDispatchRecordSessionBeanLocal {

    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord reservationRecord) throws InputDataValidationException, UnknownPersistenceException ;

    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecord();

    public void assignDriver(Long driverId, Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException, EmployeeIsNotFromAssignedOutletException, TransitAlreadyCompletedException;

    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;

    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, TransitAlreadyCompletedException;

    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordByOutletId(Long outletId, Date date);
    
    public List<TransitDriverDispatchRecord> retrieveNotCompletedTransitDriverDispatchRecordByOutletId(Long outletId, Date date);
    
    public Long createNewTranspatchDriverRecordCommit(Long destinationOutletId, Long rentalReservationId, Date transitDate) throws RentalReservationNotFoundException, OutletNotFoundException;
}
