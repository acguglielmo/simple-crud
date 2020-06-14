package com.acguglielmo.simplecrud.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "TB_CUSTOMER")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @Column(name = "DC_CNPJ")
    @EqualsAndHashCode.Include
    private String cnpj;

    @Column(name = "DC_NAME")
    private String name;

    @Column(name = "IN_ACTIVE")
    private boolean active = true;

    @OneToMany
    private Set<Contract> contracts;

    public Customer(final String cnpj) {

    	super();

    	this.cnpj = cnpj;

    }

    public Customer inactivate() {

    	this.active = false;

    	return this;

    }

}
