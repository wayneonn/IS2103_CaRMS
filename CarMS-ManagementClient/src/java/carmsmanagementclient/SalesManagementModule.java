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
import entity.Category;
import entity.Employee;
import entity.Model;
import entity.RentalRate;
import enumerations.EmployeeEnum;
import enumerations.RentalRateTypeEnum;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidAccessRightException;
import exception.ModelNotFoundException;
import exception.RentalRateNotFoundException;
import exception.UnknownPersistenceException;
import exception.ValidityDateNotValidException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Wayne
 */
public class SalesManagementModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private ReservationRecordSessionBeanRemote reservationRecordSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    private Employee employee;

    /**
     * @param args the command line arguments
     */
    public SalesManagementModule() {

    }

    public SalesManagementModule(Employee employee, ModelSessionBeanRemote modelSessionBean, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBean,
            CustomerSessionBeanRemote customerSessionBean, ReservationRecordSessionBeanRemote reservationRecordSessionBean,
            EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean,
            CategorySessionBeanRemote categorySessionBean, CarSessionBeanRemote carSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean) {
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
        this.rentalRateSessionBeanRemote = rentalRateSessionBean;
    }

    public void accessRightAllocator() throws InvalidAccessRightException {
        System.out.println("***   CarMS Management Client || Sales Management Module   ***\n");
        if (employee.getAccessRights() == EmployeeEnum.SALESMANAGER) {
            salesManagerMenu();
        } else if (employee.getAccessRights() == EmployeeEnum.OPERATIONSMANAGER) {
            operationsManagerMenu();
        } else {
            throw new InvalidAccessRightException("You don't have MANAGER rights to access the sales management module.");
        }
    }

    private void salesManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer choice = 0;

        while (true) {
            System.out.println("***   CarMS Management Client || Sales Manager Menu   ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Exit\n");
            choice = 0;

            while (choice < 1 || choice > 4) {
                System.out.print("> ");
                choice = scanner.nextInt();
                if (choice == 1) {
                    createRentalRate();
                } else if (choice == 2) {
                    viewAllRentalRates();
                } else if (choice == 3) {
                    viewRentalRateDetails();
                } else if (choice == 4) {
                    break;
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            if (choice == 4) {
                break;
            }
        }
    }

    private void operationsManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("***    Car Rental Management System || Operations manager module    ***\n");
            System.out.println("1: Create New Make And Model");
            System.out.println("2: View All Make And Models");
            System.out.println("3: Update Make and Models");
            System.out.println("4: Delete Make and Models");
            System.out.println("===============================");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("===============================");
            System.out.println("8: View Transit Driver Dispatch Record For Current Day Reservation");
            System.out.println("9: Assign Transit Driver");
            System.out.println("10: Update Transit As Complete");
            System.out.println("11: Log out\n");
            response = 0;

            while (response < 1 || response > 11) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    createNewModel();
                } else if (response == 2) {
                    viewAllModels();
                } else if (response == 3) {
                    viewModelDetail();
                } else if (response == 4) {
                    viewModelDetail();
                } else if (response == 5) {
                    createNewCar();
                } else if (response == 6) {
                    viewAllCars();
                } else if (response == 7) {
                    viewCarDetails();
                } else if (response == 8) {
                    viewTransitDriverDispatchRecords();
                } else if (response == 9) {
                    assignTransitDriver();
                } else if (response == 10) {
                    updateTransitAsComplete();
                } else if (response == 11) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 11) {
                break;
            }
        }
    }

    private void createNewModel() {
        Scanner scanner = new Scanner(System.in);
        Model model = new Model();
        model.setIsEnabled(true);
        System.out.println("*** CarMS Management Client || Sales Management || Create new Model***\n");
        System.out.print("Enter Make name> ");
        model.setMake(scanner.nextLine().trim());
        System.out.print("Enter Model name> ");
        model.setModel(scanner.nextLine().trim());
        System.out.print("This is the list of car categories you are able to choose from. Please enter"
                + " the ID you would like to choose from.\n\n");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        System.out.printf("%4s%15s\n", "ID", "Category");
        for (Category category : categories) {
            System.out.printf("%4s%15s\n", category.getCategoryId(), category.getCategoryName());
        }
        System.out.print("\nEnter Car Category ID > ");
        Long categoryId = scanner.nextLong();
        scanner.nextLine();
        try {
            Long modelId = modelSessionBeanRemote.createNewModel(model, categoryId);
            System.out.println("Model ID: " + modelId + " sucessfully created!");
        } catch (CategoryNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the model");
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void viewAllModels() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***   CarMS Management Client || Operations Manager Menu || View All Rental Rates  ***\n");
        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
        System.out.printf("%4s%40s%40s\n", "ID", "Make", "Model");

        for (Model model : models) {
            System.out.printf("%4s%40s%40s\n", model.getModelId(),
                    model.getMake(), model.getModel());
        }

        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }

    private void viewModelDetail() {
        Scanner scanner = new Scanner(System.in);
        Integer choice = 0;
        System.out.println("***   CarMS Management Client || Operations Manager Menu || View Model Details  ***\n");
        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
         System.out.print("This is the list of types of models you are able to choose from. Please enter"
                + " the ID you would like to choose from.\n\n");
        System.out.printf("%4s%40s%40s\n", "ID", "Make", "Model");

        for (Model model : models) {
            System.out.printf("%4s%40s%40s\n", model.getModelId(),
                    model.getMake(), model.getModel());
        }
        
        System.out.print("Enter Model ID > ");
        Long modelId = scanner.nextLong();
        
        try {
            Model model = modelSessionBeanRemote.retrieveModelById(modelId);

            System.out.println("You have chosen ID: " + modelId + " and its details are shown below.\n");

            System.out.printf("%4s%40s%40s\n", "ID", "Make", "Model");
            System.out.printf("%4s%40s%40s\n", model.getModelId(),
                    model.getMake(), model.getModel());
            System.out.println("------------------------");
            System.out.println("1: Update Model");
            System.out.println("2: Delete Model");
            System.out.println("3: Back\n");
            System.out.print("> ");
            choice = scanner.nextInt();
            if (choice == 1) {
                updateModel(model);
            } else if (choice == 2) {
                deleteModel(model);
            }
            System.out.print("Press any key to continue...> ");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found for ID: " + modelId);
        }
    }

    private void updateModel(Model model) {
        Scanner scanner = new Scanner(System.in);
        Long modelId = model.getModelId();
        
        System.out.println("*** CarMS Management Client || Sales Management || Update Model***\n");
        System.out.print("Enter Make name> ");
        model.setMake(scanner.nextLine().trim());
        System.out.print("Enter Model name> ");
        model.setModel(scanner.nextLine().trim());
        System.out.print("This is the list of car categories you are able to choose from. Please enter"
                + " the ID you would like to choose from.\n\n");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        System.out.printf("%4s%15s\n", "ID", "Category");
        for (Category category : categories) {
            System.out.printf("%4s%15s\n", category.getCategoryId(), category.getCategoryName());
        }
        System.out.print("\nEnter Car Category ID > ");
        Long categoryId = scanner.nextLong();
        scanner.nextLine();
        try {
            model.setCategory(categorySessionBeanRemote.retrieveCategoryById(categoryId));
            modelSessionBeanRemote.updateModel(model);
            System.out.println("Model ID: " + modelId + " sucessfully updated!");
        } catch (ModelNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the rental rate");
        } catch (CategoryNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void deleteModel(Model model) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("***   CaRMS Management Client || Operations Manager Menu || Delete Rental Rate  ***\n");
        Long modelId = model.getModelId();
        try {
            modelSessionBeanRemote.deleteModel(modelId);
            System.out.println("Rental Rate ID: " + modelId + " sucessfully deleted!");
        } catch (ModelNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + modelId);
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void createNewCar() {

    }

    private void viewAllCars() {

    }

    private void viewCarDetails() {

    }

    private void viewTransitDriverDispatchRecords() {

    }

    private void assignTransitDriver() {

    }

    private void updateTransitAsComplete() {

    }

    private void createRentalRate() {
        Scanner scanner = new Scanner(System.in);
        RentalRate rentalRate = new RentalRate();

        System.out.println("***   CarMS Management Client || Operations Manager Menu || Create Rental Rate  ***\n");
        Long categoryId;

        System.out.print("Please Enter the Rental Rate Description > ");
        rentalRate.setRentalRateDescription(scanner.nextLine().trim());
        System.out.print("This is the list of car categories you are able to choose from. Please enter"
                + " the ID you would like to choose from.\n\n");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        System.out.printf("%4s%15s\n", "ID", "Category");
        for (Category category : categories) {
            System.out.printf("%4s%15s\n", category.getCategoryId(), category.getCategoryName());
        }
        System.out.print("\nEnter Car Category ID > ");
        categoryId = scanner.nextLong();

        try {
            rentalRate.setCategory(categorySessionBeanRemote.retrieveCategoryById(categoryId));

            System.out.print("Enter rate per day> ");
            rentalRate.setRateCost(scanner.nextDouble());
            scanner.nextLine();

            System.out.print("This is the list of types of rate you are able to choose from. Please enter"
                    + " the ID you would like to choose from.\n\n");
            System.out.printf("%4s%40s\n", "ID", "Rental Rate Type");
            System.out.printf("%4s%40s\n", "1", "Non Peak");
            System.out.printf("%4s%40s\n", "2", "Peak");
            System.out.printf("%4s%40s\n", "3", "Promotional Rate");
            Integer choice = 0;
            while (choice < 1 || choice > 3) {
                System.out.print("Enter Rental Rate Type Choice> ");
                //Long rentalRateId = scanner.nextLong();
                choice = scanner.nextInt();
                if (choice == 1) {
                    rentalRate.setRentalRateType(RentalRateTypeEnum.NONPEAK);
                } else if (choice == 2) {
                    rentalRate.setRentalRateType(RentalRateTypeEnum.PEAK);
                } else if (choice == 3) {
                    rentalRate.setRentalRateType(RentalRateTypeEnum.PROMOTIONAL);
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            scanner.nextLine();
            System.out.print("Enter validity period? (Enter 'Y' to set validity period) > ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.print("Enter start date (DD/MM/YYYY HH:MM)> ");
                Date startDate = sdf.parse(scanner.nextLine().trim());
                System.out.print("Enter end date (DD/MM/YYYY HH:MM)> ");
                Date endDate = sdf.parse(scanner.nextLine().trim());
                if (endDate.before(startDate)) {
                    throw new ValidityDateNotValidException();
                }
                rentalRate.setStartDate(startDate);
                rentalRate.setEndDate(endDate);
            } else {
                System.out.println("Validity period not entered!");
            }
            rentalRate.setIsEnabled(true);
            Long rentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(rentalRate, categoryId);
            System.out.println("Rental Rate ID: " + rentalRateId + " sucessfully created!");
        } catch (CategoryNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } catch (ValidityDateNotValidException ex) {
            System.out.println("Validity is not valid as End date is before start date!");
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the rental rate");
        } catch (UnknownPersistenceException ex) {
            System.out.println("UnknownPersistenceException when creating new Rental Rate");
        }

        System.out.print("Press any key to continue...  > ");
        scanner.nextLine();
    }

    private void viewAllRentalRates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***   CarMS Management Client || Operations Manager Menu || View All Rental Rates  ***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveRentalRates();
        System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Description", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (RentalRate rentalRate : rentalRates) {

            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            String startDate = "NOT SET";
            if (rentalRate.getStartDate() != null) {
                startDate = sdf.format(rentalRate.getStartDate());
            }
            String endDate = "NOT SET";
            if (rentalRate.getEndDate() != null) {
                endDate = sdf.format(rentalRate.getEndDate());
            }
            System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateDescription(), rentalRate.getCategory().getCategoryName(),
                    rentalRate.getRateCost(), isEnabled, startDate, endDate);
        }

        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();

    }

    private void viewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***   CarMS Management Client || Operations Manager Menu || View Rental Rate Details  ***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveRentalRates();
        System.out.print("This is the list of types of rental rates you are able to choose from. Please enter"
                + " the ID you would like to choose from.\n\n");
        System.out.println("");
        Integer choice = 0;
        System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Description", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (RentalRate rentalRate : rentalRates) {

            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            String startDate = "NO SET DATE";
            if (rentalRate.getStartDate() != null) {
                startDate = sdf.format(rentalRate.getStartDate());
            }
            String endDate = "NO SET DATE";
            if (rentalRate.getEndDate() != null) {
                endDate = sdf.format(rentalRate.getEndDate());
            }
            System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateDescription(), rentalRate.getCategory().getCategoryName(),
                    rentalRate.getRateCost(), isEnabled, startDate, endDate);
        }

        System.out.print("Enter Rental Rate ID > ");
        Long rentalRateId = scanner.nextLong();

        try {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateById(rentalRateId);
            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }

            String startDate = "No Set Date";
            if (rentalRate.getStartDate() != null) {
                startDate = sdf.format(rentalRate.getStartDate());
            }
            String endDate = "No Set Date";
            if (rentalRate.getEndDate() != null) {
                endDate = sdf.format(rentalRate.getEndDate());
            }

            System.out.println("You have chosen ID: " + rentalRateId + " and its details are shown below.\n");

            System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Description", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
            System.out.printf("%4s%50s%32s%16s%16s%20s%20s\n\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateDescription(), rentalRate.getCategory().getCategoryName(),
                    rentalRate.getRateCost(), isEnabled, startDate, endDate);
            System.out.println("------------------------");
            System.out.println("1: Update Rental Rate");
            System.out.println("2: Delete Rental Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            choice = scanner.nextInt();
            if (choice == 1) {
                updateRentalRate(rentalRate);
            } else if (choice == 2) {
                deleteRentalRate(rentalRate);
            }
            System.out.print("Press any key to continue...> ");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
        }
    }

    private void updateRentalRate(RentalRate rentalRate) {
        Scanner scanner = new Scanner(System.in);
        Long rentalRateId = rentalRate.getRentalRateId();

        System.out.println("***   CaRMS Management Client || Operations Manager Menu || Update Rental Rate  ***\n");
        Long categoryId;

        System.out.print("Please Enter the Rental Rate Description > ");
        rentalRate.setRentalRateDescription(scanner.nextLine().trim());
        System.out.print("This is the list of car categories you are able to choose from. Please enter"
                + " the ID you would like to choose from.\n\n");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        System.out.printf("%4s%15s\n", "ID", "Category");
        for (Category category : categories) {
            System.out.printf("%4s%15s\n", category.getCategoryId(), category.getCategoryName());
        }
        System.out.print("\nEnter Car Category ID > ");
        categoryId = scanner.nextLong();

        try {
            rentalRate.setCategory(categorySessionBeanRemote.retrieveCategoryById(categoryId));

            System.out.print("Enter rate per day> ");
            rentalRate.setRateCost(scanner.nextDouble());
            scanner.nextLine();

            System.out.print("This is the list of types of rate you are able to choose from. Please enter"
                    + " the ID you would like to choose from.\n\n");
            System.out.printf("%4s%40s\n", "ID", "Rental Rate Type");
            System.out.printf("%4s%40s\n", "1", "Non Peak");
            System.out.printf("%4s%40s\n", "2", "Peak");
            System.out.printf("%4s%40s\n", "3", "Promotional Rate");
            Integer choice = 0;
            while (choice < 1 || choice > 3) {
                System.out.print("Enter Rental Rate Type Choice> ");
                //Long rentalRateId = scanner.nextLong();
                choice = scanner.nextInt();
                if (choice == 1) {
                    rentalRate.setRentalRateType(RentalRateTypeEnum.NONPEAK);
                } else if (choice == 2) {
                    rentalRate.setRentalRateType(RentalRateTypeEnum.PEAK);
                } else if (choice == 3) {
                    rentalRate.setRentalRateType(RentalRateTypeEnum.PROMOTIONAL);
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            scanner.nextLine();
            System.out.print("Enter validity period? (Enter 'Y' to set validity period) > ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.print("Enter start date (DD/MM/YYYY HH:MM)> ");
                Date startDate = sdf.parse(scanner.nextLine().trim());
                System.out.print("Enter end date (DD/MM/YYYY HH:MM)> ");
                Date endDate = sdf.parse(scanner.nextLine().trim());
                if (endDate.before(startDate)) {
                    throw new ValidityDateNotValidException();
                }
                rentalRate.setStartDate(startDate);
                rentalRate.setEndDate(endDate);
            } else {
                System.out.println("Validity period not entered!");
            }
            rentalRate.setIsEnabled(true);
            rentalRateSessionBeanRemote.updateRentalRate(rentalRate);
            System.out.println("Rental Rate ID: " + rentalRateId + " successfully updated!");
        } catch (CategoryNotFoundException ex) {
            System.out.println("Car Category of ID: " + categoryId + " does not exist!");
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } catch (ValidityDateNotValidException ex) {
            System.out.println("Validity is not valid as End date is before start date!");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRate.getRentalRateId());
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the rental rate");
        }

        System.out.print("Press any key to continue...  > ");
        scanner.nextLine();
    }

    private void deleteRentalRate(RentalRate rentalRate) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("***   CaRMS Management Client || Operations Manager Menu || Delete Rental Rate  ***\n");
        Long rentalRateId = rentalRate.getRentalRateId();
        try {
            rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
            System.out.println("Rental Rate ID: " + rentalRateId + " sucessfully deleted!");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
