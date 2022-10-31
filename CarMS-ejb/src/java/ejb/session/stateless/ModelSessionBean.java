/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class ModelSessionBean implements ModelSessionBeanLocal, ModelSessionBeanRemote {

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ModelSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @PersistenceContext(unitName = "CarMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewModel(Model model, Long categoryId) throws InputDataValidationException, CategoryNotFoundException {

        Set<ConstraintViolation<Model>> constraintViolations = validator.validate(model);

        if (constraintViolations.isEmpty()) {
            try{
            Category category = categorySessionBean.retrieveCategoryById(categoryId);
            model.setCategory(category);
            em.persist(model);
            em.flush();

            return model.getModelId();
            } catch (CategoryNotFoundException ex) {
                throw new CategoryNotFoundException("Car Category not found for ID: " + categoryId); 
            }
        } else {
            throw new InputDataValidationException(this.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Model>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<Model> retrieveAllModels() {
        Query query = em.createQuery("SELECT m FROM Model m");

        return query.getResultList();
    }

}
