/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String categoryName;
    
    @OneToMany(mappedBy = "category")
    private List<Model> models;
    @OneToMany(mappedBy = "category")
    private List<RentalRate> rentalRates;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long id) {
        this.categoryId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Category[ id=" + categoryId + " ]";
    }

    /**
     * @return the name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param name the name to set
     */
    public void setCategoryName(String name) {
        this.categoryName = name;
    }
    
}
