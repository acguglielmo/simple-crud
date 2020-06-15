package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.response.ServiceResponse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ServiceResponseTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( ServiceResponse.class ).addTemplate("valid", new Rule() {{
			add("id", 54L);
			add("name", "service");
		}});

	}

}
