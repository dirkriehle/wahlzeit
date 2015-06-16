package org.wahlzeit.model.persistance;

import org.wahlzeit.model.PhotoSize;
import org.wahlzeit.services.LogBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Abstract super class that offers a convenient interface for all kinds of storage types to store images.
 * <p/>
 * Created by Lukas Hahmann on 11.06.15.
 */
public abstract class ImageStorage {

    private static final Logger log = Logger.getLogger(ImageStorage.class.getName());
    private static ImageStorage instance = null;

    /**
     * @methodtype get
     */
    public static ImageStorage getInstance()
            throws NullPointerException {

        if (instance == null) {
            throw new NullPointerException("Image storage instance is null, call setInstance() first!");
        }
        return instance;
    }

    /**
     * @methodtype set
     */
    public static void setInstance(ImageStorage newInstance) {
        log.config(LogBuilder.createSystemMessage().
                addAction("set ImageStorage instance").
                addParameter("instance", newInstance).toString());
        instance = newInstance;
    }


    // write-methods ---------------------------------------------------------------------------------------------------

    /**
     * Writes the image to the storage, so you can access it via photoId and size again. An existing file with that
     * parameter is overwritten.
     *
     * @methodtype command
     * @methodproperty wrapper
     */
    public void writeImage(Serializable image, String photoIdAsString, int size)
            throws InvalidParameterException, IOException {

        assertImageNotNull(image);
        assertValidPhotoId(photoIdAsString);
        PhotoSize.assertIsValidPhotoSizeAsInt(size);

        log.config(LogBuilder.createSystemMessage().
                addAction("write image to storage").
                addParameter("image", image).
                addParameter("photo id", photoIdAsString).
                addParameter("size", size).toString());

        doWriteImage(image, photoIdAsString, size);
    }

    /**
     * @methodtype assert
     */
    protected void assertImageNotNull(Serializable image)
            throws IllegalArgumentException {

        if (image == null) {
            throw new IllegalArgumentException("Image is null!");
        }
    }


    // read methods ----------------------------------------------------------------------------------------------------

    /**
     * @methodtype assert
     */
    protected void assertValidPhotoId(String photoId)
            throws IllegalArgumentException {

        if (photoId == null || "".equals(photoId)) {
            throw new IllegalArgumentException("Invalid photoId:" + photoId);
        }
    }

    /**
     * Actually writes the image to the storage
     *
     * @methodtype command
     * @methodproperty hook
     */
    protected abstract void doWriteImage(Serializable image, String photoIdAsString, int size)
            throws IOException, InvalidParameterException;


    // exist method ----------------------------------------------------------------------------------------------------

    /**
     * Reads an image from storage via photoId and the size. When the image is not found, null is returned.
     *
     * @methodtype get
     * @methodproperty convenience
     */
    public Serializable readImage(String photoIdAsString, int size)
            throws IllegalArgumentException, IOException {

        assertValidPhotoId(photoIdAsString);
        PhotoSize.assertIsValidPhotoSizeAsInt(size);

        log.config(LogBuilder.createSystemMessage().
                addAction("read image from storage").
                addParameter("photo id", photoIdAsString).
                addParameter("size", size).toString());

        return doReadImage(photoIdAsString, size);
    }

    /**
     * Actually reads the specified file from the storage. When not found, null is returned.
     *
     * @methodtype get
     * @methodproperty hook
     */
    protected abstract Serializable doReadImage(String filename, int size)
            throws IOException;


    // assertion method ------------------------------------------------------------------------------------------------

    /**
     * Checks if the specified image already exists in the storage
     *
     * @methodtype boolean query
     * @methodproperty wrapper
     */
    public boolean doesImageExist(String photoIdAsString, int size)
            throws IllegalArgumentException {

        assertValidPhotoId(photoIdAsString);
        PhotoSize.assertIsValidPhotoSizeAsInt(size);

        log.config(LogBuilder.createSystemMessage().
                addAction("check if image exists in storage").
                addParameter("photo id", photoIdAsString).
                addParameter("size", size).toString());

        return doDoesImageExist(photoIdAsString, size);
    }

    /**
     * Actually checks if the specified image already exists in the storage
     *
     * @methodtype boolean query
     * @methodproperty hook
     */
    protected abstract boolean doDoesImageExist(String photoIdAsString, int size);

}
