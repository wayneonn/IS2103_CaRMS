/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import entity.Outlet;
import exception.InputDataValidationException;
import exception.OutletNotFoundException;
import exception.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface OutletSessionBeanRemote {
    
    public Long createNewOutlet(Outlet outlet) throws InputDataValidationException, UnknownPersistenceException ;
    
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
    public void deleteOutlet(Long outletId) throws OutletNotFoundException;
    
    public Outlet updateOutlet(Outlet updatedOutlet) throws InputDataValidationException, UnknownPersistenceException;
    
    public List<Outlet> retrieveAllOutlets();
}
