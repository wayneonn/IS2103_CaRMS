/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import exception.EmployeeNotFoundException;
import exception.EmployeeUserNameAlreadyExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
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
public class EmployeeSessionBean implements EmployeeSessionBeanLocal, EmployeeSessionBeanRemote {

    @EJB
    private OutletSessionBeanRemote outletSessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public EmployeeSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewEmployee(Employee employee, Long outletId) throws OutletNotFoundException, InputDataValidationException, EmployeeUserNameAlreadyExistException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);

            if (constraintViolations.isEmpty()) {
                em.persist(employee);
                Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
                employee.setOutlet(outlet);
                outlet.getEmployees().add(employee);
                em.flush();
                return employee.getEmployeeId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new EmployeeUserNameAlreadyExistException("Username of " + employee.getEmployeeUsername() + " is already taken!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }

    @Override
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {

        Employee employee = em.find(Employee.class, employeeId);

        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }
    }

    @Override
    public List<Employee> retrieveAllEmployeesByOutletId(Long outletId) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.outlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);

        return query.getResultList();
    }

    @Override
    public Employee retrieveStaffByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.employeeUsername = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee username " + username + " not found in system.");
        }
    }

    @Override
    public Employee staffLogin(String username, String password) throws InvalidLoginException {

        try {
            Employee employee = retrieveStaffByUsername(username);

            if (employee.getEmployeePassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginException("Username does not exist or invalid password!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
