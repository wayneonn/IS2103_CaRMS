/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.ReservationRecord;
import entity.TransitDriverDispatchRecord;
import exception.EmployeeIsNotFromAssignedOutletException;
import exception.EmployeeNotFoundException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.RentalReservationNotFoundException;
import exception.TransitAlreadyCompletedException;
import exception.TransitDriverDispatchRecordNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Wayne
 */
@Stateless
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @EJB
    private ReservationRecordSessionBeanLocal reservationRecordSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransitDriverDispatchRecordSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord reservationRecord) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<TransitDriverDispatchRecord>> constraintViolations = validator.validate(reservationRecord);

            if (constraintViolations.isEmpty()) {
                em.persist(reservationRecord);
                em.flush();

                return reservationRecord.getDispatchedId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransitDriverDispatchRecord>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Long createNewTranspatchDriverRecordCommit(Long destinationOutletId, Long rentalReservationId, Date transitDate) throws RentalReservationNotFoundException, OutletNotFoundException {
        try {
            
            TransitDriverDispatchRecord transitDriverDispatchRecord = new TransitDriverDispatchRecord(transitDate);
            Outlet destinationOutlet = outletSessionBean.retrieveOutletById(destinationOutletId);
            ReservationRecord rentalReservation = reservationRecordSessionBean.retrieveReservationRecordById(rentalReservationId);
            transitDriverDispatchRecord.setOutlet(destinationOutlet);
            destinationOutlet.getTransitDriverDispatchRecords().add(transitDriverDispatchRecord);
            transitDriverDispatchRecord.setReservationRecord(rentalReservation);
            rentalReservation.setTransitDriverDispatchRecord(transitDriverDispatchRecord);
            em.persist(transitDriverDispatchRecord);
            em.flush();
            return transitDriverDispatchRecord.getDispatchedId();
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet ID: " + destinationOutletId + " not found!");
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + " not found!");
        }
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
    public List<TransitDriverDispatchRecord> retrieveNotCompletedTransitDriverDispatchRecordByOutletId(Long outletId, Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        System.out.println("Date is " + date);
        GregorianCalendar nDate = new GregorianCalendar(date.getYear() + 1900,
                date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
        nDate.add(Calendar.DATE, 1);
        Date nextDay = nDate.getTime();
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t WHERE t.outlet.outletId = :inOutletId AND t.transitDate >= :inToday AND t.transitDate < :inNextDay AND t.isComplete = FALSE");
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
    public void assignDriver(Long driverId, Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException, EmployeeIsNotFromAssignedOutletException, TransitAlreadyCompletedException {
        try {
            Employee driver = employeeSessionBean.retrieveEmployeeById(driverId);
            TransitDriverDispatchRecord transitDriverDispatchRecord = retrieveTransitDriverDispatchRecordById(transitDriverDispatchRecordId);
            Long driverOutlet = driver.getOutlet().getOutletId();
            Long outletId = transitDriverDispatchRecord.getOutlet().getOutletId();
            if (!driverOutlet.equals(outletId)) {
                throw new EmployeeIsNotFromAssignedOutletException("Employee is not from the outlet");
            } else if (transitDriverDispatchRecord.isIsComplete()) {
                throw new TransitAlreadyCompletedException("Transit Record has already been completed!");
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
    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, TransitAlreadyCompletedException {
        try {
            TransitDriverDispatchRecord transitDriverDispatchRecord = retrieveTransitDriverDispatchRecordById(transitDriverDispatchRecordId);
            if (transitDriverDispatchRecord.isIsComplete()) {
                throw new TransitAlreadyCompletedException("Transit Record has already been completed!");
            }
            transitDriverDispatchRecord.setIsComplete(true);
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID: " + transitDriverDispatchRecordId + " not found!");
        }
    }

}
