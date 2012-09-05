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

package org.wahlzeit.main;

import java.io.*;
import java.sql.*;

import org.mortbay.http.*;
import org.mortbay.http.handler.*;
import org.mortbay.jetty.servlet.*;

import org.wahlzeit.agents.AgentManager;
import org.wahlzeit.handlers.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.webparts.*;

/**
 * A Main class that runs a Wahlzeit web server.
 * 
 * @author driehle
 *
 */
public abstract class ServerMain extends ModelMain {
	
	/**
	 * 
	 */
	protected static boolean isToStop = false;
	
	/**
	 * 
	 */
	public static void requestStop() {
		SysLog.logInfo("setting stop signal for http server");
		synchronized(instance) {
			isToStop = true;
			instance.notify();
		}
	}
	
	/**
	 * 
	 */
	public static boolean isShuttingDown() {
		return isToStop;
	}
	
	/**
	 * 
	 */
	protected HttpServer httpServer = null;
	
	/**
	 * 
	 */
	protected void startUp() throws Exception {
		super.startUp();

		httpServer = createHttpServer();
		configureHttpServer(httpServer);
		
		configurePartHandlers();
		configureLanguageModels();
		
		PhotoFactory.initialize();
			
		AgentManager am = AgentManager.getInstance();
		am.startAllThreads();

		startHttpServer(httpServer);
	}
	
	/**
	 * 
	 */
	protected void execute() throws Exception {
		wait(); // really, any condition is fine
	}

	/**
	 * 
	 */
	protected void shutDown() throws Exception {
		AgentManager am = AgentManager.getInstance();
		am.stopAllThreads();
		
		if (httpServer != null) {
			stopHttpServer(httpServer);
		}
		
		super.shutDown();
	}
	
	/**
	 * 
	 */
	protected HttpServer createHttpServer() throws IOException {
		HttpServer server = new HttpServer();

		SocketListener listener = new SocketListener();
		listener.setPort(SysConfig.getHttpPortAsInt());
		server.addListener(listener);

		return server;
	}
	
	/**
	 * 
	 */
	protected void configureHttpServer(HttpServer server) {
		HttpContext servletContext = new HttpContext();
		servletContext.setContextPath("/");
		server.addContext(servletContext);
	    
		ServletHandler servlets = new ServletHandler();
		servletContext.addHandler(servlets);
		servlets.addServlet("/*","org.wahlzeit.main.MainServlet");

		servletContext.addHandler(new NotFoundHandler());

		// Photos content
		
		HttpContext photosContext = new HttpContext();
		photosContext.setContextPath(SysConfig.getPhotosDirAsString());
		server.addContext(photosContext);

		ResourceHandler photosHandler = new ResourceHandler();
		photosContext.setResourceBase(SysConfig.getPhotosDirAsString());
		photosContext.addHandler(photosHandler);

		photosContext.addHandler(new NotFoundHandler());

		// Static content
		
		HttpContext staticContext = new HttpContext();
		staticContext.setContextPath(SysConfig.getStaticDir().getRootPath());
		server.addContext(staticContext);

		ResourceHandler imagesHandler = new ResourceHandler();
		staticContext.setResourceBase(SysConfig.getStaticDir().getRootPath());
		staticContext.addHandler(imagesHandler);

		staticContext.addHandler(new NotFoundHandler());
	}

	/**
	 * 
	 */
	public void startHttpServer(HttpServer httpServer) throws Exception {
		httpServer.start();
		SysLog.logInfo("http server was started");
	}
	
	/**
	 * 
	 */
	public void stopHttpServer(HttpServer httpServer) {
		try {
			httpServer.stop(true);
		} catch (InterruptedException ie) {
			SysLog.logThrowable(ie);
		}
		
		SysLog.logInfo("http server was stopped");
	}
	
