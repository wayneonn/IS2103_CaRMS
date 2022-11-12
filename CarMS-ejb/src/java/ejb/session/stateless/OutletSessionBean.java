/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import java.util.Set;
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
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OutletSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewOutlet(Outlet outlet) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<Outlet>> constraintViolations = validator.validate(outlet);

            if (constraintViolations.isEmpty()) {
                em.persist(outlet);
                em.flush();

                return outlet.getOutletId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Outlet>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException {

        Outlet outlet = em.find(Outlet.class,
                outletId);

        if (outlet != null) {
            return outlet;
        } else {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
    }

    @Override
    public Outlet updateOutlet(Outlet updatedOutlet) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<Outlet>> constraintViolations = validator.validate(updatedOutlet);
            if (constraintViolations.isEmpty()) {
                return em.merge(updatedOutlet);

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public void deleteOutlet(Long outletId) throws OutletNotFoundException {
        Outlet outletToRemove = retrieveOutletById(outletId);
        em.remove(outletToRemove);
    }

    @Override
    public List<Outlet> retrieveAllOutlets() {
        Query query = em.createQuery("SELECT o FROM Outlet o");
        query.getResultList().size();
        return query.getResultList();
    }
}
