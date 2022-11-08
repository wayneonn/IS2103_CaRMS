/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.ReservationRecordSessionBeanRemote;
import entity.MCRCustomer;
import entity.ReservationRecord;
import exception.CustomerUsernameExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.ReservationNotFoundException;
import exception.UnknownPersistenceException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
/**
 *
 * @author User
 */
public class MainApp {
    
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private ReservationRecordSessionBeanRemote reservationRecordSessionBeanRemote;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private MCRCustomer mcrCustomerEntity;
    
    public MainApp() {       
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public MainApp(CategorySessionBeanRemote categorySessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, ReservationRecordSessionBeanRemote reservationRecordSessionBeanRemote, MCRCustomer mcrCustomerEntity) {       
        this();
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.reservationRecordSessionBeanRemote = reservationRecordSessionBeanRemote;
        this.mcrCustomerEntity = mcrCustomerEntity;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Merlion Car Rental! ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    registerCustomer();
                }
                else if (response == 3) {
                    break;
                }
                else 
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CaRM System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            mcrCustomerEntity = customerSessionBeanRemote.login(username, password);      
        }
        else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void registerCustomer() {
        Scanner scanner = new Scanner(System.in);
        MCRCustomer newMCRCCustomer = new MCRCustomer();
        
        System.out.println("*** CarMS :: Create New Customer ***\n");
        
        System.out.print("Enter Username> ");
        newMCRCCustomer.setCustUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newMCRCCustomer.setCustPassword(scanner.nextLine().trim());
        System.out.print("Enter ID Number> ");
        newMCRCCustomer.setIdNumber(scanner.nextLine().trim());
        System.out.print("Enter Phone Number> ");
        newMCRCCustomer.setPhoneNumber(scanner.nextLine().trim());
        System.out.print("Enter First Name> ");
        newMCRCCustomer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newMCRCCustomer.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Email> ");
        newMCRCCustomer.setCustEmail(scanner.nextLine().trim());
        System.out.print("Enter Creditcard Number> ");
        newMCRCCustomer.setCreditCardNumber(scanner.nextLine().trim());
        
        Set<ConstraintViolation<MCRCustomer>>constraintViolations = validator.validate(newMCRCCustomer);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                Long newCustId = customerSessionBeanRemote.createNewCustomer(newMCRCCustomer);
                System.out.println(newCustId + " is successfully registered!" + "\n");
            }
            catch(CustomerUsernameExistException ex)
            {
                System.out.println("An error has occurred during registration!: The user name already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred during registration!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForMCRCustomer(constraintViolations);
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Car Rental ***\n");
            System.out.println("You are login as " + mcrCustomerEntity.getCustUsername());
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: Cancel Reservation");
            System.out.println("4: View Reservation Details");
            System.out.println("5: View All My Reservations");
            System.out.println("6: Logout\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                   // searchCar();
                }
                else if(response == 2)
                {
                   // reserveCar();
                }
                else if(response == 3)
                {
                   // cancelReservation();
                }
                else if(response == 4)
                {
                    doViewReservationDetails();
                }
                else if(response == 5)
                {
                    doViewMyReservations();
                }
                else if (response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
/*    
     private void searchCar() 
     {
        try
        {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
            Date pickUpDateTime;
            String pickUpOutlet;
            Date returnDateTime;
            String returnOutlet;

            System.out.println("*** CarMS :: Search Car ***\n");
            System.out.print("Enter Pickup Date and Time (dd/mm/yyyy hh:mm)> ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());           
            System.out.print("Enter Pickup Outlet> ");
            pickUpOutlet = scanner.nextLine().trim();
            System.out.print("Enter Return Date and Time (dd/mm/yyyy hh:mm)> ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Outlet> ");
            returnOutlet = scanner.nextLine().trim();
            
            List<Cars> itineraryItems = carSessionBeanRemote.searchCars(pickUpDateTime, pickUpOutlet, returnDateTime, returnOutlet);
            
            System.out.printf("%8s%22s   %s\n", "Seq. No.", "Date/Time", "Itinerary");
            
            for(ItineraryItem itineraryItem:itineraryItems)
            {
                System.out.printf("%8s%22s   %s\n", itineraryItem.getSequenceNumber(), outputDateFormat.format(itineraryItem.getDateTime()), itineraryItem.getActivity());
            }
            
            System.out.println("------------------------");
            System.out.println("1: Make Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            
            if(response == 1)
            {
                if(currentCustomer != null)
                {
                    PaymentModeEnum paymentMode;
                    String creditCardNumber;
                    
                    System.out.println("\nTotal Amount Payable is " + BigDecimalHelper.formatCurrency(holidayReservationSessionBeanRemote.getTotalAmount()));
                    
                    while(true)
                    {
                        System.out.print("Select Payment Mode (1: VISA, 2: MasterCard, 3: AMEX)> ");
                        Integer paymentModeInt = scanner.nextInt();

                        if(paymentModeInt >= 1 && paymentModeInt <= 3)
                        {
                            paymentMode = PaymentModeEnum.values()[paymentModeInt-1];
                            break;
                        }
                        else
                        {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                    
                    scanner.nextLine();
                    System.out.print("Enter Credit Card Number> ");
                    creditCardNumber = scanner.nextLine().trim();
                    
                    try 
                    {
                        Long newTransactionId = holidayReservationSessionBeanRemote.reserveHoliday(currentCustomer.getCustomerId(), paymentMode, creditCardNumber);
                        
                        System.out.println("Reservation of holiday completed successfully!: " + newTransactionId + "\n");
                    } 
                    catch (CheckoutException ex) 
                    {
                        System.out.println("An error has occurred while making the reservation: " + ex.getMessage() + "\n");
                    }
                }
                else
                {
                    System.out.println("Please login first before making a reservation!\n");
                }
            }
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid date input!\n");
        }
    }
*/
    
    private void doViewReservationDetails()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CarMS :: View Reservation Details ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        
        try
        {
        ReservationRecord reservation = reservationRecordSessionBeanRemote.retrieveReservationRecordById(reservationId);
        System.out.printf("%8s%8s%8%13s%20s%20s\n", "Reservation ID", "Pickup Date & Time", "Return Date & Time", "Amount Paid", "Car Rented", "Dispatched Transit Driver");
        System.out.printf("%8s%8s%8%13s%20s%20s\n", reservation.getReservationId().toString(), reservation.getPickupDateTime(), reservation.getReturnDateTime(), NumberFormat.getCurrencyInstance().format(reservation.getAmtPaid()), reservation.getCar(), reservation.getTransitDriverDispatchRecord());  
        }
        catch(ReservationNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving reservation: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewMyReservations()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CarMS :: View All Of My Reservations ***\n");
        
        String username = mcrCustomerEntity.getCustUsername();
        List<ReservationRecord> reservations = reservationRecordSessionBeanRemote.retrieveReservationsByUsername(username);
        System.out.printf("%8s%8s%8%13s%20s%20s\n", "Reservation ID", "Pickup Date & Time", "Return Date & Time", "Amount Paid", "Car Rented", "Dispatched Transit Driver");

        for(ReservationRecord reservation:reservations)
        {
            System.out.printf("%8s%8s%8%13s%20s%20s\n", reservation.getReservationId().toString(), reservation.getPickupDateTime(), reservation.getReturnDateTime(), NumberFormat.getCurrencyInstance().format(reservation.getAmtPaid()), reservation.getCar(), reservation.getTransitDriverDispatchRecord());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void showInputDataValidationErrorsForMCRCustomer(Set<ConstraintViolation<MCRCustomer>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
