package com.acguglielmo.simplecrud.service;

import static java.util.Collections.singletonList;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

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

	public CustomerResponse create( final CustomerRequest request) {

		final Customer entity = new Customer( request.getCnpj() );
		entity.setName( request.getName() );

		final Customer savedEntity = repository.save(entity);

		final CustomerResponse customerResponse =
			new CustomerResponse(
				savedEntity.getCnpj(),
				savedEntity.getName(),
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

	public boolean delete( final String cnpj) {

		return findBy( cnpj )
			.map(e -> true)
			.orElse( false );

	}

}
