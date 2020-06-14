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

import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;
import com.acguglielmo.simplecrud.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping
	public ResponseEntity<Page<CustomerResponse>> findAll(
		@PageableDefault final Pageable pageable) {

		return ResponseEntity.ok( customerService.findAll(pageable) );

	}

	@GetMapping("/{cnpj}")
	public ResponseEntity<CustomerResponse> findBy(@PathVariable final String cnpj) {

		return ResponseEntity.of( customerService.findBy(cnpj) );

	}

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
    	@RequestBody final CustomerRequest request) {

    	final CustomerResponse customerResponse = customerService.create(request);

        final URI location = URI.create( format("/customers/%s", customerResponse.getCnpj() ));

        return ResponseEntity.created(location).body(customerResponse);

    }

    @PutMapping("/{cnpj}")
    public ResponseEntity<CustomerResponse> update(
    	@PathVariable final String cnpj,
    	@RequestBody final CustomerRequest request) {

    	return ResponseEntity.of( customerService.update(cnpj, request) );

    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> deleteBy(@PathVariable final String cnpj) {

    	return customerService.delete(cnpj) ?
    		ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }

}
