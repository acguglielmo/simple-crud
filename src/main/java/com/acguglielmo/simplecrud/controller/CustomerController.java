package com.acguglielmo.simplecrud.controller;

import static java.lang.String.format;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

@RestController("/customers")
public class CustomerController {

    @PostMapping
    public ResponseEntity<CustomerResponse> create(final CustomerRequest request) {

        final URI location = URI.create(format("/customers/%d", 1));

        return ResponseEntity.created(location).build();

    }

}
