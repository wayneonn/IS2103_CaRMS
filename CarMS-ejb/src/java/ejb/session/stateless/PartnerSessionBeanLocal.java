/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import exception.PartnerNotFoundException;
import entity.Partner;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface PartnerSessionBeanLocal {
    
    public Long createNewPartner(Partner partner);
    public Partner retrievePartnerById(Long partnerId)throws PartnerNotFoundException;
    public Partner updatePartner(Partner updatedPartner);
    public void deleteOutlet(Long partnerId) throws PartnerNotFoundException;
}
