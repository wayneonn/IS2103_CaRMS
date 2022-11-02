/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.InvalidLoginCredentialException;
import exception.CustomerNotFoundException;
import exception.CustomerUsernameExistException;
import exception.InputDataValidationException;
import exception.UnknownPersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Wayne
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal, CustomerSessionBeanRemote {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomerSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newCustomer);
                em.flush();

                return newCustomer.getCustEmail();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CustomerUsernameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");

        return query.getResultList();
    }
    
    @Override 
    public Customer retrieveCustByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT C FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Customer login(String username, String password) throws InvalidLoginCredentialException {
        try
        {
            Customer customer = retrieveCustByUsername(username);
            
            if(customer.getCustPassword().equals(password))
            {
                return customer;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
    
    @Override
    public Car searchCar(LocalDateTime pickupDateTime, LocalDateTime returnDateTime, ) {
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
