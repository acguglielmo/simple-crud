package com.acguglielmo.simplecrud.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

import br.com.six2six.fixturefactory.Fixture;

public class CustomerIntegrationTest extends AbstractIntegrationTest {

    private static final String CUSTOMERS_BASE_URI = "/customers";

    private static final String CUSTOMERS_RESOURCE_URI = CUSTOMERS_BASE_URI + "/{id}";

	@Test
	public void shouldCreateNewCustomerTest() throws Exception {

		final CustomerResponse customer = create();

		find(customer);

		update(customer);

	}

	private void find(final CustomerResponse customer) throws Exception {

        mockMvc.perform( get(CUSTOMERS_RESOURCE_URI, customer.getCnpj() ) )
	        .andExpect( status().isOk() )
	        .andExpect( jsonPath("$").exists() )
	        .andExpect( jsonPath("$.cnpj").value( customer.getCnpj() ) );

	}

	private CustomerResponse create() throws Exception {

		final CustomerRequest request = Fixture.from(CustomerRequest.class).gimme("valid");

        final String contentAsString = mockMvc.perform( post( CUSTOMERS_BASE_URI )
	    		.contentType( MediaType.APPLICATION_JSON )
	    		.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isCreated())
	        .andReturn()
	        .getResponse()
	        .getContentAsString();

        return mapper.readValue(contentAsString, CustomerResponse.class);

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
}
