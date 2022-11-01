/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import exception.InputDataValidationException;
import exception.LicenseNumberExsistsException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Wayne
 */
@Local
public interface CarSessionBeanLocal {

    public Long createNewCar(Cars car, Long outletId, Long modelId) throws OutletNotFoundException, ModelNotFoundException, InputDataValidationException, LicenseNumberExsistsException, UnknownPersistenceException;

    public List<Cars> retrieveAllCars();
}
