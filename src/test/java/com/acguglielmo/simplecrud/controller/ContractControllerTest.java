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

import com.acguglielmo.simplecrud.request.ContractRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@WebMvcTest
@AutoConfigureRestDocs(outputDir = "target/snippets/contracts")
public class ContractControllerTest {

    private static final String CONTRACTS_BASE_URI = "/contracts";

    private static final String CONTRACTS_RESOURCE_URI = CONTRACTS_BASE_URI + "/{id}";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

    @Test
    public void shouldReturnHttp201CreatedWhenContractIsCreatedSucessfullyTest() throws Exception {

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

        mockMvc.perform( post( CONTRACTS_BASE_URI )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	)
        	.andExpect( status().isCreated() )
        	.andDo( document("POST-201", new RequestBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsFoundByIdTest() throws Exception {

        mockMvc.perform( get(CONTRACTS_RESOURCE_URI, 1) )
            .andExpect(status().isOk() )
            .andDo(document("GET-by-id-200", new ResponseBodySnippet() ));

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundByIdTest() throws Exception {

        mockMvc.perform( get(CONTRACTS_RESOURCE_URI, 2) )
            .andExpect(status().isNotFound() )
            .andDo( document("GET-by-id-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsUpdatedSucessfullyTest() throws Exception {

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

        mockMvc.perform( put(CONTRACTS_RESOURCE_URI, 1)
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk())
        	.andDo( document("PUT-200", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundForUpdateTest() throws Exception {

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

        mockMvc.perform( put(CONTRACTS_RESOURCE_URI, 2)
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound())
        	.andDo( document("PUT-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsDeletedSucessfullyTest() throws Exception {

        mockMvc.perform( delete(CONTRACTS_RESOURCE_URI, 1))
        	.andExpect(status().isOk())
        	.andDo( document("DELETE-200") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundForDeletionTest() throws Exception {

        mockMvc.perform( delete(CONTRACTS_RESOURCE_URI, 2))
        	.andExpect(status().isNotFound())
        	.andDo( document("DELETE-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenNoContractsAreFoundTest() throws Exception {

        mockMvc.perform( get(CONTRACTS_BASE_URI) )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-no-itens") );

    }

    @Test
    public void shouldReturnHttp200OkWhenOneOrMoreContractsAreFoundTest() throws Exception {

        mockMvc.perform( get(CONTRACTS_BASE_URI).queryParam("page", "10") )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-itens", new ResponseBodySnippet() ) );

    }

}
