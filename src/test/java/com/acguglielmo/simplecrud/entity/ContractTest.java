package com.acguglielmo.simplecrud.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class ContractTest {

    @Test
    public void equalsShouldReturnTrueAccordingToTheBusinessDefinition() {

        final Customer customer = new Customer("039665870001-05");

        final ContractId id = new ContractId("17338-2019", customer);

        final Contract firstContract = new Contract(id);

        final Contract secondContract = new Contract(id);

        assertThat(firstContract.equals(secondContract), is(true));

        assertThat(firstContract.hashCode(), equalTo(secondContract.hashCode()));

    }

    @Test
    public void equalsShouldReturnFalseAccordingToTheBusinessDefinition() {

        final Customer customer = new Customer("039665870001-05");

        final Contract firstContract = new Contract(new ContractId("17338-2019", customer));

        final Contract secondContract = new Contract(new ContractId("17338-2020", customer));

        assertThat(firstContract.equals(secondContract), is(false));

    }

}
