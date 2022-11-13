/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationRecordSessionBeanRemote;
import entity.Category;
import entity.Customer;
import entity.MCRCustomer;
import entity.Model;
import entity.Outlet;
import entity.ReservationRecord;
import exception.BeyondOperatingHoursException;
import exception.CategoryNotFoundException;
import exception.CustomerNotFoundException;
import exception.CustomerUsernameExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.ModelNotFoundException;
import exception.NoCustomerReservationsFoundException;
import exception.NoRentalRateApplicableException;
import exception.OutletNotFoundException;
import exception.PickUpDateAfterReturnDateException;
import exception.RentalReservationNotFoundException;
import exception.ReservationNotFoundException;
import exception.UnknownPersistenceException;
import exception.YearMustBeBeyond2022Exception;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    private MCRCustomer mcrCustomerEntity;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CategorySessionBeanRemote categorySessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, ReservationRecordSessionBeanRemote reservationRecordSessionBeanRemote, MCRCustomer mcrCustomerEntity, OutletSessionBeanRemote outletSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this();
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.reservationRecordSessionBeanRemote = reservationRecordSessionBeanRemote;
        this.mcrCustomerEntity = mcrCustomerEntity;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }

    public void runApp() throws InputDataValidationException, RentalReservationNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Merlion Car Rental! ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");

                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    registerCustomer();
                } else if (response == 3) {
                    searchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
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

        if (username.length() > 0 && password.length() > 0) {
            mcrCustomerEntity = customerSessionBeanRemote.login(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void registerCustomer() throws InputDataValidationException {
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

        Set<ConstraintViolation<MCRCustomer>> constraintViolations = validator.validate(newMCRCCustomer);

        if (constraintViolations.isEmpty()) {
            try {
                Long newCustId = customerSessionBeanRemote.createNewCustomer(newMCRCCustomer);
                System.out.println(newCustId + " is successfully registered!" + "\n");
            } catch (CustomerUsernameExistException ex) {
                System.out.println("An error has occurred during registration!: The user name already exist\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred during registration!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForMCRCustomer(constraintViolations);
        }
    }

    private void menuMain() throws RentalReservationNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Car Rental ***\n");
            System.out.println("Welcome " + mcrCustomerEntity.getFirstName() + " " + mcrCustomerEntity.getLastName() + "!\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchCar();
                } else if (response == 2) {
                    reserveCar();
                } else if (response == 3) {
                    doViewReservationDetails();
                } else if (response == 4) {
                    doViewMyReservations();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
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
            Integer choice = 0;
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
            Date pickUpDateTime;
            String pickUpOutlet;
            Date returnDateTime;
            String outlet;

            System.out.println("*** CarMS :: Search Car ***\n");
            System.out.print("Enter Pickup Date and Time (dd/mm/yyyy hh:mm)> ");
            pickUpDateTime = sdf.parse(scanner.nextLine().trim());           
            System.out.print("Enter Pickup Outlet> ");
            pickUpOutlet = scanner.nextLine().trim();
            System.out.print("Enter Return Date and Time (dd/mm/yyyy hh:mm)> ");
            returnDateTime = sdf.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Outlet> ");
            outlet = scanner.nextLine().trim();
            
            List<Cars> itineraryItems = carSessionBeanRemote.searchCars(pickUpDateTime, pickUpOutlet, returnDateTime, outlet);
            
            System.out.printf("%8s%22s   %s\n", "Seq. No.", "Date/Time", "Itinerary");
            
            for(ItineraryItem itineraryItem:itineraryItems)
            {
                System.out.printf("%8s%22s   %s\n", itineraryItem.getSequenceNumber(), outputDateFormat.format(itineraryItem.getDateTime()), itineraryItem.getActivity());
            }
            
            System.out.println("------------------------");
            System.out.println("1: Make Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            choice = scanner.nextInt();
            
            if(choice == 1)
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
    private void searchCar() {
        Scanner scanner = new Scanner(System.in);
        Integer choice = 0;
        long categoryId = 0;
        long modelId = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        hourFormat.setLenient(false);

        System.out.println("*** CaRMS Reservation Client || Search Car ***\n");
        Boolean ableToReserve = false;

        try {
            System.out.print("Please enter the Pickup Date & Time (DD/MM/YYYY HH:MM)> ");
            Date pickUpDateTime = sdf.parse(scanner.nextLine().trim());
            System.out.print("Please enter the Return Date & Time (DD/MM/YYYY HH:MM)> ");
            Date returnDateTime = sdf.parse(scanner.nextLine().trim());

            if (returnDateTime.before(pickUpDateTime)) {
                throw new PickUpDateAfterReturnDateException();
            }
            
            if (((returnDateTime.getYear() + 1900) < 2000) || ((pickUpDateTime.getYear() + 1900) < 2000)) {
                throw new YearMustBeBeyond2022Exception("Year must be beyond 2000!");
            }

            System.out.print("\nThis is the list of outlets you are able to choose from. Please enter"
                    + " the ID you would like to choose from.\n\n");
            List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
            System.out.printf("%4s%30s%50s%20s%20s\n", "ID", "Outlet Name", "Address", "Opening Hour", "Closing Hour");
            for (Outlet outlet : outlets) {
                String openingHours = outlet.getOpeningHour() == null ? "24 Hours" : outlet.getOpeningHour().toString();
                String closingHours = outlet.getClosingHour() == null ? " - " : outlet.getClosingHour().toString();
                System.out.printf("%4s%30s%50s%20s%20s\n", outlet.getOutletId(), outlet.getOutletName(), outlet.getAddress(), openingHours, closingHours);
            }

            System.out.print("Enter Pickup Outlet ID> ");
            Long pickupOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID> ");
            Long returnOutletId = scanner.nextLong();

            operatingHoursChecker(pickupOutletId, pickUpDateTime, 1);
            operatingHoursChecker(returnOutletId, returnDateTime, 2);

            List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();

            System.out.println("\n\n*******    Search Results For Car(s) Available    *******\n\n");

            System.out.println("***    Search Results For Car Category   ***\n");
            for (Category category : categories) {
                ableToReserve = carSessionBeanRemote.searchCarByCategory(pickupOutletId, returnOutletId, category.getCategoryId(), pickUpDateTime, returnDateTime);
                try {
                    BigDecimal totalRentalFee = rentalRateSessionBeanRemote.calculateRentalFee(category.getCategoryId(), pickUpDateTime, returnDateTime);
                    if (ableToReserve) {
                        System.out.println("Car(s) available for rental for the category of " + category.getCategoryName() + ". The total rental fee will be $" + totalRentalFee + ". ");
                    } else {
                        System.out.println("No car(s) are available for rental for the category of " + category.getCategoryName());
                    }
                } catch (NoRentalRateApplicableException ex) {
                    System.out.println("There are no available rental rates found for the period stated for the category " + category.getCategoryName() + "\n");
                }

            }

            List<Model> models = modelSessionBeanRemote.retrieveAvailAllModels();

            System.out.println("\n***    Search Results For Make and Model   ***\n\n");
            for (Model model : models) {
                ableToReserve = carSessionBeanRemote.searchCarByModel(pickupOutletId, returnOutletId, model.getModelId(), pickUpDateTime, returnDateTime);
                categoryId = modelSessionBeanRemote.retrieveModelById(model.getModelId()).getCategory().getCategoryId();
                try {
                    BigDecimal totalRentalFee = rentalRateSessionBeanRemote.calculateRentalFee(categoryId, pickUpDateTime, returnDateTime);
                    if (ableToReserve) {
                        System.out.println("Car(s) available for rental for the make and model of " + model.getMake() + " " + model.getModel() + ". The total rental fee will be $" + totalRentalFee + ". ");
                    } else {
                        System.out.println("No car(s) are available for rental for the make and model of " + model.getMake() + " " + model.getModel());
                    }
                } catch (NoRentalRateApplicableException ex) {
                    System.out.println("There are no available rental rates found for the period stated for the make and model of " + model.getMake() + " " + model.getModel() + "\n");
                }

            }

        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } catch (CategoryNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet not found!\n");
        } catch (BeyondOperatingHoursException ex) {
            System.out.println(ex.getMessage());
        } catch (PickUpDateAfterReturnDateException ex) {
            System.out.println("Return Date Is Before Pickup Date!");
        } catch (YearMustBeBeyond2022Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }

    private void reserveCar() {
        Scanner scanner = new Scanner(System.in);
        ReservationRecord reservationRecord = new ReservationRecord();
        Integer choice = 0;
        long categoryId = 0;
        long modelId = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

        System.out.println("*** CaRMS Reservation Client || Reserve Car ***\n");
        Boolean canReserve = false;

        try {
            System.out.print("Please enter the Pickup Date & Time (DD/MM/YYYY HH:MM)> ");
            Date pickUpDateTime = sdf.parse(scanner.nextLine().trim());
            System.out.print("Please enter the Return Date & Time (DD/MM/YYYY HH:MM)> ");
            Date returnDateTime = sdf.parse(scanner.nextLine().trim());

            if (returnDateTime.before(pickUpDateTime)) {
                throw new PickUpDateAfterReturnDateException();
            }

            System.out.print("This is the list of outlets you are able to choose from. Please enter"
                    + " the ID you would like to choose from.\n\n");
            List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
            System.out.printf("%4s%30s%50s%20s%20s\n", "ID", "Outlet Name", "Address", "Opening Hour", "Closing Hour");
            for (Outlet outlet : outlets) {
                String openingHours = outlet.getOpeningHour() == null ? "24 Hours" : outlet.getOpeningHour().toString();
                String closingHours = outlet.getClosingHour() == null ? " - " : outlet.getClosingHour().toString();
                System.out.printf("%4s%30s%50s%20s%20s\n", outlet.getOutletId(), outlet.getOutletName(), outlet.getAddress(), openingHours, closingHours);
            }

            System.out.print("Enter Pickup Outlet ID> ");
            Long pickupOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID> ");
            Long returnOutletId = scanner.nextLong();

            operatingHoursChecker(pickupOutletId, pickUpDateTime, 1);
            operatingHoursChecker(returnOutletId, returnDateTime, 2);

            while (true) {
                System.out.println("*** Search by Car Category or Car Model? ***\n");
                System.out.println("1: Search by Car Category");
                System.out.println("2: Search by Car Model");
                choice = 0;

                while (choice < 1 || choice > 2) {
                    System.out.print("> ");
                    choice = scanner.nextInt();

                    if (choice == 1) {
                        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
                        System.out.print("This is the list of car categories you are able to choose from. Please enter"
                                + " the ID you would like to choose from.\n\n");
                        System.out.printf("%4s%20s\n", "ID", "Category");
                        for (Category category : categories) {
                            System.out.printf("%4s%20s\n", category.getCategoryId(), category.getCategoryName());
                        }
                        System.out.print("\nEnter Car Category ID > ");
                        categoryId = scanner.nextLong();
                        canReserve = carSessionBeanRemote.searchCarByCategory(pickupOutletId, returnOutletId, categoryId, pickUpDateTime, returnDateTime);
                        break;
                    } else if (choice == 2) {
                        List<Model> models = modelSessionBeanRemote.retrieveAvailAllModels();
                        System.out.print("This is the list of models you are able to choose from. Please enter"
                                + " the ID you would like to choose from.\n\n");
                        System.out.printf("%4s%30s%30s\n", "ID", "Make", "Model");
                        for (Model model : models) {
                            System.out.printf("%4s%30s%30s\n", model.getModelId(), model.getMake(), model.getModel());
                        }
                        System.out.print("Enter model ID> ");
                        modelId = scanner.nextLong();
                        categoryId = modelSessionBeanRemote.retrieveModelById(modelId).getCategory().getCategoryId();
                        canReserve = carSessionBeanRemote.searchCarByModel(pickupOutletId, returnOutletId, modelId, pickUpDateTime, returnDateTime);
                        break;
                    } else {
                        System.out.println("Invalid option, please try again\n");
                    }
                }
                if (choice == 1 || choice == 2) {
                    break;
                }
            }
            scanner.nextLine();
            if (!canReserve) {
                System.out.println("No cars are available for rental.");
            } else {
                BigDecimal totalRentalFee = rentalRateSessionBeanRemote.calculateRentalFee(categoryId, pickUpDateTime, returnDateTime);
                System.out.println("Car(s) available for rental. The total rental fee will be $" + totalRentalFee + ". ");
                System.out.print("Reserve a car? (Enter 'Y' to reserve a car)> ");
                String input = scanner.nextLine().trim();
                if (input.equals("Y")) {
                    reservationRecord.setPickupDateTime(pickUpDateTime);
                    reservationRecord.setReturnDateTime(returnDateTime);
                    reservationRecord.setAmtPaid(new Double(totalRentalFee.toString()));
                    reservationRecord.setCompleted(false);
                    reservationRecord.setPickedUp(false);
                    reservationRecord.setReservationCancelled(false);

                    //String creditCardNumber = mcrCustomerEntity.getCreditCardNumber();
                    System.out.print("Please Enter your Credit Card Number >");
                    String creditCardNumber = scanner.nextLine().trim();
                    System.out.print("Your Credit Card Number: " + creditCardNumber + " will be used.\n");
                    reservationRecord.setCreditCardNum(creditCardNumber);

                    System.out.print("Would you like to pay now or pay later at the outlet? (Enter 'Y' to make payment now.)> ");
                    input = scanner.nextLine().trim();
                    if (input.equals("Y")) {
                        reservationRecord.setPaid(true);
                        System.out.println("Charged " + NumberFormat.getCurrencyInstance().format(totalRentalFee) + " to credit card: " + creditCardNumber);
                    } else {
                        reservationRecord.setPaid(false);
                        System.out.println("You will be charged the total fee of " + NumberFormat.getCurrencyInstance().format(totalRentalFee) + " when you collect the car.");
                    }
                    Long rentalReservationId = reservationRecordSessionBeanRemote.createNewCarRentalReservation(categoryId, modelId, mcrCustomerEntity.getCustomerId(), pickupOutletId, returnOutletId, reservationRecord);
                    System.out.println("Rental reservation created with ID: " + rentalReservationId);
                } else {
                    System.out.print("You did not choose to reserve a car.");
                }
            }
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } catch (NoRentalRateApplicableException ex) {
            System.out.println("There are no available rental rates found for the period stated.\n");
        } catch (CategoryNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet not found!\n");
        } catch (BeyondOperatingHoursException ex) {
            System.out.println(ex.getMessage());
        } catch (PickUpDateAfterReturnDateException ex) {
            System.out.println("Return Date Is Before Pickup Date!");
        } catch (CustomerNotFoundException ex) {
            System.out.println("Customer of ID: " + categoryId + " does not exist!");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }

    private void operatingHoursChecker(Long outletId, Date time, int choice) throws OutletNotFoundException, BeyondOperatingHoursException {
        Outlet outlet = outletSessionBeanRemote.retrieveOutletById(outletId);

        if (choice == 1) {
            if (outlet.getOpeningHour() != null) {
                if ((time.getHours() < outlet.getOpeningHour().getHour())
                        || (time.getHours() == outlet.getOpeningHour().getHour()
                        && time.getMinutes() < outlet.getOpeningHour().getMinute())) {
                    throw new BeyondOperatingHoursException("Pickup time is before opening hours of the pickup outlet");
                } else if ((time.getHours() > outlet.getClosingHour().getHour())
                        || (time.getHours() == outlet.getClosingHour().getHour()
                        && time.getMinutes() > outlet.getClosingHour().getMinute())) {
                    throw new BeyondOperatingHoursException("Pickup time is after closing hours of the pickup outlet");
                }
            }
        } else if (choice == 2) {
            if (outlet.getClosingHour() != null) {
                if ((time.getHours() > outlet.getClosingHour().getHour())
                        || (time.getHours() == outlet.getClosingHour().getHour()
                        && time.getMinutes() > outlet.getClosingHour().getMinute())) {
                    throw new BeyondOperatingHoursException("Return time is after operating hours of the pickup outlet");
                } else if ((time.getHours() < outlet.getOpeningHour().getHour())
                        || (time.getHours() == outlet.getOpeningHour().getHour()
                        && time.getMinutes() < outlet.getOpeningHour().getMinute())) {
                    throw new BeyondOperatingHoursException("Return time is before operating hours of the pickup outlet");
                }
            }
        }

    }

    private void doViewReservationDetails() throws RentalReservationNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CarMS || View All Of My Reservations ***\n");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<ReservationRecord> reservations = reservationRecordSessionBeanRemote.retrieveReservationsByCustId(mcrCustomerEntity.getCustomerId());
        //String username = mcrCustomerEntity.getCustUsername();

        try {
            if (reservations.isEmpty()) {
                throw new NoCustomerReservationsFoundException("There are no reservation records found in your account.\n");
            }

            System.out.print("This is the list of records you are able to choose from. Please enter"
                    + " the ID you would like to choose from.\n\n");

            System.out.printf("%20s%25s%25s%13s%20s%20s%20s%20s\n", "Reservation ID", "Pickup Date & Time", "Return Date & Time", "Amount Paid", "Pickup Outlet", "Return Outlet", "Refunded?", "Refund Amount");

            for (ReservationRecord reservation : reservations) {
                String transitDriver = "NOT ASSIGNED";
                if (!(reservation.getTransitDriverDispatchRecord() == null)) {
                    transitDriver = reservation.getTransitDriverDispatchRecord().getEmployee().getEmployeeName();
                }

                String refunded = "Not Refunded";
                if (reservation.getReservationCancelled() == true) {
                    refunded = "Refunded";
                }
                String refundAmount = " - ";
                if (reservation.getRefundAmount() != null) {
                    refundAmount = NumberFormat.getCurrencyInstance().format(reservation.getRefundAmount());
                }
                System.out.printf("%20s%25s%25s%13s%20s%20s%20s%20s\n", reservation.getReservationId().toString(), sdf.format(reservation.getPickupDateTime()), sdf.format(reservation.getReturnDateTime()), NumberFormat.getCurrencyInstance().format(reservation.getAmtPaid()), reservation.getPickupOutlet().getOutletName(), reservation.getReturnOutlet().getOutletName(), refunded, refundAmount);
            }

            System.out.print("Enter Reservation ID > ");
            Long reservationId = scanner.nextLong();
            Integer choice = 0;

            try {
                ReservationRecord reservation = reservationRecordSessionBeanRemote.retrieveReservationRecordById(reservationId);
                String transitDriver = "NOT ASSIGNED";
                if (!(reservation.getTransitDriverDispatchRecord() == null)) {
                    transitDriver = reservation.getTransitDriverDispatchRecord().getEmployee().getEmployeeName();
                }
                String refunded = "Not Refunded";
                if (reservation.getReservationCancelled() == true) {
                    refunded = "Refunded";
                }
                String refundAmount = " - ";
                if (reservation.getRefundAmount() != null) {
                    refundAmount = NumberFormat.getCurrencyInstance().format(reservation.getRefundAmount());
                }
                System.out.println("\nYou have chosen ID: " + reservationId + " and its details are shown below.\n");

                System.out.printf("%20s%25s%25s%13s%20s%20s%20s%20s\n", "Reservation ID", "Pickup Date & Time", "Return Date & Time", "Amount Paid", "Pickup Outlet", "Return Outlet", "Refunded?", "Refund Amount");

                System.out.printf("%20s%25s%25s%13s%20s%20s%20s%20s\n", reservation.getReservationId().toString(), sdf.format(reservation.getPickupDateTime()), sdf.format(reservation.getReturnDateTime()), NumberFormat.getCurrencyInstance().format(reservation.getAmtPaid()), reservation.getPickupOutlet().getOutletName(), reservation.getReturnOutlet().getOutletName(), refunded, refundAmount);
                System.out.println("------------------------");
                System.out.println("1: Delete Reservation Record");
                System.out.println("2: Back\n");
                System.out.print("> ");
                choice = scanner.nextInt();
                if (choice == 1) {
                    cancelReservation(reservation.getReservationId());
                } else if (choice == 2) {
                    System.out.print("You have chosen to exit.\n\n");
                }
                System.out.print("Press any key to continue...> ");
            } catch (RentalReservationNotFoundException ex) {
                System.out.println("Reservation Record not found for ID: " + reservationId);
            }
        } catch (NoCustomerReservationsFoundException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void cancelReservation(Long reservationRecordId) {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00");
        ReservationRecord reservationRecord;

        System.out.println("*** CaRMS Reservation Client || Cancel Reservation ***\n");
        try {
            Double penalty = reservationRecordSessionBeanRemote.cancelReservation(reservationRecordId);
            reservationRecord = reservationRecordSessionBeanRemote.retrieveReservationRecordById(reservationRecordId);

            System.out.println("Reservation successfully cancelled!");

            if (reservationRecord.getPaid()) {
                System.out.println("You have been refunded $"
                        + df.format(reservationRecord.getAmtPaid() - penalty) + " to your card " + reservationRecord.getCreditCardNum() + ". The cancellation penalty is $" + df.format(penalty) + ".");
            } else {
                System.out.println("Your card : " + reservationRecord.getCreditCardNum() + " has been charged a fee of $" + penalty + " for the cancellation penalty.");
            }

        } catch (RentalReservationNotFoundException ex) {
            System.out.println("Rental Reservation not found for ID " + reservationRecordId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewMyReservations() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CarMS :: View All Of My Reservations ***\n");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<ReservationRecord> reservations = reservationRecordSessionBeanRemote.retrieveReservationsByCustId(mcrCustomerEntity.getCustomerId());
        try {
            if (reservations.isEmpty()) {
                throw new NoCustomerReservationsFoundException("There are no reservation records found in your account.\n");
            }
            //String username = mcrCustomerEntity.getCustUsername();

            System.out.printf("%20s%25s%25s%13s%20s%20s%20s%20s\n", "Reservation ID", "Pickup Date & Time", "Return Date & Time", "Amount Paid", "Pickup Outlet", "Return Outlet", "Refunded?", "Refund Amount");

            for (ReservationRecord reservation : reservations) {
                String transitDriver = "NOT ASSIGNED";
                if (!(reservation.getTransitDriverDispatchRecord() == null)) {
                    transitDriver = reservation.getTransitDriverDispatchRecord().getEmployee().getEmployeeName();
                }
                String refunded = "Not Refunded";
                if (reservation.getReservationCancelled() == true) {
                    refunded = "Refunded";
                }
                String refundAmount = " - ";
                if (reservation.getRefundAmount() != null) {
                    refundAmount = NumberFormat.getCurrencyInstance().format(reservation.getRefundAmount());
                }
                System.out.printf("%20s%25s%25s%13s%20s%20s%20s%20s\n", reservation.getReservationId().toString(), sdf.format(reservation.getPickupDateTime()), sdf.format(reservation.getReturnDateTime()), NumberFormat.getCurrencyInstance().format(reservation.getAmtPaid()), reservation.getPickupOutlet().getOutletName(), reservation.getReturnOutlet().getOutletName(), refunded, refundAmount);
            }
        } catch (NoCustomerReservationsFoundException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void showInputDataValidationErrorsForMCRCustomer(Set<ConstraintViolation<MCRCustomer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
