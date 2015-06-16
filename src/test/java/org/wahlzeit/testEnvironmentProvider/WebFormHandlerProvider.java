package org.wahlzeit.testEnvironmentProvider;

import org.junit.rules.ExternalResource;
import org.wahlzeit.handlers.PartUtil;
import org.wahlzeit.handlers.TellFriendFormHandler;
import org.wahlzeit.handlers.WebFormHandler;
import org.wahlzeit.handlers.WebPartHandlerManager;

/**
 * Created by Lukas Hahmann on 22.05.15.
 */
public class WebFormHandlerProvider extends ExternalResource {

    private WebFormHandler webFormHandler;

    @Override
    protected void before() throws Throwable {
        WebPartHandlerManager.getInstance().addWebPartHandler(PartUtil.TELL_FRIEND_FORM_NAME, new TellFriendFormHandler());
        webFormHandler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
    }

    public WebFormHandler getWebFormHandler() {
        return webFormHandler;
    }
}
