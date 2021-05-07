/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.net.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;


/**
 * A configuration that provide easy access to Wahlzeit model configuration data.
 */
public interface ModelConfig extends Configuration {
		
	// Meta stuff
	public Language getLanguage();
	public String getLanguageCode();
	
	// System config
	public EmailAddress getModeratorEmailAddress();
	public EmailAddress getAdministratorEmailAddress();
	public EmailAddress getAuditEmailAddress();

	// Page template
	public String getPageTitle();
	public String getPageHeading();
	public String getPageFooter(PhotoSize ss);
	public String getPageMission();
	
	// Page menu
	public String getGuestMenu();
	public String getUserMenu();
	public String getModeratorMenu();
	public String getAdministratorMenu();
	public String getCommunityMenu();

	// General error messages
	public String getIllegalArgumentError();
	public String getIllegalAccessError();
	public String getInternalProcessingError();
	public String getFieldIsMissing();
	public String getInputIsInvalid();
	public String getInputIsTooLong();
	public String getEmailAddressIsMissing();
	public String getEmailAddressIsInvalid();
	public String getUrlIsInvalid();

	// General messages
	public String getKeepGoing();
	public String getContinueWithTellFriends();
	public String getContinueWithShowPhoto();
	public String getContinueWithShowUserHome();
	
	// SHOW_NOTE_PAGE
	public String getThankYou();
	public String getInformation();
	
	// SHOW_PHOTO_PAGE
	public String getAnonUserName();
	public String getResetSession();
	
	// TELL_FRIEND_FORM
	public String getEmailWasSent();

	// FLAG_PHOTO_FORM
	public String getModeratorWasInformed();
	
	// SEND_EMAIL_FORM
	public String getNeedToSignupFirst();
	
	// SET_OPTIONS_FORM
	public String getOptionsWereSet();

	// SIGNUP_FORM
	public String getUserAlreadyExists();
	public String getUserNameIsReserved();
	public String getPasswordsDontMatch();
	public String getDidntCheckTerms();
	public String getConfirmationEmailWasSent();

	// VERIFY_ACCOUNT_PAGE
	public String getNeedToLoginFirst();
	public String getConfirmAccountSucceeded();
	public String getConfirmAccountFailed();

	// LOGIN_FORM
	public String getLoginIsIncorrect();
	public String getUserIsDisabled();
	
	// EMAIL_USER_NAME_FORM
	public String getUnknownEmailAddress();
	public String getSendUserNameEmailSubject();
	public String getUserNameWasEmailed();

	// EMAIL_PASSWORD_FORM
	public String getUserNameIsUnknown();
	public String getSendPasswordEmailSubject();
	public String getPasswordWasEmailed();
	
	// SET_LANGUAGE_PAGE
	public String getNewLanguageSet();

	// SET_PHOTO_SIZE_PAGE
	public String getNewPhotoSizeSet(PhotoSize ss);
	public String getNoteMaximumPhotoSize();

	// SHOW_USER_PROFILE_FORM
	public String getNoPhotoUploaded();
	
	// SHOW_USER_PHOTO_FORM
	public String getNoCharacterName();
	public String getNoSeriesName();
	public String getNoTags();

	// EDIT_USER_PROFILE_FORM
	public String getProfileUpdateSucceeded();

	// CHANGE_PASSWORD_FORM
	public String getPasswordChangeSucceeded();

	// EDIT_USER_PHOTO_FORM
	public String getPhotoUpdateSucceeded();
	
	// UPLOAD_PHOTO_FORM
	public String getPhotoUploadFailed();
	public String getPhotoUploadSucceeded();

	// LOGOUT_PAGE
	public String getLogoutSucceeded();

	// SHOW_PHOTO_CASE_FORM
	public String getNoFlaggedPhotoCases();

	// SHOW_ADMIN_MENU_FORM
	public String getPhotoIsUnknown();
	
	// General email
	public String getGeneralEmailRegards();
	public String getGeneralEmailFooter();
	
	// Tell friend email
	public String getTellFriendEmailSubject();
	public String getTellFriendEmailWebsite();
	public String getTellFriendEmailPhoto();

	// Send email
	public String getSendEmailSubjectPrefix();
	public String getSendEmailBodyPrefix();
	public String getSendEmailBodyPostfix();
	
	// Welcome email
	public String getWelcomeEmailSubject();
	public String getWelcomeEmailBody();
	public String getWelcomeEmailUserName();
	
	// Request confirmation email
	public String getConfirmAccountEmailSubject();
	public String getConfirmAccountEmailBody();

	// Notify about praise email
	public String getNotifyAboutPraiseEmailSubject();
	public String getNotifyAboutPraiseEmailBody();
	public String getNotifyAboutPraiseEmailPostScriptum();

	// Data types
	public String asValueString(EnumValue ev);

	// Localisation
	public String asYesOrNoString(boolean yesOrNo);
	public String asDateString(long millis);
	public String asPhotoSummary(String un);
	public String asPhotoCaption(String un, URL url);
	public String asPraiseString(double praise);

}
