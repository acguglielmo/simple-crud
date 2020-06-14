package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;

import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.response.CustomerResponse;

@Mapper(componentModel = "spring", uses = ContractMapper.class)
public interface CustomerMapper {

	CustomerResponse fromEntity( Customer entity );

}
