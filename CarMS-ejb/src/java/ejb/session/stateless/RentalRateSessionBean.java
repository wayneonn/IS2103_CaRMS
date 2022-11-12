/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
import entity.ReservationRecord;
import enumerations.RentalRateTypeEnum;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.NoRentalRateApplicableException;
import exception.RentalRateNotFoundException;
import exception.UnknownPersistenceException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewRentalRate(RentalRate rentalRate, Long categoryId) throws CategoryNotFoundException, InputDataValidationException, UnknownPersistenceException, PersistenceException {
        try {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                try {
                    Category category = categorySessionBean.retrieveCategoryById(categoryId);
                    category.addRentalRate(rentalRate);
                    rentalRate.setCategory(category);
                    em.persist(rentalRate);
                    em.flush();
                    return rentalRate.getRentalRateId();
                } catch (CategoryNotFoundException ex) {
                    throw new CategoryNotFoundException("Car Category ID: " + categoryId + " not found!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException {

        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public List<RentalRate> retrieveRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.category, r.startDate ASC");
        query.getResultList().size();
        return query.getResultList();
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException {
        if (rentalRate != null && rentalRate.getRentalRateId() != null) {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                RentalRate rentalRateToUpdate = retrieveRentalRateById(rentalRate.getRentalRateId());
                rentalRateToUpdate.setRentalRateDescription(rentalRate.getRentalRateDescription());
                rentalRateToUpdate.setRateCost(rentalRate.getRateCost());
                rentalRateToUpdate.setStartDate(rentalRate.getStartDate());
                rentalRateToUpdate.setEndDate(rentalRate.getEndDate());
                rentalRateToUpdate.setCategory(rentalRate.getCategory());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new RentalRateNotFoundException("Rental Rate Id not provided for rental rate to be updated");
        }
    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        try {
            RentalRate rentalRateToRemove = retrieveRentalRateById(rentalRateId);
            if (rentalRateInUse(rentalRateId).isEmpty()) {
                em.remove(rentalRateToRemove);
            } else {
                rentalRateToRemove.setIsEnabled(false);
            }
        } catch (RentalRateNotFoundException ex) {
            throw new RentalRateNotFoundException("Rental rate of ID: " + rentalRateId + " not found!");
        }
    }

    @Override
    public List<ReservationRecord> rentalRateInUse(Long rentalRateId) throws RentalRateNotFoundException {
        try {
            RentalRate rentalRate = retrieveRentalRateById(rentalRateId);
            List<ReservationRecord> reservationRecords;
            rentalRate.getReservationRecords().size();
            reservationRecords = rentalRate.getReservationRecords();
            return reservationRecords;
        } catch (RentalRateNotFoundException ex) {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public BigDecimal calculateRentalFee(Long categoryId, Date pickUpDateTime, Date returnDateTime) throws NoRentalRateApplicableException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        BigDecimal totalRentalFee = new BigDecimal(0);
        long diff = returnDateTime.getTime() - pickUpDateTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffHours = diff / (60 * 60 * 1000) % 24;
//        System.out.print(diffDays + " days, ");
//        System.out.print(diffHours + " hours, ");
//        System.out.print(diffMinutes + " minutes, ");
//        System.out.print(diffSeconds + " seconds.\n");

//        long countNewDay = (diffHours * 60 * 60) + (diffMinutes * 60) + (diffSeconds);
        if ((diffHours > 0 || diffMinutes > 0 || diffSeconds > 0)) {
            diffDays = diffDays + 1;
        }
        System.out.println(diffDays + " updatedDays, ");

        returnDateTime.setHours(pickUpDateTime.getHours());
        returnDateTime.setMinutes(pickUpDateTime.getMinutes());
        //Date eachDay = pickUpDateTime;

        RentalRateSessionBean obj = new RentalRateSessionBean();
        Calendar eachDay = obj.dateToCalendar(pickUpDateTime);

        try {

            for (int i = 0; i < diffDays; i++) {
                Date currentDate = eachDay.getTime();
                RentalRate cheapestRentalRate = retrieveCheapestRentalRate(categoryId, currentDate);
                eachDay.add(Calendar.DATE, 1);
                BigDecimal dailyCheapestRentalRate = new BigDecimal(cheapestRentalRate.getRateCost());
                totalRentalFee = totalRentalFee.add(dailyCheapestRentalRate);
                System.out.println("Rental Fee is " + cheapestRentalRate.getRateCost() + " " + sdf.format(currentDate));

            }
            return totalRentalFee;
        } catch (NoRentalRateApplicableException ex) {
            throw new NoRentalRateApplicableException();
        }
    }

    @Override
    public RentalRate retrieveCheapestRentalRate(Long categoryId, Date pickupDate) throws NoRentalRateApplicableException {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE (r.category.categoryId = :inCategoryId)"
                + " AND ((r.startDate <= :inCurrentCheckedDate AND r.endDate >= :inCurrentCheckedDate)"
                + " OR (r.startDate IS NULL AND r.endDate IS NULL)) ORDER BY r.rateCost ASC");
        query.setParameter("inCurrentCheckedDate", pickupDate);
        query.setParameter("inCategoryId", categoryId);
        List<RentalRate> rentalRates = query.getResultList();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Description", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
        for (RentalRate rentalRate : rentalRates) {
            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            String startDate = "NOT SET";
            if (rentalRate.getStartDate() != null) {
                startDate = sdf.format(rentalRate.getStartDate());
            }
            String endDate = "NOT SET";
            if (rentalRate.getEndDate() != null) {
                endDate = sdf.format(rentalRate.getEndDate());
            }
            System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateDescription(), rentalRate.getCategory().getCategoryName(),
                    rentalRate.getRateCost(), isEnabled, startDate, endDate);
            if (rentalRate.getRentalRateType() == RentalRateTypeEnum.PEAK) {
                return rentalRate;
            }
        }
        if (rentalRates.isEmpty()) {
            throw new NoRentalRateApplicableException();
        }

        return rentalRates.get(0);
    }

    private Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }
}
