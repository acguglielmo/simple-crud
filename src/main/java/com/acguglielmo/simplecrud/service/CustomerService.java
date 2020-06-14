package com.acguglielmo.simplecrud.service;

import static java.util.Collections.singletonList;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

@Service
public class CustomerService {

	public Page<CustomerResponse> findAll(final Pageable pageable) {

		if (pageable.getPageNumber() == 0 ) {

			final CustomerResponse customer =
				new CustomerResponse("01567964000189", "customer", new HashSet<>() );

			return new PageImpl<>( singletonList(customer) );

		}

		return Page.empty();

	}

	public Optional<CustomerResponse> findBy(final String cnpj) {

		if ( "01567964000189".equals( cnpj ) ) {

			final CustomerResponse customer =
				new CustomerResponse("01567964000189", "customer", new HashSet<>() );

			return Optional.of(customer);

		}

		return Optional.empty();
	}

	public CustomerResponse create( final CustomerRequest customer) {

		final CustomerResponse customerResponse =
			new CustomerResponse(
				customer.getCnpj(),
				customer.getName(),
				new HashSet<>() );

		return customerResponse;

	}

	public Optional<CustomerResponse> update( final String cnpj, final CustomerRequest customer) {

		return findBy( cnpj )
			.map( e ->
				new CustomerResponse(
					customer.getCnpj(),
					customer.getName(),
					new HashSet<>() )

			);
	}

}
