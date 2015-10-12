/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.model.persistence;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
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
 * Adapter for the Google Cloud Storage.
 * Use {@link org.wahlzeit.model.persistence.GcsAdapter.Builder} to create an object.
 * 
 * @review
 */
public class GcsAdapter extends ImageStorage {

	private static final Logger log = Logger.getLogger(GcsAdapter.class.getName());

	private String bucketName;
	private String photoFolder;
	private String defaultImageMimeTypeName;
	private int bufferLength;
	private GcsService gcsService;

	/**
	 * Do not use directly, instead use {@link org.wahlzeit.model.persistence.GcsAdapter.Builder} to create an object.
	 */
	private GcsAdapter(String bucketName, String photoFolderName, String defaultImageMimeTypeName, int bufferLength,
					   GcsService gcsService) {
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
		Image result = null;
		try {
			readChannel.read(bb);
			result = ImagesServiceFactory.makeImage(bb.array());
		} catch (IOException e) {
			// when image does not exist, IOException is thrown
		}
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
			GcsFileMetadata gcsFileMetadata = gcsService.getMetadata(gcsFilename);
			result = gcsFileMetadata != null;
		} catch (IOException e) {
			result = false;
		}
		log.config(LogBuilder.createSystemMessage().addParameter("does image exist", result).toString());
		return result;
	}


	/**
	 * Creates a <code>GcsFilename</code> for the photo in the specified size. The name structure is:
	 *
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
