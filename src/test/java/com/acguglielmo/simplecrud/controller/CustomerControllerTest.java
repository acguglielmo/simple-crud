package com.acguglielmo.simplecrud.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@WebMvcTest
public class CustomerControllerTest {

    private static final String CUSTOMERS_BASE_URI = "/customers";

    private static final String CUSTOMERS_RESOURCE_URI = CUSTOMERS_BASE_URI + "/{id}";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

    @Test
    public void shouldReturnHttp201CreatedWhenCustomerIsCreatedSucessfullyTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

        mockMvc.perform( post( CUSTOMERS_BASE_URI )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isCreated());

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsFoundByIdTest() throws Exception {

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, 1) )
            .andExpect(status().isOk() );

    }

    @Test
    public void shouldReturnHttp404OkWhenCustomerIsNotFoundByIdTest() throws Exception {

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, 2) )
            .andExpect(status().isNotFound() );

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsUpdatedSucessfullyTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, 1)
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk());

    }

    @Test
    public void shouldReturnHttp404OkWhenCustomerIsNotFoundForUpdateTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, 2)
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound());

    }

}
