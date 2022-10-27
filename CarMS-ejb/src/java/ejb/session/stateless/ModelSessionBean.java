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

}
