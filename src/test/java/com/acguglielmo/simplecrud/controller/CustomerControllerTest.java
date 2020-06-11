package com.acguglielmo.simplecrud.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest
public class CustomerControllerTest {

    private static final String CUSTOMERS_BASE_URL = "/customers";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnHttp201CreatedWhenCustomerIsCreatesSucessfullyTest() throws Exception {

        mockMvc.perform(post(CUSTOMERS_BASE_URL))
            .andExpect(status().isCreated());

    }

}
