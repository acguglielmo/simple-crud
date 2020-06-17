package com.acguglielmo.simplecrud.integrationtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.acguglielmo.simplecrud.SimpleCrudApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleCrudApplication.class)
public abstract class AbstractIntegrationTest<T, Y> {

    public static final int NUMBER_OF_ELEMENTS = 19;

	@Autowired
    protected ObjectMapper mapper;

	@Autowired
	protected MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

    protected void shouldPerformPaginatedQueryUsingGetTest() throws Exception {

		mockMvc.perform( get( getBaseUri().build().toUri() ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(0) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( empty() )) );

		for (int i = 0; i < NUMBER_OF_ELEMENTS; i++ ) {

			shouldCreate("random info");

		}

		mockMvc.perform( get( getBaseUri().build().toUri() ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(10) );

		mockMvc.perform( get( getBaseUri().build().toUri() ).queryParam("page", "1") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(9) );

		mockMvc.perform( get( getBaseUri().build().toUri() ).queryParam("page", "2") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( empty()  ) ) );

		mockMvc.perform( get( getBaseUri().build().toUri() ).queryParam("page", "0").queryParam("size", "5") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(5) );

	}

    protected Y shouldCreate(final String fixtureName) throws Exception {

    	final T request = getSpecificRequestObjectBeforeCreateOrUpdate()
    		.orElse( Fixture.from( getRequestClass() ).gimme( fixtureName ) );

        final ResultActions result = mockMvc.perform( post( getPostUri() )
	    		.contentType( MediaType.APPLICATION_JSON )
	    		.content( mapper.writeValueAsString(request) )
	    	)
        	.andExpect(status().isCreated());

        applyCustomActionsAfterCreateOrUpdate(result, request);

        final String contentAsString = result.andReturn().getResponse().getContentAsString();

		return mapper.readValue(contentAsString, getResponseClass() );

    }

	protected void shouldFind(final Map<String, Object> uriVariables) throws Exception {

		mockMvc.perform( get( getResourceUri().build(uriVariables) ) )
	        .andExpect( status().isOk() )
	        .andExpect( jsonPath("$").exists() );
	        //.andExpect( resourceIdMatcher().value( resourceId ) );

	}

	protected void shouldNotFind(final Map<String, Object> uriVariables) throws Exception {

		mockMvc.perform( get( getResourceUri().build(uriVariables) ) )
	        .andExpect( status().isNotFound() )
	        .andExpect( jsonPath("$").doesNotExist() );

	}

	protected void shouldUpdate(final Map<String, Object> uriVariables) throws Exception {

    	final T request = getSpecificRequestObjectBeforeCreateOrUpdate()
        	.orElse(Fixture.from( getRequestClass() ).gimme("updating") );

        final ResultActions resultActions = mockMvc.perform( put(getResourceUri().build(uriVariables) )
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isOk())
	    	.andExpect( jsonPath("$").exists() );

        applyCustomActionsAfterCreateOrUpdate(resultActions, request );

	}


	protected void shouldDelete(final Map<String, Object> uriVariables) throws Exception {

        mockMvc.perform( MockMvcRequestBuilders.delete(getResourceUri().build(uriVariables) ))
	    	.andExpect( status().isNoContent() )
	    	.andExpect( jsonPath("$").doesNotExist() );

	}

	protected Optional<T> getSpecificRequestObjectBeforeCreateOrUpdate() {

		return Optional.empty();

	};

	protected URI getPostUri() {

		return getBaseUri().build().toUri();

	}

	protected void applyCustomActionsAfterCreateOrUpdate(ResultActions resultActions, T postRequest) throws Exception {};

	protected abstract UriComponentsBuilder getBaseUri();

    protected abstract UriComponentsBuilder getResourceUri();

    protected abstract Class<T> getRequestClass();

    protected abstract Class<Y> getResponseClass();

}
