/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import exception.CategoryNameAlreadyExistException;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.UnknownPersistenceException;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Wayne
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CategorySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCategory(Category category) throws CategoryNameAlreadyExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

            if (constraintViolations.isEmpty()) {
                em.persist(category);
                em.flush();
                return category.getCategoryId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CategoryNameAlreadyExistException("Category Name already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Category>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Category retrieveCategoryById(Long categoryId) throws CategoryNotFoundException {

        Category category = em.find(Category.class, categoryId);

        if (category != null) {
            return category;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }

    @Override
    public List<Category> retrieveAllCategories() {
        Query query = em.createQuery("SELECT c FROM Category c ORDER BY c.categoryId ASC");
        query.getResultList().size();

        return query.getResultList();
    }

}
