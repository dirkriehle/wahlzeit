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

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.mailing.EmailService;
import org.wahlzeit.services.mailing.EmailServiceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


/**
 * The UserManager provides access to and manages Users (including Moderators and Administrators).
 *
 * @author dirkriehle
 */
public class UserManager extends ClientManager {

    private static final Logger log = Logger.getLogger(UserManager.class.getName());
    /**
     * Reserved names that cannot be registered by regular users
     *
     * @FIXME Load from file eventually
     */
    public static List<String> reservedNames = Arrays.asList(
            "admin",
            "anonymous",
            "flickr",
            "guest#"
    );


    /**
     *
     */
    protected static UserManager instance;

    /**
     *
     */
    private UserManager() {
    }

    /**
     *
     */
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void init() {
        loadExistingUsers();
    }

    /**
     *
     */
    public void loadExistingUsers() {
        ObjectifyService.run(new Work<Void>() {
            @Override
            public Void run() {
                Collection<User> existingUser = new ArrayList<User>();
                readObjects(existingUser, User.class);

                for (User user : existingUser) {
                    if (!hasClientById(user.getId())) {
                        doAddClient(user);
                    } else {
                        log.config(LogBuilder.createSystemMessage().addParameter("user has been loaded", user.getId()).toString());
                    }
                }
                return null;
            }
        });

        log.info(LogBuilder.createSystemMessage().addMessage("loaded all clients").toString());
    }

    /**
     *
     */
    public boolean isReservedUserName(String userName) {
        return reservedNames.contains(Tags.asTag(userName));
    }

    /**
     *
     */
    public void emailWelcomeMessage(UserSession us, User user) {
        EmailAddress to = user.getEmailAddress();

        String emailSubject = us.getConfiguration().getWelcomeEmailSubject();
        String emailBody = us.getConfiguration().getWelcomeEmailBody() + "\n\n";
        emailBody += us.getConfiguration().getWelcomeEmailUserName() + user.getNickName() + "\n\n";
        emailBody += us.getConfiguration().getGeneralEmailRegards() + "\n\n----\n";
        emailBody += us.getConfiguration().getGeneralEmailFooter() + "\n\n";

        EmailService emailService = EmailServiceManager.getDefaultService();
        emailService.sendEmailIgnoreException(to, us.getConfiguration().getAuditEmailAddress(), emailSubject, emailBody);
    }

    /**
     *
     */
    public User getUserByEmailAddress(String emailAddress) {
        return getUserByEmailAddress(EmailAddress.getFromString(emailAddress));
    }

    /**
     *
     */
    public User getUserByEmailAddress(EmailAddress emailAddress) {
        User result;
        result = readObject(User.class, User.EMAIL_ADDRESS, emailAddress.asString());

        if (result != null) {
            User current = getUserById(result.getId());
            if (current == null) {
                doAddClient(result);
            } else {
                result = current;
            }
        }

        return result;
    }

    /**
     * @methodtype get
     */
    public User getUserById(String name) {
        Client client = super.getClientById(name);
        if (client instanceof User) {
            return (User) client;
        } else {
            return null;
        }
    }

}
