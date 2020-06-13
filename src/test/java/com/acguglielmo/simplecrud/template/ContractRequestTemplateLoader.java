package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.request.ContractRequest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ContractRequestTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( ContractRequest.class ).addTemplate("valid", new Rule() {{
			add("serviceId", 1L);
		}});

	}

}
