/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
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

package org.wahlzeit.api;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.wahlzeit.agent.AgentManager;
import org.wahlzeit.config.WahlzeitConfig;
import org.wahlzeit.database.repository.PhotoRepository;
import org.wahlzeit.main.DatabaseMain;
import org.wahlzeit.service.PhotoService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Set;

@WebListener
public class ServletInitializer implements ApplicationEventListener {

    private static final Logger LOG = LogManager.getLogger(ServletInitializer.class);

    @Inject
    AgentManager agentManager;
    @Inject
    WahlzeitConfig config;
    @Inject
    PhotoService photoService;
    @Inject
    PhotoRepository photoRepository;
    // Not injected
    DatabaseMain databaseMain;

    /**
     * Callback to Jakarta events
     * Delegates event to the according helper method
     *
     * @param event jakarta Event
     */
    @Override
    public void onEvent(ApplicationEvent event) {
        if (event.getType() == ApplicationEvent.Type.INITIALIZATION_FINISHED) {
            try {
                onStartUp();
            } catch (Exception e) {
                LOG.fatal("Failed setting up wahlzeit", e);
            }
        } else if (event.getType() == ApplicationEvent.Type.DESTROY_FINISHED) {
            onTearDown();
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }

    /**
     * Setups database and agents inside of the jakarta context
     */
    private void onStartUp() throws Exception {
        LOG.fatal("Starting Java EE Container..");

        // Init database
        databaseMain = new DatabaseMain(config);
        databaseMain.startUp();

        // Setup business logic
        setupInitialPhotos();
        agentManager.startAllThreads();
    }

    /**
     * Clean up
     */
    private void onTearDown() {
        LOG.info("Shutting down Java EE Container..");

        databaseMain.shutDown();
        agentManager.stopAllThreads();
    }

    /**
     * Executed by Jakarta on the first execution of the application
     * Inserts all photos of the configured directory into the database
     *
     * @throws SQLException
     * @throws IOException
     */
    private void setupInitialPhotos() throws SQLException, IOException {
        if (!photoRepository.findAll().isEmpty()) {
            return;
        }

        int count = 0;
        ClassLoader classLoader = getClass().getClassLoader();
        for (String currentFilePath : config.getPhotosPath()) {
            InputStream is = classLoader.getResourceAsStream(currentFilePath);
            byte[] imageData = is.readAllBytes();
            photoService.addPhoto(null, imageData, Set.of());
            count++;
        }
        LOG.info(String.format("Initialized wahlzeit with %s photos", count));
    }

}
