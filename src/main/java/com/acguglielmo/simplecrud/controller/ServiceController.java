package com.acguglielmo.simplecrud.controller;

import static java.lang.String.format;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;
import com.acguglielmo.simplecrud.service.ServiceService;

@RestController
@RequestMapping("/services")
public class ServiceController {

	@Autowired
	private ServiceService serviceService;

	@GetMapping
	public ResponseEntity<Page<ServiceResponse>> findAll(
		@PageableDefault final Pageable pageable) {

		return ResponseEntity.ok( serviceService.findAll(pageable) );

	}

	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse> findBy(@PathVariable final Long id) {

		return ResponseEntity.of( serviceService.findBy(id) );

	}

    @PostMapping
    public ResponseEntity<ServiceResponse> create(
    	@RequestBody final ServiceRequest request) {

    	final ServiceResponse serviceResponse = serviceService.create(request);

        final URI location = URI.create( format("/services/%s", serviceResponse.getId() ));

        return ResponseEntity.created(location).body(serviceResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
    	@PathVariable final Long id,
    	@RequestBody final ServiceRequest request) {

    	return ResponseEntity.of( serviceService.update(id, request) );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable final Long id) {

    	return serviceService.delete(id) ?
        	ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
