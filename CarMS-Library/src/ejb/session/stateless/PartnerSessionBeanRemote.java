/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import exception.PartnerNotFoundException;
import java.util.List;

/**
 *
 * @author User
 */
public interface PartnerSessionBeanRemote {
    
    public Long createNewPartner(Partner partner);
    public Partner retrievePartnerById(Long partnerId)throws PartnerNotFoundException;
    public Partner updatePartner(Partner updatedPartner);
    public void deleteOutlet(Long partnerId) throws PartnerNotFoundException;
    
}
