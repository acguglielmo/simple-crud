package com.acguglielmo.simplecrud.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.entity.Customer;
import com.acguglielmo.simplecrud.entity.Service;
import com.acguglielmo.simplecrud.mapper.ContractMapper;
import com.acguglielmo.simplecrud.mapper.MapperConfig;
import com.acguglielmo.simplecrud.repository.ContractRepository;
import com.acguglielmo.simplecrud.repository.CustomerRepository;
import com.acguglielmo.simplecrud.repository.ServiceRepository;
import com.acguglielmo.simplecrud.request.ContractRequest;
import com.acguglielmo.simplecrud.response.ContractResponse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@ExtendWith({
	MockitoExtension.class,
	SpringExtension.class
})
@ContextConfiguration(classes = MapperConfig.class)
public class ContractServiceTest {

	@Mock
	private ContractRepository repository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private ServiceRepository serviceRepository;

	@SpyBean
	private ContractMapper mapper;

	@InjectMocks
	private ContractService contractService;

	@BeforeEach
	public void beforeEach() {

		FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void shouldReturnPageWithContractsTest() {

		when( repository.findAll( any(Pageable.class) ) )
			.thenReturn( new PageImpl<>( Fixture.from( Contract.class ).gimme(1, "valid") ) );

		final Pageable pageable = PageRequest.of(0, 10);

		final Page<ContractResponse> result = contractService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(false) );

		assertThat(result.getSize(), is(1) );

	}

	@Test
	public void shouldReturnEmptyPageTest() {

		when( repository.findAll( any(Pageable.class) ) )
			.thenReturn( Page.empty() );

		final Pageable pageable = PageRequest.of(1, 10);

		final Page<ContractResponse> result = contractService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(true) );

	}

	@Test
	public void shouldReturnOptionalWithContractWhenFoundByNumberAndCustomersCnpjTest() {

		when( repository.findByIdNumberAndIdCustomerCnpj( anyString(), anyString() ) )
			.thenReturn( Optional.of( Fixture.from( Contract.class ).gimme("valid") ) );

		final Optional<ContractResponse> result =
			contractService.findBy( "number-90", "01567964000189" );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(true) );

	}

	@Test
	public void shouldReturnOptionalEmptyWhenNotFoundByNumberAndCustomersCnpjTest() {

		when( repository.findByIdNumberAndIdCustomerCnpj( anyString(), anyString() ) )
			.thenReturn( Optional.empty() );

		final Optional<ContractResponse> result =
			contractService.findBy( "number-90", "01567964000189" );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(false) );

	}

	@Test
	public void shouldCreateNewContractSucessfullyTest() throws Exception {

		final ContractRequest request =
				Fixture.from( ContractRequest.class ).gimme("valid");

		final Customer customer = Fixture.from(Customer.class).gimme("valid");

		when( customerRepository.findById( "50263675000100" ) )
			.thenReturn( Optional.of(customer) );

		final Service service = Fixture.from(Service.class).gimme("valid");

		when( serviceRepository.findById( anyLong() ) )
			.thenReturn( Optional.of(service) );

		when( repository.save(any()) )
			.thenAnswer( e -> e.getArgument(0) );

		final ContractResponse result = contractService.create("50263675000100" ,request);

		assertThat(result, notNullValue() );

		assertThat(result.getCustomer().getCnpj(), equalTo( customer.getCnpj() ) );

		assertThat(result.getService().getId(), equalTo( service.getId() ) );

	}

	@Test
	public void shouldReturnOptionalWithUpdatedContractInfoIfContractExistsTest() throws Exception {

		final Contract oldContract = Fixture.from( Contract.class ).gimme( "valid");

		when( repository.findByIdNumberAndIdCustomerCnpj( anyString(), anyString() ) )
			.thenReturn( Optional.of( oldContract ) );

		final Service service = Fixture.from( Service.class).gimme("valid");

		when( serviceRepository.findById( anyLong() ))
			.thenReturn( Optional.of( service ) );

		when( repository.save(any()) )
			.thenAnswer( e -> e.getArgument(0) );

		final ContractRequest request =
			Fixture.from( ContractRequest.class ).gimme( "valid");

		final Optional<ContractResponse> result =
			contractService.update("number-909", "01567964000189", request );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(true) );

		assertThat(result.get().getCustomer().getCnpj(),
			equalTo( oldContract.getId().getCustomer().getCnpj() ) );

		assertThat(result.get().getService().getId(), is( service.getId() ) );

	}

	@Test
	public void shouldReturnOptionalEmptyWhenUpdatingIfContractDoesNotExistTest() throws Exception {

		final ContractRequest request =
			Fixture.from( ContractRequest.class ).gimme( "valid");

		final Optional<ContractResponse> result =
			contractService.update("number-000", "000000000", request );

		assertThat(result, notNullValue() );

		assertThat(result.isPresent(), is(false) );

	}

	@Test
	public void shouldReturnTrueWhenDeletingIfContractExistsTest() throws Exception {

		when( repository.findByIdNumberAndIdCustomerCnpj( anyString(), anyString() ) )
			.thenReturn( Optional.of( Fixture.from( Contract.class ).gimme( "valid") ) );

		boolean result = contractService.delete("number", "01567964000189");

		assertThat(result, is(true) );

	}

	@Test
	public void shouldReturnFalseWhenDeletingIfCustomerDoesNotExistTest() throws Exception {

		when( repository.findByIdNumberAndIdCustomerCnpj( anyString(), anyString() ) )
			.thenReturn( Optional.empty() );

		boolean result = contractService.delete("number", "01567964000189");

		assertThat(result, is(false) );

	}

}
