package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.response.ContractResponse;

@Mapper(componentModel = "spring")
public interface ContractMapper {

	ContractResponse fromEntity( Contract entity );

}
