package com.acguglielmo.simplecrud.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.acguglielmo.simplecrud.entity.Service;
import com.acguglielmo.simplecrud.mapper.MapperConfig;
import com.acguglielmo.simplecrud.mapper.ServiceMapper;
import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ServiceRequest;
import com.acguglielmo.simplecrud.response.ServiceResponse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@ExtendWith({
	MockitoExtension.class,
	SpringExtension.class
})
@ContextConfiguration(classes = MapperConfig.class)
public class ServiceServiceTest {

	@Mock
	private ServiceRepository repository;

	@SpyBean
	private ServiceMapper mapper;

	@InjectMocks
	private ServiceService serviceService;

	@BeforeEach
	public void beforeEach() {

		FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

	}

	@Test
	public void shouldReturnPageWithServicesTest() {

		when( repository.findAll( any(Pageable.class) ) )
			.thenReturn( new PageImpl<>( Fixture.from( Service.class ).gimme(1, "valid") ) );

		final Pageable pageable = PageRequest.of(0, 10);

		final Page<ServiceResponse> result = serviceService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(false) );

		assertThat(result.getSize(), is(1) );

	}

	@Test
	public void shouldReturnEmptyPageTest() {

		when( repository.findAll( any(Pageable.class) ) )
			.thenReturn( Page.empty() );

		final Pageable pageable = PageRequest.of(1, 10);

		final Page<ServiceResponse> result = serviceService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(true) );

	}

	@Test
	public void shouldReturnOptionalWithServiceWhenFoundByCnpjTest() {

		when( repository.findById( anyLong() ) )
			.thenReturn( Optional.of( Fixture.from( Service.class ).gimme( "valid") ) );

		final Optional<ServiceResponse> result = serviceService.findBy( 32L );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(true) );

	}

	@Test
	public void shouldReturnOptionalEmptyWhenNotFoundByCnpjTest() {

		final Optional<ServiceResponse> result = serviceService.findBy( 55L );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(false) );

	}

	@Test
	public void shouldCreateNewServiceSucessfullyTest() throws Exception {

		final ServiceRequest service =
			Fixture.from( ServiceRequest.class ).gimme("valid");

		when( repository.save(any()) )
			.thenAnswer( e -> e.getArgument(0) );

		final ServiceResponse result = serviceService.create(service);

		assertThat(result, notNullValue() );

		assertThat(result.getName(), equalTo( service.getName() ) );

	}

	@Test
	public void shouldReturnOptionalWithUpdatedServiceInfoIfServiceExistsTest() throws Exception {

		final Service oldService = Fixture.from( Service.class ).gimme( "valid");

		when( repository.findById( anyLong() ) )
			.thenReturn( Optional.of( oldService ) );

		when( repository.save(any()) )
			.thenAnswer( e -> e.getArgument(0) );

		final ServiceRequest request =
			Fixture.from( ServiceRequest.class ).gimme( "valid");

		final Optional<ServiceResponse> result =
			serviceService.update( 50L, request );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(true) );

		assertThat(result.get().getId(), is( oldService.getId() ) );

		assertThat(result.get().getName(), is( request.getName() ) );

	}

	@Test
	public void shouldReturnOptionalEmptyWhenUpdatingIfServiceDoesNotExistTest() throws Exception {

		final ServiceRequest request =
			Fixture.from( ServiceRequest.class ).gimme( "valid");

		final Optional<ServiceResponse> result =
			serviceService.update( 78L, request );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(false) );

	}

	@Test
	public void shouldReturnTrueWhenDeletingIfServiceExistsTest() throws Exception {

		when( repository.findById( anyLong() ) )
			.thenReturn( Optional.of( Fixture.from( Service.class ).gimme( "valid") ) );

		boolean result = serviceService.delete(5L);

		assertThat(result, is(true) );

	}

	@Test
	public void shouldReturnFalseWhenDeletingIfServiceDoesNotExistTest() throws Exception {

		boolean result = serviceService.delete(78L);

		assertThat(result, is(false) );

	}

}
