/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
import entity.ReservationRecord;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.RentalRateNotFoundException;
import exception.UnknownPersistenceException;
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
                    Category carCategory = categorySessionBean.retrieveCategoryById(categoryId);
                    carCategory.addRentalRate(rentalRate);
                    rentalRate.setCategory(carCategory);
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
        Query query = em.createQuery("SELECT r FROM RentalRate r");
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
}
