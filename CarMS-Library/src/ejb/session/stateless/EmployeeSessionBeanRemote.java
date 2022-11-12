/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Category;
import entity.Customer;
import entity.Employee;
import exception.EmployeeNotFoundException;
import exception.EmployeeUserNameAlreadyExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Wayne
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Long createNewEmployee(Employee employee, Long outletId) throws OutletNotFoundException, InputDataValidationException, EmployeeUserNameAlreadyExistException, UnknownPersistenceException ;

    public List<Employee> retrieveAllEmployees();
    
    public Employee retrieveStaffByUsername(String username) throws EmployeeNotFoundException;
    
    public Employee staffLogin(String username, String password) throws InvalidLoginException;

    public List<Employee> retrieveAllEmployeesByOutletId(Long outletId);

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
}
