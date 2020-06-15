package com.acguglielmo.simplecrud.integrationtests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

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

		final ServiceResponse service = super.create("valid");

		super.shouldFind( service.getId() );

		super.update( service.getId() );

		super.delete( service.getId() );

		super.shouldNotFind(service.getId());

	}

	@Test
	@Override
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest();

	}

	@Override
	protected String getBaseUri() {

		return "/services";
	}

	@Override
	protected String getResourceUri() {

		return getBaseUri() + "/{id}";

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
	protected JsonPathResultMatchers resourceIdMatcher() {

		return jsonPath("$.id") ;

	}

	@Override
    protected void applyCustomActionsAfterUpdate(
    	final ResultActions resultActions, final ServiceRequest serviceRequest) throws Exception {

		resultActions.andExpect(
			jsonPath("$.name").value(serviceRequest.getName()));

	}

}
