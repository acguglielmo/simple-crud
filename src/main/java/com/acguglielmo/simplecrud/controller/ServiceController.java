package com.acguglielmo.simplecrud.controller;

import static java.lang.String.format;

import java.net.URI;
import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

@RestController
@RequestMapping("/services")
public class ServiceController {

	@GetMapping
	public ResponseEntity<Page<ServiceResponse>> findAll(
		@PageableDefault final Pageable pageable) {

		if (pageable.getPageNumber() == 10) {

			final ServiceResponse content = new ServiceResponse();

			return ResponseEntity.ok(new PageImpl<ServiceResponse>(Collections.singletonList(content)));

		}

		return ResponseEntity.ok(Page.empty());

	}

	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse> findBy(@PathVariable final Long id) {

		if ( Long.valueOf(1L).equals(id) ) {

			return ResponseEntity.ok().build();

		}

		return ResponseEntity.notFound().build();

	}

    @PostMapping
    public ResponseEntity<ServiceResponse> create(
    	@RequestBody final ServiceRequest request) {

        final URI location = URI.create(format("/services/%d", 1));

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
    	@PathVariable final Long id,
    	@RequestBody final ServiceRequest request) {

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
