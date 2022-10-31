/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 *
 * @author User
 */
public interface RentalRateSessionBeanLocal {
    public List<RentalRate> retrieveRentalRates();
    public Long createNewRentalRate(RentalRate rentalRate, Long categoryId) throws CategoryNotFoundException, InputDataValidationException, UnknownPersistenceException, PersistenceException;
}
