package com.acguglielmo.simplecrud.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@WebMvcTest
public class ServiceControllerTest {

    private static final String SERVICES_BASE_URI = "/services";

    private static final String SERVICES_RESOURCE_URI = SERVICES_BASE_URI + "/{id}";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

    @Test
    public void shouldReturnHttp201CreatedWhenServiceIsCreatedSucessfullyTest() throws Exception {

    	final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("valid");

        mockMvc.perform( post( SERVICES_BASE_URI )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isCreated());

    }

    @Test
    public void shouldReturnHttp200OkWhenServiceIsFoundByIdTest() throws Exception {

        mockMvc.perform( get(SERVICES_RESOURCE_URI, 1) )
            .andExpect(status().isOk() );

    }

    @Test
    public void shouldReturnHttp404OkWhenServiceIsNotFoundByIdTest() throws Exception {

        mockMvc.perform( get(SERVICES_RESOURCE_URI, 2) )
            .andExpect(status().isNotFound() );

    }

    @Test
    public void shouldReturnHttp200OkWhenServiceIsUpdatedSucessfullyTest() throws Exception {

    	final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("valid");

        mockMvc.perform( put(SERVICES_RESOURCE_URI, 1)
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk());

    }

    @Test
    public void shouldReturnHttp404OkWhenServiceIsNotFoundForUpdateTest() throws Exception {

    	final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("valid");

        mockMvc.perform( put(SERVICES_RESOURCE_URI, 2)
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnHttp200OkWhenServiceIsDeletedSucessfullyTest() throws Exception {

        mockMvc.perform( delete(SERVICES_RESOURCE_URI, 1))
        	.andExpect(status().isOk());

    }

    @Test
    public void shouldReturnHttp404OkWhenServiceIsNotFoundForDeletionTest() throws Exception {

        mockMvc.perform( delete(SERVICES_RESOURCE_URI, 2))
        	.andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnHttp200OkWhenNoServicesAreFoundTest() throws Exception {

        mockMvc.perform( get(SERVICES_BASE_URI) )
            .andExpect(status().isOk() );

    }

    @Test
    public void shouldReturnHttp200OkWhenOneOrMoreServicesAreFoundTest() throws Exception {

        mockMvc.perform( get(SERVICES_BASE_URI).queryParam("page", "10") )
            .andExpect(status().isOk() );

    }

}
