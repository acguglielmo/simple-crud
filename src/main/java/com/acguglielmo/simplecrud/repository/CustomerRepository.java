package com.acguglielmo.simplecrud.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.acguglielmo.simplecrud.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>{

	Optional<Customer> findByCnpjAndActiveTrue(String cnpj);

	Page<Customer> findAllByActiveTrue(Pageable pageable);

}
