/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumerations.EmployeeEnum;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayneonn
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 6, max = 32)
    private String employeeUsername;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 8, max = 32)
    private String employeePassword;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String employeeName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private EmployeeEnum accessRights;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @OneToMany(mappedBy = "employee")
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;

    /**
     * @return the employeeId
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    public Employee(String employeeUsername, String employeePassword, String employeeName, EmployeeEnum accessRights) {
        this.employeeUsername = employeeUsername;
        this.employeePassword = employeePassword;
        this.employeeName = employeeName;
        this.accessRights = accessRights;
    }

    public Employee() {
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeUsername
     */
    public String getEmployeeUsername() {
        return employeeUsername;
    }

    /**
     * @param employeeUsername the employeeUsername to set
     */
    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }

    /**
     * @return the employeePassword
     */
    public String getEmployeePassword() {
        return employeePassword;
    }

    /**
     * @param employeePassword the employeePassword to set
     */
    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the accessRights
     */
    public EmployeeEnum getAccessRights() {
        return accessRights;
    }

    /**
     * @param accessRights the accessRights to set
     */
    public void setAccessRights(EmployeeEnum accessRights) {
        this.accessRights = accessRights;
    }

    /**
     * @return the outlet
     */
    public Outlet getOutlet() {
        return outlet;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    /**
     * @return the transitDriverDispatchRecords
     */
    public List<TransitDriverDispatchRecord> getTransitDriverDispatchRecords() {
        return transitDriverDispatchRecords;
    }

    /**
     * @param transitDriverDispatchRecords the transitDriverDispatchRecords to set
     */
    public void setTransitDriverDispatchRecords(List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }
}
