/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationwebserviceclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.HolidayReservationSystem.BeyondOperatingHoursException;
import ws.client.HolidayReservationSystem.BeyondOperatingHoursException_Exception;
import ws.client.HolidayReservationSystem.Category;
import ws.client.HolidayReservationSystem.CategoryNotFoundException_Exception;
import ws.client.HolidayReservationSystem.Customer;
import ws.client.HolidayReservationSystem.Partner;
import ws.client.HolidayReservationSystem.HolidayReservationSystemWebService;
import ws.client.HolidayReservationSystem.HolidayReservationSystemWebService_Service;
import ws.client.HolidayReservationSystem.InvalidLoginCredentialException_Exception;
//import ws.client.HolidayReservationSystem.BeyondOperatingHoursException_Exception;
import ws.client.HolidayReservationSystem.InvalidLoginCredentialException;
import ws.client.HolidayReservationSystem.Model;
import ws.client.HolidayReservationSystem.ModelNotFoundException_Exception;
import ws.client.HolidayReservationSystem.NoRentalRateApplicableException_Exception;
import ws.client.HolidayReservationSystem.Outlet;
import ws.client.HolidayReservationSystem.OutletNotFoundException_Exception;
import ws.client.HolidayReservationSystem.PickUpDateAfterReturnDateException;
import ws.client.HolidayReservationSystem.PickUpDateAfterReturnDateException_Exception;

/**
 *
 * @author User
 */
public class HolidayReservationWebServiceClient {

    private static Partner partner;
    private static Customer customer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        HolidayReservationSystemWebService_Service holidayReservationWebServiceClient_Service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService holidayReservationSystemWebServicePort = holidayReservationWebServiceClient_Service.getHolidayReservationSystemWebServicePort();

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {

            System.out.println("*** Welcome to Merlion Car Rental System(v1.0) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");

            response = sc.nextInt();

            if (response == 1) {
                try {
                    doLogin();
                    System.out.println("\nLogin successful!\n");
                    menuMain();
                } catch (InvalidLoginCredentialException_Exception ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (response == 2) {
                break;
            } else {
                System.out.println("\nInvalid login credential. Please try again.\n");
            }
        }

        //holidayReservationSystemWebServicePort.retrieveAllCategory();
    }

    public static void doLogin() throws InvalidLoginCredentialException_Exception {
        HolidayReservationSystemWebService_Service holidayReservationWebServiceClient_Service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService holidayReservationSystemWebServicePort = holidayReservationWebServiceClient_Service.getHolidayReservationSystemWebServicePort();

        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Merlion System :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();

        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            partner = holidayReservationSystemWebServicePort.login(username, password);
        } else {
            throw new InvalidLoginCredentialException_Exception("Missing Login Credentials!", new InvalidLoginCredentialException());
        }
    }

