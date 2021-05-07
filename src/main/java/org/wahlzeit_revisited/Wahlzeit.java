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

package org.wahlzeit_revisited;

import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.wahlzeit_revisited.agent.AgentManager;
import org.wahlzeit_revisited.main.DatabaseMain;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.repository.CaseRepository;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.service.*;
import org.wahlzeit_revisited.utils.SysConfig;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

import java.net.URI;


public class Wahlzeit {

    private static final SysConfig sysConfig = new SysConfig(); // Global config

    private static class ServiceInjectBinder extends AbstractBinder {
        @Override
        protected void configure() {
            // Setup @inject annotation -> bind(InjectClass).to(ImplClass)
            bind(sysConfig).to(WahlzeitConfig.class);
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

    private static void setupLanguageConfig() {
        LanguageConfigs.put(Language.ENGLISH, new EnglishModelConfig());
        LanguageConfigs.put(Language.GERMAN, new GermanModelConfig());
    }

    private static void startAgents() {
        AgentManager.getInstance().startAllThreads();
    }

    private static void startServer() {
        // setup endpoints/API
        ResourceConfig config = new ResourceConfig()
                .packages("org.wahlzeit_revisited.auth")
                .packages("org.wahlzeit_revisited.agent")
                .packages("org.wahlzeit_revisited.filter")
                .packages("org.wahlzeit_revisited.resource")
                .packages("org.wahlzeit_revisited.service");
        config.register(new ServiceInjectBinder());

        // setup server
        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
        GrizzlyHttpServerFactory.createHttpServer(baseUri, config, true);
    }

    public static void main(String[] args) throws Exception {
        // setup database-connection
        DatabaseMain databaseMain = new DatabaseMain(sysConfig);
        databaseMain.startUp();

        setupLanguageConfig();
        startAgents();
        startServer();
    }

}
