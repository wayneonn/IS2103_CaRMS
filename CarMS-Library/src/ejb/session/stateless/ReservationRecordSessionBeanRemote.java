/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationRecord;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface ReservationRecordSessionBeanRemote {
    
    public Long createNewReservationRecord (ReservationRecord reservationRecord);
    public List<ReservationRecord> retrieveReservationRecord();
}
