package org.wahlzeit.testEnvironmentProvider;

import org.junit.rules.ExternalResource;
import org.wahlzeit.services.OfyService;

/**
 * A test setup class.
 * 
 * @review
 */
public class RegisteredOfyEnvironmentProvider extends ExternalResource {
	@Override
	protected void before() throws Throwable {
		// make sure the static register part is done
		OfyService.factory();
	}
}
