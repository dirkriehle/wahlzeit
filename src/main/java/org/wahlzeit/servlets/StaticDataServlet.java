package org.wahlzeit.servlets;

import com.google.appengine.api.images.Image;
import org.apache.http.HttpStatus;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.PhotoSize;
import org.wahlzeit.model.persistence.ImageStorage;
import org.wahlzeit.services.LogBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Servlet that returns static data like the Photos to the user.
 *
 * As there are several links for each Photo, this can not the handled via the MainServlet, which has a unique link for
 * each Handler. Instead web.xml redirects all static requests to this Servlet.
 * 
 * @review
 */
public class StaticDataServlet extends AbstractServlet {

	Logger log = Logger.getLogger(StaticDataServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String type = request.getParameter("type");
			String photoId = request.getParameter("photoId");
			String sizeString = request.getParameter("size");
			int size = Integer.valueOf(sizeString);
			log.info(LogBuilder.createSystemMessage().
					addAction("Provide static resource").
					addParameter("type", type).
					addParameter("photoId", photoId).
					addParameter("size", size).toString());

			if ("image".equals(type)) {
				Image image = getImage(photoId, size);
				if (image != null) {
					response.getOutputStream().write(image.getImageData());
					response.getOutputStream().flush();
					response.setStatus(HttpStatus.SC_OK);
				} else {
					log.warning(LogBuilder.createSystemMessage().addMessage("image not found").toString());
					response.setStatus(HttpStatus.SC_NOT_FOUND);
				}
			} else {
				log.warning(LogBuilder.createSystemMessage().
						addMessage("unimplemented static resource type has been requested").toString());
				response.setStatus(HttpStatus.SC_NOT_IMPLEMENTED);
			}

		} catch (Exception e) {
			log.severe(LogBuilder.createSystemMessage().addException("Problem when loading image", e).toString());
		}
	}

	/**
	 * @methodtype command
	 *
	 * Loads image either from the <@link>PhotoManager</@link> or from the <@link>ImageStorage</@link>. If image does
	 * not exist, null is returned.
	 */
	private Image getImage(String photoId, int size) {
		Image image = null;
		Photo photo = PhotoManager.getInstance().getPhoto(photoId);
		if (photo != null) {
			PhotoSize photoSize = PhotoSize.getFromInt(size);
			image = photo.getImage(photoSize);
		}
		// if not in cache load from Google Cloud Storage
		if (image == null) {
			Serializable rawImage = null;
			try {
				rawImage = ImageStorage.getInstance().readImage(photoId, size);
			} catch (IOException e) {
				log.warning(LogBuilder.createSystemMessage().addException("Problem when reading image.", e).toString());
			}
			if (rawImage != null && rawImage instanceof Image) {
				image = (Image) rawImage;
			}
		}
		return image;
	}
}

