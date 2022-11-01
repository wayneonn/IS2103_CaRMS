/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import entity.ReservationRecord;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.RentalRateNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.PersistenceException;

/**
 *
 * @author User
 */
@Local
public interface RentalRateSessionBeanLocal {

    public List<RentalRate> retrieveRentalRates();

    public Long createNewRentalRate(RentalRate rentalRate, Long categoryId) throws CategoryNotFoundException, InputDataValidationException, UnknownPersistenceException, PersistenceException;

    //public String viewRentalRates();
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException;
    
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException;
    
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;
    
    public List<ReservationRecord> rentalRateInUse(Long rentalRateId) throws RentalRateNotFoundException;
}
