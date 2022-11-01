/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Cars;
import entity.Category;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import exception.CategoryNotFoundException;
import exception.InputDataValidationException;
import exception.ModelNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
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
            try {
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
    public Model retrieveModelById(Long modelId) throws ModelNotFoundException {

        Model model = em.find(Model.class, modelId);

        if (model != null) {
            return model;
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }

    @Override
    public List<Model> retrieveAllModels() {
        Query query = em.createQuery("SELECT m FROM Model m ORDER BY m.category, m.model, m.make ASC");

        return query.getResultList();
    }

    @Override
    public Model updateModel(Model updatedModel) throws ModelNotFoundException, InputDataValidationException {
        if (updatedModel != null && updatedModel.getModelId() != null) {
            Set<ConstraintViolation<Model>> constraintViolations = validator.validate(updatedModel);

            if (constraintViolations.isEmpty()) {
                return em.merge(updatedModel);
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ModelNotFoundException("Rental Rate Id not provided for rental rate to be updated");
        }
    }

    @Override
    public void deleteModel(Long modelId) throws ModelNotFoundException {
        try {
            Model modelToRemove = retrieveModelById(modelId);
            if (rentalRateInUse(modelId).isEmpty()) {
                em.remove(modelToRemove);
            } else {
                modelToRemove.setIsEnabled(false);
            }
        } catch (ModelNotFoundException ex) {
            throw new ModelNotFoundException("Rental rate of ID: " + modelId + " not found!");
        }
    }
    
    @Override
    public List<Cars> rentalRateInUse(Long modelId) throws ModelNotFoundException {
        try {
            Model model = retrieveModelById(modelId);
            List<Cars> carRecords;
            model.getCars().size();
            carRecords = model.getCars();
            return carRecords;
        } catch (ModelNotFoundException ex) {
            throw new ModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }
}
