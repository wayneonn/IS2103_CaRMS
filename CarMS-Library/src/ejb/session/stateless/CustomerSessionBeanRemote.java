/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Category;
import entity.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Wayne
 */
@Remote
public interface CustomerSessionBeanRemote {
    public Long createNewCustomer(Customer customer);

    public List<Customer> retrieveAllCustomers();
}
