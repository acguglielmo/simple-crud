package com.acguglielmo.simplecrud.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

import br.com.six2six.fixturefactory.Fixture;

public class CustomerIntegrationTest extends AbstractIntegrationTest {

    private static final String CUSTOMERS_BASE_URI = "/customers";

    private static final String CUSTOMERS_RESOURCE_URI = CUSTOMERS_BASE_URI + "/{id}";

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    public void cleanUp() {

    	repository.deleteAll();

    }

	@Test
	public void shouldPerformCrudActionsAccordingToAssertionsTest() throws Exception {

		final CustomerResponse customer = super.create("valid");

		super.shouldFind(customer.getCnpj());

		update(customer);

		delete(customer);

		super.shouldNotFind(customer.getCnpj());

	}

	@Test
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest( CUSTOMERS_BASE_URI );

	}

	private void update(final CustomerResponse customer) throws Exception {

		final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("updating");

        mockMvc.perform( put(CUSTOMERS_RESOURCE_URI, customer.getCnpj() )
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isOk())
	    	.andExpect( jsonPath("$").exists() )
	    	.andExpect( jsonPath("$.cnpj").value( customer.getCnpj() ) )
	    	.andExpect( jsonPath("$.name").value( request.getName() ) );

	}

	private void delete(final CustomerResponse customer) throws Exception {

        mockMvc.perform( MockMvcRequestBuilders.delete(CUSTOMERS_RESOURCE_URI, customer.getCnpj() ))
	    	.andExpect( status().isNoContent() )
	    	.andExpect( jsonPath("$").doesNotExist() );

	}

	@Override
	protected String getBaseUri() {

		return CUSTOMERS_BASE_URI;
	}

	@Override
	protected String getResourceUri() {

		return CUSTOMERS_RESOURCE_URI;

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
	protected JsonPathResultMatchers resourceIdMatcher() {

		return jsonPath("$.cnpj");
	}

}
