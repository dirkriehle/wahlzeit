package org.wahlzeit.model.persistance;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.SysConfig;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

/**
 * Adapter for the Google Cloud Storage. Use {@link org.wahlzeit.model.persistance.GcsAdapter.Builder} to create an
 * object.
 * <p/>
 * Created by Lukas Hahmann on 28.04.15.
 */
public class GcsAdapter extends ImageStorage {

    private static final Logger log = Logger.getLogger(GcsAdapter.class.getName());

    private String bucketName;
    private String photoFolder;
    private String defaultImageMimeTypeName;
    private int bufferLength;
    private GcsService gcsService;

    /**
     * Do not use directly, instead use {@link org.wahlzeit.model.persistance.GcsAdapter.Builder} to create an object.
     */
    private GcsAdapter(String bucketName, String photoFolderName, String defaultImageMimeTypeName, int bufferLength, GcsService gcsService) {
        this.bucketName = bucketName;
        this.photoFolder = photoFolderName;
        this.defaultImageMimeTypeName = defaultImageMimeTypeName;
        this.bufferLength = bufferLength;
        this.gcsService = gcsService;
    }


    @Override
    protected void doWriteImage(Serializable image, String photoIdAsString, int size)
            throws IOException, InvalidParameterException {

        GcsFilename gcsFilename = getGcsFileName(photoIdAsString, size);
        log.config(LogBuilder.createSystemMessage().addParameter("gcsFileName", gcsFilename).toString());

        String fileType = URLConnection.guessContentTypeFromName(gcsFilename.getObjectName());
        GcsFileOptions.Builder fileOptionsBuilder = new GcsFileOptions.Builder();
        if (fileType != null) {
            fileOptionsBuilder.mimeType(fileType);
            log.config(LogBuilder.createSystemMessage().addParameter("found file type", fileType).toString());
        } else {
            fileOptionsBuilder.mimeType(defaultImageMimeTypeName);
            log.warning(LogBuilder.createSystemMessage().
                    addMessage("did not found file type, used default type").
                    addParameter("default type", defaultImageMimeTypeName).toString());
        }

        GcsFileOptions fileOptions = fileOptionsBuilder.build();
        GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, fileOptions);
        if (image instanceof Image) {
            Image imageObject = (Image) image;
            outputChannel.write(ByteBuffer.wrap(imageObject.getImageData()));
            outputChannel.close();
            log.config(LogBuilder.createSystemMessage().addMessage("image successfully written").toString());
        } else {
            throw new InvalidParameterException("not an Image object!");
        }
    }

    @Override
    protected Image doReadImage(String filename, int size) throws IOException {
        GcsFilename gcsFilename = getGcsFileName(filename, size);
        log.config(LogBuilder.createSystemMessage().addParameter("gcsFileName", gcsFilename).toString());

        GcsInputChannel readChannel = gcsService.openReadChannel(gcsFilename, 0);
        ByteBuffer bb = ByteBuffer.allocate(bufferLength);
        readChannel.read(bb);
        Image result = ImagesServiceFactory.makeImage(bb.array());
        if (result == null) {
            log.warning(LogBuilder.createSystemMessage().addMessage("does not exist!").toString());
        } else {
            log.config(LogBuilder.createSystemMessage().addMessage("image successfully read").toString());
        }
        return result;
    }

    @Override
    protected boolean doDoesImageExist(String photoIdAsString, int size) {
        GcsFilename gcsFilename = getGcsFileName(photoIdAsString, size);
        boolean result;
        try {
            // will be null if file does not exist
            result = gcsService.getMetadata(gcsFilename) != null;
        } catch (IOException e) {
            result = false;
        }
        log.config(LogBuilder.createSystemMessage().addParameter("does image exist", result).toString());
        return result;
    }


    /**
     * Creates a <code>GcsFilename</code> for the photo in the specified size. The name structure is:
     * <p/>
     * BUCKET_NAME - ownerId/fileName/photoIdAsString
     *
     * @methodtype get
     */
    private GcsFilename getGcsFileName(String photoIdAsString, int size) {
        String filePath = photoFolder + File.separator + photoIdAsString + size;
        return new GcsFilename(bucketName, filePath);
    }


    public static class Builder {
        GcsService gcsService;
        private String bucketName;
        private String photoFolderName;
        private String defaultImageMimeTypeName;
        private int bufferLength;

        public Builder() {
            bucketName = SysConfig.DATA_PATH;
            photoFolderName = "photos";
            defaultImageMimeTypeName = "image/jpeg";
            /**
             * 1 MB Buffer, does not limit the size of the files. A valid compromise between unused allocation and
             * reallocation.
             */
            bufferLength = 1024 * 1024;
            gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public void setPhotoFolderName(String photoFolderName) {
            this.photoFolderName = photoFolderName;
        }

        public void setDefaultImageMimeTypeName(String defaultImageMimeTypeName) {
            this.defaultImageMimeTypeName = defaultImageMimeTypeName;
        }

        public void setBufferLength(int bufferLength) {
            this.bufferLength = bufferLength;
        }

        public void setGcsService(GcsService gcsService) {
            this.gcsService = gcsService;
        }

        public GcsAdapter build() {
            return new GcsAdapter(bucketName, photoFolderName, defaultImageMimeTypeName, bufferLength, gcsService);
        }
    }
}
