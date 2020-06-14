package com.acguglielmo.simplecrud.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private String cnpj;

    private String name;

    private Set<ContractResponse> contracts;

}
