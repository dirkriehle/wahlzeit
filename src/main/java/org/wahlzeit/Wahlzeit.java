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

package org.wahlzeit;

import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.wahlzeit.agent.AgentManager;
import org.wahlzeit.api.ServletInitializer;
import org.wahlzeit.config.WahlzeitConfig;
import org.wahlzeit.database.repository.CaseRepository;
import org.wahlzeit.database.repository.PhotoRepository;
import org.wahlzeit.database.repository.UserRepository;
import org.wahlzeit.model.CaseFactory;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.UserFactory;
import org.wahlzeit.service.*;
import org.wahlzeit.service.mailing.EmailService;
import org.wahlzeit.service.mailing.LoggingEmailService;
import org.wahlzeit.utils.SysConfig;

import java.net.URI;


public class Wahlzeit {

    private static class ServiceInjectBinder extends AbstractBinder {
        @Override
        protected void configure() {
            // Setup @inject annotation -> bind(InjectClass).to(ImplClass)
            bind(SysConfig.class).to(WahlzeitConfig.class);
            // manager
            bind(AgentManager.class).to(AgentManager.class);
            // email
            bind(LoggingEmailService.class).to(EmailService.class);
            // factory
            bind(UserFactory.class).to(UserFactory.class);
            bind(PhotoFactory.class).to(PhotoFactory.class);
            bind(CaseFactory.class).to(CaseFactory.class);
            // service
            bind(Transformer.class).to(Transformer.class);
            bind(PhotoService.class).to(PhotoService.class);
            bind(PhotoFlagService.class).to(PhotoFlagService.class);
            bind(UserService.class).to(UserService.class);
            bind(CaseService.class).to(CaseService.class);
            // repository
            bind(UserRepository.class).to(UserRepository.class);
            bind(PhotoRepository.class).to(PhotoRepository.class);
            bind(CaseRepository.class).to(CaseRepository.class);
        }
    }

    public static void main(String[] args) throws Exception {
        // setup endpoints/API
        ResourceConfig config = new ResourceConfig()
                .packages("org.wahlzeit.agent")
                .packages("org.wahlzeit.api")
                .packages("org.wahlzeit.api.auth")
                .packages("org.wahlzeit.api.filter")
                .packages("org.wahlzeit.api.resource")
                .packages("org.wahlzeit.api.service")
                .packages("org.wahlzeit.api.service.mailing")
                .packages("org.wahlzeit.db.repository")
                .register(ServletInitializer.class)
                .register(ServiceInjectBinder.class);

        // setup server
        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);

        WebappContext context = new WebappContext("Wahlzeit");
        context.deploy(httpServer); // Call container startup hooks before starting the httpServer
        httpServer.start();
    }

}
