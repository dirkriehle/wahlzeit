/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

/**
 * A utility class for web parts.
 */
public interface PartUtil {
	
	public static final String DEFAULT_PAGE_NAME = "index";
	
	public static final String NULL_INFO_NAME = "nullInfo";
	public static final String NULL_INFO_FILE = "infos/NullInfo";
	public static final String NULL_FORM_NAME = "nullForm";
	public static final String NULL_FORM_FILE = "forms/NullForm";
		
	public static final String SHOW_INFO_PAGE_FILE = "pages/ShowInfoPage";
	public static final String SHOW_PART_PAGE_FILE = "pages/ShowPartPage";

	public static final String SHOW_NOTE_PAGE_NAME = "note";
	public static final String SHOW_NOTE_PAGE_FILE = "pages/ShowNotePage";
	
	public static final String SHOW_PHOTO_PAGE_NAME = "index";
	public static final String SHOW_PHOTO_PAGE_FILE = "pages/ShowPhotoPage";
	public static final String CAPTION_INFO_FILE = "infos/CaptionInfo";
	public static final String BLURP_INFO_FILE = "infos/BlurpInfo";
	public static final String PHOTO_INFO_FILE = "infos/PhotoInfo";
	public static final String LINKS_INFO_FILE = "infos/LinksInfo";
	public static final String BANNER_INFO_FILE = "infos/BannerInfo";
	public static final String FILTER_PHOTOS_FORM_NAME = "filterPhotosForm";
	public static final String FILTER_PHOTOS_FORM_FILE = "forms/FilterPhotosForm";
	public static final String PRAISE_PHOTO_FORM_NAME = "praisePhotoForm";
	public static final String PRAISE_PHOTO_FORM_FILE = "forms/PraisePhotoForm";
	public static final String ENGAGE_GUEST_FORM_NAME = "engageGuestForm";
	public static final String ENGAGE_GUEST_FORM_FILE = "forms/EngageGuestForm";

	public static final String ABOUT_PAGE_NAME = "about";
	public static final String ABOUT_INFO_FILE = "infos/AboutInfo";
	
	public static final String CONTACT_PAGE_NAME = "contact";
	public static final String CONTACT_INFO_FILE = "infos/ContactInfo";
	
	public static final String IMPRINT_PAGE_NAME = "imprint";
	public static final String IMPRINT_INFO_FILE = "infos/ImprintInfo";
	
	public static final String TERMS_PAGE_NAME = "terms";
	public static final String TERMS_INFO_FILE = "infos/TermsInfo";

	public static final String HIDDEN_INFO_FILE = "infos/HiddenInfo";
	public static final String DONE_INFO_FILE = "infos/DoneInfo";

	public static final String FILTER_PHOTOS_PAGE_NAME = "filter";

	public static final String FLAG_PHOTO_PAGE_NAME = "flag";
	public static final String FLAG_PHOTO_FORM_NAME = "flagPhotoForm";
	public static final String FLAG_PHOTO_FORM_FILE = "forms/FlagPhotoForm";
	
	public static final String TELL_FRIEND_PAGE_NAME = "tell";
	public static final String TELL_FRIEND_FORM_NAME = "tellFriendForm";
	public static final String TELL_FRIEND_FORM_FILE = "forms/TellFriendForm";
	
	public static final String SEND_EMAIL_PAGE_NAME = "connect";
	public static final String SEND_EMAIL_FORM_NAME = "sendEmailForm";
	public static final String SEND_EMAIL_FORM_FILE = "forms/SendEmailForm";
	
	public static final String SET_OPTIONS_PAGE_NAME = "options";
	public static final String SET_OPTIONS_FORM_NAME = "setOptionsForm";
	public static final String SET_OPTIONS_FORM_FILE = "forms/SetOptionsForm";
	
	public static final String SIGNUP_PAGE_NAME = "signup";
	public static final String SIGNUP_FORM_NAME = "signupForm";
	public static final String SIGNUP_FORM_FILE = "forms/SignupForm";
	
	public static final String CONFIRM_ACCOUNT_PAGE_NAME = "confirm";
	
	public static final String LOGIN_PAGE_NAME = "login";
	public static final String LOGIN_FORM_NAME = "loginForm";
	public static final String LOGIN_FORM_FILE = "forms/LoginForm";
	
