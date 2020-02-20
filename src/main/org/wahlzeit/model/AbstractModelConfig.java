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

import org.wahlzeit.main.*;
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
			ConfigDir templatesDir = SysConfig.getTemplatesDir();
			
			String shortDefaultFileName = myLanguage.asIsoCode() + File.separator + "ModelConfig.properties";
			if(templatesDir.hasDefaultFile(shortDefaultFileName)) {
				String absoluteDefaultFileName = templatesDir.getAbsoluteDefaultConfigFileName(shortDefaultFileName);
				loadProperties(absoluteDefaultFileName);
			}
			
			String shortCustomFileName = myLanguage.asIsoCode() + File.separator + "CustomModelConfig.properties";
			if(templatesDir.hasCustomFile(shortCustomFileName)) {
				String absoluteCustomFileName = templatesDir.getAbsoluteCustomConfigFileName(shortCustomFileName);
				loadProperties(absoluteCustomFileName);
			}
		} catch (IOException ioex) {
			SysLog.logThrowable(ioex);
		}
		
		String menuDash = "&nbsp;" + doGetValue("MenuDash") + "&nbsp;";

		String footerCommunityPart = doGetValue("FooterCommunityPart");
		String footerAboutPart = doGetValue("FooterAboutPart");
		String footerLanguagePart = doGetValue("FooterLanguagePart");
		String footerPhotoSizePart0 = doGetValue("FooterPhotoSizePart0");
		String footerPhotoSizePart1 = doGetValue("FooterPhotoSizePart1");
		String footerPhotoSizePart2 = doGetValue("FooterPhotoSizePart2");
		String footerPhotoSizePart3 = doGetValue("FooterPhotoSizePart3");
		String footerPhotoSizePart4 = doGetValue("FooterPhotoSizePart4");
		
		boolean isInProduction = ServiceMain.getInstance().isInProduction();
		String footerDebugPart = !isInProduction ? menuDash + doGetValue("FooterDebugPart") : "";
		
		doSetValue("PageFooter0", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart0 + footerDebugPart);
		doSetValue("PageFooter1", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart1 + footerDebugPart);
		doSetValue("PageFooter2", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart2 + footerDebugPart);
		doSetValue("PageFooter3", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart3 + footerDebugPart);
		doSetValue("PageFooter4", footerCommunityPart + menuDash + footerAboutPart + menuDash + footerLanguagePart + menuDash + footerPhotoSizePart4 + footerDebugPart);

		String baseMenu = doGetValue("BaseMenuPart");
		// there is no separate base menu
		
		String guestMenu = baseMenu + menuDash + doGetValue("GuestMenuPart");
		doSetValue("GuestMenu", guestMenu);
		
		String userMenu = guestMenu + menuDash + doGetValue("UserMenuPart");
		doSetValue("UserMenu", userMenu);

		String moderatorMenu = userMenu + menuDash + doGetValue("ModeratorMenuPart");
		doSetValue("ModeratorMenu", moderatorMenu);

		String administratorMenu = moderatorMenu + menuDash + doGetValue("AdministratorMenuPart");
		doSetValue("AdministratorMenu", administratorMenu);
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
		return doGetValue("LanguageCode");
	}
	
	/**
	 * 
	 */
	public EmailAddress getModeratorEmailAddress() {
		return EmailAddress.getFromString(doGetValue("ModeratorEmailAddress"));
	}
	
	/**
	 * 
	 */
	public EmailAddress getAdministratorEmailAddress() {
		return EmailAddress.getFromString(doGetValue("AdministratorEmailAddress"));
	}

	/**
	 * 
	 */
	public EmailAddress getAuditEmailAddress() {
		return EmailAddress.getFromString(doGetValue("AuditEmailAddress"));
	}

	/**
	 * 
	 */
	public String getPageTitle() {
		return doGetValue("PageTitle");
	}
	
	/**
	 * 
	 */
	public String getPageHeading() {
		return doGetValue("PageHeading"); 
	}
	
	/**
	 * 
	 */
	public String getPageFooter(PhotoSize ss) {
		return doGetValue("PageFooter" + (ss.asInt() - 1));
	}
	
	/**
	 * 
	 */
	public String getPageMission() {
		return doGetValue("PageMission");
	}

	/**
	 * 
	 */
	public String getGuestMenu() {
		return doGetValue("GuestMenu"); 		
	}

	/**
	 * 
	 */
	public String getUserMenu(){
		return doGetValue("UserMenu"); 		
	}

	/**
	 * 
	 */
	public String getModeratorMenu() {
		return doGetValue("ModeratorMenu"); 		
	}

	/**
	 * 
	 */
	public String getAdministratorMenu() {
		return doGetValue("AdministratorMenu"); 			
	}

	/**
	 * 
	 */
	public String getCommunityMenu() {
		return doGetValue("CommunityMenu"); 			
	}

	/**
	 * 
	 */
	public String getIllegalArgumentError() {
		return doGetValue("IllegalArgumentError"); 
	}

	/**
	 * 
	 */
	public String getIllegalAccessError() {
		return doGetValue("IllegalAccessError"); 
	}

	/**
	 * 
	 */
	public String getInternalProcessingError() {
		return doGetValue("InternalProcessingError"); 
	}

	/**
	 * 
	 */
	public String getFieldIsMissing() {
		return doGetValue("FieldIsMissing"); 
	}

	/**
	 * 
	 */
	public String getInputIsInvalid() {
		return doGetValue("InputIsInvalid"); 
	}

	/**
	 * 
	 */
	public String getInputIsTooLong() {
		return doGetValue("InputIsTooLong"); 
	}

	/**
	 * 
	 */
	public String getEmailAddressIsInvalid() {
		return doGetValue("EmailAddressIsInvalid"); 
	}

	/**
	 * 
	 */
	public String getEmailAddressIsMissing() {
		return doGetValue("EmailAddressIsMissing"); 
	}

	/**
	 * 
	 */
	public String getUrlIsInvalid() {
		return doGetValue("UrlIsInvalid"); 
	}

	/**
	 * 
	 */
	public String getKeepGoing() { 
		return doGetValue("KeepGoing"); 
	}

	/**
	 * 
	 */
	public String getContinueWithShowPhoto() { 
		return doGetValue("ContinueWithShowPhoto"); 
	}

	/**
	 * 
	 */
	public String getContinueWithShowUserHome() { 
		return doGetValue("ContinueWithShowUserHome");
	}

	/**
	 * 
	 */
	public String getContinueWithTellFriends() {
		return doGetValue("ContinueWithTellFriends"); 
	}
	
	/**
	 * 
	 */
	public String getThankYou() {
		return doGetValue("ThankYou");
	}

	/**
	 * 
	 */
	public String getInformation() {
		return doGetValue("Information");
	}

	/**
	 * 
	 */
	public String getAnonUserName() {
		return doGetValue("AnonUserName"); 
	}

	/**
	 * 
	 */
	public String getResetSession() {
		return doGetValue("ResetSession"); 
	}
	
	/**
	 * 
	 */
	public String getEmailWasSent() {
		return doGetValue("EmailWasSent"); 
	}

	/**
	 * 
	 */
	public String getModeratorWasInformed() {
		return doGetValue("ModeratorWasInformed"); 
	}

	/**
	 * 
	 */
	public String getOptionsWereSet() {
		return doGetValue("OptionsWereSet"); 
	}

	/**
	 * 
	 */
	public String getNeedToSignupFirst() {
		return doGetValue("NeedToSignupFirst"); 
	}

	/**
	 * 
	 */
	public String getUserAlreadyExists() {
		return doGetValue("UserAlreadyExists"); 
	}

	/**
	 * 
	 */
	public String getPasswordsDontMatch() {
		return doGetValue("PasswordsDontMatch"); 
	}

	/**
	 * 
	 */
	public String getDidntCheckTerms() {
		return doGetValue("DidntCheckTerms"); 
	}

	/**
	 * 
	 */
	public String getConfirmationEmailWasSent() {
		return doGetValue("ConfirmationEmailWasSent"); 
	}

	/**
	 * 
	 */
	public String getNeedToLoginFirst() {
		return doGetValue("NeedToLoginFirst"); 
	}

	/**
	 * 
	 */
	public String getConfirmAccountSucceeded() {
		return doGetValue("ConfirmAccountSucceeded"); 
	}

	/**
	 * 
	 */
	public String getConfirmAccountFailed() {
		return doGetValue("ConfirmAccountFailed"); 
	}

	/**
	 * 
	 */
	public String getLoginIsIncorrect() {
		return doGetValue("LoginIsIncorrect"); 
	}

	/**
	 * 
	 */
	public String getUserIsDisabled() {
		return doGetValue("UserIsDisabled"); 
	}

	/**
	 * 
	 */
	public String getUnknownEmailAddress() {
		return doGetValue("EmailAddressIsUnknown"); 
	}

	/**
	 * 
	 */
	public String getUserNameWasEmailed() {
		return doGetValue("UserNameWasEmailed"); 
	}

	/**
	 * 
	 */
	public String getSendUserNameEmailSubject() {
		return doGetValue("EmailUserNameSubject"); 
	}

	/**
	 * 
	 */
	public String getUserNameIsUnknown() {
		return doGetValue("UserNameIsUnknown"); 
	}

	/**
	 * 
	 */
	public String getUserNameIsReserved() {
		return doGetValue("UserNameIsReserved"); 
	}

	/**
	 * 
	 */
	public String getPasswordWasEmailed() {
		return doGetValue("PasswordWasEmailed"); 
	}

	/**
	 * 
	 */
	public String getSendPasswordEmailSubject() {
		return doGetValue("EmailPasswordSubject"); 
	}

	/**
	 * 
	 */
	public String getNewLanguageSet() {
		return doGetValue("NewLanguageSet"); 
	}

	/**
	 * 
	 */
	public String getNoteMaximumPhotoSize() {
		return doGetValue("NoteMaximumPhotoSize"); 
	}

	/**
	 * 
	 */
	public String getNoPhotoUploaded() {
		return doGetValue("NoPhotoUploaded"); 
	}

	/**
	 * 
	 */
	public String getNoCharacterName() {
		return doGetValue("NoCharacterName"); 
	}

	/**
	 * 
	 */
	public String getNoSeriesName() {
		return doGetValue("NoSeriesName"); 
	}

	/**
	 * 
	 */
	public String getNoTags() {
		return doGetValue("NoTags"); 
	}

	/**
	 * 
	 */
	public String getProfileUpdateSucceeded() {
		return doGetValue("ProfileUpdateSucceeded"); 
	}

	/**
	 * 
	 */
	public String getPasswordChangeSucceeded() {
		return doGetValue("PasswordChangeSucceeded"); 
	}

	/**
	 * 
	 */
	public String getPhotoUpdateSucceeded() {
		return doGetValue("PhotoUpdateSucceeded"); 
	}

	/**
	 * 
	 */
	public String getPhotoUploadFailed() { 
		return doGetValue("PhotoUploadFailed"); 
	}

	/**
	 * 
	 */
	public String getPhotoUploadSucceeded() { 
		return doGetValue("PhotoUploadSucceeded"); 
	}

	/**
	 * 
	 */
	public String getLogoutSucceeded() { 
		return doGetValue("LogoutSucceeded"); 
	}

	/**
	 * 
	 */
	public String getNoFlaggedPhotoCases() {
		return doGetValue("NoFlaggedPhotoCases"); 
	}

	/**
	 * 
	 */
	public String getPhotoIsUnknown() {
		return doGetValue("UnknownPhoto"); 
	}
	
	/**
	 * 
	 */
	public String getGeneralEmailRegards() {
		return doGetValue("GeneralEmailRegards");
	}

	/**
	 * 
	 */
	public String getGeneralEmailFooter() {
		return doGetValue("GeneralEmailFooter");
	}

	/**
	 * 
	 */
	public String getTellFriendEmailSubject() {
		return doGetValue("TellFriendEmailSubject");
	}

	/**
	 * 
	 */
	public String getTellFriendEmailWebsite() {
		return doGetValue("TellFriendEmailSite");
	}

	/**
	 * 
	 */
	public String getTellFriendEmailPhoto() {
		return doGetValue("TellFriendEmailPhoto"); 
	}

	/**
	 * 
	 */
	public String getSendEmailSubjectPrefix() {
		return doGetValue("SendEmailSubjectPrefix"); 
	}

	/**
	 * 
	 */
	public String getSendEmailBodyPrefix() {
		return doGetValue("SendEmailBodyPrefix"); 
	}
	
	/**
	 * 
	 */
	public String getSendEmailBodyPostfix() {
		return doGetValue("SendEmailBodyPostfix"); 
	}
	
	/**
	 * 
	 */
	public String getWelcomeEmailSubject() {
		return doGetValue("WelcomeEmailSubject");
	}
	
	/**
	 * 
	 */
	public String getWelcomeEmailBody() {
		return doGetValue("WelcomeEmailBody");
	}
	
	/**
	 * 
	 */
	public String getWelcomeEmailUserName() {
		return doGetValue("WelcomeEmailUserName");
	}
	
	/**
	 * 
	 */
	public String getConfirmAccountEmailSubject() {
		return doGetValue("ConfirmAccountEmailSubject");
	}

	/**
	 * 
	 */
	public String getConfirmAccountEmailBody() {
		return doGetValue("ConfirmAccountEmailBody");
	}

	/**
	 * 
	 */
	public String getNotifyAboutPraiseEmailSubject() {
		return doGetValue("NotifyAboutPraiseEmailSubject");
	}

	/**
	 * 
	 */
	public String getNotifyAboutPraiseEmailBody() {
		return doGetValue("NotifyAboutPraiseEmailBody");
	}

	/**
	 * 
	 */
	public String getNotifyAboutPraiseEmailPostScriptum() {
		return doGetValue("NotifyAboutPraiseEmailPostScriptum"); 
	}

	/**
	 * 
	 */
	public String asValueString(EnumValue ev) {
		return doGetValue(ev.getTypeName() + "#" + ev.asInt());
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
	public String asPraiseString(double praise) {
		return praiseFormatter.format(praise);
	}
	
}
