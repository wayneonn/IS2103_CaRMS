/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
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
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewOutlet(Outlet outlet) {
        em.persist(outlet);
        em.flush();

        return outlet.getOutletId();
    }

    @Override
    public List<Outlet> retrieveOutlet() {
        Query query = em.createQuery("SELECT T FROM Outlet t");

        return query.getResultList();
    }
   
}
