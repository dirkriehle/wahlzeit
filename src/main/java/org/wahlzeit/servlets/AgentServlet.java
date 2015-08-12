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

import org.wahlzeit.agents.AgentManager;
import org.wahlzeit.services.LogBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Lukas Hahmann
 */
public class AgentServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(AgentServlet.class.getName());

    /**
     * @methodtype command
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        int nameStart = requestUri.lastIndexOf("/") + 1;
        int nameEnd = requestUri.length();
        String agentName = requestUri.substring(nameStart, nameEnd);
        log.config(LogBuilder.createSystemMessage().addParameter("agent name", agentName).toString());

        try {
            AgentManager.getInstance().startAgent(agentName);
            response.setStatus(200);
        } catch (Exception e) {
            log.warning(LogBuilder.createSystemMessage().addException("Problem when starting the agent", e).toString());
            response.setStatus(299);
        }
    }


}
