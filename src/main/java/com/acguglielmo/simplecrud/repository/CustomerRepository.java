package com.acguglielmo.simplecrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acguglielmo.simplecrud.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>{

}
