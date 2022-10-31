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
    public Outlet retrieveOutletById(Long outletId) {
        Outlet outlet = em.find(Outlet.class, outletId);
        return outlet;
    }
   
    @Override
    public Outlet updateOutlet(Outlet updatedOutlet) {
        return em.merge(updatedOutlet);
    }
    
    @Override
    public void deleteOutlet(Long outletId){
        Outlet outletToRemove = retrieveOutletById(outletId); 
        em.remove(outletToRemove);
    }
}
