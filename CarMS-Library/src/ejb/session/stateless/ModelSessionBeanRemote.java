/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Model;
import java.util.List;
import javax.ejb.Remote;
import exception.ModelNotFoundException;

/**
 *
 * @author Wayne
 */
@Remote
public interface ModelSessionBeanRemote {

    public Long createNewModel(Model model);

    public List<Model> retrieveAllModels();
    
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException;
    
    public Model updateModel(Model updatedModel);
    
    public void deleteModel(Long modelId)throws ModelNotFoundException;
}
