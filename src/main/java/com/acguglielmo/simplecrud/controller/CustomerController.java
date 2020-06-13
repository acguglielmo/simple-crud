package com.acguglielmo.simplecrud.controller;

import static java.lang.String.format;

import java.net.URI;

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

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@GetMapping
	public ResponseEntity<Page<CustomerResponse>> findAll(
		@PageableDefault final Pageable pageable) {

		return ResponseEntity.ok(Page.empty());

	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponse> findBy(@PathVariable final Long id) {

		if ( Long.valueOf(1L).equals(id) ) {

			return ResponseEntity.ok().build();

		}

		return ResponseEntity.notFound().build();

	}

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
    	@RequestBody final CustomerRequest request) {

        final URI location = URI.create(format("/customers/%d", 1));

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
    	@PathVariable final Long id,
    	@RequestBody final CustomerRequest request) {

		if ( Long.valueOf(1L).equals(id) ) {

			return ResponseEntity.ok().build();

		}

		return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable final Long id) {

		if ( Long.valueOf(1L).equals(id) ) {

			return ResponseEntity.ok().build();

		}

		return ResponseEntity.notFound().build();

    }

}
