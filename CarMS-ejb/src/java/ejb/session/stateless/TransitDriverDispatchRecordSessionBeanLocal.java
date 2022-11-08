/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import exception.EmployeeIsNotFromAssignedOutletException;
import exception.EmployeeNotFoundException;
import exception.TransitDriverDispatchRecordNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface TransitDriverDispatchRecordSessionBeanLocal {

    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord reservationRecord);

    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecord();

    public void assignDriver(Long driverId, Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException, EmployeeIsNotFromAssignedOutletException;

    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;

    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;

    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordByOutletId(Long outletId, Date date);
}
