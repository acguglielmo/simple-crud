package com.acguglielmo.simplecrud.request;

import lombok.Data;

@Data
public class ContractRequest {

	private String number;

	private Long serviceId;

	private TermRequest term;

}
