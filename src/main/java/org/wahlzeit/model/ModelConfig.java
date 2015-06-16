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

package org.wahlzeit.model;

import org.wahlzeit.services.Configuration;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.utils.EnumValue;

import java.io.Serializable;


/**
 * A configuration that provide easy access to Wahlzeit model configuration data.
 *
 * @author dirkriehle
 */
public interface ModelConfig extends Configuration, Serializable {

    // Meta stuff
    Language getLanguage();

    String getLanguageCode();

    // System config
    EmailAddress getModeratorEmailAddress();

    EmailAddress getAuditEmailAddress();

    // Page template
    String getPageTitle();

    String getPageHeading();

    String getPageFooter(PhotoSize ss);

    String getPageMission();

    // Page menu
    String getGuestMenu();

    String getUserMenu();

    String getModeratorMenu();

    String getAdministratorMenu();

    String getCommunityMenu();

    // General error messages
    String getIllegalArgumentError();

    String getIllegalAccessError();

    String getInternalProcessingError();

    String getFieldIsMissing();

    String getInputIsInvalid();

    String getInputIsTooLong();

    String getEmailAddressIsMissing();

    String getEmailAddressIsInvalid();

    String getUrlIsInvalid();

    // General messages
    String getKeepGoing();

    String getContinueWithTellFriends();

    String getContinueWithShowPhoto();

    String getContinueWithShowUserHome();

    // SHOW_NOTE_PAGE
    String getThankYou();

    String getInformation();

    // SHOW_PHOTO_PAGE
    String getAnonUserName();

    String getResetSession();

    // TELL_FRIEND_FORM
    String getEmailWasSent();

    // FLAG_PHOTO_FORM
    String getModeratorWasInformed();

    // SEND_EMAIL_FORM
    String getNeedToSignupFirst();

    // SET_OPTIONS_FORM
    String getOptionsWereSet();

    // SIGNUP_FORM
    String getUserAlreadyExists();

    String getUserNameIsReserved();

    String getPasswordsDontMatch();

    String getDidntCheckTerms();

    String getConfirmationEmailWasSent();

    // VERIFY_ACCOUNT_PAGE
    String getNeedToLoginFirst();

    String getConfirmAccountSucceeded();

    String getConfirmAccountFailed();

    // LOGIN_FORM
    String getLoginIsIncorrect();

    String getUserIsDisabled();

    // EMAIL_USER_NAME_FORM
    String getUnknownEmailAddress();

    String getSendUserNameEmailSubject();

    String getUserNameWasEmailed();

    // EMAIL_PASSWORD_FORM
    String getUserNameIsUnknown();

    String getSendPasswordEmailSubject();

    String getPasswordWasEmailed();

    // SET_LANGUAGE_PAGE
    String getNewLanguageSet();

    // SET_PHOTO_SIZE_PAGE
    String getNewPhotoSizeSet(PhotoSize ss);

    String getNoteMaximumPhotoSize();

    // SHOW_USER_PROFILE_FORM
    String getNoPhotoUploaded();

    // SHOW_USER_PHOTO_FORM
    String getNoCharacterName();

    String getNoSeriesName();

    String getNoTags();

    // EDIT_USER_PROFILE_FORM
    String getProfileUpdateSucceeded();

    // CHANGE_PASSWORD_FORM
    String getPasswordChangeSucceeded();

    // EDIT_USER_PHOTO_FORM
    String getPhotoUpdateSucceeded();

    // UPLOAD_PHOTO_FORM
    String getPhotoUploadFailed();

    String getPhotoUploadSucceeded();

    // LOGOUT_PAGE
    String getLogoutSucceeded();

    // SHOW_PHOTO_CASE_FORM
    String getNoFlaggedPhotoCases();

    // SHOW_ADMIN_MENU_FORM
    String getPhotoIsUnknown();

    // General email
    String getGeneralEmailRegards();

    String getGeneralEmailFooter();

    // Tell friend email
    String getTellFriendEmailSubject();

    String getTellFriendEmailWebsite();

    String getTellFriendEmailPhoto();

    // Send email
    String getSendEmailSubjectPrefix();

    String getSendEmailBodyPrefix();

    String getSendEmailBodyPostfix();

    // Welcome email
    String getWelcomeEmailSubject();

    String getWelcomeEmailBody();

    String getWelcomeEmailUserName();

    // Notify about praise email
    String getNotifyAboutPraiseEmailSubject();

    String getNotifyAboutPraiseEmailBody();

    String getNotifyAboutPraiseEmailPostScriptum();

    // Data types
    String asValueString(EnumValue ev);

    // Localisation
    String asYesOrNoString(boolean yesOrNo);

    String asDateString(long millis);

    String asPhotoSummary(String un);

    String asPhotoCaption(String un);

    String asPraiseString(double praise);


    String getNickNameExists(String nickName);

}
