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
import enumerations.CarStateEnumeration;
import exception.InputDataValidationException;
import exception.InvalidLoginException;
import exception.LicenseNumberExsistsException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
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

    public void runApp() {

    }

}
