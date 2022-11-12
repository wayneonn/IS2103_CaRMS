/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import exception.InputDataValidationException;
import exception.PartnerNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public Long createNewPartner(Partner partner) throws InputDataValidationException, UnknownPersistenceException;
    public Partner retrievePartnerById(Long partnerId)throws PartnerNotFoundException;
    public Partner updatePartner(Partner updatedPartner) throws InputDataValidationException, UnknownPersistenceException;
    public void deleteOutlet(Long partnerId) throws PartnerNotFoundException;
    
}
