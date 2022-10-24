/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CategorySessionBean;
import ejb.session.stateless.CategorySessionBeanLocal;
import entity.Cars;
import entity.Category;
import enumerations.CarStateEnumeration;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Wayne
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
//        if (em.find(Cars.class, 1l) == null) {
//            carSessionBean.createNewCar(new Cars("TESTING", CarStateEnumeration.AVAILABLE, "Toyota", 5));
//        }

        if (em.find(Category.class, 1l) == null) {
            categorySessionBean.createNewCategory(new Category("Sedan"));
            categorySessionBean.createNewCategory(new Category("SUV"));
            categorySessionBean.createNewCategory(new Category("Van"));
            categorySessionBean.createNewCategory(new Category("Pickup Truck"));
        }
    }

}
