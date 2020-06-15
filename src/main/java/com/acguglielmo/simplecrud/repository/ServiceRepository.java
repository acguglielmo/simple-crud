package com.acguglielmo.simplecrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acguglielmo.simplecrud.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

}
