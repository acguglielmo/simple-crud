package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.entity.Service;
import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;

@Mapper(
	componentModel = "spring",
	uses = {
		CustomerMapper.class,
		ServiceMapper.class,
		TermMapper.class
})
public interface ContractMapper {

	@Mapping(source = "id.number", target = "number")
	@Mapping(source = "id.customer", target = "customer")
	ContractResponse fromEntity(Contract entity);

	@Mapping(source = "customer", target = "id.customer")
	@Mapping(source = "request.number", target = "id.number")
	Contract buildFrom(Customer customer, Service service, ContractRequest request);

}
