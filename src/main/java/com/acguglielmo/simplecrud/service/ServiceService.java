package com.acguglielmo.simplecrud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.acguglielmo.simplecrud.entity.Service;
import com.acguglielmo.simplecrud.exception.EntityAlreadyExistsException;
import com.acguglielmo.simplecrud.mapper.ServiceMapper;
import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;

@org.springframework.stereotype.Service
public class ServiceService {

	@Autowired
	private ServiceMapper mapper;

	@Autowired
	private ServiceRepository repository;

	public Page<ServiceResponse> findAll(final Pageable pageable) {

		return repository.findAll(pageable)
			.map( mapper::fromEntity ) ;

	}

	public Optional<ServiceResponse> findBy(final Long id) {

		return repository.findById(id)
			.map( mapper::fromEntity );

	}

	public ServiceResponse create(final ServiceRequest request) {

		checkIfServiceAlreadyExists( request.getName() );

		final Service savedEntity = repository.save( mapper.fromRequest(request) );

		return mapper.fromEntity(savedEntity);

	}

	public Optional<ServiceResponse> update(final Long id, final ServiceRequest service) {

		return repository.findById(id)
			.map( e ->
				{
					e.setName( service.getName() );
					return e;
				}
			)
			.map( repository::save )
			.map( mapper::fromEntity );

	}

	public boolean delete(final Long id) {

		return repository.findById(id)
			.map(e -> {

				repository.delete( e );

				return true;

			})
			.orElse( false );

	}

	private void checkIfServiceAlreadyExists(final String name) {

	   if ( repository.findByName( name ).isPresent() ) {

	       throw new EntityAlreadyExistsException("Service already exists!");

	   }

    }

}
