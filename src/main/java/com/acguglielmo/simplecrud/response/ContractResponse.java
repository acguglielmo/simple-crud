package com.acguglielmo.simplecrud.response;

import lombok.Data;

@Data
public class ContractResponse {

	private String number;

	private CustomerResponse customer;

	private ServiceResponse service;

	private TermResponse term;

}
