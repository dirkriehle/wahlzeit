package org.wahlzeit.agents;

import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.services.LogBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Servlet to persist Photos that are only in the Cache.
 * As it has nothing to do with <code>UserSession</code> or UI, it
 * is not implemented as a Handler or a child of <code>AbstractServlet</code>.
 * 
 * @review
 */
public class PersistPhotoAgent extends HttpServlet {

	private static final Logger log = Logger.getLogger(PersistPhotoAgent.class.getName());

	/**
	 * @methodtype command
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter(Photo.ID);
		log.config(LogBuilder.createSystemMessage().addParameter("Try to persist PhotoId", id).toString());
		if (id != null && !"".equals(id)) {
			Photo photo = PhotoManager.getInstance().getPhoto(id);
			if (photo != null) {
				PhotoManager.getInstance().savePhoto(photo);
				log.config(LogBuilder.createSystemMessage().addMessage("Photo saved.").toString());
			} else {
				response.setStatus(299);
				throw new IllegalArgumentException("Could not find Photo with ID " + id);
			}
		}
		response.setStatus(200);
	}
}
