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

import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;

import br.com.six2six.fixturefactory.Fixture;

public class ServiceIntegrationTest extends AbstractIntegrationTest {

    private static final String SERVICES_BASE_URI = "/services";

    private static final String SERVICES_RESOURCE_URI = SERVICES_BASE_URI + "/{id}";

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

		update(service);

		delete(service);

		super.shouldNotFind(service.getId());

	}

	@Test
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		super.shouldPerformPaginatedQueryUsingGetTest( SERVICES_BASE_URI );

	}

	private void update(final ServiceResponse service) throws Exception {

		final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("updating");

        mockMvc.perform( put(SERVICES_RESOURCE_URI, service.getId() )
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isOk())
	    	.andExpect( jsonPath("$").exists() )
	    	.andExpect( jsonPath("$.id").value( service.getId() ) )
	    	.andExpect( jsonPath("$.name").value( request.getName() ) );

	}

	private void delete(final ServiceResponse service) throws Exception {

        mockMvc.perform( MockMvcRequestBuilders.delete(SERVICES_RESOURCE_URI, service.getId() ))
	    	.andExpect( status().isNoContent() )
	    	.andExpect( jsonPath("$").doesNotExist() );

	}

	@Override
	protected String getBaseUri() {

		return SERVICES_BASE_URI;
	}

	@Override
	protected String getResourceUri() {

		return SERVICES_RESOURCE_URI;

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

}
