/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Outlet;
import entity.RentalRate;
import exception.CategoryNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Wayne
 */
@Local
public interface CategorySessionBeanLocal {

    public Long createNewCategory(Category category);
    
    public Category retrieveCategoryById(Long categoryId) throws CategoryNotFoundException;

    public List<Category> retrieveAllCategories();

}
