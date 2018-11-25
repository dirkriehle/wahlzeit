package org.wahlzeit.testEnvironmentProvider;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import org.junit.rules.ExternalResource;

/**
 * Provider for the {@link com.google.appengine.api.users.UserService}.
 * 
 * @review
 */
public class UserServiceProvider extends ExternalResource {

	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true);

	@Override
	protected void before() throws Throwable {
		super.before();
	}

	@Override
	protected void after() throws Throwable {
		super.after();
	}
}
