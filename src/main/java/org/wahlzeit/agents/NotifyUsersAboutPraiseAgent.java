package org.wahlzeit.agents;

import com.google.apphosting.api.ApiProxy;
import com.google.common.collect.ArrayListMultimap;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.ModelConfig;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoId;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.mailing.EmailService;
import org.wahlzeit.services.mailing.EmailServiceManager;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Lukas Hahmann on 12.08.15.
 */
public class NotifyUsersAboutPraiseAgent extends Agent {

	public static final String NAME = "notifyUsersAboutPraise";

	private static final Logger log = Logger.getLogger(NotifyUsersAboutPraiseAgent.class.getName());

	public NotifyUsersAboutPraiseAgent() {
		initialize(NAME);
	}

	/**
	 * @methodtype command
	 * <p/>
	 * Notifies all users that want to get informed if their photos have been praised.
	 */
	protected void doRun() {
		Map<PhotoId, Photo> photoCache = PhotoManager.getInstance().getPhotoCache();
		Collection<Photo> photos = photoCache.values();

		ArrayListMultimap<String, Photo> ownerIdPhotosMap = ArrayListMultimap.create();
		for (Photo photo : photos) {
			if (photo != null && photo.isVisible() && photo.hasNewPraise()) {
				String ownerId = photo.getOwnerId();
				if (ownerId != null) {
					if (ownerIdPhotosMap.containsValue(ownerId)) {
						Collection<Photo> photosOfUser = ownerIdPhotosMap.get(ownerId);
						photosOfUser.add(photo);
					}
					else {
						ownerIdPhotosMap.put(ownerId, photo);
					}
					photo.setNoNewPraise();
					PhotoManager.getInstance().savePhoto(photo);
				}
			}
		}

		log.config(LogBuilder.createSystemMessage().addAction("notify owner")
				.addParameter("number of user to notify", ownerIdPhotosMap.keys().size()).toString());

		for (String ownerId : ownerIdPhotosMap.keys()) {
			notifyOwner(ownerId, ownerIdPhotosMap.get(ownerId));
		}
	}

	/**
	 * @methotype command
	 * <p/>
	 * Actually notifies one user about the praise of his/her photos.
	 */
	protected void notifyOwner(String ownerId, Collection<Photo> allPhotosOfUser) {
		User owner = UserManager.getInstance().getUserById(ownerId);
		ModelConfig cfg = LanguageConfigs.get(owner.getLanguage());

		EmailAddress from = cfg.getAdministratorEmailAddress();
		EmailAddress to = owner.getEmailAddress();
		String emailSubject = cfg.getNotifyAboutPraiseEmailSubject();

		String emailBody = cfg.getNotifyAboutPraiseEmailBody() + "\n\n";

		log.config(LogBuilder.createSystemMessage().addAction("sending email")
				.addParameter("recipient", to.asString()).toString());

		for (Photo current : allPhotosOfUser) {
			String id = current.getId().asString();

			String appId = ApiProxy.getCurrentEnvironment().getAppId();
			appId = appId.substring(2); // app id is given as "s~appid"
			String link = "https://" + appId + ".appspot.com/" + id + ".html\n";
			emailBody += link;

			log.config(LogBuilder.createSystemMessage().addParameter("appid", appId)
					.addParameter("link", link).toString());

		}
		emailBody += "\n";

		emailBody += cfg.getGeneralEmailRegards() + "\n\n";
		emailBody += cfg.getNotifyAboutPraiseEmailPostScriptum() + "\n\n----\n";
		emailBody += cfg.getGeneralEmailFooter() + "\n\n";

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, emailSubject, emailBody);
	}


}
