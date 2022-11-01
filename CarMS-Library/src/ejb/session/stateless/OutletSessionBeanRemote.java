/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import entity.Outlet;
import exception.OutletNotFoundException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface OutletSessionBeanRemote {
    
    public Long createNewOutlet(Outlet outlet);
    
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
    public void deleteOutlet(Long outletId) throws OutletNotFoundException;
    
    public Outlet updateOutlet(Outlet updatedOutlet);
    
    public List<Outlet> retrieveAllOutlets();
}
