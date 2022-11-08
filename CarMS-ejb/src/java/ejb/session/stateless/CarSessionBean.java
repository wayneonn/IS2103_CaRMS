/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Model;
import entity.Outlet;
import entity.ReservationRecord;
import exception.CarNotFoundException;
import exception.InputDataValidationException;
import exception.LicenseNumberExsistsException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Wayne
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private OutletSessionBeanRemote outletSessionBean;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCar(Cars car, Long outletId, Long modelId) throws OutletNotFoundException, ModelNotFoundException, InputDataValidationException, LicenseNumberExsistsException, UnknownPersistenceException {
        car.setLocation("Location");
        Set<ConstraintViolation<Cars>> constraintViolations = validator.validate(car);
        try {
            if (constraintViolations.isEmpty()) {
                try {
                    Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
                    Model model = modelSessionBean.retrieveModelById(modelId);
                    //car.setIsEnabled(true);
                    car.setLocation(outlet.getOutletName());
                    System.out.println("outlet.getOutletName()" + outlet.getOutletName());

                    //em.persist(car);
                    System.out.println("Outlet " + outlet.getOutletName());
                    System.out.println("Car " + car.getLicenseNumber());
                    //outlet.addCar(car);
                    model.addCar(car);
                    car.setModel(model);
                    car.setOutlet(outlet);
                    em.persist(car);
                    em.flush();
                    return car.getCarId();
                } catch (OutletNotFoundException ex) {
                    throw new OutletNotFoundException("Outlet Not Found for ID: " + outletId);
                } catch (ModelNotFoundException ex) {
                    throw new ModelNotFoundException("Model Not Found for ID: " + modelId);
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new LicenseNumberExsistsException("License Number: " + car.getLicenseNumber() + " exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Cars>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<Cars> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Cars c ORDER BY c.model.category, c.model.make, c.model.model, c.licenseNumber ASC");

        return query.getResultList();
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public Cars retrieveCarsById(Long categoryId) throws CarNotFoundException {
        
        Cars category = em.find(Cars.class, categoryId);
        
        if (category != null){
            return category;
        } else {
            throw new CarNotFoundException("Cars ID " + categoryId + " does not exist!");
        }
    }
    
    @Override
    public Cars updateCar(Cars updatedCars) throws CarNotFoundException, InputDataValidationException {
        if (updatedCars != null && updatedCars.getCarId()!= null) {
            Set<ConstraintViolation<Cars>> constraintViolations = validator.validate(updatedCars);

            if (constraintViolations.isEmpty()) {
                return em.merge(updatedCars);
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarNotFoundException("Car ID not provided for car to be updated");
        }
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException {
        try {
            Cars modelToRemove = retrieveCarsById(carId);
            if (carsInUse(carId).isEmpty()) {
                em.remove(modelToRemove);
            } else {
                modelToRemove.setIsEnabled(false);
            }
        } catch (CarNotFoundException ex) {
            throw new CarNotFoundException("Car of ID: " + carId + " not found!");
        }
    }
    
    @Override
    public List<Cars> carsInUse(Long carId) {
        Query query = em.createQuery("SELECT c FROM Cars c WHERE c.carState != enumerations.CarStateEnumeration.AVAILABLE AND c.carId = :inCarId");
        query.setParameter("inCarId", carId);
        query.getResultList().size();
        return query.getResultList();
    }
    
    /*
    @Override
    public List<ReservationRecord> searchCars(Date carPickUpDateTime, String pickUpOutlet, Date carReturnDateTime, String returnOutlet)
    {
        Random random = new Random();
        
        List<ReservationRecord> cars = new ArrayList<>();
        
        GregorianCalendar pickupDateTimeCalendar = new GregorianCalendar();
        pickupDateTimeCalendar.setTime(carPickUpDateTime);
        pickupDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, 0);
        Date pickupDateTime = pickupDateTimeCalendar.getTime();
        
        GregorianCalendar returnDateTimeCalendar = new GregorianCalendar();
        returnDateTimeCalendar.setTime(carReturnDateTime);
        returnDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, -1);
        Date returnDateTime = returnDateTimeCalendar.getTime();
        
        String carModel = String.valueOf((char)(random.nextInt(26) + 'A')) + String.valueOf((char)(random.nextInt(26) + 'A')) + String.valueOf((char)(random.nextInt(26) + 'A'));
        
        carModel.add(new ItineraryItem(1, collectDateTime, "Collect rental car model " + rentalCarModel));
        carModel.add(new ItineraryItem(2, returnDateTime, "Return rental car model " + rentalCarModel));
        
        return rentalCars;
    }
*/
}
