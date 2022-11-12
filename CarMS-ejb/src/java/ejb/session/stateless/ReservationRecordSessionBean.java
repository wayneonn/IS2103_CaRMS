/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Category;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.ReservationRecord;
import enumerations.CarStateEnumeration;
import exception.CategoryNotFoundException;
import exception.CustomerNotFoundException;
import exception.InputDataValidationException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalReservationNotFoundException;
import exception.ReservationNotFoundException;
import exception.UnknownPersistenceException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
public class ReservationRecordSessionBean implements ReservationRecordSessionBeanRemote, ReservationRecordSessionBeanLocal {

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationRecordSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReservationRecord>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewReservationRecord(ReservationRecord reservationRecord) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<ReservationRecord>> constraintViolations = validator.validate(reservationRecord);

            if (constraintViolations.isEmpty()) {
                em.persist(reservationRecord);
                em.flush();

                return reservationRecord.getReservationId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
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

    @Override
    public Long createNewCarRentalReservation(Long categoryId, Long modelId, Long customerId,
            Long pickupOutletId, Long returnOutletId, ReservationRecord newRentalReservation)
            throws OutletNotFoundException, CustomerNotFoundException, InputDataValidationException, UnknownPersistenceException,
            CategoryNotFoundException, ModelNotFoundException {
        try {
            Set<ConstraintViolation<ReservationRecord>> constraintViolations = validator.validate(newRentalReservation);

            if (constraintViolations.isEmpty()) {
                Customer customer = customerSessionBean.retrieveCustomerById(customerId);
                Outlet pickupOutlet = outletSessionBean.retrieveOutletById(pickupOutletId);
                Outlet returnOutlet = outletSessionBean.retrieveOutletById(returnOutletId);
                newRentalReservation.setCustomer(customer);
                newRentalReservation.setPickupOutlet(pickupOutlet);
                newRentalReservation.setReturnOutlet(returnOutlet);
                customer.getReservationRecords().add(newRentalReservation);
                Category category = null;
                Model model = null;
                if (modelId > 0) {
                    model = modelSessionBean.retrieveModelById(modelId);
                    category = model.getCategory();
                    newRentalReservation.setModelCriteria(model);
                    newRentalReservation.setCategoryCriteria(category);
                } else {
                    category = categorySessionBean.retrieveCategoryById(categoryId);
                    newRentalReservation.setCategoryCriteria(category);
                }
                em.persist(newRentalReservation);
                em.flush();
                return newRentalReservation.getReservationId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet IDs: " + pickupOutletId + " and " + returnOutletId + " either or both does not exist!");
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        } catch (CategoryNotFoundException ex) {
            throw new CategoryNotFoundException("Car Category ID: " + categoryId + " does not exist!");
        } catch (ModelNotFoundException ex) {
            throw new ModelNotFoundException("Model ID: " + modelId + " does not exist!");
        }
    }

    @Override
    public Double cancelReservation(Long reservationRecordId) throws RentalReservationNotFoundException {
        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            ReservationRecord reservationRecord = retrieveReservationRecordById(reservationRecordId);
            reservationRecord.setReservationCancelled(true);
            long daysToReservation = 0;
            Date dateToday = new Date();
            long diff = reservationRecord.getPickupDateTime().getTime() - dateToday.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffHours = diff / (60 * 60 * 1000) % 24;
            System.out.println("Today date : " + sdf.format(dateToday) + " PickUp Date : " + sdf.format(reservationRecord.getPickupDateTime()));
            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.\n");

            if ((diffHours > 0 || diffMinutes > 0 || diffSeconds > 0)) {
                daysToReservation = daysToReservation + diffDays + 1;
            }

//            LocalDateTime startDateTemporal = rentalReservation.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//            LocalDateTime today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//            Long noOfDaysToStartReservation = ChronoUnit.DAYS.between(today, startDateTemporal);
            Double price = reservationRecord.getAmtPaid();
            Double penalty = null;

            if (daysToReservation >= 14) {
                penalty = new Double("0");
            } else if (daysToReservation < 14 && daysToReservation >= 7) {
                penalty = price * 0.2;
            } else if (daysToReservation < 7 && daysToReservation >= 3) {
                penalty = price * 0.5;
            } else if (daysToReservation < 3) {
                penalty = price * 0.7;
            }

            reservationRecord.setRefundAmount(price - penalty);
            return penalty;

        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation of ID: " + reservationRecordId + " not found!");
        }
    }

}
