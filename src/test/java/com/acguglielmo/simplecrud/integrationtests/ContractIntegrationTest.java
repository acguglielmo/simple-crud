package com.acguglielmo.simplecrud.integrationtests;

import static java.util.Objects.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
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

		final ContractResponse contract = createContract();

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
	protected UriComponentsBuilder getResourceUri() {

		return getBaseUri().path("/{number}");

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

	private ContractResponse createContract() throws Exception {

		final String contractNumber = RandomStringUtils.randomAlphanumeric(10);

		final ContractRequest request = new ContractRequest();
		request.setServiceId(serviceId);
		request.setNumber( contractNumber );

        final String contentAsString =
        	mockMvc.perform( post( getBaseUri().build(Collections.singletonMap("cnpj", customerCnpj)) )
	    		.contentType( MediaType.APPLICATION_JSON )
	    		.content( mapper.writeValueAsString(request) ))
        			.andExpect( status().isCreated() )
        			.andExpect( jsonPath("$").exists() )
        			.andExpect( jsonPath("$.number").value(contractNumber) )
        			.andExpect( jsonPath("$.service.id").value(serviceId) )
        			.andExpect( jsonPath("$.customer.cnpj").value(customerCnpj) )
        			.andReturn()
        			.getResponse()
        			.getContentAsString();

        return mapper.readValue(contentAsString, getResponseClass() );

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
