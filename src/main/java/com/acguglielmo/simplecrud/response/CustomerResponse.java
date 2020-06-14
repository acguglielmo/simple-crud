package com.acguglielmo.simplecrud.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerResponse {

    private String cnpj;

    private String name;

    private Set<ContractResponse> contracts;

}
