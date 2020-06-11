package com.acguglielmo.simplecrud.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class ServiceTest {

    @Test
    public void equalsShouldReturnTrueAccordingToTheBusinessDefinition() {

        final Service firstService = new Service("Software development");

        final Service secondService = new Service("Software development");

        assertThat(firstService.equals(secondService), is(true));

        assertThat(firstService.hashCode(), equalTo(secondService.hashCode()));

    }

    @Test
    public void equalsShouldReturnFalseAccordingToTheBusinessDefinition() {

        final Service firstService = new Service("Software development");

        final Service secondService = new Service("Software QA");

        assertThat(firstService.equals(secondService), is(false));

    }

}
