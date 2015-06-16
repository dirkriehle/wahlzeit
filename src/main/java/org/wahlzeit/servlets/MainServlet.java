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

package org.wahlzeit.servlets;

import com.google.api.client.util.Charsets;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.common.io.CharStreams;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.wahlzeit.handlers.PartUtil;
import org.wahlzeit.handlers.WebFormHandler;
import org.wahlzeit.handlers.WebPageHandler;
import org.wahlzeit.handlers.WebPartHandlerManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.SessionManager;
import org.wahlzeit.webparts.WebPart;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * @author dirkriehle
 */
public class MainServlet extends AbstractServlet {

    private static final Logger log = Logger.getLogger(MainServlet.class.getName());
    /**
     *
     */
    private static final long serialVersionUID = 42L; // any one does; class never serialized

    /**
     *
     */
    public void myPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        UserSession us = (UserSession) SessionManager.getThreadLocalSession();
        String link = request.getRequestURI();
        int linkStart = link.lastIndexOf("/") + 1;
        int linkEnd = link.indexOf(".form");
        if (linkEnd != -1) {
            link = link.substring(linkStart, linkEnd);
        } else {
            link = PartUtil.NULL_FORM_NAME;
        }
        log.info(LogBuilder.createUserMessage().addParameter("posted to", link).toString());

        Map args = getRequestArgs(request, us);
        log.info(LogBuilder.createSystemMessage().
                addParameter("POST arguments", getRequestArgsAsString(us, args)).toString());

        WebFormHandler formHandler = WebPartHandlerManager.getWebFormHandler(link);
        link = PartUtil.DEFAULT_PAGE_NAME;
        if (formHandler != null) {
            link = formHandler.handlePost(us, args);
        }

        redirectRequest(response, link);
        us.addProcessingTime(System.currentTimeMillis() - startTime);
        SessionManager.dropThreadLocalSession();
    }

    /**
     *
     */
    public void myGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        UserSession us = (UserSession) SessionManager.getThreadLocalSession();
        String link = request.getRequestURI();
        int linkStart = link.lastIndexOf("/") + 1;
        int linkEnd = link.indexOf(".html");
        if (linkEnd == -1) {
            linkEnd = link.length();
        }

        link = link.substring(linkStart, linkEnd);
        log.info(LogBuilder.createUserMessage().addParameter("requested URI", request.getRequestURI()).toString());


        WebPageHandler handler = WebPartHandlerManager.getWebPageHandler(link);
        String newLink = PartUtil.DEFAULT_PAGE_NAME;
        if (handler != null) {
            Map args = getRequestArgs(request, us);
            log.info(LogBuilder.createSystemMessage().
                    addParameter("GET arguments", getRequestArgsAsString(us, args)).toString());
            newLink = handler.handleGet(us, link, args);
        }

        if (newLink.equals(link)) { // no redirect necessary
            WebPart result = handler.makeWebPart(us);
            us.addProcessingTime(System.currentTimeMillis() - startTime);
            configureResponse(us, response, result);
            us.clearSavedArgs(); // saved args go from post to next get
            us.resetProcessingTime();
        } else {
            redirectRequest(response, newLink);
            us.addProcessingTime(System.currentTimeMillis() - startTime);
        }
        SessionManager.dropThreadLocalSession();
    }

    /**
     *
     */
    protected Map getRequestArgs(HttpServletRequest request, UserSession us) throws IOException, ServletException {
        String contentType = request.getContentType();
        if ((contentType != null) && contentType.startsWith("multipart/form-data")) {
            return getMultiPartRequestArgs(request, us);
        } else {
            return request.getParameterMap();
        }
    }

    /**
     * Searches for files in the request and puts them in the resulting map with the key "fileName". When a file is
     * found, you can access its path by searching for elements with the key "fileName".
     */
    protected Map getMultiPartRequestArgs(HttpServletRequest request, UserSession us) throws IOException, ServletException {
        Map<String, String> result = new HashMap<String, String>();
        result.putAll(request.getParameterMap());
        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterator = upload.getItemIterator(request);

            while (iterator.hasNext()) {
                FileItemStream fileItemStream = iterator.next();
                String filename = fileItemStream.getName();

                if (!fileItemStream.isFormField()) {
                    InputStream inputStream = fileItemStream.openStream();
                    Image image = getImage(inputStream);
                    us.setUploadedImage(image);
                    result.put("fileName", filename);
                    log.config(LogBuilder.createSystemMessage().addParameter("Uploaded image", filename).toString());
                } else {
                    String key = fileItemStream.getFieldName();
                    InputStream is = fileItemStream.openStream();
                    String value = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
                    result.put(key, value);
                    log.config(LogBuilder.createSystemMessage().
                            addParameter("Key of uploaded parameter", key).
                            addParameter("value", value).toString());
                }
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

        return result;
    }

    /**
     * Create an Image object from the Input stream.
     */
    private Image getImage(InputStream input) throws IOException {
        Image image;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
            image = ImagesServiceFactory.makeImage(outputStream.toByteArray());
        } finally {
            input.close();
        }
        return image;
    }
}
