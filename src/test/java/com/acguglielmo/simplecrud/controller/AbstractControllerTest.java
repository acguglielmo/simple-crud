package com.acguglielmo.simplecrud.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.acguglielmo.simplecrud.SimpleCrudApplication;
import com.acguglielmo.simplecrud.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@WebMvcTest
@ActiveProfiles("default")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleCrudApplication.class)
public abstract class AbstractControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

}
