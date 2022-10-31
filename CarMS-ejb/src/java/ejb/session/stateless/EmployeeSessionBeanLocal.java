/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import exception.OutletNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Wayne
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Long createNewEmployee(Employee employee, Long outletId) throws OutletNotFoundException ;

    public List<Employee> retrieveAllEmployees();
}
