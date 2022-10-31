/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import exception.PartnerNotFoundException;
import entity.Partner;
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
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewPartner(Partner partner) {
        em.persist(partner);
        em.flush();

        return partner.getPartnerId();
    }

    @Override
    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);
        
        if(partner != null)
        {
            return partner;
        }
        else
        {
            throw new PartnerNotFoundException("Partner does not exist: " + partnerId);
        }
    }
    
    @Override
    public Partner updatePartner(Partner updatedPartner){
        return em.merge(updatedPartner);
    }
   
    @Override
    public void deleteOutlet(Long partnerId) throws PartnerNotFoundException{
        Partner partnerToRemove = retrievePartnerById(partnerId); 
        em.remove(partnerToRemove);
    }
}
