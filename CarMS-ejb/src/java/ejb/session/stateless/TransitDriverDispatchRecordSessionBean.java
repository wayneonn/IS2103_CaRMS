/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
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
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewTransitDriverDispatchRecord (TransitDriverDispatchRecord reservationRecord) {
        em.persist(reservationRecord);
        em.flush();

        return reservationRecord.getDispatchedId();
    }

    @Override
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecord() {
        Query query = em.createQuery("SELECT D FROM Outlet d");

        return query.getResultList();
    }
}
