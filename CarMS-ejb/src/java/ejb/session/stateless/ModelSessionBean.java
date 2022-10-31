/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Model;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import exception.ModelNotFoundException;

/**
 *
 * @author Wayne
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanLocal, ModelSessionBeanRemote {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewModel(Model model) {
        em.persist(model);
        em.flush();

        return model.getModelId();
    }

    @Override
    public List<Model> retrieveAllModels() {
        Query query = em.createQuery("SELECT m FROM Model m");

        return query.getResultList();
    }
    
    @Override
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException {
        Model model = em.find(Model.class, modelId);
        
        if(model != null)
        {
            return model;
        }
        else
        {
            throw new ModelNotFoundException("Customer does not exist: " + modelId);
        }
    }
    
    @Override 
    public Model updateModel(Model updatedModel){
        return em.merge(updatedModel);
    }

    @Override
    public void deleteModel(Long modelId)throws ModelNotFoundException{
        Model modelToRemove = retrieveModelById(modelId); //check if in use
        em.remove(modelToRemove); //mark as disabled
    }
}
