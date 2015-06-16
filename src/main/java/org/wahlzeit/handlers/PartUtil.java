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

/**
 * @author dirkriehle
 */
public interface PartUtil {

    String DEFAULT_PAGE_NAME = "index";

    String NULL_INFO_NAME = "nullInfo";
    String NULL_INFO_FILE = "infos/NullInfo";
    String NULL_FORM_NAME = "nullForm";
    String NULL_FORM_FILE = "forms/NullForm";

    String SHOW_INFO_PAGE_FILE = "pages/ShowInfoPage";
    String SHOW_PART_PAGE_FILE = "pages/ShowPartPage";

    String SHOW_NOTE_PAGE_NAME = "note";
    String SHOW_NOTE_PAGE_FILE = "pages/ShowNotePage";

    String SHOW_PHOTO_PAGE_NAME = "index";
    String SHOW_PHOTO_PAGE_FILE = "pages/ShowPhotoPage";
    String CAPTION_INFO_FILE = "infos/CaptionInfo";
    String BLURP_INFO_FILE = "infos/BlurpInfo";
    String PHOTO_INFO_FILE = "infos/PhotoInfo";
    String LINKS_INFO_FILE = "infos/LinksInfo";
    String BANNER_INFO_FILE = "infos/BannerInfo";
    String FILTER_PHOTOS_FORM_NAME = "filterPhotosForm";
    String FILTER_PHOTOS_FORM_FILE = "forms/FilterPhotosForm";
    String PRAISE_PHOTO_FORM_NAME = "praisePhotoForm";
    String PRAISE_PHOTO_FORM_FILE = "forms/PraisePhotoForm";
    String ENGAGE_GUEST_FORM_NAME = "engageGuestForm";
    String ENGAGE_GUEST_FORM_FILE = "forms/EngageGuestForm";

    String ABOUT_PAGE_NAME = "about";
    String ABOUT_INFO_FILE = "infos/AboutInfo";

    String CONTACT_PAGE_NAME = "contact";
    String CONTACT_INFO_FILE = "infos/ContactInfo";

    String IMPRINT_PAGE_NAME = "imprint";
    String IMPRINT_INFO_FILE = "infos/ImprintInfo";

    String TERMS_PAGE_NAME = "terms";
    String TERMS_INFO_FILE = "infos/TermsInfo";

    String HIDDEN_INFO_FILE = "infos/HiddenInfo";
    String DONE_INFO_FILE = "infos/DoneInfo";

    String FILTER_PHOTOS_PAGE_NAME = "filter";

    String FLAG_PHOTO_PAGE_NAME = "flag";
    String FLAG_PHOTO_FORM_NAME = "flagPhotoForm";
    String FLAG_PHOTO_FORM_FILE = "forms/FlagPhotoForm";

    String TELL_FRIEND_PAGE_NAME = "tell";
    String TELL_FRIEND_FORM_NAME = "tellFriendForm";
    String TELL_FRIEND_FORM_FILE = "forms/TellFriendForm";

    String SEND_EMAIL_PAGE_NAME = "connect";
    String SEND_EMAIL_FORM_NAME = "sendEmailForm";
    String SEND_EMAIL_FORM_FILE = "forms/SendEmailForm";

    String SET_OPTIONS_PAGE_NAME = "options";
    String SET_OPTIONS_FORM_NAME = "setOptionsForm";
    String SET_OPTIONS_FORM_FILE = "forms/SetOptionsForm";

    String CONFIRM_ACCOUNT_PAGE_NAME = "confirm";

    String LOGIN_PAGE_NAME = "login";
    String LOGIN_FORM_NAME = "loginForm";
    String LOGIN_FORM_FILE = "forms/LoginForm";

    String EMAIL_USER_NAME_PAGE_NAME = "emailun";
    String EMAIL_USER_NAME_FORM_NAME = "emailUserNameForm";
    String EMAIL_USER_NAME_FORM_FILE = "forms/EmailUserNameForm";

