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
import javax.ejb.EJB;

/**
 *
 * @author Wayne
 */
public class Main {

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private static TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBean;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;

    @EJB
    private static ReservationRecordSessionBeanRemote reservationRecordSessionBean;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;

    @EJB
    private static ModelSessionBeanRemote modelSessionBean;

    @EJB
    private static CategorySessionBeanRemote categorySessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(modelSessionBean, transitDriverDispatchRecordSessionBean, customerSessionBean,
        reservationRecordSessionBean, employeeSessionBean, outletSessionBean, categorySessionBean, carSessionBean, rentalRateSessionBean);
        mainApp.runApp();
    }
    
}
