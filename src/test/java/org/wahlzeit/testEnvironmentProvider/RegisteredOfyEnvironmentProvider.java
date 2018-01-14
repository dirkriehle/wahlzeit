package org.wahlzeit.testEnvironmentProvider;

import org.junit.rules.ExternalResource;
import org.wahlzeit.services.CloudDB;

/**
 * A test setup class.
 * 
 * @review
 */
public class RegisteredOfyEnvironmentProvider extends ExternalResource {
	@Override
	protected void before() {
		// make sure the static register part is done
		CloudDB.init();
	}
}
