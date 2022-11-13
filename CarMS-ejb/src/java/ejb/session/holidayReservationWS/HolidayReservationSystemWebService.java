/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.holidayReservationWS;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationRecordSessionBeanLocal;
import entity.Cars;
import entity.Category;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.TransitDriverDispatchRecord;
import exception.BeyondOperatingHoursException;
import exception.CategoryNotFoundException;
import exception.InvalidLoginCredentialException;
import exception.ModelNotFoundException;
import exception.NoRentalRateApplicableException;
import exception.OutletNotFoundException;
import exception.PickUpDateAfterReturnDateException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wayneonn
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private ReservationRecordSessionBeanLocal reservationRecordSessionBean;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;
    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod
    public List<Model> retrieveAllModel() {
        List<Model> models = modelSessionBean.retrieveAllModels();

        for (Model model : models) {
            em.detach(model);

            for (Cars car : model.getCars()) {
                em.detach(car);
                car.setOutlet(null);
            }
            
            em.detach(model.getCategory());
            model.getCategory().setModels(null);
        }
        
        
        return models;
    }

    @WebMethod
    public List<Outlet> retrieveAllOutlet() {
        List<Outlet> outlets = outletSessionBean.retrieveAllOutlets();

        for (Outlet outlet : outlets) {
            
            em.detach(outlet);

            for (Cars car : outlet.getCars()) {
                em.detach(car);
                car.setOutlet(null);
            }

            for (TransitDriverDispatchRecord transitDriverDispatchRecord : outlet.getTransitDriverDispatchRecords()) {
                em.detach(transitDriverDispatchRecord);
                transitDriverDispatchRecord.setOutlet(null);
            }

            for (Employee employee : outlet.getEmployees()) {
                em.detach(employee);
                employee.setOutlet(null);
            }
            
        }

        return outlets;
    }

    @WebMethod
    public List<Category> retrieveAllCategory() {
        return categorySessionBean.retrieveAllCategories();
    }

    @WebMethod
    public Outlet retrieveOutletById(@WebParam Long outletId) throws OutletNotFoundException, BeyondOperatingHoursException, PickUpDateAfterReturnDateException, ModelNotFoundException, NoRentalRateApplicableException {
        return outletSessionBean.retrieveOutletById(outletId);
    }

    @WebMethod
    public Partner login(@WebParam String username, @WebParam String password) throws InvalidLoginCredentialException {
        return partnerSessionBean.login(username, password);
    }

    @WebMethod
    public List<Cars> retrieveCarsByCategoryId(@WebParam Long categoryId) {
        return carSessionBean.retrieveCarsByCategoryId(categoryId);
    }

    @WebMethod
    public BigDecimal calculateRentalFee(@WebParam Long categoryId, @WebParam Date pickUpDateTime, @WebParam Date returnDateTime) throws NoRentalRateApplicableException {
        return rentalRateSessionBean.calculateRentalFee(categoryId, pickUpDateTime, returnDateTime);
    }

    @WebMethod
    public List<Model> retrieveAvailAllModels() {
        return modelSessionBean.retrieveAvailAllModels();
    }

    @WebMethod
    public Boolean searchCarByCategory(@WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long categoryId, @WebParam Date pickupDate, @WebParam Date returnDate) throws CategoryNotFoundException {
        return carSessionBean.searchCarByCategory(pickupOutletId, returnOutletId, categoryId, pickupDate, returnDate);
    }

    @WebMethod
    public Boolean searchCarByModel(Long pickupOutletId, Long returnOutletId, Long modelId, Date pickupDate, Date returnDate) throws ModelNotFoundException {
        return carSessionBean.searchCarByModel(pickupOutletId, returnOutletId, modelId, pickupDate, returnDate);
    }

    @WebMethod
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException {
        return modelSessionBean.retrieveModelById(modelId);
    }


}
