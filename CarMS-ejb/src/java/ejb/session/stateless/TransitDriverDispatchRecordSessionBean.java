/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Employee;
import entity.TransitDriverDispatchRecord;
import exception.CarNotFoundException;
import exception.EmployeeIsNotFromAssignedOutletException;
import exception.EmployeeNotFoundException;
import exception.TransitDriverDispatchRecordNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord reservationRecord) {
        em.persist(reservationRecord);
        em.flush();

        return reservationRecord.getDispatchedId();
    }

    @Override
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecord() {
        Query query = em.createQuery("SELECT d FROM Outlet d");
        query.getResultList().size();

        return query.getResultList();
    }

    @Override
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordByOutletId(Long outletId, Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        System.out.println("Date is " + date);
        GregorianCalendar nDate = new GregorianCalendar(date.getYear() + 1900,
                date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
        nDate.add(Calendar.DATE, 1);
        Date nextDay = nDate.getTime();
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t WHERE t.outlet.outletId = :inOutletId AND t.transitDate >= :inToday AND t.transitDate < :inNextDay");
        query.setParameter("inOutletId", outletId);
        query.setParameter("inToday", date);
        query.setParameter("inNextDay", nextDay);
        query.getResultList().size();

        //em.flush();
        return query.getResultList();
    }

    @Override
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException {

        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, transitDriverDispatchRecordId);

        if (transitDriverDispatchRecord != null) {
            return transitDriverDispatchRecord;
        } else {
            throw new TransitDriverDispatchRecordNotFoundException("TransitDriverDispatchRecord ID " + transitDriverDispatchRecordId + " does not exist!");
        }
    }

    @Override
    public void assignDriver(Long driverId, Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException, EmployeeIsNotFromAssignedOutletException {
        try {
            Employee driver = employeeSessionBean.retrieveEmployeeById(driverId);
            TransitDriverDispatchRecord transitDriverDispatchRecord = retrieveTransitDriverDispatchRecordById(transitDriverDispatchRecordId);
            Long driverOutlet = driver.getOutlet().getOutletId();
            Long outletId = transitDriverDispatchRecord.getOutlet().getOutletId();
            if (!driverOutlet.equals(outletId)) {
                throw new EmployeeIsNotFromAssignedOutletException("Employee is not from the outlet");
            } else {
                transitDriverDispatchRecord.setEmployee(driver);
                driver.getTransitDriverDispatchRecords().add(transitDriverDispatchRecord);
            }
        } catch (EmployeeNotFoundException ex) {
            throw new EmployeeNotFoundException("Employee ID: " + driverId + " not found!");
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID: " + transitDriverDispatchRecordId + " not found!");
        }

    }

    @Override
    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException {
        try {
            TransitDriverDispatchRecord transitDriverDispatchRecord = retrieveTransitDriverDispatchRecordById(transitDriverDispatchRecordId);
            transitDriverDispatchRecord.setIsComplete(true);
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID: " + transitDriverDispatchRecordId + " not found!");
        }
    }
    
}
