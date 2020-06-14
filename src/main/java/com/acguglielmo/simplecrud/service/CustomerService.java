package com.acguglielmo.simplecrud.service;

import static java.util.Collections.singletonList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.acguglielmo.simplecrud.response.CustomerResponse;

@Service
public class CustomerService {

	public Page<CustomerResponse> findAll(final Pageable pageable) {

		if (pageable.getPageNumber() == 0 ) {

			final CustomerResponse customer = new CustomerResponse();

			customer.setCnpj("454545");

			return new PageImpl<>( singletonList(customer) );

		}

		return Page.empty();

	}

}
