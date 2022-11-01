/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Category;
import entity.Outlet;
import exception.CategoryNotFoundException;
import exception.OutletNotFoundException;
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
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCategory(Category category) {
        
        em.persist(category);
        em.flush();

        return category.getCategoryId();
    }
    
    @Override
    public Category retrieveCategoryById(Long categoryId) throws CategoryNotFoundException {
        
        Category category = em.find(Category.class, categoryId);
        
        if (category != null){
            return category;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }

    @Override
    public List<Category> retrieveAllCategories() {
        Query query = em.createQuery("SELECT c FROM Category c");
        query.getResultList().size();
        
        return query.getResultList();
    }

}
