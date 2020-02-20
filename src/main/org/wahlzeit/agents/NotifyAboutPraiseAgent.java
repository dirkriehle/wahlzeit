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

package org.wahlzeit.agents;

import java.util.HashSet;
import java.util.Set;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.services.mailing.*;


/**
 * 
 * @author dirkriehle
 *
 */
public class NotifyAboutPraiseAgent extends Agent {

	/**
	 * 
	 */
	public static final String NAME = "NotifyAboutPraise";

	/**
	 * 
	 */
	protected Set<Photo> praisedPhotos = new HashSet<Photo>();
	
	/**
	 * 
	 */
	public NotifyAboutPraiseAgent() {
		initialize(NAME, 3 * 24 * 60 * 60 * 1000); // every three days
	}
	
	/**
	 * 
	 */
	public void addForNotify(Photo photo) {
		synchronized(praisedPhotos) {
			praisedPhotos.add(photo);
		}
	}
	
	/**
	 * 
	 */
	protected void doRun() {
		notifyOwners();
	}
	
	/**
	 * 
	 */
	protected void notifyOwners() {
		Photo[] photos = null;
		synchronized(praisedPhotos) {
			photos = praisedPhotos.toArray(new Photo[0]);
			praisedPhotos.clear();
		}

		for (int i = 0; i < photos.length; i++) {
			Photo photo = photos[i];
			if (photo != null) {
				notifyOwner(photo, photos);
			}
		}
	}
	
	/**
	 * 
	 */
	protected void notifyOwner(Photo photo, Photo[] photos) {
		UserManager um = UserManager.getInstance();
		User user = um.getUserByName(photo.getOwnerName());
		ModelConfig cfg = LanguageConfigs.get(photo.getOwnerLanguage());
		
		EmailAddress from = cfg.getAdministratorEmailAddress();
		EmailAddress to = photo.getOwnerEmailAddress();
		String emailSubject = cfg.getNotifyAboutPraiseEmailSubject();

		String emailBody = cfg.getNotifyAboutPraiseEmailBody() + "\n\n";
		
		for (int i = 0; i < photos.length; i++) {
			Photo current = photos[i];
			if ((current != null) && current.hasSameOwner(photo)) {
				String id = current.getId().asString();
				UserLog.logUserInfo("notifying user: " + photo.getOwnerName() + " about photo: " + id);
				emailBody += user.getSiteUrlAsString() + id + ".html\n"; // @TODO Application
				photos[i] = null;
			}
		}
		emailBody += "\n";
		
		emailBody += cfg.getGeneralEmailRegards() + "\n\n";
		emailBody += cfg.getNotifyAboutPraiseEmailPostScriptum() + "\n\n----\n";
		emailBody += cfg.getGeneralEmailFooter() + "\n\n";

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, emailSubject, emailBody);
	}

}
