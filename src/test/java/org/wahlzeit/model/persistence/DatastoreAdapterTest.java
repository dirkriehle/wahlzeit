package org.wahlzeit.model.persistence;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.RegisteredOfyEnvironmentProvider;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.fail;

/**
 * Test class for {@link DatastoreAdapter}
 * 
 * @review
 */
public class DatastoreAdapterTest extends AbstractAdapterTest {

	@ClassRule
	public static TestRule chain = RuleChain.
			outerRule(new LocalDatastoreServiceTestConfigProvider()).
			around(new RegisteredOfyEnvironmentProvider());

	private Image tooLargeTestImage;


	@Override
	protected void storageDependentSetUp() {
		imageStorage = new DatastoreAdapter();

		ByteBuffer bb = ByteBuffer.allocate(1024 * 1025);
		tooLargeTestImage = ImagesServiceFactory.makeImage(bb.array());
	}


	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testUpperSizeLimit() {
		try {
			imageStorage.writeImage(tooLargeTestImage, "blub", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}
	}
}
