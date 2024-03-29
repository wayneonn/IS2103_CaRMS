/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import exception.PartnerNotFoundException;
import entity.Partner;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createNewPartner(Partner partner) throws InputDataValidationException, UnknownPersistenceException;

    public Partner retrievePartnerById(Long partnerId) throws PartnerNotFoundException;

    public Partner updatePartner(Partner updatedPartner) throws InputDataValidationException, UnknownPersistenceException;

    public void deletePartner(Long partnerId) throws PartnerNotFoundException;

    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    public Partner login(String username, String password) throws InvalidLoginCredentialException;
}
