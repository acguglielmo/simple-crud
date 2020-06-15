package com.acguglielmo.simplecrud.entity;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "TB_CONTRACT")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contract {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ContractId id;

    @Embedded
    private Term term;

    @ManyToOne
    private Service service;

    public Contract(final ContractId id) {

    	super();

    	this.id = id;

    }

}
