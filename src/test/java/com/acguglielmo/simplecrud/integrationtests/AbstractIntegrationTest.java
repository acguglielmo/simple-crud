package com.acguglielmo.simplecrud.integrationtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import com.acguglielmo.simplecrud.SimpleCrudApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleCrudApplication.class)
public abstract class AbstractIntegrationTest<T, Y> {

    @Autowired
    protected ObjectMapper mapper;

	@Autowired
	protected MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

    protected void shouldPerformPaginatedQueryUsingGetTest(final String path) throws Exception {

		mockMvc.perform( get(path) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(0) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( empty() )) );

		for (int i = 0; i < 19; i++ ) {

			create("random info");

		}

		mockMvc.perform( get(path) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(10) );

		mockMvc.perform( get(path).queryParam("page", "1") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(9) );

		mockMvc.perform( get(path).queryParam("page", "2") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content").exists() )
			.andExpect( jsonPath("$.content", is( empty()  ) ) );

		mockMvc.perform( get(path).queryParam("page", "0").queryParam("size", "5") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$").exists() )
			.andExpect( jsonPath("$.totalElements").value(19) )
			.andExpect( jsonPath("$.content", is( not( empty() ) ) ) )
			.andExpect( jsonPath("$.content.size()").value(5) );

	}

    protected Y create(final String fixtureName) throws Exception {

		final T request = Fixture.from( getRequestClass() ).gimme(fixtureName);

        final String contentAsString = mockMvc.perform( post( getBaseUri() )
	    		.contentType( MediaType.APPLICATION_JSON )
	    		.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isCreated())
	        .andReturn()
	        .getResponse()
	        .getContentAsString();

        return mapper.readValue(contentAsString, getResponseClass() );

    }

	protected void shouldFind(final Object resourceId, final Object... resourceIds) throws Exception {

        mockMvc.perform( get(getResourceUri(), resourceId, resourceIds ) )
	        .andExpect( status().isOk() )
	        .andExpect( jsonPath("$").exists() )
	        .andExpect( resourceIdMatcher().value( resourceId ) );

	}

	protected void shouldNotFind(final Object resourceId, final Object... resourceIds) throws Exception {

		mockMvc.perform( get(getResourceUri(), resourceId, resourceIds ) )
	        .andExpect( status().isNotFound() )
	        .andExpect( jsonPath("$").doesNotExist() );

	}

	protected void update(final Object resourceId, final Object... resourceIds) throws Exception {

		final T request = Fixture.from( getRequestClass() ).gimme("updating");

        final ResultActions resultActions = mockMvc.perform( put(getResourceUri(), resourceId, resourceIds )
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
	    	).andExpect(status().isOk())
	    	.andExpect( jsonPath("$").exists() )
	    	.andExpect( resourceIdMatcher().value( resourceId ) );

        applyCustomActionsAfterUpdate(resultActions, request );

	}


	protected void delete(final Object resourceId, final Object... resourceIds) throws Exception {

        mockMvc.perform( MockMvcRequestBuilders.delete(getResourceUri(), resourceId, resourceIds ))
	    	.andExpect( status().isNoContent() )
	    	.andExpect( jsonPath("$").doesNotExist() );

	}

    protected abstract String getBaseUri();

    protected abstract String getResourceUri();

    protected abstract Class<T> getRequestClass();

    protected abstract Class<Y> getResponseClass();

    protected abstract JsonPathResultMatchers resourceIdMatcher();

    protected abstract void applyCustomActionsAfterUpdate(ResultActions resultActions, T putRequest) throws Exception;

}
