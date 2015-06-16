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

package org.wahlzeit.webparts;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.wahlzeit.services.ConfigDir;
import org.wahlzeit.services.LogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The WebPartTemplateService creates WebPartTemplates upon request by reading them from disk.
 * It requires configuration with a template directory and uses the following naming convention:
 * tmplDir/language/part-type/part-name.html
 *
 * @author dirkriehle
 */
public class WebPartTemplateService {

    /**
     *
     */
    protected static final WebPartTemplateService instance = new WebPartTemplateService();
    private static final Logger log = Logger.getLogger(WebPartTemplateService.class.getName());
    /**
     *
     */
    protected Map<String, WebPartTemplate> templates = new HashMap<String, WebPartTemplate>();
    /**
     *
     */
    protected ConfigDir templatesDir = null;

    /**
     *
     */
    protected WebPartTemplateService() {
        // do nothing
    }

    /**
     * Convenience method...
     */
    public static WebPartTemplateService getInstance() {
        return instance;
    }

    /**
     *
     */
    public WebPartTemplate getTemplate(String lang, String name) {
        String shortName = lang + File.separator + name;
        WebPartTemplate result = templates.get(shortName);

        if (result == null) {
            try {
                loadTemplate(shortName);
                result = templates.get(shortName);
            } catch (IOException ioex) {
                log.warning(LogBuilder.createSystemMessage().
                        addParameter("template name", shortName).
                        addException("Problem loading template", ioex).toString());
            }
        }

        return result;
    }

    /**
     *
     */
    protected void loadTemplate(String shortName) throws IOException {
        WebPartTemplate template = new WebPartTemplate(shortName);
        String fileName = getTemplatesDir().getAbsoluteConfigFileName(shortName + ".html");
        log.config(LogBuilder.createSystemMessage().
                addAction("open html template file").
                addParameter("file name", fileName).toString());
        File file = new File(fileName);

        try {
            String source = Files.toString(file, Charsets.UTF_8);
            //String source = Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);

            if(source != null) {
                template.initialize(source);
                log.config(LogBuilder.createSystemMessage().addAction("Initialize template").toString());
            }

            templates.put(shortName, template);
        }
        catch (IOException e) {
            log.warning(LogBuilder.createSystemMessage().
                    addException("I/O Error while reading Template file", e).toString());
        }
    }

    /**
     *
     */
    public ConfigDir getTemplatesDir() {
        return templatesDir;
    }

    /**
     *
     */
    public void setTemplatesDir(ConfigDir newTemplatesDir) {
        templatesDir = newTemplatesDir;
    }

}
