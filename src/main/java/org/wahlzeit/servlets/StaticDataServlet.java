package org.wahlzeit.servlets;

import com.google.appengine.api.images.Image;
import org.apache.http.HttpStatus;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.PhotoSize;
import org.wahlzeit.model.persistance.ImageStorage;
import org.wahlzeit.services.LogBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Servlet that returns static data like the Photos to the user.
 * <p/>
 * As there are several links for each Photo, this can not the handled via the MainServlet, which has a unique link
 * for each Handler. Instead web.xml redirects all static requests to this Servlet.
 * <p/>
 * Created by Lukas Hahmann on 29.04.15.
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
                Photo photo = PhotoManager.getPhoto(photoId);
                Image image = null;
                if(photo != null) {
                    PhotoSize photoSize = PhotoSize.getFromInt(size);
                    image = photo.getImage(photoSize);
                }
                // if not in cache load from Google Cloud Storage
                if (image == null) {
                    Serializable rawImage = ImageStorage.getInstance().readImage(photoId, size);
                    if (rawImage != null && rawImage instanceof Image) {
                        image = (Image) rawImage;
                    }
                }
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
}

