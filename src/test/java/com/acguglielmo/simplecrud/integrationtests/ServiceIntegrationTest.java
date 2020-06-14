package com.acguglielmo.simplecrud.integrationtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

		final ServiceResponse service = create("valid");

		shouldFind(service);

		update(service);

		delete(service);

		shouldNotFind(service);

	}

	@Test
	public void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		mockMvc.perform( get(SERVICES_BASE_URI) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(0) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( empty() )) );

		for (int i = 0; i < 19; i++ ) {

			create("random info");

		}

		mockMvc.perform( get(SERVICES_BASE_URI) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(10) );

		mockMvc.perform( get(SERVICES_BASE_URI).queryParam("page", "1") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(9) );

		mockMvc.perform( get(SERVICES_BASE_URI).queryParam("page", "2") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( empty()  ) ) );

		mockMvc.perform( get(SERVICES_BASE_URI).queryParam("page", "0").queryParam("size", "5") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(5) );

	}

	private void shouldNotFind(final ServiceResponse service) throws Exception {

        mockMvc.perform( get(SERVICES_RESOURCE_URI, service.getId() ) )
	        .andExpect( status().isNotFound() )
	        .andExpect( jsonPath("$").doesNotExist() );

	}

	private void shouldFind(final ServiceResponse service) throws Exception {

        mockMvc.perform( get(SERVICES_RESOURCE_URI, service.getId() ) )
	        .andExpect( status().isOk() )
	        .andExpect( jsonPath("$").exists() )
	        .andExpect( jsonPath("$.name").value( service.getName() ) );

	}

	private ServiceResponse create(final String fixtureName) throws Exception {

		final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme(fixtureName);

        final String contentAsString = mockMvc.perform( post( SERVICES_BASE_URI )
	    		.contentType( MediaType.APPLICATION_JSON )
	    		.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isCreated())
	        .andReturn()
	        .getResponse()
	        .getContentAsString();

        return mapper.readValue(contentAsString, ServiceResponse.class);

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

}
