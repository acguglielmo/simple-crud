package com.acguglielmo.simplecrud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.exception.EntityAlreadyExistsException;
import com.acguglielmo.simplecrud.mapper.CustomerMapper;
import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

@Service
public class CustomerService {

	@Autowired
	private CustomerMapper mapper;

	@Autowired
	private CustomerRepository repository;

	public Page<CustomerResponse> findAll(final Pageable pageable) {

		return repository.findAll(pageable)
			.map( mapper::fromEntity ) ;

	}

	public Optional<CustomerResponse> findBy(final String cnpj) {

		return repository.findById(cnpj)
			.map( mapper::fromEntity );

	}

	public CustomerResponse create( final CustomerRequest request) {

	    checkIfCustomerAlreadyExists( request.getCnpj() );

		final Customer savedEntity = repository.save( mapper.fromRequest(request) );

		return mapper.fromEntity(savedEntity);

	}

    public Optional<CustomerResponse> update( final String cnpj, final CustomerRequest customer) {

		return repository.findById(cnpj)
			.map( e ->
				{
					e.setName( customer.getName() );
					return e;
				}
			)
			.map( repository::save )
			.map( mapper::fromEntity );

	}

	public boolean delete(final String cnpj) {

		return repository.findById(cnpj)
			.map(e -> {

				repository.delete( e );

				return true;

			})
			.orElse( false );

	}

   private void checkIfCustomerAlreadyExists(final String cnpj) {

       if ( repository.findById( cnpj ).isPresent() ) {

           throw new EntityAlreadyExistsException("Customer already exists!");

       }

    }

}
