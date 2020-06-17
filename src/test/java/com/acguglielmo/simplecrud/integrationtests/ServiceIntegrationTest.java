package com.acguglielmo.simplecrud.integrationtests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;

public class ServiceIntegrationTest extends AbstractIntegrationTest<ServiceRequest, ServiceResponse> {

    @Autowired
    private ServiceRepository repository;

    @AfterEach
    public void cleanUp() {

    	repository.deleteAll();

    }

	@Test
	public void shouldPerformCrudActionsAccordingToAssertionsTest() throws Exception {

		final ServiceResponse service = super.shouldCreate("valid");

		final Map<String, Object> uriVariables = Collections.singletonMap("id", service.getId() );

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

	@Override
	protected UriComponentsBuilder getBaseUri() {

		return fromPath("/services");
	}

	@Override
	protected UriComponentsBuilder getResourceUri() {

		return getBaseUri().path("/{id}");

	}

	@Override
	protected Class<ServiceResponse> getResponseClass() {

		return ServiceResponse.class;

	}

	@Override
	protected Class<ServiceRequest> getRequestClass() {

		return ServiceRequest.class;
	}

	@Override
    protected void applyCustomActionsAfterUpdate(
    	final ResultActions resultActions, final ServiceRequest serviceRequest) throws Exception {

		resultActions.andExpect(
			jsonPath("$.name").value(serviceRequest.getName()));

	}

}
