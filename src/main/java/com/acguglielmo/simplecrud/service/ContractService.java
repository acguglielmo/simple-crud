package com.acguglielmo.simplecrud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.entity.ContractId;
import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.entity.Service;
import com.acguglielmo.simplecrud.exception.EntityAlreadyExistsException;
import com.acguglielmo.simplecrud.exception.NotFoundException;
import com.acguglielmo.simplecrud.mapper.ContractMapper;
import com.acguglielmo.simplecrud.repository.ContractRepository;
import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;

@org.springframework.stereotype.Service
public class ContractService {

	@Autowired
	private ContractMapper mapper;

	@Autowired
	private ContractRepository repository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	public Page<ContractResponse> findAll(final Pageable pageable) {

		return repository.findAll(pageable)
			.map( mapper::fromEntity ) ;

	}

	public Optional<ContractResponse> findBy(final String number, final String cnpj) {

		return repository.findByIdNumberAndIdCustomerCnpj(number, cnpj)
			.map( mapper::fromEntity );

	}

	public ContractResponse create(final String customerCnpj, final ContractRequest request) {

		final Customer customer = findCustomer(customerCnpj);

		final Service service = findService(request);

		final Contract contract = mapper.buildFrom(customer, service, request);

		checkIfContractAlreadyExists( contract.getId() );

		final Contract savedEntity = repository.save(contract);

		return mapper.fromEntity(savedEntity);

	}

    public Optional<ContractResponse> update(final String number,
		final String cnpj, final ContractRequest request) {

		return repository.findByIdNumberAndIdCustomerCnpj(number, cnpj)
			.map( e ->
				{
					e.setService( findService(request) );
					return e;
				}
			)
			.map( repository::save )
			.map( mapper::fromEntity );

	}

	public boolean delete(final String number, final String cnpj) {

		return repository.findByIdNumberAndIdCustomerCnpj(number, cnpj)
			.map(e -> {

				repository.delete( e );

				return true;

			})
			.orElse( false );

	}

	private Customer findCustomer(final String cnpj) {

		return customerRepository.findById( cnpj )
			.orElseThrow( () -> new NotFoundException("Customer not found!") ) ;
	}

	private Service findService(final ContractRequest request) {

		return serviceRepository.findById(request.getServiceId() )
			.orElseThrow( () -> new NotFoundException("Service not found!") ) ;
	}

   private void checkIfContractAlreadyExists(final ContractId contractId) {

       if ( repository.findById( contractId ).isPresent() ) {

           throw new EntityAlreadyExistsException("Contract already exists!");

       }

   }

}
