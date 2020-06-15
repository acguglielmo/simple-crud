package com.acguglielmo.simplecrud.template;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.entity.ContractId;
import com.acguglielmo.simplecrud.entity.Service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ContractTemplateLoader implements TemplateLoader {

	@Override
	public void load() {

		Fixture.of(Contract.class).addTemplate("valid", new Rule () {
			{
				add("id", one(ContractId.class, "valid") );
				add("term", null);
				add("service", one(Service.class, "valid"));
			}

		});

	}

}
