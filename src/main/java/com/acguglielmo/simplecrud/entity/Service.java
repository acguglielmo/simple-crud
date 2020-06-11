package com.acguglielmo.simplecrud.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Service {

    @Column(name = "DC_NAME")
    @EqualsAndHashCode.Include
    private final String name;

}
