package com.acguglielmo.simplecrud.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.acguglielmo.simplecrud.SimpleCrudApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleCrudApplication.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected ObjectMapper mapper;

	@Autowired
	protected MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {

    	FixtureFactoryLoader.loadTemplates("com.acguglielmo.simplecrud.template");

    }

}
