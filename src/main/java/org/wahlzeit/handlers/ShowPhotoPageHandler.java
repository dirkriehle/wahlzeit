/*
 *  Copyright
 *
 *  Classname: ShowPhotoPageHandler
 *  Author: Tango1266
 *  Version: 08.11.17 22:26
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;
import org.wahlzeit.webparts.Writable;
import org.wahlzeit.webparts.WritableList;

import java.util.Map;

/**
 * A handler class for a specific web page.
 */
public class ShowPhotoPageHandler extends AbstractWebPageHandler implements WebFormHandler {

    /**
     *
     */
    public ShowPhotoPageHandler() {
        initialize(PartUtil.SHOW_PHOTO_PAGE_FILE, AccessRights.GUEST);
    }

    /**
     *
     */
    protected void makeLeftSidebar(UserSession us, WebPart page) {
        WritableList parts = new WritableList();

        Client client = us.getClient();
        Photo lastPraisedPhoto = client.getLastPraisedPhoto();
        if (lastPraisedPhoto != null) {
            parts.append(makePriorPhotoInfo(us, lastPraisedPhoto));
        } else {
            parts.append(createWebPart(us, PartUtil.BLURP_INFO_FILE));
        }

        WebFormHandler handler = getFormHandler(PartUtil.FILTER_PHOTOS_FORM_NAME);
        Writable filterPhotos = handler.makeWebPart(us);
        parts.append(filterPhotos);

        parts.append(createWebPart(us, PartUtil.LINKS_INFO_FILE));

        page.addWritable("sidebar", parts);
    }

    /**
     *
     */
    protected void makePhoto(UserSession us, WebPart page) {
        Client client = us.getClient();
        PhotoSize pagePhotoSize = client.getPhotoSize();

        PhotoId photoId = us.getPhotoId();
        Photo photo = PhotoManager.getInstance().getPhoto(photoId);

        if (photo == null) {
            page.addString("mainWidth", String.valueOf(pagePhotoSize.getMaxPhotoWidth()));
            WebPart done = createWebPart(us, PartUtil.DONE_INFO_FILE);
            page.addWritable(Photo.IMAGE, done);
            return;
        }

        if (!photo.isVisible() && !client.hasModeratorRights() && !us.isPhotoOwner(photo)) {
            page.addString("mainWidth", String.valueOf(pagePhotoSize.getMaxPhotoWidth()));
            WebPart done = createWebPart(us, PartUtil.HIDDEN_INFO_FILE);
            page.addWritable(Photo.IMAGE, done);
            return;
        }

        PhotoSize maxPhotoSize = photo.getMaxPhotoSize();
        PhotoSize photoSize = (maxPhotoSize.isSmaller(pagePhotoSize)) ? maxPhotoSize : pagePhotoSize;
        String imageLink = getPhotoAsRelativeResourcePathString(photo, photoSize);
        page.addString(Photo.IMAGE, HtmlUtil.asImg(HtmlUtil.asPath(imageLink)));
    }

    /**
     *
     */
    protected void makePhotoCaption(UserSession us, WebPart page) {
        PhotoId photoId = us.getPhotoId();
        Photo photo = PhotoManager.getInstance().getPhoto(photoId);

        WebPart caption = createWebPart(us, PartUtil.CAPTION_INFO_FILE);
        caption.addString(Photo.CAPTION, getPhotoCaption(us, photo));
        page.addWritable(Photo.CAPTION, caption);
    }

    /**
     *
     */
    protected void makeEngageGuest(UserSession us, WebPart page) {
        PhotoId photoId = us.getPhotoId();

        WebPart engageGuest = createWebPart(us, PartUtil.ENGAGE_GUEST_FORM_FILE);
        engageGuest.addString(Photo.LINK, HtmlUtil.asHref(getResourceAsRelativeHtmlPathString(photoId.asString())));
        engageGuest.addString(Photo.ID, photoId.asString());

        page.addWritable("engageGuest", engageGuest);
    }

