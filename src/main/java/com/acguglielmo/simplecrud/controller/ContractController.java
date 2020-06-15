package com.acguglielmo.simplecrud.controller;

import static java.lang.String.format;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;
import com.acguglielmo.simplecrud.service.ContractService;

@RestController
@RequestMapping("/contracts")
public class ContractController {

	@Autowired
	private ContractService contractService;

	@GetMapping
	public ResponseEntity<Page<ContractResponse>> findAll(
		@PageableDefault final Pageable pageable) {

		return ResponseEntity.ok( contractService.findAll(pageable) );

	}

    @PostMapping
    public ResponseEntity<ContractResponse> create(
    	@RequestBody final ContractRequest request) {

    	final ContractResponse contractResponse = contractService.create(request);

        final URI location = URI.create( format("/contracts/%s", contractResponse.getNumber() ));

        return ResponseEntity.created(location).body(contractResponse);

    }

}
