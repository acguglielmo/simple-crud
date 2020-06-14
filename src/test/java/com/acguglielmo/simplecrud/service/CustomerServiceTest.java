package com.acguglielmo.simplecrud.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.mapper.CustomerMapper;
import com.acguglielmo.simplecrud.mapper.MapperConfig;
import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.request.CustomerRequest;
import com.acguglielmo.simplecrud.response.CustomerResponse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@ExtendWith({
	MockitoExtension.class,
	SpringExtension.class
})
@ContextConfiguration(classes = MapperConfig.class)
public class CustomerServiceTest {

	@Mock
	private CustomerRepository repository;

	@SpyBean
	private CustomerMapper mapper;

	@InjectMocks
	private CustomerService customerService;

	@BeforeEach
	public void beforeEach() {

		FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

	}

	@Test
	public void shouldReturnPageWithCustomersTest() {

		when( repository.findAllByActiveTrue( any() ) )
			.thenReturn( new PageImpl<>( Fixture.from( Customer.class ).gimme(1, "valid") ) );

		final Pageable pageable = PageRequest.of(0, 10);

		final Page<CustomerResponse> result = customerService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(false) );

		assertThat(result.getSize(), is(1) );

	}

	@Test
	public void shouldReturnEmptyPageTest() {

		when( repository.findAllByActiveTrue( any() ) )
			.thenReturn( Page.empty() );

		final Pageable pageable = PageRequest.of(1, 10);

		final Page<CustomerResponse> result = customerService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(true) );

	}

	@Test
	public void shouldReturnOptionalWithCustomerWhenFoundByCnpjTest() {

		when( repository.findByCnpjAndActiveTrue( anyString() ) )
			.thenReturn( Optional.of( Fixture.from( Customer.class ).gimme( "valid") ) );

		final Optional<CustomerResponse> result = customerService.findBy( "01567964000189" );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(true) );

	}

	@Test
	public void shouldReturnOptionalEmptyWhenNotFoundByCnpjTest() {

		final Optional<CustomerResponse> result = customerService.findBy( "000000000000" );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(false) );

	}

	@Test
	public void shouldCreateNewCustomerSucessfullyTest() throws Exception {

		final CustomerRequest customer =
			Fixture.from( CustomerRequest.class ).gimme("valid");

		when( repository.save(any()) )
			.thenAnswer( e -> e.getArgument(0) );

		final CustomerResponse result = customerService.create(customer);

		assertThat(result, notNullValue() );

		assertThat(result.getCnpj(), equalTo( customer.getCnpj() ) );

		assertThat(result.getName(), equalTo( customer.getName() ) );

	}

	@Test
	public void shouldReturnOptionalWithUpdatedCustomerInfoIfCustomerExistsTest() throws Exception {

		final Customer oldCustomer = Fixture.from( Customer.class ).gimme( "valid");

		when( repository.findByCnpjAndActiveTrue( anyString() ) )
			.thenReturn( Optional.of( oldCustomer ) );

		when( repository.save(any()) )
			.thenAnswer( e -> e.getArgument(0) );

		final CustomerRequest request =
			Fixture.from( CustomerRequest.class ).gimme( "valid");

		final Optional<CustomerResponse> result =
			customerService.update( "01567964000189", request );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(true) );

		assertThat(result.get().getCnpj(), is( oldCustomer.getCnpj() ) );

		assertThat(result.get().getName(), is( request.getName() ) );

	}

	@Test
	public void shouldReturnOptionalEmptyWhenUpdatingIfCustomerDoesNotExistTest() throws Exception {

		final CustomerRequest request =
			Fixture.from( CustomerRequest.class ).gimme( "valid");

		final Optional<CustomerResponse> result =
			customerService.update( "000000000", request );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(false) );

	}

	@Test
	public void shouldReturnTrueWhenDeletingIfCustomerExistsTest() throws Exception {

		when( repository.findByCnpjAndActiveTrue( anyString() ) )
			.thenReturn( Optional.of( Fixture.from( Customer.class ).gimme( "valid") ) );

		boolean result = customerService.delete("01567964000189");

		assertThat(result, is(true) );

	}

	@Test
	public void shouldReturnFalseWhenDeletingIfCustomerDoesNotExistTest() throws Exception {

		boolean result = customerService.delete("000000000");

		assertThat(result, is(false) );

	}

}
