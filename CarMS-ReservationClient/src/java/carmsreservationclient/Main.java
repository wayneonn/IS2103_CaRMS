/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import javax.ejb.EJB;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;

/**
 *
 * @author Wayne
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static ModelSessionBeanRemote modelSessionBean;

    @EJB
    private static CategorySessionBeanRemote categorySessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //Testing CODE
        System.out.println(modelSessionBean.retrieveAllModels());
        System.out.println(customerSessionBeanRemote.retrieveAllCustomers());
        System.out.println(categorySessionBean.retrieveAllCategories());
        
    }
    
}
