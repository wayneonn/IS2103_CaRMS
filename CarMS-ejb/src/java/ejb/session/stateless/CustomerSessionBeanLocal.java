/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.CustomerNotFoundException;
import exception.CustomerUsernameExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Wayne
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createNewCustomer(Customer customer)throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    public Customer retrieveCustByUsername(String username) throws CustomerNotFoundException;
    public Customer login(String username, String password) throws InvalidLoginCredentialException;
    public List<Customer> retrieveAllCustomers();
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;
}
