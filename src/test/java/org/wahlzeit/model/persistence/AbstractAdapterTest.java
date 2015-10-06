package org.wahlzeit.model.persistence;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

import static org.junit.Assert.fail;

/**
 * Abstract super class for all Adapter classes that implement the {@link ImageStorage}.
 *
 * Created by Lukas Hahmann on 24.08.15.
 */
public abstract class AbstractAdapterTest {

	protected ImageStorage imageStorage;
	protected Image smallTestImage;
	protected Image maxSizeTestImage;

	@Before
	public void SetUp() {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		smallTestImage = ImagesServiceFactory.makeImage(bb.array());

		bb = ByteBuffer.allocate(1024 * 1023);
		maxSizeTestImage = ImagesServiceFactory.makeImage(bb.array());

		storageDependentSetUp();
	}

	@After
	public void tearDown() {
		storageDependentTearDown();
	}

	/**
	 * @methodproperty hook
	 */
	protected void storageDependentSetUp() {
	}

	/**
	 * @methodproperty hook
	 */
	protected void storageDependentTearDown() {
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
		boolean exists;

		exists = imageStorage.doesImageExist("doesNotExist", 1);
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
