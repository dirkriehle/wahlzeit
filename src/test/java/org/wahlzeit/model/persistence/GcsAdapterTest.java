package org.wahlzeit.model.persistence;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * Created by Lukas Hahmann on 24.08.15.
 */
public class GcsAdapterTest extends AbstractAdapterTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig());

	@Override
	protected void storageDependentSetUp() {
		helper.setUp();
		imageStorage = new GcsAdapter.Builder().build();
	}

	@Override
	protected void storageDependentTearDown() {
		helper.tearDown();
	}
}
