/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationRecord;
import exception.CustomerNotFoundException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Wayne
 */
@Stateless
public class ReservationRecordSessionBean implements ReservationRecordSessionBeanRemote, ReservationRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewReservationRecord (ReservationRecord reservationRecord) {
        em.persist(reservationRecord);
        em.flush();

        return reservationRecord.getReservationId();
    }

    @Override
    public List<ReservationRecord> retrieveReservationRecords() {
        Query query = em.createQuery("SELECT R FROM ReservationRecord r");

        return query.getResultList();
    }
    
    @Override
    public List<ReservationRecord> retrieveReservationsByUsername(String username) {
        Query query = em.createQuery("SELECT r FROM Reservation r IN (r.customer) c = :inUsername");
        query.setParameter("inId", username);
        return query.getResultList();
    }
}
