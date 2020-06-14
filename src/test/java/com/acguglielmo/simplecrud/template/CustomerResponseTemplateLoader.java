package com.acguglielmo.simplecrud.template;

import java.util.HashSet;

import com.acguglielmo.simplecrud.response.CustomerResponse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class CustomerResponseTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( CustomerResponse.class ).addTemplate("valid", new Rule() {{
			add("cnpj", "01567964000189");
			add("name", "customer");
			add("contracts", new HashSet<>() );
		}});

	}

}
