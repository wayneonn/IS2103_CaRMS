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
import entity.Cars;
import entity.Employee;
import entity.ReservationRecord;
import enumerations.CarStateEnumeration;
import enumerations.EmployeeEnum;
import exception.InputDataValidationException;
import exception.InvalidAccessRightException;
import exception.InvalidLoginException;
import exception.LicenseNumberExsistsException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalReservationNotFoundException;
import exception.ReservationNotPaidException;
import exception.UnknownPersistenceException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Wayne
 */
public class CustomerServiceModule {

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
    public CustomerServiceModule() {

    }

    public CustomerServiceModule(Employee employee, ModelSessionBeanRemote modelSessionBean, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBean,
            CustomerSessionBeanRemote customerSessionBean, ReservationRecordSessionBeanRemote reservationRecordSessionBean,
            EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean,
            CategorySessionBeanRemote categorySessionBean, CarSessionBeanRemote carSessionBean) {
        this();

        this.employee = employee;
        this.modelSessionBeanRemote = modelSessionBean;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBean;
        this.customerSessionBeanRemote = customerSessionBean;
        this.reservationRecordSessionBeanRemote = reservationRecordSessionBean;
        this.employeeSessionBeanRemote = employeeSessionBean;
        this.outletSessionBeanRemote = outletSessionBean;
        this.categorySessionBeanRemote = categorySessionBean;
        this.carSessionBeanRemote = carSessionBean;

    }

    public void accessRightAllocator() throws InvalidAccessRightException {
        //System.out.println("***   CarMS Management Client || Sales Management Module   ***\n");
        if (employee.getAccessRights() == EmployeeEnum.CUSTSERVICEEXEC) {
            custServiceExecMenu();
        } else {
            throw new InvalidAccessRightException("You do not have CUSTOMER EXECUTIVE rights to access the customer service module.");
        }
    }

    public void custServiceExecMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer choice = 0;

        while (true) {
            System.out.println("***   CarMS Management Client || Customer Executive Menu   ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");
            choice = 0;

            while (choice < 1 || choice > 4) {
                System.out.print("> ");
                choice = scanner.nextInt();
                if (choice == 1) {
                    pickUpCar();
                } else if (choice == 2) {
                    returnCar();
                } else if (choice == 3) {
                    break;
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            if (choice == 3) {
                break;
            }
        }
    }

    public void pickUpCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***   CarMS Management Client || Customer Service Management || Pickup Car   ***\n");
        List<ReservationRecord> reservationRecords = reservationRecordSessionBeanRemote.
                retrieveCustomerReservationsRecordsByPickupOutletId(employee.getOutlet().getOutletId());
        if (reservationRecords.isEmpty()) {
            System.out.println("No cars are available to be picked up!");
        } else {
            try {
                System.out.print("This is the list of reservations you are able to choose from. Please enter"
                        + " the ID you would like to choose from.\n\n");
                System.out.printf("%4s%20s%20s\n", "ID", "Start Date", "End Date");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                for (ReservationRecord reservationRecord : reservationRecords) {
                    System.out.printf("%4s%20s%20s\n", reservationRecord.getReservationId(),
                            sdf.format(reservationRecord.getPickupDateTime()),
                            sdf.format(reservationRecord.getReturnDateTime()));
                }
                System.out.print("Enter Rental Reservation ID> ");
                Long reservationRecordId = scanner.nextLong();
                scanner.nextLine();
                ReservationRecord reservationRecord = reservationRecordSessionBeanRemote.retrieveReservationRecordById(reservationRecordId);
                System.out.println("\nYou have entered ID :" + reservationRecordId + "\n");
                reservationRecord.getPaid();
                if (!reservationRecord.getPaid()) {
                    System.out.print("To pay rental fee? (Enter 'Y' to pay)> ");
                    String input = scanner.nextLine().trim();
                    if (!input.equals("Y")) {
                        throw new ReservationNotPaidException("Please pay for the rental reservation ID: " + reservationRecordId + " !");
                    } else {
                        System.out.println("The amount of $" + reservationRecord.getAmtPaid().toString() + " is charged to the credit card number: " + reservationRecord.getCreditCardNum());
                    }
                }
                reservationRecordSessionBeanRemote.pickupCar(reservationRecordId);
                System.out.println("The car is successfully picked up by customer");
            } catch (RentalReservationNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (ReservationNotPaidException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    public void returnCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***   CarMS Management Client || Customer Service Management || Return Car   ***\n");

        List<ReservationRecord> reservationRecords = reservationRecordSessionBeanRemote.
                retrieveCustomerRentalReservationsByReturnOutletId(employee.getOutlet().getOutletId());

        if (reservationRecords.isEmpty()) {
            System.out.println("No cars to be returned!");
        } else {
            System.out.print("This is the list of reservations you are able to choose from to return the car. Please enter"
                    + " the ID you would like to choose from.\n\n");
            System.out.printf("%4s%20s%20s\n", "ID", "Start Date", "End Date");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (ReservationRecord reservationRecord : reservationRecords) {
                System.out.printf("%4s%20s%20s\n", reservationRecord.getReservationId(),
                        sdf.format(reservationRecord.getPickupDateTime()),
                        sdf.format(reservationRecord.getReturnDateTime()));
            }
            System.out.print("Enter Rental Reservation ID> ");
            Long rentalReservationId = scanner.nextLong();
            scanner.nextLine();

            try {
                reservationRecordSessionBeanRemote.returnCar(rentalReservationId);
                System.out.println("Car returned by customer");
            } catch (RentalReservationNotFoundException ex) {
                System.out.println("No Rental Reservation of ID: " + rentalReservationId);
            }
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        }
    }
}
