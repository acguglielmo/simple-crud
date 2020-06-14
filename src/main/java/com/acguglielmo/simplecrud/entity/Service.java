package com.acguglielmo.simplecrud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "TB_SERVICE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Service {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

    @Column(name = "DC_NAME")
    @EqualsAndHashCode.Include
    private String name;

    public Service(final String name) {

    	super();

    	this.name = name;

    }

}
