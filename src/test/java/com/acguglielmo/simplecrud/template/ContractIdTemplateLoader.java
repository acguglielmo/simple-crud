package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.entity.ContractId;
import com.acguglielmo.simplecrud.entity.Customer;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ContractIdTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of(ContractId.class).addTemplate("valid", new Rule () {
			{
				add("number", "number-0191" );
				add("customer", one(Customer.class, "valid"));
			}

		});

	}

}
