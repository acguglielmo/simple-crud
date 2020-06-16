package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.response.ContractResponse;
import com.acguglielmo.simplecrud.response.CustomerResponse;
import com.acguglielmo.simplecrud.response.ServiceResponse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ContractResponseTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of(ContractResponse.class).addTemplate("valid", new Rule() {{
			add("number", "number-9090");
			add("customer", one(CustomerResponse.class, "valid") );
			add("service", one(ServiceResponse.class, "valid") );
		}});

	}

}
