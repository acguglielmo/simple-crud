package com.acguglielmo.simplecrud.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.acguglielmo.simplecrud.response.CustomerResponse;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

	@InjectMocks
	private CustomerService customerService;

	@Test
	public void shouldReturnPageWithCustomersTest() {

		final Pageable pageable = PageRequest.of(0, 10);

		final Page<CustomerResponse> result = customerService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(false) );

		assertThat(result.getSize(), is(1) );

	}

	@Test
	public void shouldReturnEmptyPageTest() {

		final Pageable pageable = PageRequest.of(1, 10);

		final Page<CustomerResponse> result = customerService.findAll(pageable);

		assertThat(result, notNullValue() );

		assertThat(result.isEmpty(), is(true) );

	}

}
