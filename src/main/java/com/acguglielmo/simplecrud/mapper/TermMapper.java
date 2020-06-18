package com.acguglielmo.simplecrud.mapper;

import org.mapstruct.Mapper;

import com.acguglielmo.simplecrud.entity.Term;
import com.acguglielmo.simplecrud.request.TermRequest;
import com.acguglielmo.simplecrud.response.TermResponse;

@Mapper(componentModel = "spring")
public interface TermMapper {

	TermResponse fromEntity(Term entity);

	Term fromRequest(TermRequest request);

}
