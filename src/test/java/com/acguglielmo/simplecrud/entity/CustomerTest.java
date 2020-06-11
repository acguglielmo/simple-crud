package com.acguglielmo.simplecrud.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    public void equalsShouldReturnTrueAccordingToTheBusinessDefinition() {

        final Customer firstCustomer = new Customer("039665870001-05");

        final Customer secondCustomer = new Customer("039665870001-05");

        assertThat(firstCustomer.equals(secondCustomer), is(true));

        assertThat(firstCustomer.hashCode(), equalTo(secondCustomer.hashCode()));

    }

    @Test
    public void equalsShouldReturnFalseAccordingToTheBusinessDefinition() {

        final Customer firstCustomer = new Customer("039665870001-05");

        final Customer secondCustomer = new Customer("039665860001-04");

        assertThat(firstCustomer.equals(secondCustomer), is(false));

    }

}