	public static final String EMAIL_USER_NAME_PAGE_NAME = "emailun";
	public static final String EMAIL_USER_NAME_FORM_NAME = "emailUserNameForm";
	public static final String EMAIL_USER_NAME_FORM_FILE = "forms/EmailUserNameForm";

	public static final String EMAIL_PASSWORD_PAGE_NAME = "emailpw";
	public static final String EMAIL_PASSWORD_FORM_NAME = "emailPasswordForm";
	public static final String EMAIL_PASSWORD_FORM_FILE = "forms/EmailPasswordForm";

	public static final String RESET_SESSION_PAGE_NAME = "reset";
	
	public static final String LOGOUT_PAGE_NAME = "logout";
	
	public static final String SET_ENGLISH_LANGUAGE_PAGE_NAME = "langen";
	public static final String SET_GERMAN_LANGUAGE_PAGE_NAME = "langde";
	public static final String SET_SPANISH_LANGUAGE_PAGE_NAME = "langes";
	public static final String SET_JAPANESE_LANGUAGE_PAGE_NAME = "langja";

	public static final String SET_EXTRA_SMALL_PHOTO_SIZE_PAGE_NAME = "psizexs";
	public static final String SET_SMALL_PHOTO_SIZE_PAGE_NAME = "psizes";
	public static final String SET_MEDIUM_PHOTO_SIZE_PAGE_NAME = "psizem";
	public static final String SET_LARGE_PHOTO_SIZE_PAGE_NAME = "psizel";
	public static final String SET_EXTRA_LARGE_PHOTO_SIZE_PAGE_NAME = "psizexl";
	
	public static final String SHOW_USER_HOME_PAGE_NAME = "home";
	public static final String SHOW_USER_HOME_PAGE_FILE = "pages/ShowUserHomePage";
	public static final String SHOW_USER_PROFILE_FORM_NAME = "showUserProfileForm";
	public static final String SHOW_USER_PROFILE_FORM_FILE = "forms/ShowUserProfileForm";
	public static final String SHOW_USER_PHOTO_FORM_NAME = "showUserPhotoForm";
	public static final String SHOW_USER_PHOTO_FORM_FILE = "forms/ShowUserPhotoForm";
	
	public static final String EDIT_USER_PROFILE_PAGE_NAME = "profile";
	public static final String EDIT_USER_PROFILE_FORM_NAME = "editUserProfileForm";
	public static final String EDIT_USER_PROFILE_FORM_FILE = "forms/EditUserProfileForm";
	
	public static final String CHANGE_PASSWORD_PAGE_NAME = "password";
	public static final String CHANGE_PASSWORD_FORM_NAME = "changePasswordForm";
	public static final String CHANGE_PASSWORD_FORM_FILE = "forms/ChangePasswordForm";
	
	public static final String UPLOAD_PHOTO_PAGE_NAME = "upload";
	public static final String UPLOAD_PHOTO_FORM_NAME = "uploadPhotoForm";
	public static final String UPLOAD_PHOTO_FORM_FILE = "forms/UploadPhotoForm";
	
	public static final String EDIT_USER_PHOTO_PAGE_NAME = "photo";
	public static final String EDIT_USER_PHOTO_FORM_NAME = "editUserPhotoForm";
	public static final String EDIT_USER_PHOTO_FORM_FILE = "forms/EditUserPhotoForm";
	
	public static final String SHOW_PHOTO_CASES_PAGE_NAME = "cases";
	public static final String SHOW_PHOTO_CASES_PAGE_FILE = "pages/ShowPhotoCasesPage";
	public static final String EDIT_PHOTO_CASE_FORM_NAME = "editPhotoCaseForm";
	public static final String EDIT_PHOTO_CASE_FORM_FILE = "forms/EditPhotoCaseForm";
	
	public static final String SHOW_ADMIN_PAGE_NAME = "admin";
	public static final String SHOW_ADMIN_PAGE_FILE = "pages/ShowAdminPage";
	public static final String SHOW_ADMIN_MENU_FORM_NAME = "showAdminMenuForm";
	public static final String ADMIN_USER_PROFILE_FORM_NAME = "adminUserProfileForm";
	public static final String ADMIN_USER_PROFILE_FORM_FILE = "forms/AdminUserProfileForm";
	public static final String ADMIN_USER_PHOTO_FORM_NAME = "adminUserPhotoForm";
	public static final String ADMIN_USER_PHOTO_FORM_FILE = "forms/AdminUserPhotoForm";
	
}
