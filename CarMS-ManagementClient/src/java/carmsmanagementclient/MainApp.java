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
import ejb.session.stateless.ReservationRecordSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Employee;
import exception.InvalidLoginException;
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

    private Employee employee;

    /**
     * @param args the command line arguments
     */
    public MainApp() {

    }

    public MainApp(ModelSessionBeanRemote modelSessionBean, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBean,
            CustomerSessionBeanRemote customerSessionBean, ReservationRecordSessionBeanRemote reservationRecordSessionBean,
            EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean,
            CategorySessionBeanRemote categorySessionBean, CarSessionBeanRemote carSessionBean) {
        this();

        this.modelSessionBeanRemote = modelSessionBean;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBean;
        this.customerSessionBeanRemote = customerSessionBean;
        this.reservationRecordSessionBeanRemote = reservationRecordSessionBean;
        this.employeeSessionBeanRemote = employeeSessionBean;
        this.outletSessionBeanRemote = outletSessionBean;
        this.categorySessionBeanRemote = categorySessionBean;
        this.carSessionBeanRemote = carSessionBean;

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
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (choice == 2) {
                break;
            }
        }

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

            while (choice < 1 || choice > 3) {
                System.out.print(" >> ");
                choice = scanner.nextInt();
            }
            if (choice == 3) {
                break;
            }
        }
    }
}
