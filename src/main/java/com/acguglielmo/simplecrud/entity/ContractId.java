package com.acguglielmo.simplecrud.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Embeddable
@RequiredArgsConstructor
public class ContractId {

    @Column(name = "NU_CONTRACT")
    public final String number;

    @ManyToOne
    public final Customer customer;

}