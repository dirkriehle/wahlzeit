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

import java.io.*;
import java.text.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * A generic implementation of ModelConfig.
 * Subclasses provide the parameters and language-specific handling of text and data.
 * 
 * @author dirkriehle
 *
 */
public abstract class AbstractModelConfig extends AbstractConfig implements ModelConfig {
	
	/**
	 * 
	 */
	protected Language language = Language.ENGLISH;
	protected DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	protected DecimalFormat praiseFormatter = new DecimalFormat("##.#");
	
	/**
	 * 
	 */
	protected AbstractModelConfig() {
		// do nothing
	}
	
	/**
	 * 
	 */
	protected void initialize(Language myLanguage, DateFormat myDateFormatter, DecimalFormat myPraiseFormatter) {
		language = myLanguage;
		dateFormatter = myDateFormatter;
		praiseFormatter = myPraiseFormatter;
		
		try {
			String basicFileName = FileUtil.getTmplFileName(language, "ModelConfig.properties");
			loadProperties(basicFileName);

			String customFileName = FileUtil.getTmplFileName(language, "CustomModelConfig.properties");
			File customFile = new File(customFileName);
			if (customFile.exists()) {
				loadProperties(customFile);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace(); //@FIXME
		}
		
		String menuDash = "&nbsp;" + doGetValueAsString("MenuDash") + "&nbsp;";

		String footerCommunityPart = doGetValueAsString("FooterCommunityPart");
		String footerAboutPart = doGetValueAsString("FooterAboutPart");
		String footerLanguagePart = doGetValueAsString("FooterLanguagePart");
		String footerPhotoSizePart0 = doGetValueAsString("FooterPhotoSizePart0");
		String footerPhotoSizePart1 = doGetValueAsString("FooterPhotoSizePart1");
		String footerPhotoSizePart2 = doGetValueAsString("FooterPhotoSizePart2");
		String footerPhotoSizePart3 = doGetValueAsString("FooterPhotoSizePart3");
		String footerPhotoSizePart4 = doGetValueAsString("FooterPhotoSizePart4");
		String footerDebugPart = SysLog.isInDevelopmentMode() ? menuDash + doGetValueAsString("FooterDebugPart") : "";
		
		doSetValue("PageFooter0", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart0 + footerDebugPart);
		doSetValue("PageFooter1", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart1 + footerDebugPart);
		doSetValue("PageFooter2", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart2 + footerDebugPart);
		doSetValue("PageFooter3", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart3 + footerDebugPart);
		doSetValue("PageFooter4", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart4 + footerDebugPart);

		String baseMenu = doGetValueAsString("BaseMenuPart");
		// there is no separate base menu
		
		String guestMenu = baseMenu + menuDash + doGetValueAsString("GuestMenuPart");
		doSetValue("GuestMenu", guestMenu);
		
		String userMenu = guestMenu + menuDash + doGetValueAsString("UserMenuPart");
		doSetValue("UserMenu", userMenu);

		String moderatorMenu = userMenu + menuDash + doGetValueAsString("ModeratorMenuPart");
		doSetValue("ModeratorMenu", moderatorMenu);

		String administratorMenu = moderatorMenu + menuDash + doGetValueAsString("AdministratorMenuPart");
		doSetValue("AdministratorMenu", administratorMenu);
	}

	/**
	 * FIXME
	 */
	public boolean isRegularUserName(String un) {
		return !un.equals("admin") && !un.equals("anonymous") && !un.equals("flickr");
	}
	
	/**
	 * 
	 */
	public Language getLanguage() {
		return language;
	}
	
	/**
	 * 
	 */
	public String getLanguageCode() {
		return doGetValueAsString("LanguageCode");
	}
	
	/**
	 * 
	 */
	public EmailAddress getModeratorEmailAddress() {
		return EmailAddress.getFromString(doGetValueAsString("ModeratorEmailAddress"));
	}
	
	/**
	 * 
	 */
	public EmailAddress getAdministratorEmailAddress() {
		return EmailAddress.getFromString(doGetValueAsString("AdministratorEmailAddress"));
	}

	/**
	 * 
	 */
	public EmailAddress getAuditEmailAddress() {
		return EmailAddress.getFromString(doGetValueAsString("AuditEmailAddress"));
	}

	/**
	 * 
	 */
	public String getPageTitle() {
		return doGetValueAsString("PageTitle");
	}
	
	/**
	 * 
	 */
	public String getPageHeading() {
		return doGetValueAsString("PageHeading"); 
	}
	
	/**
	 * 
	 */
	public String getPageFooter(PhotoSize ss) {
		return doGetValueAsString("PageFooter" + (ss.asInt() - 1));
	}
	
	/**
	 * 
	 */
	public String getPageMission() {
		return doGetValueAsString("PageMission");
	}

	/**
	 * 
	 */
	public String getGuestMenu() {
		return doGetValueAsString("GuestMenu"); 		
	}

	/**
	 * 
	 */
	public String getUserMenu(){
		return doGetValueAsString("UserMenu"); 		
	}

	/**
	 * 
	 */
	public String getModeratorMenu() {
		return doGetValueAsString("ModeratorMenu"); 		
	}

	/**
	 * 
	 */
	public String getAdministratorMenu() {
		return doGetValueAsString("AdministratorMenu"); 			
	}

	/**
	 * 
	 */
	public String getCommunityMenu() {
		return doGetValueAsString("CommunityMenu"); 			
	}

	/**
	 * 
	 */
	public String getIllegalArgumentError() {
		return doGetValueAsString("IllegalArgumentError"); 
	}

	/**
	 * 
	 */
	public String getIllegalAccessError() {
		return doGetValueAsString("IllegalAccessError"); 
	}

	/**
	 * 
	 */
	public String getInternalProcessingError() {
		return doGetValueAsString("InternalProcessingError"); 
	}

	/**
	 * 
	 */
	public String getFieldIsMissing() {
		return doGetValueAsString("FieldIsMissing"); 
	}

	/**
	 * 
	 */
	public String getInputIsInvalid() {
		return doGetValueAsString("InputIsInvalid"); 
	}

	/**
	 * 
	 */
	public String getInputIsTooLong() {
		return doGetValueAsString("InputIsTooLong"); 
	}

	/**
	 * 
	 */
	public String getEmailAddressIsInvalid() {
		return doGetValueAsString("EmailAddressIsInvalid"); 
	}

	/**
	 * 
	 */
	public String getEmailAddressIsMissing() {
		return doGetValueAsString("EmailAddressIsMissing"); 
	}

	/**
	 * 
	 */
	public String getUrlIsInvalid() {
		return doGetValueAsString("UrlIsInvalid"); 
	}

	/**
	 * 
	 */
	public String getKeepGoing() { 
		return doGetValueAsString("KeepGoing"); 
	}

	/**
	 * 
	 */
	public String getContinueWithShowPhoto() { 
		return doGetValueAsString("ContinueWithShowPhoto"); 
	}

	/**
	 * 
	 */
	public String getContinueWithShowUserHome() { 
		return doGetValueAsString("ContinueWithShowUserHome");
	}

	/**
	 * 
	 */
	public String getContinueWithTellFriends() {
		return doGetValueAsString("ContinueWithTellFriends"); 
	}
	
	/**
	 * 
	 */
	public String getThankYou() {
		return doGetValueAsString("ThankYou");
	}

	/**
	 * 
	 */
	public String getInformation() {
		return doGetValueAsString("Information");
	}

	/**
	 * 
	 */
	public String getAnonUserName() {
		return doGetValueAsString("AnonUserName"); 
	}

	/**
	 * 
	 */
	public String getResetSession() {
		return doGetValueAsString("ResetSession"); 
	}
	
	/**
	 * 
	 */
	public String getEmailWasSent() {
		return doGetValueAsString("EmailWasSent"); 
	}

	/**
	 * 
	 */
	public String getModeratorWasInformed() {
		return doGetValueAsString("ModeratorWasInformed"); 
	}

	/**
	 * 
	 */
	public String getOptionsWereSet() {
		return doGetValueAsString("OptionsWereSet"); 
	}

	/**
	 * 
	 */
	public String getNeedToSignupFirst() {
		return doGetValueAsString("NeedToSignupFirst"); 
	}

	/**
	 * 
	 */
	public String getUserAlreadyExists() {
		return doGetValueAsString("UserAlreadyExists"); 
	}

	/**
	 * 
	 */
	public String getPasswordsDontMatch() {
		return doGetValueAsString("PasswordsDontMatch"); 
	}

	/**
	 * 
	 */
	public String getDidntCheckTerms() {
		return doGetValueAsString("DidntCheckTerms"); 
	}

	/**
	 * 
	 */
	public String getConfirmationEmailWasSent() {
		return doGetValueAsString("ConfirmationEmailWasSent"); 
	}

	/**
	 * 
	 */
	public String getNeedToLoginFirst() {
		return doGetValueAsString("NeedToLoginFirst"); 
	}

	/**
	 * 
	 */
	public String getConfirmAccountSucceeded() {
		return doGetValueAsString("ConfirmAccountSucceeded"); 
	}

	/**
	 * 
	 */
	public String getConfirmAccountFailed() {
		return doGetValueAsString("ConfirmAccountFailed"); 
	}

	/**
	 * 
	 */
	public String getLoginIsIncorrect() {
		return doGetValueAsString("LoginIsIncorrect"); 
	}

	/**
	 * 
	 */
	public String getUserIsDisabled() {
		return doGetValueAsString("UserIsDisabled"); 
	}

	/**
	 * 
	 */
	public String getUnknownEmailAddress() {
		return doGetValueAsString("EmailAddressIsUnknown"); 
	}

	/**
	 * 
	 */
	public String getUserNameWasEmailed() {
		return doGetValueAsString("UserNameWasEmailed"); 
	}

	/**
	 * 
	 */
	public String getSendUserNameEmailSubject() {
		return doGetValueAsString("EmailUserNameSubject"); 
	}

	/**
	 * 
	 */
	public String getUserNameIsUnknown() {
		return doGetValueAsString("UserNameIsUnknown"); 
	}

	/**
	 * 
	 */
	public String getPasswordWasEmailed() {
		return doGetValueAsString("PasswordWasEmailed"); 
	}

	/**
	 * 
	 */
	public String getSendPasswordEmailSubject() {
		return doGetValueAsString("EmailPasswordSubject"); 
	}

	/**
	 * 
	 */
	public String getNewLanguageSet() {
		return doGetValueAsString("NewLanguageSet"); 
	}

	/**
	 * 
	 */
	public String getNoteMaximumPhotoSize() {
		return doGetValueAsString("NoteMaximumPhotoSize"); 
	}

	/**
	 * 
	 */
	public String getNoPhotoUploaded() {
		return doGetValueAsString("NoPhotoUploaded"); 
	}

	/**
	 * 
	 */
	public String getNoCharacterName() {
		return doGetValueAsString("NoCharacterName"); 
	}

	/**
	 * 
	 */
	public String getNoSeriesName() {
		return doGetValueAsString("NoSeriesName"); 
	}

	/**
	 * 
	 */
	public String getNoTags() {
		return doGetValueAsString("NoTags"); 
	}

	/**
	 * 
	 */
	public String getProfileUpdateSucceeded() {
		return doGetValueAsString("ProfileUpdateSucceeded"); 
	}

	/**
	 * 
	 */
	public String getPasswordChangeSucceeded() {
		return doGetValueAsString("PasswordChangeSucceeded"); 
	}

	/**
	 * 
	 */
	public String getPhotoUpdateSucceeded() {
		return doGetValueAsString("PhotoUpdateSucceeded"); 
	}

	/**
	 * 
	 */
	public String getPhotoUploadFailed() { 
		return doGetValueAsString("PhotoUploadFailed"); 
	}

	/**
	 * 
	 */
	public String getPhotoUploadSucceeded() { 
		return doGetValueAsString("PhotoUploadSucceeded"); 
	}

	/**
	 * 
	 */
	public String getLogoutSucceeded() { 
		return doGetValueAsString("LogoutSucceeded"); 
	}

	/**
	 * 
	 */
	public String getNoFlaggedPhotoCases() {
		return doGetValueAsString("NoFlaggedPhotoCases"); 
	}

	/**
	 * 
	 */
	public String getPhotoIsUnknown() {
		return doGetValueAsString("UnknownPhoto"); 
	}
	
	/**
	 * 
	 */
	public String getGeneralEmailRegards() {
		return doGetValueAsString("GeneralEmailRegards");
	}

	/**
	 * 
	 */
	public String getGeneralEmailFooter() {
		return doGetValueAsString("GeneralEmailFooter");
	}

	/**
	 * 
	 */
	public String getTellFriendEmailSubject() {
		return doGetValueAsString("TellFriendEmailSubject");
	}

	/**
	 * 
	 */
	public String getTellFriendEmailWebsite() {
		return doGetValueAsString("TellFriendEmailSite");
	}

	/**
	 * 
	 */
	public String getTellFriendEmailPhoto() {
		return doGetValueAsString("TellFriendEmailPhoto"); 
	}

	/**
	 * 
	 */
	public String getSendEmailSubjectPrefix() {
		return doGetValueAsString("SendEmailSubjectPrefix"); 
	}

	/**
	 * 
	 */
	public String getSendEmailBodyPrefix() {
		return doGetValueAsString("SendEmailBodyPrefix"); 
	}
	
	/**
	 * 
	 */
	public String getSendEmailBodyPostfix() {
		return doGetValueAsString("SendEmailBodyPostfix"); 
	}
	
	/**
	 * 
	 */
	public String getWelcomeEmailSubject() {
		return doGetValueAsString("WelcomeEmailSubject");
	}
	
	/**
	 * 
	 */
	public String getWelcomeEmailBody() {
		return doGetValueAsString("WelcomeEmailBody");
	}
	
	/**
	 * 
	 */
	public String getWelcomeEmailUserName() {
		return doGetValueAsString("WelcomeEmailUserName");
	}
	
	/**
	 * 
	 */
	public String getConfirmAccountEmailSubject() {
		return doGetValueAsString("ConfirmAccountEmailSubject");
	}

	/**
	 * 
	 */
	public String getConfirmAccountEmailBody() {
		return doGetValueAsString("ConfirmAccountEmailBody");
	}

	/**
	 * 
	 */
	public String getNotifyAboutPraiseEmailSubject() {
		return doGetValueAsString("NotifyAboutPraiseEmailSubject");
	}

	/**
	 * 
	 */
	public String getNotifyAboutPraiseEmailBody() {
		return doGetValueAsString("NotifyAboutPraiseEmailBody");
	}

	/**
	 * 
	 */
	public String getNotifyAboutPraiseEmailPostScriptum() {
		return doGetValueAsString("NotifyAboutPraiseEmailPostScriptum"); 
	}

	/**
	 * 
	 */
	public String asValueString(EnumValue ev) {
		return doGetValueAsString(ev.getTypeName() + "#" + ev.asInt());
	}
	
	/**
	 * 
	 */
	public String asDateString(long millis) {
		return dateFormatter.format(millis);
	}
	
	/**
	 * 
	 */
	public String getPraise(double praise) {
		return praiseFormatter.format(praise);
	}
	
}
