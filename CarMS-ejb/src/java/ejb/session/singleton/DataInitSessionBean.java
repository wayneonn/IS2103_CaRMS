/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Cars;
import entity.Category;
import entity.Customer;
import entity.Employee;
import entity.MCRCustomer;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import enumerations.CarStateEnumeration;
import enumerations.EmployeeEnum;
import enumerations.RentalRateTypeEnum;
import exception.CategoryNotFoundException;
import exception.CustomerUsernameExistException;
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
    private CustomerSessionBeanRemote customerSessionBean;

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
    
    @EJB
    private CarSessionBeanRemote carSessionBeanRemote;

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
                modelAId = modelSessionBean.createNewModel(new Model("Corolla Altis", "Toyota", true), categoryStandardSedanId);
                modelBId = modelSessionBean.createNewModel(new Model("E200", "Mercedes Benz", true), categoryLuxurySedanId);
                modelCId = modelSessionBean.createNewModel(new Model("Qashqai", "Nissan", true), categoryMinivanSuvId);
                modelDId = modelSessionBean.createNewModel(new Model("Picnic", "Toyota", true), categoryFamilySedanId);
            }
        } catch (CategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            if (em.find(Cars.class, 1l) == null) {
                carSessionBean.createNewCar(new Cars("SKU1856P", CarStateEnumeration.AVAILABLE, "Red", true), outletAId, modelAId);
                carSessionBean.createNewCar(new Cars("SPV2132U", CarStateEnumeration.AVAILABLE, "Blue", true), outletBId, modelBId);
                carSessionBean.createNewCar(new Cars("SGD6421Z", CarStateEnumeration.AVAILABLE, "Yellow", true), outletCId, modelCId);
                carSessionBean.createNewCar(new Cars("SID4221L", CarStateEnumeration.AVAILABLE, "Green", true), outletDId, modelDId);
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
            Date startDateTime1 = df.parse("31/10/2022 00:00");
            Date endDateTime1 = df.parse("04/11/2022 00:00");
            Date startDateTime2 = df.parse("20/10/2022 00:00");
            Date endDateTime2 = df.parse("03/11/2022 00:00");
            Date startDateTime3 = df.parse("01/10/2022 00:00");
            Date endDateTime3 = df.parse("01/11/2022 00:00");
            RentalRate standardSedanNonPeakRate = new RentalRate("Standard Sedan - Non Peak Rate", RentalRateTypeEnum.NONPEAK, 100.0, true);
            RentalRate standardSedanPeakRate = new RentalRate("Standard Sedan - Peak Rate", RentalRateTypeEnum.PEAK, 200.0, true);
            RentalRate standardSedanPromo = new RentalRate("Standard Sedan - Promotional", RentalRateTypeEnum.PEAK, 70.0, true);
            RentalRate familySedanNonPeakRate = new RentalRate("Family Sedan - Non Peak Rate", RentalRateTypeEnum.NONPEAK, 150.0, true);
            RentalRate familySedanPeakRate = new RentalRate("Family Sedan - Non Peak Rate", RentalRateTypeEnum.NONPEAK, 300.0, true);
            RentalRate familySedanPromoRate = new RentalRate("Family Sedan - Promotional", RentalRateTypeEnum.PROMOTIONAL, 100.0, true);
            RentalRate luxurySedanNonPeak = new RentalRate("Luxury Sedan - Non Peak Rate", RentalRateTypeEnum.NONPEAK, 400.0, true);
            RentalRate luxurySedanPeak = new RentalRate("Luxury Sedan - Peak Rate", RentalRateTypeEnum.NONPEAK, 600.0, true);
            RentalRate luxurySedanPromo = new RentalRate("Luxury Sedan - Promotional", RentalRateTypeEnum.PROMOTIONAL, 300.0, true);
            RentalRate SUVSedanNonPeak = new RentalRate("SUV/Minivan - Non Peak Rate", RentalRateTypeEnum.NONPEAK, 500.0, true);
            RentalRate SUVSedanPeak = new RentalRate("SUV/Minivan - Peak Rate", RentalRateTypeEnum.NONPEAK, 800.0, true);
            RentalRate SUVSedanPromo = new RentalRate("SUV/Minivan - Promotional", RentalRateTypeEnum.PROMOTIONAL, 400.0, true);

            if (em.find(RentalRate.class, 1l) == null) {
                standardSedanNonPeakRate.setStartDate(startDateTime1);
                standardSedanNonPeakRate.setEndDate(endDateTime1);
                standardSedanPeakRate.setStartDate(startDateTime2);
                standardSedanPeakRate.setEndDate(endDateTime2);
                standardSedanPromo.setStartDate(startDateTime3);
                standardSedanPromo.setEndDate(endDateTime3);
                rentalRateSessionBean.createNewRentalRate(standardSedanNonPeakRate, categoryStandardSedanId);
                rentalRateSessionBean.createNewRentalRate(standardSedanPeakRate, categoryStandardSedanId);
                rentalRateSessionBean.createNewRentalRate(standardSedanPromo, categoryStandardSedanId);
                rentalRateSessionBean.createNewRentalRate(familySedanNonPeakRate, categoryFamilySedanId);
                rentalRateSessionBean.createNewRentalRate(familySedanPeakRate, categoryFamilySedanId);
                rentalRateSessionBean.createNewRentalRate(familySedanPromoRate, categoryFamilySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanNonPeak, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanPeak, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanPromo, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(SUVSedanNonPeak, categoryMinivanSuvId);
                rentalRateSessionBean.createNewRentalRate(SUVSedanPeak, categoryMinivanSuvId);
                rentalRateSessionBean.createNewRentalRate(SUVSedanPromo, categoryMinivanSuvId);
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
        
        try{
            if (em.find(MCRCustomer.class, 1l) == null) {
            customerSessionBean.createNewCustomer(new MCRCustomer("customer", "password", "S991771s", "92238212", "Wayne", "Yow", "wengonn99@hotmail.com", "1234567890123456"));
            customerSessionBean.createNewCustomer(new Customer("Steven", "Halim", "stevenhalim@hotmail.com", "1234567890123456"));
            }
        } catch (CustomerUsernameExistException ex) {
        
        } catch (UnknownPersistenceException ex) {
        
        } catch (InputDataValidationException ex) {
        
        }

    }

}
