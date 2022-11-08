/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Category;
import entity.Customer;
import exception.CustomerNotFoundException;
import exception.CustomerUsernameExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Wayne
 */
@Remote
public interface CustomerSessionBeanRemote {
    public Long createNewCustomer(Customer customer)throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    public Customer retrieveCustByUsername(String username) throws CustomerNotFoundException;
    public Customer login(String username, String password) throws InvalidLoginCredentialException;
    public List<Customer> retrieveAllCustomers();

    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;
}
