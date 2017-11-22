/*
 *  Copyright
 *
 *  Classname: PersistPhotoAgent
 *  Author: Tango1266
 *  Version: 08.11.17 22:26
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

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
 * @review
 */
public class PersistPhotoAgent extends HttpServlet {

    private static final Logger log = Logger.getLogger(PersistPhotoAgent.class.getName());

    /**
     * @methodtype command
     */
    @Override
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
