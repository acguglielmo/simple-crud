package com.acguglielmo.simplecrud.response;

import java.util.Set;

import lombok.Setter;

@Setter
public class CustomerResponse {

    private String cnpj;

    private String name;

    private Set<ContractResponse> contracts;

}