	/**
	 * 
	 */
	public static void configurePartHandlers() {
		WebPartHandler temp = null;
		WebPartHandlerManager manager = WebPartHandlerManager.getInstance();
		
		// NullInfo and NullForm
		manager.addWebPartHandler(PartUtil.NULL_FORM_NAME, new NullFormHandler());
		
		// Note page
		manager.addWebPartHandler(PartUtil.SHOW_NOTE_PAGE_NAME, new ShowNotePageHandler());

		// ShowPhoto page
		manager.addWebPartHandler(PartUtil.FILTER_PHOTOS_FORM_NAME, new FilterPhotosFormHandler());
		manager.addWebPartHandler(PartUtil.PRAISE_PHOTO_FORM_NAME, new PraisePhotoFormHandler());

		temp = new ShowPhotoPageHandler();
		manager.addWebPartHandler(PartUtil.SHOW_PHOTO_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.ENGAGE_GUEST_FORM_NAME, temp);
		
		manager.addWebPartHandler(PartUtil.FILTER_PHOTOS_PAGE_NAME, new FilterPhotosPageHandler());

		manager.addWebPartHandler(PartUtil.RESET_SESSION_PAGE_NAME, new ResetSessionPageHandler());
		
		// About and Terms pages
		manager.addWebPartHandler(PartUtil.ABOUT_PAGE_NAME, new ShowInfoPageHandler(AccessRights.GUEST, PartUtil.ABOUT_INFO_FILE));
		manager.addWebPartHandler(PartUtil.CONTACT_PAGE_NAME, new ShowInfoPageHandler(AccessRights.GUEST, PartUtil.CONTACT_INFO_FILE));
		manager.addWebPartHandler(PartUtil.IMPRINT_PAGE_NAME, new ShowInfoPageHandler(AccessRights.GUEST, PartUtil.IMPRINT_INFO_FILE));
		manager.addWebPartHandler(PartUtil.TERMS_PAGE_NAME, new ShowInfoPageHandler(AccessRights.GUEST, PartUtil.TERMS_INFO_FILE));

		// Flag, Send, Tell, and Options pages
		temp = manager.addWebPartHandler(PartUtil.FLAG_PHOTO_FORM_NAME, new FlagPhotoFormHandler());
		manager.addWebPartHandler(PartUtil.FLAG_PHOTO_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));
		temp = manager.addWebPartHandler(PartUtil.SEND_EMAIL_FORM_NAME, new SendEmailFormHandler());
		manager.addWebPartHandler(PartUtil.SEND_EMAIL_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));
		temp = manager.addWebPartHandler(PartUtil.TELL_FRIEND_FORM_NAME, new TellFriendFormHandler());
		manager.addWebPartHandler(PartUtil.TELL_FRIEND_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));
		temp = manager.addWebPartHandler(PartUtil.SET_OPTIONS_FORM_NAME, new SetOptionsFormHandler());
		manager.addWebPartHandler(PartUtil.SET_OPTIONS_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));
		
		// Signup, Login, EmailUserName/Password, and Logout pages
		temp = manager.addWebPartHandler(PartUtil.SIGNUP_FORM_NAME, new SignupFormHandler());
		manager.addWebPartHandler(PartUtil.SIGNUP_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));

		manager.addWebPartHandler(PartUtil.CONFIRM_ACCOUNT_PAGE_NAME, new ConfirmAccountPageHandler());

		temp = manager.addWebPartHandler(PartUtil.LOGIN_FORM_NAME, new LoginFormHandler());
		manager.addWebPartHandler(PartUtil.LOGIN_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));
		temp = manager.addWebPartHandler(PartUtil.EMAIL_USER_NAME_FORM_NAME, new EmailUserNameFormHandler());
		manager.addWebPartHandler(PartUtil.EMAIL_USER_NAME_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));
		temp = manager.addWebPartHandler(PartUtil.EMAIL_PASSWORD_FORM_NAME, new EmailPasswordFormHandler());
		manager.addWebPartHandler(PartUtil.EMAIL_PASSWORD_PAGE_NAME, new ShowPartPageHandler(AccessRights.GUEST, temp));

		manager.addWebPartHandler(PartUtil.LOGOUT_PAGE_NAME, new LogoutPageHandler());
		
		// SetLanguage pages
		temp = new SetLanguagePageHandler();
		manager.addWebPartHandler(PartUtil.SET_ENGLISH_LANGUAGE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_GERMAN_LANGUAGE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_SPANISH_LANGUAGE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_JAPANESE_LANGUAGE_PAGE_NAME, temp);

		// SetPhotoSize pages
		temp = new SetPhotoSizePageHandler();
		manager.addWebPartHandler(PartUtil.SET_EXTRA_SMALL_PHOTO_SIZE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_SMALL_PHOTO_SIZE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_MEDIUM_PHOTO_SIZE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_LARGE_PHOTO_SIZE_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SET_EXTRA_LARGE_PHOTO_SIZE_PAGE_NAME, temp);

		// ShowHome page
		manager.addWebPartHandler(PartUtil.SHOW_USER_PROFILE_FORM_NAME, new ShowUserProfileFormHandler());
		manager.addWebPartHandler(PartUtil.SHOW_USER_PHOTO_FORM_NAME, new ShowUserPhotoFormHandler());
		manager.addWebPartHandler(PartUtil.SHOW_USER_HOME_PAGE_NAME, new ShowUserHomePageHandler());
		
		// EditProfile, ChangePassword, EditPhoto, and UploadPhoto pages
		temp = manager.addWebPartHandler(PartUtil.EDIT_USER_PROFILE_FORM_NAME, new EditUserProfileFormHandler());
		manager.addWebPartHandler(PartUtil.EDIT_USER_PROFILE_PAGE_NAME, new ShowPartPageHandler(AccessRights.USER, temp));
		temp = manager.addWebPartHandler(PartUtil.CHANGE_PASSWORD_FORM_NAME, new ChangePasswordFormHandler());
		manager.addWebPartHandler(PartUtil.CHANGE_PASSWORD_PAGE_NAME, new ShowPartPageHandler(AccessRights.USER, temp));
		temp = manager.addWebPartHandler(PartUtil.EDIT_USER_PHOTO_FORM_NAME, new EditUserPhotoFormHandler());
		manager.addWebPartHandler(PartUtil.EDIT_USER_PHOTO_PAGE_NAME, new ShowPartPageHandler(AccessRights.USER, temp));
		temp = manager.addWebPartHandler(PartUtil.UPLOAD_PHOTO_FORM_NAME, new UploadPhotoFormHandler());
		manager.addWebPartHandler(PartUtil.UPLOAD_PHOTO_PAGE_NAME, new ShowPartPageHandler(AccessRights.USER, temp));
		
		manager.addWebPartHandler(PartUtil.EDIT_PHOTO_CASE_FORM_NAME, new EditPhotoCaseFormHandler());
		manager.addWebPartHandler(PartUtil.SHOW_PHOTO_CASES_PAGE_NAME, new ShowPhotoCasesPageHandler());

		// Admin page incl. AdminUserProfile and AdminUserPhoto
		temp = new ShowAdminPageHandler();
		manager.addWebPartHandler(PartUtil.SHOW_ADMIN_PAGE_NAME, temp);
		manager.addWebPartHandler(PartUtil.SHOW_ADMIN_MENU_FORM_NAME, temp);
		manager.addWebPartHandler(PartUtil.ADMIN_USER_PROFILE_FORM_NAME, new AdminUserProfileFormHandler());
		manager.addWebPartHandler(PartUtil.ADMIN_USER_PHOTO_FORM_NAME, new AdminUserPhotoFormHandler());
	}
	
	/**
	 * 
	 */
	public static void configureLanguageModels() {
		LanguageConfigs.put(Language.ENGLISH, new EnglishModelConfig());
		LanguageConfigs.put(Language.GERMAN, new GermanModelConfig());
	}
		
}
