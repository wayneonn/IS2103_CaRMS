/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.List;

/**
 *
 * @author User
 */
public interface TransitDriverDispatchRecordSessionBeanLocal {
    public Long createNewTransitDriverDispatchRecord (TransitDriverDispatchRecord reservationRecord);
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecord();
}
