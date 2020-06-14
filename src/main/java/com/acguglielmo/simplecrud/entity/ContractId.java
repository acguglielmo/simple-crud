package com.acguglielmo.simplecrud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContractId implements Serializable {

	private static final long serialVersionUID = -8401112530595544304L;

	@Column(name = "NU_CONTRACT")
    public String number;

    @ManyToOne
    public Customer customer;

}