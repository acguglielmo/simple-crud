package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;

import com.acguglielmo.simplecrud.entity.Service;
import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

	Service fromRequest(final ServiceRequest request);

	ServiceResponse fromEntity(final Service entity);

}
