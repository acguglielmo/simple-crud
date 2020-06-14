package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.entity.Service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ServiceTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of(Service.class).addTemplate("valid", new Rule () {
			{
				add("id", 457L);
				add("name", "service");
			}

		});

	}

}