    String EMAIL_PASSWORD_PAGE_NAME = "emailpw";
    String EMAIL_PASSWORD_FORM_NAME = "emailPasswordForm";
    String EMAIL_PASSWORD_FORM_FILE = "forms/EmailPasswordForm";

    String RESET_SESSION_PAGE_NAME = "reset";

    String LOGOUT_PAGE_NAME = "logout";

    String SET_ENGLISH_LANGUAGE_PAGE_NAME = "langen";
    String SET_GERMAN_LANGUAGE_PAGE_NAME = "langde";
    String SET_SPANISH_LANGUAGE_PAGE_NAME = "langes";
    String SET_JAPANESE_LANGUAGE_PAGE_NAME = "langja";

    String SET_EXTRA_SMALL_PHOTO_SIZE_PAGE_NAME = "psizexs";
    String SET_SMALL_PHOTO_SIZE_PAGE_NAME = "psizes";
    String SET_MEDIUM_PHOTO_SIZE_PAGE_NAME = "psizem";
    String SET_LARGE_PHOTO_SIZE_PAGE_NAME = "psizel";
    String SET_EXTRA_LARGE_PHOTO_SIZE_PAGE_NAME = "psizexl";

    String SHOW_USER_HOME_PAGE_NAME = "home";
    String SHOW_USER_HOME_PAGE_FILE = "pages/ShowUserHomePage";
    String SHOW_USER_PROFILE_FORM_NAME = "showUserProfileForm";
    String SHOW_USER_PROFILE_FORM_FILE = "forms/ShowUserProfileForm";
    String SHOW_USER_PHOTO_FORM_NAME = "showUserPhotoForm";
    String SHOW_USER_PHOTO_FORM_FILE = "forms/ShowUserPhotoForm";

    String EDIT_USER_PROFILE_PAGE_NAME = "profile";
    String EDIT_USER_PROFILE_FORM_NAME = "editUserProfileForm";
    String EDIT_USER_PROFILE_FORM_FILE = "forms/EditUserProfileForm";

    String CHANGE_PASSWORD_PAGE_NAME = "password";
    String CHANGE_PASSWORD_FORM_NAME = "changePasswordForm";
    String CHANGE_PASSWORD_FORM_FILE = "forms/ChangePasswordForm";

    String UPLOAD_PHOTO_PAGE_NAME = "upload";
    String UPLOAD_PHOTO_FORM_NAME = "uploadPhotoForm";
    String UPLOAD_PHOTO_FORM_FILE = "forms/UploadPhotoForm";

    String EDIT_USER_PHOTO_PAGE_NAME = "photo";
    String EDIT_USER_PHOTO_FORM_NAME = "editUserPhotoForm";
    String EDIT_USER_PHOTO_FORM_FILE = "forms/EditUserPhotoForm";

    String SHOW_PHOTO_CASES_PAGE_NAME = "cases";
    String SHOW_PHOTO_CASES_PAGE_FILE = "pages/ShowPhotoCasesPage";
    String EDIT_PHOTO_CASE_FORM_NAME = "editPhotoCaseForm";
    String EDIT_PHOTO_CASE_FORM_FILE = "forms/EditPhotoCaseForm";

    String SHOW_ADMIN_PAGE_NAME = "admin";
    String SHOW_ADMIN_PAGE_FILE = "pages/ShowAdminPage";
    String SHOW_ADMIN_MENU_FORM_NAME = "showAdminMenuForm";
    String ADMIN_USER_PROFILE_FORM_NAME = "adminUserProfileForm";
    String ADMIN_USER_PROFILE_FORM_FILE = "forms/AdminUserProfileForm";
    String ADMIN_USER_PHOTO_FORM_NAME = "adminUserPhotoForm";
    String ADMIN_USER_PHOTO_FORM_FILE = "forms/AdminUserPhotoForm";

}
