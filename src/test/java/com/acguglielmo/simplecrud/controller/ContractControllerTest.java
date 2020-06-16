package com.acguglielmo.simplecrud.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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

    private static final String CONTRACTS_BASE_URI = "/customers/{cnpj}/contracts";

    private static final String CONTRACTS_RESOURCE_URI = CONTRACTS_BASE_URI + "/{number}";

    @Test
    public void shouldReturnHttp201CreatedWhenContractIsCreatedSucessfullyTest() throws Exception {

    	when( contractService.create(any()) )
    		.thenReturn( Fixture.from( ContractResponse.class ).gimme( "valid" ) );

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

        mockMvc.perform( post( CONTRACTS_BASE_URI, "39100116000138" )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	)
        	.andExpect( status().isCreated() )
        	.andDo( document("POST-201", new RequestBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenNoContractsAreFoundTest() throws Exception {

        mockMvc.perform( get(CONTRACTS_BASE_URI, "39100116000138") )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-no-itens") );

    }

    @Test
    public void shouldReturnHttp200OkWhenOneOrMoreContractsAreFoundTest() throws Exception {

        mockMvc.perform( get(CONTRACTS_BASE_URI, "39100116000138").queryParam("page", "10") )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-itens", new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsFoundByCnpjAndNumberTest() throws Exception {

    	when( contractService.findBy("number-01", "39100116000138") )
    		.thenReturn( Optional.of( Fixture.from( ContractResponse.class ).gimme( "valid") ) );

        mockMvc.perform( get(CONTRACTS_RESOURCE_URI, "39100116000138", "number-01") )
            .andExpect(status().isOk() )
            .andExpect(jsonPath("$").exists() )
            .andDo(document("GET-by-cnpj-and-number-200", new ResponseBodySnippet() ));

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundByCnpjAndNumberTest() throws Exception {

    	when( contractService.findBy("number-01", "39100116000138") )
			.thenReturn( Optional.empty() );

    	mockMvc.perform( get(CONTRACTS_RESOURCE_URI, "39100116000138", "number-01") )
            .andExpect(status().isNotFound() )
            .andExpect(jsonPath("$").doesNotExist() )
            .andDo( document("GET-by-cnpj-and-number-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsUpdatedSucessfullyTest() throws Exception {

    	when( contractService.update( eq("number-01"), eq("39100116000138"), any() ) )
    		.thenReturn( Optional.of( Fixture.from( ContractResponse.class ).gimme( "valid") ) );

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

    	mockMvc.perform( put(CONTRACTS_RESOURCE_URI, "39100116000138", "number-01")
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk())
        	.andDo( document("PUT-200", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundForUpdateTest() throws Exception {

    	when( contractService.update( eq("number-01"), eq("39100116000138"), any() ) )
			.thenReturn( Optional.empty() );

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

		mockMvc.perform( put(CONTRACTS_RESOURCE_URI, "39100116000138", "number-01")
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound())
        	.andDo( document("PUT-404") );

    }

    @Test
    public void shouldReturnHttp204NoContentWhenContractIsDeletedSucessfullyTest() throws Exception {

    	when( contractService.delete( "number-01", "39100116000138") )
			.thenReturn( true );

		mockMvc.perform( delete(CONTRACTS_RESOURCE_URI, "39100116000138", "number-01"))
        	.andExpect(status().isNoContent() )
        	.andExpect(jsonPath("$").doesNotExist() )
        	.andDo( document("DELETE-204") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundForDeletionTest() throws Exception {

    	when( contractService.delete( "number-01", "39100116000138") )
			.thenReturn( false );

		mockMvc.perform( delete(CONTRACTS_RESOURCE_URI, "39100116000138", "number-01"))
        	.andExpect(status().isNotFound() )
        	.andExpect(jsonPath("$").doesNotExist() )
        	.andDo( document("DELETE-404") );

    }

}
