package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.request.CustomerRequest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class CustomerRequestTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( CustomerRequest.class ).addTemplate("valid", new Rule() {{
			add("cnpj", "454548485");
			add("name", "customer");
		}});

	}

}
