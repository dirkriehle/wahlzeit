package org.wahlzeit.model;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.wahlzeit.model.persistance.DatastoreAdapter;
import org.wahlzeit.model.persistance.ImageStorage;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.RegisteredOfyEnvironmentProvider;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

import static org.junit.Assert.fail;

/**
 * Test class for {@link DatastoreAdapter}
 * <p/>
 * Created by Lukas Hahmann on 20.08.15.
 */
public class DatastoreAdapterTest {

	@ClassRule
	public static TestRule chain = RuleChain.
			outerRule(new LocalDatastoreServiceTestConfigProvider()).
			around(new RegisteredOfyEnvironmentProvider());

	private ImageStorage imageStorage;
	private Image smallTestImage;
	private Image maxSizeTestImage;
	private Image tooLargeTestImage;


	@Before
	public void setUp() {
		imageStorage = new DatastoreAdapter();

		ByteBuffer bb = ByteBuffer.allocate(1024);
		smallTestImage = ImagesServiceFactory.makeImage(bb.array());

		bb = ByteBuffer.allocate(1024*1023);
		maxSizeTestImage = ImagesServiceFactory.makeImage(bb.array());

		bb = ByteBuffer.allocate(1024*1025);
		tooLargeTestImage = ImagesServiceFactory.makeImage(bb.array());
	}


	@Test
	public void testWriteImage() {
		try {
			imageStorage.writeImage(smallTestImage, "blub", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}

		try {
			imageStorage.writeImage(maxSizeTestImage, "blub", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}
	}


	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testUpperSizeLimit() {
		try {
			imageStorage.writeImage(tooLargeTestImage, "blub", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}
	}


	@Test
	public void testReadImage() {
		try {
			imageStorage.writeImage(smallTestImage, "blub", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}

		Serializable image = null;
		try {
			image = imageStorage.readImage("blub", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}

		assert image != null;
		assert image instanceof Image;

		// load image with wrong size
		try {
			image = imageStorage.readImage("blub", 2);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}

		assert image == null;

		try {
			image = imageStorage.readImage("bla", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}

		assert image == null;
	}

	@Test
	public void testImageExistence() {
		boolean exists = false;

		exists = imageStorage.doesImageExist("exists", 1);
		assert !exists;

		try {
			imageStorage.writeImage(smallTestImage, "exists", 1);
		} catch (IOException e) {
			fail("IOException should not be thrown!");
		}

		exists = imageStorage.doesImageExist("exists", 1);
		assert exists;

		// check for wrong size
		exists = imageStorage.doesImageExist("exists", 2);
		assert !exists;

		// check for wrong name
		exists = imageStorage.doesImageExist("wrong file name", 1);
		assert !exists;
	}
}
