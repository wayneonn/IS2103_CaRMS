/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationRecordSessionBeanRemote;
import entity.MCRCustomer;
import exception.InputDataValidationException;
import exception.RentalReservationNotFoundException;

/**
 *
 * @author Wayne
 */
public class Main {

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;
    
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;
    
    @EJB
    private static ReservationRecordSessionBeanRemote reservationRecordSessionBeanRemote;
    
    private static MCRCustomer mcrCustomerEntity;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InputDataValidationException, RentalReservationNotFoundException {
        // TODO code application logic here
        
        //Testing CODE
        //System.out.println(modelSessionBean.retrieveAllModels());
        //System.out.println(customerSessionBeanRemote.retrieveAllCustomers());
        //System.out.println(categorySessionBean.retrieveAllCategories());
        MainApp mainApp = new MainApp(categorySessionBeanRemote, customerSessionBeanRemote, modelSessionBeanRemote, reservationRecordSessionBeanRemote, mcrCustomerEntity, outletSessionBean, carSessionBean, rentalRateSessionBean);
        mainApp.runApp();
    }
    
    
}
