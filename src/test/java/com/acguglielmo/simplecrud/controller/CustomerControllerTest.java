package com.acguglielmo.simplecrud.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestBodySnippet;
import org.springframework.restdocs.payload.ResponseBodySnippet;
import org.springframework.test.web.servlet.MockMvc;

import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@WebMvcTest
@AutoConfigureRestDocs(outputDir = "target/snippets/customers")
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
        	).andExpect(status().isCreated())
        	.andDo( document("POST-201", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsFoundByIdTest() throws Exception {

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, 1) )
            .andExpect(status().isOk() )
            .andDo( document("GET-by-id-200") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenCustomerIsNotFoundByIdTest() throws Exception {

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, 2) )
            .andExpect(status().isNotFound() )
            .andDo( document("GET-by-id-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsUpdatedSucessfullyTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, 1)
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk())
        	.andDo( document("PUT-200", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenCustomerIsNotFoundForUpdateTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, 2)
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound())
        	.andDo( document("PUT-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsDeletedSucessfullyTest() throws Exception {

        mockMvc.perform( delete(CUSTOMERS_RESOURCE_URI, 1))
        	.andExpect(status().isOk())
        	.andDo( document("DELETE-200") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenCustomerIsNotFoundForDeletionTest() throws Exception {

        mockMvc.perform( delete(CUSTOMERS_RESOURCE_URI, 2))
        	.andExpect(status().isNotFound())
        	.andDo( document("DELETE-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenNoCustomersAreFoundTest() throws Exception {

        mockMvc.perform( get(CUSTOMERS_BASE_URI) )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-no-itens") );

    }

    @Test
    public void shouldReturnHttp200OkWhenOneOrMoreCustomersAreFoundTest() throws Exception {

        mockMvc.perform( get(CUSTOMERS_BASE_URI).queryParam("page", "10") )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-itens", new ResponseBodySnippet() ) );

    }

}
