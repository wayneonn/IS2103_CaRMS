/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Model;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Wayne
 */
@Local
public interface ModelSessionBeanLocal {

    public Long createNewModel(Model model);

    public List<Model> retrieveAllModels();
}
