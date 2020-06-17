package com.acguglielmo.simplecrud.template;

import java.util.stream.IntStream;

import org.apache.commons.lang.RandomStringUtils;

import com.acguglielmo.simplecrud.integrationtests.AbstractIntegrationTest;
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

		Fixture.of( ServiceRequest.class ).addTemplate("updating", new Rule() {{
			add("name", "updated service name");
		}});

		Fixture.of( ServiceRequest.class ).addTemplate("random info", new Rule() {{
			add("name", uniqueRandom( generateServiceNames() ) );
		}});

	}

	private Object[] generateServiceNames() {

		return IntStream.rangeClosed(0, AbstractIntegrationTest.NUMBER_OF_ELEMENTS )
			.mapToObj( e -> RandomStringUtils.randomAlphabetic( 8 ) )
			.toArray();

	}

}
