/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import entity.Outlet;
import exception.EmployeeNotFoundException;
import exception.InvalidLoginException;
import exception.OutletNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewEmployee(Employee employee, Long outletId) throws OutletNotFoundException {
        
        try {
            em.persist(employee);
            Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
            employee.setOutlet(outlet);
            outlet.getEmployees().add(employee);
            em.flush();
            return employee.getEmployeeId();
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

}
