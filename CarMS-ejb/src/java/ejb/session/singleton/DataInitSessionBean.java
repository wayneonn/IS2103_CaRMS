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
import entity.Partner;
import entity.RentalRate;
import enumerations.CarStateEnumeration;
import enumerations.EmployeeEnum;
import enumerations.RentalRateTypeEnum;
import exception.CategoryNameAlreadyExistException;
import exception.CategoryNotFoundException;
import exception.CustomerUsernameExistException;
import exception.EmployeeUserNameAlreadyExistException;
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
    private PartnerSessionBeanLocal partnerSessionBean1;

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

    Long outletAId;
    Long outletBId;
    Long outletCId;
    //Long outletDId;

    Long categoryStandardSedanId;
    Long categoryFamilySedanId;
    Long categoryLuxurySedanId;
    Long categoryMinivanSuvId;

    Long modelAId;
    Long modelBId;
    Long modelCId;
    Long modelDId;
    Long modelEId;
    Long modelFId;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {

        LocalTime openingHour = LocalTime.parse("08:00");
        LocalTime closingHour = LocalTime.parse("22:00");

        try {
            if (em.find(Outlet.class, 1l) == null) {
                outletAId = outletSessionBean.createNewOutlet(new Outlet("Kent Ridge Drive", "Outlet A", null, null));
                outletBId = outletSessionBean.createNewOutlet(new Outlet("Holland Village Drive", "Outlet B", null, null));
                outletCId = outletSessionBean.createNewOutlet(new Outlet("Lentor Plains", "Outlet C", openingHour, closingHour));
            }
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            if (em.find(Employee.class, 1l) == null) {
                employeeSessionBean.createNewEmployee(new Employee("A1", "password", "Employee A1", EmployeeEnum.SALESMANAGER), outletAId);
                employeeSessionBean.createNewEmployee(new Employee("A2", "password", "Employee A2", EmployeeEnum.OPERATIONSMANAGER), outletAId);
                employeeSessionBean.createNewEmployee(new Employee("A3", "password", "Employee A3", EmployeeEnum.CUSTSERVICEEXEC), outletAId);
                employeeSessionBean.createNewEmployee(new Employee("A4", "password", "Employee A4", EmployeeEnum.EMPLOYEE), outletAId);
                employeeSessionBean.createNewEmployee(new Employee("A5", "password", "Employee A5", EmployeeEnum.EMPLOYEE), outletAId);

                employeeSessionBean.createNewEmployee(new Employee("B1", "password", "Employee B1", EmployeeEnum.SALESMANAGER), outletBId);
                employeeSessionBean.createNewEmployee(new Employee("B2", "password", "Employee B2", EmployeeEnum.OPERATIONSMANAGER), outletBId);
                employeeSessionBean.createNewEmployee(new Employee("B3", "password", "Employee B3", EmployeeEnum.CUSTSERVICEEXEC), outletBId);

                employeeSessionBean.createNewEmployee(new Employee("C1", "password", "Employee C1", EmployeeEnum.SALESMANAGER), outletCId);
                employeeSessionBean.createNewEmployee(new Employee("C2", "password", "Employee C2", EmployeeEnum.OPERATIONSMANAGER), outletCId);
                employeeSessionBean.createNewEmployee(new Employee("C3", "password", "Employee C3", EmployeeEnum.CUSTSERVICEEXEC), outletCId);

            }
        } catch (OutletNotFoundException ex) {
            ex.getMessage();
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        } catch (EmployeeUserNameAlreadyExistException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            if (em.find(Category.class, 1l) == null) {
                categoryStandardSedanId = categorySessionBean.createNewCategory(new Category("Standard Sedan"));
                categoryFamilySedanId = categorySessionBean.createNewCategory(new Category("Family Sedan"));
                categoryLuxurySedanId = categorySessionBean.createNewCategory(new Category("Luxury Sedan"));
                categoryMinivanSuvId = categorySessionBean.createNewCategory(new Category("SUV and Minivan"));
            }
        } catch (CategoryNameAlreadyExistException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            if (em.find(Model.class, 1l) == null) {
                modelAId = modelSessionBean.createNewModel(new Model("Corolla", "Toyota", true), categoryStandardSedanId);
                modelBId = modelSessionBean.createNewModel(new Model("Civic", "Honda", true), categoryStandardSedanId);
                modelCId = modelSessionBean.createNewModel(new Model("Sunny", "Nissan", true), categoryStandardSedanId);
                modelDId = modelSessionBean.createNewModel(new Model("E Class", "Mercedes", true), categoryLuxurySedanId);
                modelEId = modelSessionBean.createNewModel(new Model("5 Series", "BMW", true), categoryLuxurySedanId);
                modelFId = modelSessionBean.createNewModel(new Model("A6", "Audi", true), categoryLuxurySedanId);
            }
        } catch (CategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            if (em.find(Cars.class, 1l) == null) {
                carSessionBean.createNewCar(new Cars("SS00A1TC", CarStateEnumeration.AVAILABLE, "Red", true), outletAId, modelAId);
                carSessionBean.createNewCar(new Cars("SS00A2TC", CarStateEnumeration.AVAILABLE, "Red", true), outletAId, modelAId);
                carSessionBean.createNewCar(new Cars("SS00A3TC", CarStateEnumeration.AVAILABLE, "Red", true), outletAId, modelAId);
                carSessionBean.createNewCar(new Cars("SS00B1HC", CarStateEnumeration.AVAILABLE, "Blue", true), outletBId, modelBId);
                carSessionBean.createNewCar(new Cars("SS00B2HC", CarStateEnumeration.AVAILABLE, "Blue", true), outletBId, modelBId);
                carSessionBean.createNewCar(new Cars("SS00B3HC", CarStateEnumeration.AVAILABLE, "Blue", true), outletBId, modelBId);
                carSessionBean.createNewCar(new Cars("SS00C1NS", CarStateEnumeration.AVAILABLE, "Yellow", true), outletCId, modelCId);
                carSessionBean.createNewCar(new Cars("SS00C2NS", CarStateEnumeration.AVAILABLE, "Yellow", true), outletCId, modelCId);
                carSessionBean.createNewCar(new Cars("SS00C3NS", CarStateEnumeration.REPAIRING, "Yellow", true), outletCId, modelCId);
                carSessionBean.createNewCar(new Cars("LS00A4ME", CarStateEnumeration.AVAILABLE, "Yellow", true), outletCId, modelDId);
                carSessionBean.createNewCar(new Cars("LS00B4B5", CarStateEnumeration.AVAILABLE, "Yellow", true), outletCId, modelEId);
                carSessionBean.createNewCar(new Cars("LS00C4A6", CarStateEnumeration.AVAILABLE, "Yellow", true), outletCId, modelFId);
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
            Date startDateTime1 = df.parse("09/12/2022 12:00");
            Date endDateTime1 = df.parse("11/12/2022 00:00");
            Date startDateTime2 = df.parse("05/12/2022 00:00");
            Date endDateTime2 = df.parse("05/12/2022 23:59");
            Date startDateTime3 = df.parse("06/12/2022 00:00");
            Date endDateTime3 = df.parse("06/12/2022 23:59");
            Date startDateTime4 = df.parse("07/12/2022 12:00");
            Date endDateTime4 = df.parse("08/12/2022 12:00");
            Date startDateTime5 = df.parse("07/12/2022 00:00");
            Date endDateTime5 = df.parse("07/12/2022 23:59");
            RentalRate standardSedanDefaultRate = new RentalRate("Standard Sedan - Default", RentalRateTypeEnum.DEFAULT, 100.0, true);
            RentalRate standardSedanPromo = new RentalRate("Standard Sedan - Weekend Promo", RentalRateTypeEnum.PROMOTIONAL, 80.0, true);
            RentalRate familySedanDefaultRate = new RentalRate("Family Sedan - Default", RentalRateTypeEnum.DEFAULT, 200.0, true);
            RentalRate luxurySedanDefaultRate = new RentalRate("Luxury Sedan - Default", RentalRateTypeEnum.DEFAULT, 300.0, true);
            RentalRate luxurySedanPeak = new RentalRate("Luxury Sedan - Monday", RentalRateTypeEnum.PEAK, 310.0, true);
            RentalRate luxurySedanPeak2 = new RentalRate("Luxury Sedan - Tuesday", RentalRateTypeEnum.PEAK, 320.0, true);
            RentalRate luxurySedanPeak3 = new RentalRate("Luxury Sedan - Wednesday", RentalRateTypeEnum.PEAK, 330.0, true);
            RentalRate luxurySedanPromo = new RentalRate("Luxury Sedan - Weekday Promo", RentalRateTypeEnum.PROMOTIONAL, 250.0, true);
            RentalRate SUVSedanDefault = new RentalRate("SUV/Minivan - Default Rate", RentalRateTypeEnum.DEFAULT, 400.0, true);

            if (em.find(RentalRate.class, 1l) == null) {
                standardSedanDefaultRate.setStartDate(null);
                standardSedanDefaultRate.setEndDate(null);
                standardSedanPromo.setStartDate(startDateTime1);
                standardSedanPromo.setEndDate(endDateTime1);
                familySedanDefaultRate.setStartDate(null);
                familySedanDefaultRate.setEndDate(null);
                luxurySedanDefaultRate.setStartDate(null);
                luxurySedanDefaultRate.setEndDate(null);
                luxurySedanPeak.setStartDate(startDateTime2);
                luxurySedanPeak.setEndDate(endDateTime2);
                luxurySedanPeak2.setStartDate(startDateTime3);
                luxurySedanPeak2.setEndDate(endDateTime3);
                luxurySedanPeak3.setStartDate(startDateTime5);
                luxurySedanPeak3.setEndDate(endDateTime5);
                luxurySedanPromo.setStartDate(startDateTime4);
                luxurySedanPromo.setEndDate(endDateTime4);
                SUVSedanDefault.setStartDate(null);
                SUVSedanDefault.setEndDate(null);

                rentalRateSessionBean.createNewRentalRate(standardSedanDefaultRate, categoryStandardSedanId);
                rentalRateSessionBean.createNewRentalRate(standardSedanPromo, categoryStandardSedanId);
                rentalRateSessionBean.createNewRentalRate(familySedanDefaultRate, categoryFamilySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanDefaultRate, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanPeak, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanPeak2, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanPeak3, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(luxurySedanPromo, categoryLuxurySedanId);
                rentalRateSessionBean.createNewRentalRate(SUVSedanDefault, categoryMinivanSuvId);
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

        try {
            if (em.find(MCRCustomer.class, 1l) == null) {
                customerSessionBean.createNewCustomer(new MCRCustomer("customer", "password", "S991771s", "92238212", "Wayne", "Yow", "wengonn99@hotmail.com", "1234567890123456"));
            }
        } catch (CustomerUsernameExistException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            if (em.find(Partner.class, 1l) == null) {
                partnerSessionBean.createNewPartner(new Partner("Holiday.com", "partner", "password"));
            }
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
