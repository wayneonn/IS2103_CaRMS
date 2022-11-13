/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import exception.PartnerNotFoundException;
import entity.Partner;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.UnknownPersistenceException;
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
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PartnerSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewPartner(Partner partner) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(partner);

            if (constraintViolations.isEmpty()) {
                em.persist(partner);
                em.flush();

                return partner.getPartnerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);

        if (partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner does not exist: " + partnerId);
        }
    }

    @Override
    public Partner updatePartner(Partner updatedPartner) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(updatedPartner);

            if (constraintViolations.isEmpty()) {
                return em.merge(updatedPartner);
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public void deletePartner(Long partnerId) throws PartnerNotFoundException {
        Partner partnerToRemove = retrievePartnerById(partnerId);
        em.remove(partnerToRemove);
    }
    
    @Override
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException {

        try {
            Query query = em.createQuery("SELECT p FROM Partner p where p.partnerUsername = :inUsername");
            query.setParameter("inUsername", username);

            return (Partner) query.getSingleResult();
        } catch (PersistenceException ex) {
            throw new PartnerNotFoundException("Partner Username " + username + "does not exist.");
        }

    }
    
    @Override
    public Partner login(String username, String password) throws InvalidLoginCredentialException {
        try
        {
            Partner partner = retrievePartnerByUsername(username);
            
            if(partner.getPartnerPassword().equals(password))
            {
                return partner;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        }
        catch(PartnerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
}
