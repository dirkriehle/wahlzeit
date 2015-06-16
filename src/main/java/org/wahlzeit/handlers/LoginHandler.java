package org.wahlzeit.handlers;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Administrator;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * When a user signs in with its Google account, this handler assures that a Wahlzeit user exists for the Google user.
 * If not {@link LoginHandler} creates one.
 * <p/>
 * Created by Lukas Hahmann on 02.06.15.
 */
public class LoginHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(LoginHandler.class.getName());


    public LoginHandler() {
        initialize(PartUtil.LOGIN_FORM_FILE, AccessRights.GUEST);
    }

    @Override
    protected void doMakeWebPart(UserSession us, WebPart part) {
        // do nothing as there is no page that should be displayed
        log.info("doMakeWebPart");
    }

    /**
     * Called when a new user logged in. Make sure that a Wahlzeit user exist.
     */
    @Override
    protected String doHandleGet(UserSession us, String link, Map args) {
        log.info("Link: " + link);

        UserService userService = UserServiceFactory.getUserService();
        com.google.appengine.api.users.User googleUser = userService.getCurrentUser();

        if (googleUser != null) {
            // googleUser logged in
            log.config(LogBuilder.createSystemMessage().
                    addMessage("Google user exists").
                    addParameter("E-Mail", googleUser.getEmail()).toString());
            String userId = googleUser.getUserId();
            UserManager userManager = UserManager.getInstance();
            User user = userManager.getUserById(userId);
            if (user != null) {
                // Wahlzeit user already exists
                us.setClient(user);
                log.config(LogBuilder.createSystemMessage().
                        addMessage("Wahlzeit user exists").
                        addParameter("id", user.getId()).toString());
            } else {
                // create new Wahlzeit user
                String emailAddress = googleUser.getEmail();
                String nickName = googleUser.getNickname();

                if (userService.isUserAdmin()) {
                    user = new Administrator(userId, nickName, emailAddress);
                } else {
                    user = new User(userId, nickName, emailAddress);
                }
                userManager.emailWelcomeMessage(us, user);
                us.setClient(user);

                log.info(LogBuilder.createUserMessage().addAction("Signup").toString());
            }

//              TODO
//            if (user.getStatus().isDisabled()) {
//                us.setMessage(us.getConfiguration().getUserIsDisabled());
//                return PartUtil.LOGIN_PAGE_NAME;
//            }

            return PartUtil.SHOW_USER_HOME_PAGE_NAME;
        } else {
            // googleUser not logged in
            return PartUtil.SHOW_NOTE_PAGE_NAME;
        }
    }

}
