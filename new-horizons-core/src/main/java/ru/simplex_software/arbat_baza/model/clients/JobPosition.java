package ru.simplex_software.arbat_baza.model.clients;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Должность.
 */
@Entity
public class JobPosition extends LongIdPersistentEntity {

    /**
     * Сотрудник.
     */
    @ManyToOne
    private NaturalClient employee;

    /**
     * Компания.
     */
    @ManyToOne
    private JuridicalClient company;

    /**
     * Должность сотрудника.
     */
    private String name;

    public NaturalClient getEmployee() {
        return employee;
    }

    public void setEmployee(NaturalClient employee) {
        this.employee = employee;
    }

    public JuridicalClient getCompany() {
        return company;
    }

    public void setCompany(JuridicalClient company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
