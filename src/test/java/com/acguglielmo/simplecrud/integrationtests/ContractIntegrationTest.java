package com.acguglielmo.simplecrud.integrationtests;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;
import com.acguglielmo.simplecrud.response.CustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;

@Disabled
public class ContractIntegrationTest extends AbstractIntegrationTest<ContractRequest, ContractResponse> {

    @Autowired
    private CustomerRepository repository;

    private String customerCnpj;

	@BeforeEach
    public void setupBeforeTests() throws Exception {

    	super.beforeEach();

    	createCustomerCnpjIfNull();

    }

	@AfterEach
    public void cleanUp() {

    	repository.deleteAll();

    }

	@Test
	public void shouldPerformCrudActionsAccordingToAssertionsTest() throws Exception {

		final ContractResponse contract = super.create("valid");

		super.shouldFind( contract.getCustomer().getCnpj(), contract.getNumber() );

		super.update( contract.getCustomer().getCnpj(), contract.getNumber() );

		super.delete( contract.getCustomer().getCnpj(), contract.getNumber() );

		super.shouldNotFind( contract.getCustomer().getCnpj(), contract.getNumber());

	}

	@Test
	@Override
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest();

	}

	@Override
	protected String getBaseUri() {

		return format("/customers/%s/contracts", customerCnpj);
	}

	@Override
	protected String getResourceUri() {

		return getBaseUri() + "/{number}";

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

}
