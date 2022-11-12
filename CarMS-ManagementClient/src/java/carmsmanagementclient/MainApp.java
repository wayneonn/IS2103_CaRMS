/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationRecordSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import entity.Employee;
import exception.InvalidAccessRightException;
import exception.InvalidLoginException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Wayne
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private ReservationRecordSessionBeanRemote reservationRecordSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private EjbTimerSessionBeanRemote ejbTimerSessionBean;

    private Employee employee;

    private SalesManagementModule salesManagementModule;
    private CustomerServiceModule customerServiceModule;

    /**
     * @param args the command line arguments
     */
    public MainApp() {

    }

    public MainApp(ModelSessionBeanRemote modelSessionBean, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBean,
            CustomerSessionBeanRemote customerSessionBean, ReservationRecordSessionBeanRemote reservationRecordSessionBean,
            EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean,
            CategorySessionBeanRemote categorySessionBean, CarSessionBeanRemote carSessionBean, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote) {
        this();

        this.modelSessionBeanRemote = modelSessionBean;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBean;
        this.customerSessionBeanRemote = customerSessionBean;
        this.reservationRecordSessionBeanRemote = reservationRecordSessionBean;
        this.employeeSessionBeanRemote = employeeSessionBean;
        this.outletSessionBeanRemote = outletSessionBean;
        this.categorySessionBeanRemote = categorySessionBean;
        this.carSessionBeanRemote = carSessionBean;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.ejbTimerSessionBean = ejbTimerSessionBeanRemote;
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (true) {
            System.out.println("This is the Car Management System | Management Client\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            choice = 0;
            while (choice < 1 || choice > 2) {
                System.out.print(" >> ");
                choice = sc.nextInt();
                if (choice == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        mainMenu();
                    } catch (InvalidLoginException ex) {
                        System.out.println("Invalid login credentials " + ex.getMessage());
                    }
                } else if (choice == 2) {
                    break;
                } else if (choice == 3) {
//                    try {
//                        bypassLogin();
//                        System.out.println("Login successful!\n");
//                        mainMenu();
//                    } catch (InvalidLoginException ex) {
//                        System.out.println("Invalid login credentials " + ex.getMessage());
//                    }
                    allocateCars();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (choice == 2) {
                break;
            }
        }

    }

    private void bypassLogin() throws InvalidLoginException { // TESTING
        //Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("Bypass Login.\n");
        //System.out.print("Enter username >> ");
        username = "A3";
        //System.out.print("Enter password >> ");
        password = "password";

        if (username.length() > 0 && password.length() > 0) {
            employee = employeeSessionBeanRemote.staffLogin(username, password);
        } else {
            throw new InvalidLoginException("Missing login credential!");
        }
        System.out.println("");
    }

    private void doLogin() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("Please Login to the System.\n");
        System.out.print("Enter username >> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password >> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            employee = employeeSessionBeanRemote.staffLogin(username, password);
        } else {
            throw new InvalidLoginException("Missing login credential!");
        }
        System.out.println("");
    }

    private void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer choice = 0;

        while (true) {
            System.out.println("You are login as " + employee.getEmployeeName() + " with " + employee.getAccessRights().toString() + " rights\n");
            System.out.println("1: Sales Management");
            System.out.println("2: Customer Service");
            System.out.println("3: Logout\n");
            choice = 0;

            while (choice < 1 || choice > 3) {
                System.out.print(" >> ");
                choice = scanner.nextInt();

                try {
                    if (choice == 1) {
                        salesManagementModule = new SalesManagementModule(employee, modelSessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote,
                                customerSessionBeanRemote, reservationRecordSessionBeanRemote, employeeSessionBeanRemote,
                                outletSessionBeanRemote, categorySessionBeanRemote, carSessionBeanRemote, rentalRateSessionBeanRemote);
                        salesManagementModule.accessRightAllocator();
                    } else if (choice == 2) {
                        customerServiceModule = new CustomerServiceModule(employee, modelSessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote,
                                customerSessionBeanRemote, reservationRecordSessionBeanRemote, employeeSessionBeanRemote,
                                outletSessionBeanRemote, categorySessionBeanRemote, carSessionBeanRemote);
                        customerServiceModule.accessRightAllocator();
                    } else if (choice == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InvalidAccessRightException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (choice == 3) {
                break;
            }
        }
    }

    private void allocateCars() {
        try {
            System.out.println("Generate car allocation for dates 05/12/2022, 06/12/2022, 07/12/2022, 08/12/2022");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date allocationDate = dateFormat.parse("05/12/2022");
            ejbTimerSessionBean.allocateCarsToCurrentDayReservations(allocationDate);
            allocationDate = dateFormat.parse("06/12/2022");
            ejbTimerSessionBean.allocateCarsToCurrentDayReservations(allocationDate);
            allocationDate = dateFormat.parse("07/12/2022");
            ejbTimerSessionBean.allocateCarsToCurrentDayReservations(allocationDate);
            allocationDate = dateFormat.parse("08/12/2022");
            ejbTimerSessionBean.allocateCarsToCurrentDayReservations(allocationDate);
            
            System.out.println("Car allocation generated!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
