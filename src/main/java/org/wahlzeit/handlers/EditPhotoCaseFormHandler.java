/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.Map;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoCase;
import org.wahlzeit.model.PhotoCaseManager;
import org.wahlzeit.model.PhotoStatus;
import org.wahlzeit.model.UserLog;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * A handler class for a specific web form.
 */
public class EditPhotoCaseFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public EditPhotoCaseFormHandler() {
		initialize(PartUtil.EDIT_PHOTO_CASE_FORM_FILE, AccessRights.MODERATOR);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		PhotoCase photoCase = us.getPhotoCase();
		Photo photo = photoCase.getPhoto();

		part.addString(Photo.THUMB, getPhotoThumb(us, photo));

		String id = String.valueOf(photoCase.getId());
		part.addString(PhotoCase.ID, id);
		
		String description = getPhotoSummary(us, photo);
		part.maskAndAddString(Photo.DESCRIPTION, description);

		String tags = photo.getTags().asString();
		tags = !StringUtil.isNullOrEmptyString(tags) ? tags : us.cfg().getNoTags();
		part.maskAndAddString(Photo.TAGS, tags);
		
		String photoId = photo.getId().asString();
		part.addString(Photo.LINK, HtmlUtil.asHref(getResourceAsRelativeHtmlPathString(photoId)));

		part.addString(PhotoCase.FLAGGER, photoCase.getFlagger());
		part.addString(PhotoCase.REASON, us.cfg().asValueString(photoCase.getReason()));
		part.addString(PhotoCase.EXPLANATION, photoCase.getExplanation());		
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String id = us.getAndSaveAsString(args, PhotoCase.ID);
		PhotoCaseManager pcm = PhotoCaseManager.getInstance();
		
		PhotoCase photoCase = pcm.getPhotoCase(Integer.parseInt(id));
		Photo photo = photoCase.getPhoto();
		PhotoStatus status = photo.getStatus();
		if (us.isFormType(args, "unflag")) {
			status = status.asFlagged(false);
		} else if (us.isFormType(args, "moderate")) {
			status = status.asModerated(true);
		} else { // something wrong?
			return PartUtil.SHOW_PHOTO_CASES_PAGE_NAME;
		}

		photo.setStatus(status);

		StringBuffer sb = UserLog.createActionEntry("EditPhotoCase");
		UserLog.addUpdatedObject(sb, "Photo", photo.getId().asString());
		UserLog.log(sb);

		photoCase.setDecided();
		pcm.removePhotoCase(photoCase);
		
		sb = UserLog.createActionEntry("EditPhotoCase");
		UserLog.addUpdatedObject(sb, "PhotoCase", String.valueOf(photoCase.getId()));
		UserLog.log(sb);

		return PartUtil.SHOW_PHOTO_CASES_PAGE_NAME;
	}
	
}
