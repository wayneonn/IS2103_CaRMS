/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Customer;
import entity.MCRCustomer;
import exception.InvalidLoginCredentialException;
import java.util.Scanner;
/**
 *
 * @author User
 */
public class MainApp {
    
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    
    private MCRCustomer currentCustomerEntity;
    
    public MainApp() {       
        
    }
    
    public MainApp(CategorySessionBeanRemote categorySessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote) {       
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Merlion Car Rental! ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
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
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2) {
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
            //currentCustomerEntity = customerSessionBeanRemote.login(username, password);      
        }
        else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Car Rental ***\n");
            System.out.println("You are login as " + currentCustomerEntity.getCustUsername());
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
                    
                }
                else if(response == 2)
                {
                    
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
}
