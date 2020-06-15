package com.acguglielmo.simplecrud.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestBodySnippet;
import org.springframework.restdocs.payload.ResponseBodySnippet;

import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;

import br.com.six2six.fixturefactory.Fixture;

@AutoConfigureRestDocs(outputDir = "target/snippets/contracts")
public class ContractControllerTest extends AbstractControllerTest {

    private static final String CONTRACTS_BASE_URI = "/contracts";

    private static final String CONTRACTS_RESOURCE_URI = CONTRACTS_BASE_URI + "/{id}";

    @Test
    public void shouldReturnHttp201CreatedWhenContractIsCreatedSucessfullyTest() throws Exception {

    	when( contractService.create(any()) )
    		.thenReturn( Fixture.from( ContractResponse.class ).gimme( "valid" ) );

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

        mockMvc.perform( post( CONTRACTS_BASE_URI )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	)
        	.andExpect( status().isCreated() )
        	.andDo( document("POST-201", new RequestBodySnippet() ) );

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
