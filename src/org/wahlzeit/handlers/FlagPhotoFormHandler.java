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

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.services.mailing.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author dirkriehle
 *
 */
public class FlagPhotoFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public FlagPhotoFormHandler() {
		initialize(PartUtil.FLAG_PHOTO_FORM_FILE, AccessRights.GUEST);
	}

	/**
	 * 
	 */
	protected boolean isWellFormedGet(UserSession us, String link, Map args) {
		return hasSavedPhotoId(us) && isSavedPhotoVisible(us);
	}

	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);
		
		String id = us.getAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);
		part.addString(Photo.ID, id);
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));
		part.maskAndAddStringFromArgsWithDefault(args, PhotoCase.FLAGGER, us.getEmailAddressAsString());
		part.addSelect(PhotoCase.REASON, FlagReason.MISMATCH);
		part.maskAndAddStringFromArgs(args, PhotoCase.EXPLANATION);
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String id = us.getAndSaveAsString(args, Photo.ID);
		String flagger = us.getAndSaveAsString(args, PhotoCase.FLAGGER);
		FlagReason reason = FlagReason.getFromString(us.getAndSaveAsString(args, PhotoCase.REASON));
		String explanation = us.getAndSaveAsString(args, PhotoCase.EXPLANATION);
		
		if (StringUtil.isNullOrEmptyString(flagger)) {
			us.setMessage(us.cfg().getEmailAddressIsMissing());
			return PartUtil.FLAG_PHOTO_PAGE_NAME;
		} else if (!StringUtil.isValidStrictEmailAddress(flagger)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.FLAG_PHOTO_PAGE_NAME;
		} else if (explanation.length() > 1024) {
			us.setMessage(us.cfg().getInputIsTooLong());
			return PartUtil.FLAG_PHOTO_PAGE_NAME;			
		}
		
		Photo photo = PhotoManager.getPhoto(id);
		photo.setStatus(photo.getStatus().asFlagged(true));
		PhotoManager pm = PhotoManager.getInstance();
		pm.savePhoto(photo);
		
		PhotoCase photoCase = new PhotoCase(photo);
		photoCase.setFlagger(flagger);
		photoCase.setReason(reason);
		photoCase.setExplanation(explanation);
		PhotoCaseManager pcm = PhotoCaseManager.getInstance();
		pcm.addPhotoCase(photoCase);
		
		EmailService emailService = EmailServiceManager.getDefaultService();

		EmailAddress from = EmailAddress.getFromString(flagger);
		EmailAddress to = us.cfg().getModeratorEmailAddress();

		String emailSubject = "Photo: " + id + " of user: " + photo.getOwnerName() + " got flagged";
		String emailBody = "Photo: " + us.getSiteUrl() + id + ".html\n\n";
		emailBody += "Reason: " + reason + "\n\n";
		emailBody += "Explanation: " + explanation + "\n\n";
		
		emailService.sendEmailIgnoreException(from, to, us.cfg().getAuditEmailAddress(), emailSubject, emailBody);
		
		us.setEmailAddress(from);

		StringBuffer sb = UserLog.createActionEntry("FlagPhoto");
		UserLog.addUpdatedObject(sb, "Photo", photo.getId().asString());
		UserLog.log(sb);
		
		us.setTwoLineMessage(us.cfg().getModeratorWasInformed(), us.cfg().getContinueWithShowPhoto());
		
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
