/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import entity.Category;
import entity.Model;
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
    private ModelSessionBeanLocal modelSessionBean;

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
            categorySessionBean.createNewCategory(new Category("Standard Sedan"));
            categorySessionBean.createNewCategory(new Category("Family Sedan"));
            categorySessionBean.createNewCategory(new Category("Luxury Sedan"));
            categorySessionBean.createNewCategory(new Category("SUV/Minivan"));
        }
        
        if (em.find(Model.class, 1l) == null) {
            modelSessionBean.createNewModel(new Model("Toyota", "Corolla Altis"));
        }
    }

}
