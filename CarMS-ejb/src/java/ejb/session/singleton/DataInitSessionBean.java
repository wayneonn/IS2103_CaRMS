/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationRecordSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanLocal;
import entity.Category;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import enumerations.EmployeeEnum;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import java.time.LocalTime;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Wayne
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private OutletSessionBeanRemote outletSessionBean1;

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBean;

    @EJB
    private ReservationRecordSessionBeanRemote reservationRecordSessionBean;

    @EJB
    private RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private PartnerSessionBeanRemote partnerSessionBean;

    @EJB
    private OutletSessionBeanRemote outletSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    Long outletAId;
    Long outletBId;
    Long outletCId;

    Long categoryStandardSedanId;
    Long categoryFamilySedanId;
    Long categoryLuxurySedanId;
    Long categoryMinivanSuvId;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
//        if (em.find(Cars.class, 1l) == null) {
//            carSessionBean.createNewCar(new Cars("TESTING", CarStateEnumeration.AVAILABLE, "Toyota", 5));
//        }
        LocalTime openingHour = LocalTime.parse("10:00");
        LocalTime closingHour = LocalTime.parse("22:00");

        if (em.find(Outlet.class, 1l) == null) {
            outletAId = outletSessionBean.createNewOutlet(new Outlet("Kent Ridge Drive", "Outlet A", openingHour, closingHour));
            outletBId = outletSessionBean.createNewOutlet(new Outlet("Holland Village Drive", "Outlet B", openingHour, closingHour));
            outletCId = outletSessionBean.createNewOutlet(new Outlet("Lentor Plains", "Outlet C", openingHour, closingHour));
        }

        try {
            if (em.find(Employee.class, 1l) == null) {
                employeeSessionBean.createNewEmployee(new Employee("EmployeeA1", "password", "A1", EmployeeEnum.SALESMANAGER), outletAId);
                employeeSessionBean.createNewEmployee(new Employee("EmployeeB1", "password", "B1", EmployeeEnum.OPERATIONSMANAGER), outletBId);
                employeeSessionBean.createNewEmployee(new Employee("EmployeeC1", "password", "C1", EmployeeEnum.CUSTSERVICEEXEC), outletCId);
            }
        } catch (OutletNotFoundException ex) {
            ex.getMessage();
        }

        if (em.find(Category.class, 1l) == null) {
            categoryStandardSedanId = categorySessionBean.createNewCategory(new Category("Standard Sedan"));
            categoryFamilySedanId = categorySessionBean.createNewCategory(new Category("Family Sedan"));
            categoryLuxurySedanId = categorySessionBean.createNewCategory(new Category("Luxury Sedan"));
            categoryMinivanSuvId = categorySessionBean.createNewCategory(new Category("SUV/Minivan"));
        }

        try {
            if (em.find(Model.class, 1l) == null) {
                modelSessionBean.createNewModel(new Model("Toyota", "Corolla Altis"), categoryStandardSedanId);
                modelSessionBean.createNewModel(new Model("Mercedes Benz", "E200"), categoryLuxurySedanId);
                modelSessionBean.createNewModel(new Model("Nissan", "Qashqai"), categoryMinivanSuvId);
                modelSessionBean.createNewModel(new Model("Toyota", "Picnic"), categoryFamilySedanId);
            }
        } catch (CategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