    public static void menuMain() {
        HolidayReservationSystemWebService_Service holidayReservationWebServiceClient_Service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService holidayReservationSystemWebServicePort = holidayReservationWebServiceClient_Service.getHolidayReservationSystemWebServicePort();

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Car Rental System (v1.0) ***\n");
            System.out.println("You are login as " + partner.getPartnerName());
            System.out.println("1: Search Car & Reserve Car");
            System.out.println("2: View Reservation Details");
            System.out.println("3: View all my reservation");
            System.out.println("4: Cancel reservation");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {

                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        searchCar();
                    } catch (DatatypeConfigurationException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    //doViewReservationDetails();
                } else if (response == 3) {
                    //doViewAllReservation();
                } else if (response == 4) {
                    //doViewReservationDetails();
                } else if (response == 5) {
                    //currentPartner = null;
                    //currentCustomer = null;
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

    public static void searchCar() throws DatatypeConfigurationException {
        HolidayReservationSystemWebService_Service holidayReservationWebServiceClient_Service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService holidayReservationSystemWebServicePort = holidayReservationWebServiceClient_Service.getHolidayReservationSystemWebServicePort();
        Scanner scanner = new Scanner(System.in);
        Integer choice = 0;
        long categoryId = 0;
        long modelId = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

        System.out.println("*** CaRMS Reservation Client || Search Car ***\n");
        Boolean ableToReserve = false;

        try {
            System.out.print("Please enter the Pickup Date & Time (DD/MM/YYYY HH:MM)> ");
            Date pickUpDateTime = sdf.parse(scanner.nextLine().trim());
            System.out.print("Please enter the Return Date & Time (DD/MM/YYYY HH:MM)> ");
            Date returnDateTime = sdf.parse(scanner.nextLine().trim());

            if (returnDateTime.before(pickUpDateTime)) {
                throw new PickUpDateAfterReturnDateException_Exception("", new PickUpDateAfterReturnDateException());
            }

            System.out.print("\nThis is the list of outlets you are able to choose from. Please enter"
                    + " the ID you would like to choose from.\n\n");
            List<Outlet> outlets = holidayReservationSystemWebServicePort.retrieveAllOutlet();
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

//            operatingHoursChecker(pickupOutletId, pickUpDateTime, 1);
//            operatingHoursChecker(returnOutletId, returnDateTime, 2);
            GregorianCalendar gPickupDate = new GregorianCalendar();
            GregorianCalendar gReturnDate = new GregorianCalendar();
            gPickupDate.setTime(pickUpDateTime);
            gReturnDate.setTime(returnDateTime);
            XMLGregorianCalendar xmlPickupDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gPickupDate);
            XMLGregorianCalendar xmlReturnDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gReturnDate);

            List<Category> categories = holidayReservationSystemWebServicePort.retrieveAllCategory();

            System.out.println("\n\n*******    Search Results For Car(s) Available    *******\n\n");

            System.out.println("***    Search Results For Car Category   ***\n");
            for (Category category : categories) {

                try {
                    BigDecimal totalRentalFee = holidayReservationSystemWebServicePort.calculateRentalFee(category.getCategoryId(), xmlPickupDate, xmlReturnDate);
                    if (ableToReserve) {
                        System.out.println("Car(s) available for rental for the category of " + category.getCategoryName() + ". The total rental fee will be $" + totalRentalFee + ". ");
                    } else {
                        System.out.println("No car(s) are available for rental for the category of " + category.getCategoryName());
                    }
                } catch (NoRentalRateApplicableException_Exception ex) {
                    System.out.println("There are no available rental rates found for the period stated for the category " + category.getCategoryName() + "\n");
                }

            }

            List<Model> models = holidayReservationSystemWebServicePort.retrieveAvailAllModels();

            System.out.println("\n***    Search Results For Make and Model   ***\n\n");
            for (Model model : models) {
                ableToReserve = holidayReservationSystemWebServicePort.searchCarByModel(pickupOutletId, returnOutletId, model.getModelId(), xmlPickupDate, xmlReturnDate);
                categoryId = holidayReservationSystemWebServicePort.retrieveModelById(model.getModelId()).getCategory().getCategoryId();
                try {
                    BigDecimal totalRentalFee = holidayReservationSystemWebServicePort.calculateRentalFee(categoryId, xmlPickupDate, xmlReturnDate);
                    if (ableToReserve) {
                        System.out.println("Car(s) available for rental for the make and model of " + model.getMake() + " " + model.getModel() + ". The total rental fee will be $" + totalRentalFee + ". ");
                    } else {
                        System.out.println("No car(s) are available for rental for the make and model of " + model.getMake() + " " + model.getModel());
                    }
                } catch (NoRentalRateApplicableException_Exception ex) {
                    System.out.println("There are no available rental rates found for the period stated for the make and model of " + model.getMake() + " " + model.getModel() + "\n");
                }

            }

        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } //catch (CategoryNotFoundException_Exception ex) {
        //  System.out.println("Car Category of ID: " + categoryId + " does not exist!");} 
        catch (ModelNotFoundException_Exception ex) {
            System.out.println("Model not found!\n");
        } catch (PickUpDateAfterReturnDateException_Exception ex) {
            System.out.println("Return Date Is Before Pickup Date!");
//        } catch (OutletNotFoundException_Exception ex) {
//            System.out.println("Outlet not found!\n");
//        } catch (BeyondOperatingHoursException_Exception ex) {
//            System.out.println(ex.getMessage());
//        } catch (NoRentalRateApplicableException_Exception ex) {
//            System.out.println(ex.getMessage());
//        }

            System.out.print("\nPress any key to continue...> ");
            scanner.nextLine();
        }

//
//    public static void operatingHoursChecker(Long outletId, Date time, int choice) throws OutletNotFoundException_Exception, BeyondOperatingHoursException_Exception, ModelNotFoundException_Exception, NoRentalRateApplicableException_Exception, PickUpDateAfterReturnDateException_Exception {
//        HolidayReservationSystemWebService_Service holidayReservationWebServiceClient_Service = new HolidayReservationSystemWebService_Service();
//        HolidayReservationSystemWebService holidayReservationSystemWebServicePort = holidayReservationWebServiceClient_Service.getHolidayReservationSystemWebServicePort();
//
//        Outlet outlet = holidayReservationSystemWebServicePort.retrieveOutletById(outletId);
//
//        if (choice
//                == 1) {
//            if (outlet.getOpeningHour() != null) {
//                if ((time.getHours() < outlet.getOpeningHour().getHour())
//                        || (time.getHours() == outlet.getOpeningHour().getHour()
//                        && time.getMinutes() < outlet.getOpeningHour().getMinute())) {
//                    throw new BeyondOperatingHoursException_Exception("Pickup time is before opening hours of the pickup outlet", new BeyondOperatingHoursException());
//                } else if ((time.getHours() > outlet.getClosingHour().getHour())
//                        || (time.getHours() == outlet.getClosingHour().getHour()
//                        && time.getMinutes() > outlet.getClosingHour().getMinute())) {
//                    throw new BeyondOperatingHoursException_Exception("Pickup time is after closing hours of the pickup outlet", new BeyondOperatingHoursException());
//                }
//            }
//        } else if (choice
//                == 2) {
//            if (outlet.getClosingHour() != null) {
//                if ((time.getHours() > outlet.getClosingHour().getHour())
//                        || (time.getHours() == outlet.getClosingHour().getHour()
//                        && time.getMinutes() > outlet.getClosingHour().getMinute())) {
//                    throw new BeyondOperatingHoursException_Exception("Return time is after operating hours of the pickup outlet", new BeyondOperatingHoursException());
//                } else if ((time.getHours() < outlet.getOpeningHour().getHour())
//                        || (time.getHours() == outlet.getOpeningHour().getHour()
//                        && time.getMinutes() < outlet.getOpeningHour().getMinute())) {
//                    throw new BeyondOperatingHoursException_Exception("Return time is before operating hours of the pickup outlet", new BeyondOperatingHoursException());
//                }
//            }
//        }
//
    }
}
