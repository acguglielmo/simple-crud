package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;

import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	CustomerResponse fromEntity( Customer entity );

	Customer fromRequest(CustomerRequest request);

}
