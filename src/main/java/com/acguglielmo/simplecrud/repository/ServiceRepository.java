package com.acguglielmo.simplecrud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acguglielmo.simplecrud.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

	Optional<Service> findByName(String name);

}
