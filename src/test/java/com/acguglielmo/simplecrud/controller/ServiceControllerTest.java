package com.acguglielmo.simplecrud.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestBodySnippet;
import org.springframework.restdocs.payload.ResponseBodySnippet;

import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;

import br.com.six2six.fixturefactory.Fixture;

@AutoConfigureRestDocs(outputDir = "target/snippets/services")
public class ServiceControllerTest extends AbstractControllerTest {

    private static final String SERVICES_BASE_URI = "/services";

    private static final String SERVICES_RESOURCE_URI = SERVICES_BASE_URI + "/{id}";

    @Test
    public void shouldReturnHttp201CreatedWhenServiceIsCreatedSucessfullyTest() throws Exception {

    	final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("valid");

    	final ServiceResponse response = Fixture.from( ServiceResponse.class ).gimme( "valid" );

    	when( serviceService.create( any() ) ).thenReturn( response );

        mockMvc.perform( post( SERVICES_BASE_URI )
        		.contentType( MediaType.APPLICATION_JSON )
        		.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isCreated())
        	.andDo( document("POST-201", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp200OkWhenServiceIsFoundByIdTest() throws Exception {

    	when( serviceService.findBy( any() ) )
			.thenReturn( Optional.of( Fixture.from( ServiceResponse.class ).gimme( "valid" ) ) );

        mockMvc.perform( get(SERVICES_RESOURCE_URI, 1) )
            .andExpect(status().isOk() )
            .andDo( document("GET-by-id-200") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenServiceIsNotFoundByIdTest() throws Exception {

    	when( serviceService.findBy( any() ) )
			.thenReturn( Optional.empty() );

        mockMvc.perform( get(SERVICES_RESOURCE_URI, 2) )
            .andExpect(status().isNotFound() )
            .andDo( document("GET-by-id-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenServiceIsUpdatedSucessfullyTest() throws Exception {

    	final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("valid");

    	final ServiceResponse response = Fixture.from(ServiceResponse.class).gimme("valid");

    	when( serviceService.update( eq(1L), any()) )
    		.thenReturn( Optional.of( response ) );

        mockMvc.perform( put(SERVICES_RESOURCE_URI, 1)
				.contentType( MediaType.APPLICATION_JSON )
				.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isOk())
        	.andDo( document("PUT-200", new RequestBodySnippet(), new ResponseBodySnippet() ) );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenServiceIsNotFoundForUpdateTest() throws Exception {

    	final ServiceRequest request = Fixture.from(ServiceRequest.class).gimme("valid");

    	when( serviceService.update( eq(2L), any()) )
    		.thenReturn( Optional.empty() );

        mockMvc.perform( put(SERVICES_RESOURCE_URI, 2)
    			.contentType( MediaType.APPLICATION_JSON )
    			.content( mapper.writeValueAsString(request) )
        	).andExpect(status().isNotFound())
        	.andDo( document("PUT-404") );

    }

    @Test
    public void shouldReturnHttp204OkWhenServiceIsDeletedSucessfullyTest() throws Exception {

    	when( serviceService.delete(1L) ).thenReturn(true);

        mockMvc.perform( delete(SERVICES_RESOURCE_URI, 1))
        	.andExpect(status().isNoContent())
        	.andDo( document("DELETE-204") );

    }

    @Test
    public void shouldReturnHttp404NotFoundWhenServiceIsNotFoundForDeletionTest() throws Exception {

    	when( serviceService.delete(2L) ).thenReturn(false);

        mockMvc.perform( delete(SERVICES_RESOURCE_URI, 2))
        	.andExpect(status().isNotFound())
        	.andDo( document("DELETE-404") );

    }

    @Test
    public void shouldReturnHttp200OkWhenNoServicesAreFoundTest() throws Exception {

    	when( serviceService.findAll( any()) )
			.thenReturn( Page.empty() );

        mockMvc.perform( get(SERVICES_BASE_URI) )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-no-itens") );

    }

    @Test
    public void shouldReturnHttp200OkWhenOneOrMoreServicesAreFoundTest() throws Exception {

    	final Page<ServiceResponse> page =
    		new PageImpl<>( Fixture.from( ServiceResponse.class ).gimme(1, "valid") );

    	when( serviceService.findAll( any()) )
			.thenReturn( page );

        mockMvc.perform( get(SERVICES_BASE_URI).queryParam("page", "10") )
            .andExpect(status().isOk() )
            .andDo( document("paginated-GET-200-returning-itens", new ResponseBodySnippet() ) );

    }

}
