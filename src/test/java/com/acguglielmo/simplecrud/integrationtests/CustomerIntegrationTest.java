package com.acguglielmo.simplecrud.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;

public class CustomerIntegrationTest extends AbstractIntegrationTest<CustomerRequest, CustomerResponse> {

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    public void cleanUp() {

    	repository.deleteAll();

    }

	@Test
	public void shouldPerformCrudActionsAccordingToAssertionsTest() throws Exception {

		final CustomerResponse customer = super.shouldCreate("valid");

		final Map<String, Object> uriVariables = Collections.singletonMap("cnpj", customer.getCnpj());

		super.shouldFind( uriVariables );

		super.shouldUpdate( uriVariables );

		super.shouldDelete( uriVariables );

		super.shouldNotFind( uriVariables );

	}

	@Test
	@Override
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest();

	}

	@Test
    public void shouldReturnHttp409ConflictWhenCreatingCustomerWithSameCnpjTest() throws Exception {

	    final CustomerRequest request = Fixture.from( CustomerRequest.class ).gimme( "valid" );

        mockMvc.perform( post( getBaseUri().build().toUri() )
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( request ) ) )
            .andExpect(status().isCreated() )
            .andExpect( jsonPath("$").exists() );

        mockMvc.perform( post( getBaseUri().build().toUri() )
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( request ) ) )
            .andExpect(status().isConflict() )
            .andExpect( jsonPath("$").exists() )
            .andExpect( jsonPath("$").value("Customer already exists!") );

	}

	@Override
	protected UriComponentsBuilder getBaseUri() {

		return fromPath("/customers");
	}

	@Override
	protected UriComponentsBuilder getResourceUri() {

		return getBaseUri().path("/{cnpj}");

	}

	@Override
	protected Class<CustomerResponse> getResponseClass() {

		return CustomerResponse.class;

	}

	@Override
	protected Class<CustomerRequest> getRequestClass() {

		return CustomerRequest.class;
	}

	@Override
    protected void applyCustomActionsAfterUpdate(
    	final ResultActions resultActions, final CustomerRequest customerRequest) throws Exception {

		resultActions.andExpect(
			jsonPath("$.name").value(customerRequest.getName()));

	}

}
