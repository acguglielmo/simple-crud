package com.acguglielmo.simplecrud.integrationtests;

import static java.util.Objects.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import com.acguglielmo.simplecrud.repository.ContractRepository;
import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;
import com.acguglielmo.simplecrud.response.CustomerResponse;
import com.acguglielmo.simplecrud.response.ServiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;

public class ContractIntegrationTest extends AbstractIntegrationTest<ContractRequest, ContractResponse> {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ContractRepository contractRepository;

    private String customerCnpj;

    private Long serviceId;

	@BeforeEach
    public void setupBeforeTests() throws Exception {

    	super.beforeEach();

    	createCustomerCnpjIfNull();

    	createServiceIdIfNull();

    }

	@AfterEach
    public void cleanUp() {

    	contractRepository.deleteAll();

    	serviceRepository.deleteAll();

    	customerRepository.deleteAll();

    }

	@Test
	public void shouldPerformCrudActionsAccordingToAssertionsTest() throws Exception {

		final ContractResponse contract = create("valid");

		final Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("cnpj", customerCnpj);
		uriVariables.put("number", contract.getNumber());

		super.shouldFind( uriVariables );

		super.update( uriVariables );

		super.delete( uriVariables );

		super.shouldNotFind( uriVariables );

	}



	@Test
	@Override
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest();

	}

	@Override
	protected UriComponentsBuilder getBaseUri() {

		return fromPath("/customers/{cnpj}/contracts");
	}

	@Override
	protected URI getPostUri() {

		return getBaseUri().build( Collections.singletonMap("cnpj", customerCnpj) );
	}

	@Override
	protected UriComponentsBuilder getResourceUri() {

		return getBaseUri().path("/{number}");

	}

	@Override
	protected Optional<ContractRequest> getSpecificRequestObjectBeforeCreate() {

		final String contractNumber = RandomStringUtils.randomAlphanumeric(10);

		final ContractRequest request = new ContractRequest();
		request.setServiceId(serviceId);
		request.setNumber( contractNumber );

		return Optional.of( request );

	}

	@Override
	protected Class<ContractResponse> getResponseClass() {

		return ContractResponse.class;

	}

	@Override
	protected Class<ContractRequest> getRequestClass() {

		return ContractRequest.class;
	}

	@Override
	protected JsonPathResultMatchers resourceIdMatcher() {

		return jsonPath("$.cnpj");
	}

	@Override
    protected void applyCustomActionsAfterUpdate(
    	final ResultActions resultActions, final ContractRequest contractRequest) throws Exception {

	}

	private void createCustomerCnpjIfNull() throws Exception {

		if ( isNull(customerCnpj) ) {

			final String contentAsString = mockMvc.perform( post( "/customers" )
				.contentType( MediaType.APPLICATION_JSON )
				.content( new ObjectMapper().writeValueAsString(Fixture.from( CustomerRequest.class ).gimme("random info")) ))
					.andExpect(status().isCreated())
					.andReturn()
					.getResponse()
					.getContentAsString();

			customerCnpj = mapper.readValue(contentAsString, CustomerResponse.class ).getCnpj();

		}

	}

	private void createServiceIdIfNull() throws Exception {

		if ( isNull(serviceId) ) {

			final String contentAsString = mockMvc.perform( post( "/services" )
				.contentType( MediaType.APPLICATION_JSON )
				.content( new ObjectMapper().writeValueAsString(Fixture.from( ServiceRequest.class ).gimme("random info")) ))
					.andExpect(status().isCreated())
					.andReturn()
					.getResponse()
					.getContentAsString();

			serviceId = mapper.readValue(contentAsString, ServiceResponse.class).getId();

		}

	}

}
