package com.acguglielmo.simplecrud.integrationtests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

public class CustomerIntegrationTest extends AbstractIntegrationTest<CustomerRequest, CustomerResponse> {

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

		super.update(customer.getCnpj());

		super.delete(customer.getCnpj());

		super.shouldNotFind(customer.getCnpj());

	}

	@Test
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest( CUSTOMERS_BASE_URI );

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

	@Override
    protected void applyCustomActionsAfterUpdate(
    	final ResultActions resultActions, final CustomerRequest customerRequest) throws Exception {

		resultActions.andExpect(
			jsonPath("$.name").value(customerRequest.getName()));

	}

}
