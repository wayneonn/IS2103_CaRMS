/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import exception.CarNotFoundException;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.LicenseNumberExsistsException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.Date;
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

    public Cars retrieveCarsById(Long categoryId) throws CarNotFoundException;

    public Cars updateCar(Cars updatedCars) throws CarNotFoundException, InputDataValidationException;

    public void deleteCar(Long carId) throws CarNotFoundException;

    public List<Cars> carsInUse(Long carId);

    public List<Cars> retrieveCarsByCategoryId(Long categoryId);

    public List<Cars> retrieveCarsByModelId(Long modelId);

    public Boolean searchCarByCategory(Long pickupOutletId, Long returnOutletId, Long categoryId, Date pickupDate, Date returnDate) throws CategoryNotFoundException;

    public Boolean searchCarByModel(Long pickupOutletId, Long returnOutletId, Long modelId, Date pickupDate, Date returnDate) throws ModelNotFoundException;
}
