package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.entity.Customer;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class CustomerTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of(Customer.class).addTemplate("valid", new Rule () {
			{
				add("cnpj", "73398692000141");
				add("name", "customer");
			}

		});

	}

}
