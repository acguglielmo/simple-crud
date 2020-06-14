package com.acguglielmo.simplecrud.entity;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Entity(name = "TB_CONTRACT")
public class Contract {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private final ContractId id;

    @Embedded
    private Term term;

    @ManyToOne
    private Service service;

}