    /**
     *
     */
    protected void makeRightSidebar(UserSession us, WebPart page) {
        String handlerName = PartUtil.NULL_FORM_NAME;
        PhotoId photoId = us.getPhotoId();
        Photo photo = PhotoManager.getInstance().getPhoto(photoId);
        if (photo != null) {
            handlerName = PartUtil.PRAISE_PHOTO_FORM_NAME;
        }

        WebFormHandler handler = getFormHandler(handlerName);
        Writable praisePhotoForm = handler.makeWebPart(us);
        page.addWritable("praisePhoto", praisePhotoForm);
    }

    /**
     *
     */
    protected WebPart makePriorPhotoInfo(UserSession us, Photo lastPraisedPhoto) {
        WebPart result = createWebPart(us, PartUtil.PHOTO_INFO_FILE);

        result.addString(Photo.PRAISE, lastPraisedPhoto.getPraiseAsString(us.getClient().getLanguageConfiguration()));
        result.addString(Photo.THUMB, getPhotoThumb(us, lastPraisedPhoto));
        result.addString(Photo.CAPTION, getPhotoCaption(us, lastPraisedPhoto));

        return result;
    }

    /**
     *
     */
    @Override
    protected String doHandleGet(UserSession us, String link, Map args) {
        Photo photo = null;

        if (!link.equals(PartUtil.SHOW_PHOTO_PAGE_NAME)) {
            photo = PhotoManager.getInstance().getPhoto(link);
        }

        PhotoManager gurkenPhotoManager = PhotoManager.getInstance();
        // check if an image has been skipped
        if (args.containsKey("prior")) {
            String skippedPhotoIdString = us.getAsString(args, "prior");
            PhotoId skippedPhotoId = PhotoId.getIdFromString(skippedPhotoIdString);
            us.getClient().addSkippedPhotoId(skippedPhotoId);
            us.getPhotoFilter().addSkippedPhotoId(skippedPhotoId);
        }

        if (photo == null) {
            PhotoFilter filter = us.getPhotoFilter();
            photo = gurkenPhotoManager.getVisiblePhoto(filter);
            if (photo != null) {
                link = photo.getId().asString();
            }
        }

        if (photo != null) {
            us.setPhotoId(photo.getId());
        } else {
            us.setPhotoId(null);
        }
        return link;
    }

    /**
     *
     */
    @Override
    protected boolean isToShowAds(UserSession us) {
        Client client = us.getClient();
        Photo lastPraisedPhoto = client.getLastPraisedPhoto();
        return lastPraisedPhoto != null;
    }

    /**
     *
     */
    @Override
    protected void makeWebPageBody(UserSession us, WebPart page) {
        PhotoId photoId = us.getPhotoId();
        Photo photo = PhotoManager.getInstance().getPhoto(photoId);

        makeLeftSidebar(us, page);

        makePhoto(us, page);

        if (photo != null && photo.isVisible()) {
            makePhotoCaption(us, page);
            makeEngageGuest(us, page);

            page.addString(Photo.ID, photoId.asString());

            Tags tags = photo.getTags();
            page.addString(Photo.DESCRIPTION, getPhotoSummary(us, photo));
            page.addString(Photo.KEYWORDS, tags.asString(false, ','));
        }

        makeRightSidebar(us, page);
    }

    /**
     *
     */
    @Override
    public String handlePost(UserSession us, Map args) {
        String result = PartUtil.DEFAULT_PAGE_NAME;

        String id = us.getAndSaveAsString(args, Photo.ID);
        Photo photo = PhotoManager.getInstance().getPhoto(id);
        if (photo != null) {
            if (us.isFormType(args, "flagPhotoLink")) {
                result = PartUtil.FLAG_PHOTO_PAGE_NAME;
            } else if (us.isFormType(args, "tellFriendLink")) {
                result = PartUtil.TELL_FRIEND_PAGE_NAME;
            } else if (us.isFormType(args, "sendEmailLink")) {
                result = PartUtil.SEND_EMAIL_PAGE_NAME;
            }
        }

        return result;
    }

}
