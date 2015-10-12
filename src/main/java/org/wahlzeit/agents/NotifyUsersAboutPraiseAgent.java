package org.wahlzeit.agents;

import com.google.apphosting.api.ApiProxy;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * An agent class to notify users about new praise received for their photos.
 */
public class NotifyUsersAboutPraiseAgent extends Agent {

	public static final String NAME = "notifyUsersAboutPraise";

	private static final Logger log = Logger.getLogger(NotifyUsersAboutPraiseAgent.class.getName());

	public NotifyUsersAboutPraiseAgent() {
		initialize(NAME);
	}

	/**
	 * @methodtype command
	 * 
	 * Notifies all users that want to get informed if their photos have been praised.
	 */
	protected void doRun() {
		Map<PhotoId, Photo> photoCache = PhotoManager.getInstance().getPhotoCache();
		Collection<Photo> photos = photoCache.values();

		ArrayList<Photo> arrayListOfPhotos;
		HashMap<String, ArrayList<Photo>> ownerIdPhotosMap = new HashMap<String, ArrayList<Photo>>();
		for (Photo photo : photos) {
			if (photo != null && photo.isVisible() && photo.hasNewPraise()) {
				String ownerId = photo.getOwnerId();
				if (ownerId != null) {
					log.config(LogBuilder.createSystemMessage().addParameter("ownerId", ownerId).toString());
					if (ownerIdPhotosMap.containsKey(ownerId)) {
						log.config(LogBuilder.createSystemMessage().addAction("add to existing owner").toString());
						arrayListOfPhotos = ownerIdPhotosMap.get(ownerId);
					} else {
						log.config(LogBuilder.createSystemMessage().addAction("add to new owner").toString());
						arrayListOfPhotos = new ArrayList<Photo>();
					}
					arrayListOfPhotos.add(photo);
					ownerIdPhotosMap.put(ownerId, arrayListOfPhotos);
					photo.setNoNewPraise();
					PhotoManager.getInstance().savePhoto(photo);
				}
			}
		}

		log.config(LogBuilder.createSystemMessage().addAction("notify owner")
				.addParameter("number of user to notify", ownerIdPhotosMap.size()).toString());

		for (String ownerId : ownerIdPhotosMap.keySet()) {
			notifyOwner(ownerId, ownerIdPhotosMap.get(ownerId));
		}
	}

	/**
	 * @methotype command
	 * 
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
