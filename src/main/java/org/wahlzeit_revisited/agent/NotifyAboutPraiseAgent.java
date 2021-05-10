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

package org.wahlzeit_revisited.agent;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.api.repository.UserRepository;
import org.wahlzeit_revisited.service.mailing.EmailService;
import org.wahlzeit_revisited.service.mailing.EmailServiceManager;
import org.wahlzeit_revisited.utils.SysLog;
import org.wahlzeit_revisited.utils.UserLog;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * @author dirkriehle
 */
public class NotifyAboutPraiseAgent extends Agent {

    @Inject
    UserRepository userRepository;

    /**
     *
     */
    public static final String NAME = "NotifyAboutPraise";

    /**
     *
     */
    protected final Set<Photo> praisedPhotos = new HashSet<>();

    /**
     *
     */
    public NotifyAboutPraiseAgent() {
        initialize(NAME, 3 * 24 * 60 * 60 * 1000); // every three days
    }

    /**
     * @methodtype command
     */
    public void addForNotify(Photo photo) {
        synchronized (praisedPhotos) {
            praisedPhotos.add(photo);
        }
    }

    /**
     * @methodtype hook
     */
    protected void doRun() {
        notifyOwners();
    }

    /**
     * @methodtype hook
     */
    protected void notifyOwners() {
        Photo[] photos;
        synchronized (praisedPhotos) {
            photos = praisedPhotos.stream()
                    .filter(Objects::nonNull)
                    .toArray(Photo[]::new);
            praisedPhotos.clear();
        }

        for (Photo photo : photos) {
            // Handled photos get masked out, by setting to null
            if (photo != null) {
                try {
                    notifyOwner(photo, photos);
                } catch (SQLException | NotFoundException e) {
                    SysLog.logThrowable(e);
                }
            }
        }
    }

    /**
     * @methodtype helper
     */
    protected void notifyOwner(Photo photo, Photo[] photos) throws SQLException, NotFoundException {
        User user = userRepository.findById(photo.getOwnerId()).orElseThrow(NotFoundException::new);

        ModelConfig cfg = LanguageConfigs.get(user.getLanguage());

        EmailAddress from = cfg.getAdministratorEmailAddress();
        EmailAddress to = user.getEmailAddress();
        String emailSubject = cfg.getNotifyAboutPraiseEmailSubject();

        StringBuilder emailBody = new StringBuilder(cfg.getNotifyAboutPraiseEmailBody() + "\n\n");

        for (int i = 0; i < photos.length; i++) {
            Photo current = photos[i];
            if ((current != null) && current.hasSameOwner(photo)) {
                Long id = current.getId();
                UserLog.logUserInfo("notifying user: " + user.getName() + " about photo: " + id);
                emailBody.append(user.getSiteUrlAsString()).append(id).append(".html\n"); // @TODO Application
                photos[i] = null;
            }
        }
        emailBody.append("\n");
        emailBody.append(cfg.getGeneralEmailRegards()).append("\n\n");
        emailBody.append(cfg.getNotifyAboutPraiseEmailPostScriptum()).append("\n\n----\n");
        emailBody.append(cfg.getGeneralEmailFooter()).append("\n\n");

        EmailService emailService = EmailServiceManager.getDefaultService();
        emailService.sendEmailIgnoreException(from, to, emailSubject, emailBody.toString());
    }

}
