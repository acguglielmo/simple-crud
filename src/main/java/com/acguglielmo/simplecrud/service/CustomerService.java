package com.acguglielmo.simplecrud.service;

import static java.util.Collections.singletonList;

import java.util.Optional;

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

			customer.setCnpj("01567964000189");

			return new PageImpl<>( singletonList(customer) );

		}

		return Page.empty();

	}

	public Optional<CustomerResponse> findBy(final String cnpj) {

		if ( "01567964000189".equals( cnpj ) ) {

			final CustomerResponse customer = new CustomerResponse();

			customer.setCnpj("01567964000189");

			return Optional.of(customer);

		}

		return Optional.empty();
	}

}
