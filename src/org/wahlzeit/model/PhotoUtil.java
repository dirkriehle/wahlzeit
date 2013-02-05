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

package org.wahlzeit.model;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

import org.wahlzeit.services.*;

/**
 * PhotoUtil provides a set of utility functions to create defined images.
 * Images are created from a source in different sizes as needed by the app.
 * 
 * @author dirkriehle
 *
 */
public class PhotoUtil {
	
	/**
	 * 
	 */
	public static Photo createPhoto(File source, PhotoId id) throws IOException,IllegalArgumentException {
		Photo result = PhotoFactory.getInstance().createPhoto(id);
		
		Image sourceImage = createImageFiles(source, id);

		int sourceWidth = sourceImage.getWidth(null);
		int sourceHeight = sourceImage.getHeight(null);
		result.setWidthAndHeight(sourceWidth, sourceHeight);

		return result;
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public static Image createImageFiles(File source, PhotoId id) throws IOException,IllegalArgumentException {
		Image sourceImage = null;
		try {
			sourceImage = ImageIO.read(source);
		} catch (IOException e) {
			if (source.exists()) {
				if (source.canRead()) {
					try {
						sourceImage = ImageIO.read(source);
					} catch (IOException e1) {
						throw new IOException(source + " exists is readable but has not been working twice"); 
					}
				}
				else {
					throw new IOException(source + " exists, but can't be read");
				}
			}
			else {
				throw new IOException(source + " does not exist");
			}
		}
		catch (IllegalArgumentException e) {
			if (source == null) {
				throw new IllegalArgumentException("File is null");
			}
			else {
				throw new IllegalArgumentException("PhotoId is null");
			}
		}
		assertIsValidImage(sourceImage);

		int sourceWidth = sourceImage.getWidth(null);
		int sourceHeight = sourceImage.getHeight(null);
		assertHasValidSize(sourceWidth, sourceHeight);
		
		for (PhotoSize size : PhotoSize.values()) {
			if (!size.isWiderAndHigher(sourceWidth, sourceHeight)) {
				createImageFile(sourceImage, id, size);
			}
		}
		
		return sourceImage;
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	protected static void createImageFile(Image source, PhotoId id, PhotoSize size) throws IOException,IllegalArgumentException {	
		int sourceWidth = source.getWidth(null);
		int sourceHeight = source.getHeight(null);
		
		int targetWidth = size.calcAdjustedWidth(sourceWidth, sourceHeight);
		int targetHeight = size.calcAdjustedHeight(sourceWidth, sourceHeight);

		BufferedImage targetImage = scaleImage(source, targetWidth, targetHeight);
		File target = new File(SysConfig.getPhotosDirAsString() + id.asString() + size.asInt() + ".jpg");
		try {
			ImageIO.write(targetImage, "jpg", target);
		} catch (IOException e) {
			if (target.exists()) {
				if (target.canWrite()) {
					try {
						ImageIO.write(targetImage, "jpg", target);
					} catch (IOException e1) {
						throw new IOException(source + " exists is writeable but is not working twice"); 
					}
				}
				else {
					throw new IOException(source + " exists, but can't be written");
				}
			}
			else {
				try {
					ImageIO.write(targetImage, "jpg", target);
				} catch (IOException e1) {
					throw new IOException(source + " does not exist but can't be written");
				}
			}
		}
		catch (IllegalArgumentException e) {
			if (targetImage == null) {
				throw new IllegalArgumentException("targetImage is null");
			}
			else {
				throw new IllegalArgumentException("target is null");
			}
		}

		SysLog.logInfo("created image file for id: " + id.asString() + " of size: " + size.asString());
	}

	/**
	 * 
	 */
	protected static BufferedImage scaleImage(Image source, int width, int height) {
		source = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = result.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.drawImage(source, 0, 0, null);
		return result;
	}
	
	/**
	 * @methodtype assertion 
	 */
	protected static void assertIsValidImage(Image image) {
		if (image == null) {
			throw new IllegalArgumentException("Not a valid photo!");
		}
	}

	/**
	 * 
	 */
	protected static void assertHasValidSize(int cw, int ch) {
		if (PhotoSize.THUMB.isWiderAndHigher(cw, ch)) {
			throw new IllegalArgumentException("Photo too small!");
		}
	}

}
