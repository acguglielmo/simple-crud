package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.request.ContractRequest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ContractRequestTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( ContractRequest.class ).addTemplate("valid", new Rule() {{
			add("number", "number-909");
			add("serviceId", 1L);
		}});

		Fixture.of( ContractRequest.class ).addTemplate("updating", new Rule() {{
			add("number", "123456789");
			add("serviceId", 1L);
		}});

		Fixture.of( ContractRequest.class ).addTemplate("random info", new Rule() {{
			add("number", uniqueRandom("customer A", "customer B", "customer C", "customer D")  );
			add("serviceId", 1L);
		}});

	}

}
