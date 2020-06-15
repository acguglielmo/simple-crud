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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestBodySnippet;
import org.springframework.restdocs.payload.ResponseBodySnippet;

import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;
import com.acguglielmo.simplecrud.response.CustomerResponse;

import br.com.six2six.fixturefactory.Fixture;

@AutoConfigureRestDocs(outputDir = "target/snippets/customers")
public class CustomerControllerTest extends AbstractControllerTest {

    private static final String CUSTOMERS_BASE_URI = "/customers";

    private static final String CUSTOMERS_RESOURCE_URI = CUSTOMERS_BASE_URI + "/{cnpj}";

    private static final String CUSTOMERS_CONTRACTS_RESOURCE_URI = CUSTOMERS_RESOURCE_URI + "/contracts/{number}";

    @Test
    public void shouldReturnHttp201CreatedWhenCustomerIsCreatedSucessfullyTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

    	final CustomerResponse response = Fixture.from( CustomerResponse.class ).gimme( "valid" );

    	when( customerService.create( any() ) ).thenReturn( response );

        mockMvc.perform( post( CUSTOMERS_BASE_URI )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isCreated())
        	.andExpect( jsonPath("$").exists() )
        	.andExpect( jsonPath("$.cnpj").value( response.getCnpj() ) )
        	.andExpect( jsonPath("$.name").value( response.getName() ) )
        	.andDo( document("POST-201", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsFoundByIdTest() throws Exception {

    	when( customerService.findBy( any() ) )
    		.thenReturn( Optional.of( Fixture.from( CustomerResponse.class ).gimme( "valid" ) ) );

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, "01567964000189") )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$").exists() )
            .andExpect( jsonPath("$.cnpj").value("01567964000189") )
            .andDo( document("GET-by-id-200") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenCustomerIsNotFoundByIdTest() throws Exception {

    	when( customerService.findBy( any() ) )
			.thenReturn( Optional.empty() );

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, "00000000000000") )
            .andExpect(status().isNotFound() )
            .andExpect( jsonPath("$").doesNotExist() )
            .andDo( document("GET-by-id-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenCustomerIsUpdatedSucessfullyTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

    	final CustomerResponse response = Fixture.from(CustomerResponse.class).gimme("valid");

    	when( customerService.update( eq("22544554"), any()) )
    		.thenReturn( Optional.of( response ) );

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, "22544554")
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk())
        	.andExpect( jsonPath("$").exists() )
        	.andExpect( jsonPath("$.cnpj").value( response.getCnpj() ) )
        	.andExpect( jsonPath("$.name").value( response.getName() ) )
        	.andDo( document("PUT-200", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenCustomerIsNotFoundForUpdateTest() throws Exception {

    	final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

    	when( customerService.update( eq("22544554"), any()) )
    		.thenReturn( Optional.empty() );

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, "22544554")
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound())
        	.andExpect( jsonPath("$").doesNotExist() )
        	.andDo( document("PUT-404") );

    }

    @Test
    public void shouldReturnHttp204OkWhenCustomerIsDeletedSucessfullyTest() throws Exception {

    	when( customerService.delete("123456789") ).thenReturn(true);

        mockMvc.perform( delete(CUSTOMERS_RESOURCE_URI, "123456789"))
        	.andExpect( status().isNoContent() )
        	.andExpect( jsonPath("$").doesNotExist() )
        	.andDo( document("DELETE-204") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenCustomerIsNotFoundForDeletionTest() throws Exception {

    	when( customerService.delete("123456789") ).thenReturn(false);

        mockMvc.perform( delete(CUSTOMERS_RESOURCE_URI, "123456789"))
        	.andExpect(status().isNotFound())
        	.andExpect( jsonPath("$").doesNotExist() )
        	.andDo( document("DELETE-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenNoCustomersAreFoundTest() throws Exception {

    	when( customerService.findAll( any()) )
    		.thenReturn( Page.empty() );

        mockMvc.perform( get(CUSTOMERS_BASE_URI) )
            .andExpect(status().isOk() )
            .andExpect( jsonPath("$").exists() )
            .andExpect( jsonPath("$.content").exists() )
            .andExpect( jsonPath("$.content.size()").value(0) )
            .andDo( document("paginated-GET-200-returning-no-itens") );

    }

    @Test
    public void shouldReturnHttp200OkWhenOneOrMoreCustomersAreFoundTest() throws Exception {

    	final Page<CustomerResponse> page =
    			new PageImpl<>( Fixture.from( CustomerResponse.class ).gimme(1, "valid") );

    	when( customerService.findAll( any()) )
			.thenReturn( page );

        mockMvc.perform( get(CUSTOMERS_BASE_URI).queryParam("page", "0") )
            .andExpect(status().isOk() )
            .andExpect( jsonPath("$").exists() )
            .andExpect( jsonPath("$.content").exists() )
            .andExpect( jsonPath("$.content.size()").value(1) )
            .andDo( document("paginated-GET-200-returning-itens", new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsFoundByCnpjAndNumberTest() throws Exception {

    	when( contractService.findBy("number-01", "39100116000138") )
    		.thenReturn( Optional.of( Fixture.from( ContractResponse.class ).gimme( "valid") ) );

        mockMvc.perform( get(CUSTOMERS_CONTRACTS_RESOURCE_URI, "39100116000138", "number-01") )
            .andExpect(status().isOk() )
            .andExpect(jsonPath("$").exists() )
            .andDo(document("GET-contract-by-cnpj-and-number-200", new ResponseBodySnippet() ));

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundByCnpjAndNumberTest() throws Exception {

    	when( contractService.findBy("number-01", "39100116000138") )
			.thenReturn( Optional.empty() );

    	mockMvc.perform( get(CUSTOMERS_CONTRACTS_RESOURCE_URI, "39100116000138", "number-01") )
            .andExpect(status().isNotFound() )
            .andExpect(jsonPath("$").doesNotExist() )
            .andDo( document("GET-contract-by-cnpj-and-number-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenContractIsUpdatedSucessfullyTest() throws Exception {

    	when( contractService.update( eq("number-01"), eq("39100116000138"), any() ) )
    		.thenReturn( Optional.of( Fixture.from( ContractResponse.class ).gimme( "valid") ) );

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

    	mockMvc.perform( put(CUSTOMERS_CONTRACTS_RESOURCE_URI, "39100116000138", "number-01")
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk())
        	.andDo( document("PUT-contract-200", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundForUpdateTest() throws Exception {

    	when( contractService.update( eq("number-01"), eq("39100116000138"), any() ) )
			.thenReturn( Optional.empty() );

    	final ContractRequest request = Fixture.from(ContractRequest.class).gimme("valid");

		mockMvc.perform( put(CUSTOMERS_CONTRACTS_RESOURCE_URI, "39100116000138", "number-01")
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound())
        	.andDo( document("PUT-contract-404") );

    }

    @Test
    public void shouldReturnHttp204NoContentWhenContractIsDeletedSucessfullyTest() throws Exception {

    	when( contractService.delete( "number-01", "39100116000138") )
			.thenReturn( true );

		mockMvc.perform( delete(CUSTOMERS_CONTRACTS_RESOURCE_URI, "39100116000138", "number-01"))
        	.andExpect(status().isNoContent() )
        	.andExpect(jsonPath("$").doesNotExist() )
        	.andDo( document("DELETE-contract-200") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenContractIsNotFoundForDeletionTest() throws Exception {

    	when( contractService.delete( "number-01", "39100116000138") )
			.thenReturn( false );

		mockMvc.perform( delete(CUSTOMERS_CONTRACTS_RESOURCE_URI, "39100116000138", "number-01"))
        	.andExpect(status().isNotFound() )
        	.andExpect(jsonPath("$").doesNotExist() )
        	.andDo( document("DELETE-contract-404") );

    }

}
