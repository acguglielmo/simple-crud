package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.response.ContractResponse;

@Mapper(
	componentModel = "spring",
	uses = { CustomerMapper.class, ServiceMapper.class })
public interface ContractMapper {

	@Mapping(source = "id.number", target = "number")
	@Mapping(source = "id.customer", target = "customer")
	ContractResponse fromEntity(Contract entity);

}
