package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.request.ServiceRequest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ServiceRequestTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( ServiceRequest.class ).addTemplate("valid", new Rule() {{
			add("name", "service");
		}});

	}

}
