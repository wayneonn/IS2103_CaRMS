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
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Cars;
import entity.Category;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import enumerations.CarStateEnumeration;
import enumerations.EmployeeEnum;
import enumerations.RentalRateTypeEnum;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.LicenseNumberExsistsException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
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
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

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
    Long outletDId;

    Long categoryStandardSedanId;
    Long categoryFamilySedanId;
    Long categoryLuxurySedanId;
    Long categoryMinivanSuvId;

    Long modelAId;
    Long modelBId;
    Long modelCId;
    Long modelDId;

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
            outletDId = outletSessionBean.createNewOutlet(new Outlet("Buona Vista Drive", "Outlet D", openingHour, closingHour));
        }

        try {
            if (em.find(Employee.class, 1l) == null) {
                employeeSessionBean.createNewEmployee(new Employee("EmployeeA1", "password", "A1", EmployeeEnum.SALESMANAGER), outletAId);
                employeeSessionBean.createNewEmployee(new Employee("EmployeeB1", "password", "B1", EmployeeEnum.OPERATIONSMANAGER), outletBId);
                employeeSessionBean.createNewEmployee(new Employee("EmployeeC1", "password", "C1", EmployeeEnum.CUSTSERVICEEXEC), outletCId);
                employeeSessionBean.createNewEmployee(new Employee("EmployeeD1", "password", "D1", EmployeeEnum.CUSTSERVICEEXEC), outletDId);
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
                modelAId = modelSessionBean.createNewModel(new Model("Toyota", "Corolla Altis"), categoryStandardSedanId);
                modelBId = modelSessionBean.createNewModel(new Model("Mercedes Benz", "E200"), categoryLuxurySedanId);
                modelCId = modelSessionBean.createNewModel(new Model("Nissan", "Qashqai"), categoryMinivanSuvId);
                modelDId = modelSessionBean.createNewModel(new Model("Toyota", "Picnic"), categoryFamilySedanId);
            }
        } catch (CategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            if (em.find(Cars.class, 1l) == null) {
                carSessionBean.createNewCar(new Cars("SKU1856P", CarStateEnumeration.AVAILABLE, "Red"), outletAId, modelAId);
                carSessionBean.createNewCar(new Cars("SPV2132U", CarStateEnumeration.AVAILABLE, "Blue"), outletBId, modelBId);
                carSessionBean.createNewCar(new Cars("SGD6421Z", CarStateEnumeration.AVAILABLE, "Yellow"), outletCId, modelCId);
                carSessionBean.createNewCar(new Cars("SID4221L", CarStateEnumeration.AVAILABLE, "Green"), outletDId, modelDId);
            }
        } catch (OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (LicenseNumberExsistsException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date startDateTime = df.parse("31/10/2022 00:00");
            Date endDateTime = df.parse("04/11/2022 00:00");
            RentalRate standardSedanNonPeakRate = new RentalRate("Standard Sedan - Non Peak Rate", RentalRateTypeEnum.NONPEAK ,100.0);
            
            if(em.find(RentalRate.class, 1l) == null){
                standardSedanNonPeakRate.setStartDate(startDateTime);
                standardSedanNonPeakRate.setEndDate(endDateTime);
                rentalRateSessionBean.createNewRentalRate(standardSedanNonPeakRate, outletAId);
            }
            
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        } catch (CategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
