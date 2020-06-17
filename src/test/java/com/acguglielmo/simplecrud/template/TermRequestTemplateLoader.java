package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.request.TermRequest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class TermRequestTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of( TermRequest.class ).addTemplate("valid", new Rule() {{
			add("beggining", 1582945200000L);
			add("end", 1590894000000L);
		}});

		Fixture.of( TermRequest.class ).addTemplate("updating", new Rule() {{
			add("beggining", 1590894000001L);
			add("end", 1598842800000L);
		}});

	}

}
