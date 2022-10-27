/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Wayne
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCar(Cars car) {
        em.persist(car);
        em.flush();

        return car.getCarId();
    }

    @Override
    public List<Cars> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c");

        return query.getResultList();
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
