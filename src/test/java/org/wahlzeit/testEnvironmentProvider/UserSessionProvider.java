package org.wahlzeit.testEnvironmentProvider;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.junit.rules.ExternalResource;
import org.wahlzeit.model.EnglishModelConfig;
import org.wahlzeit.model.GermanModelConfig;
import org.wahlzeit.model.Guest;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.Language;
import org.wahlzeit.services.SessionManager;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Rule that provides a <code>UserSession</code> in the <code>SessionManager</code>
 */
public class UserSessionProvider extends ExternalResource {

	public static final String USER_SESSION_NAME = "testContext";

	@Override
	protected void before() throws Throwable {
		// init language configs because they are used e.g. for AbstractWebPartHandler
		LanguageConfigs.put(Language.ENGLISH, new EnglishModelConfig());
		LanguageConfigs.put(Language.GERMAN, new GermanModelConfig());

		HttpSession httpSession = mock(HttpSession.class);
		when(httpSession.getAttribute(UserSession.INITIALIZED)).thenReturn(UserSession.INITIALIZED);
		String guestName = ObjectifyService.run(new Work<String>() {
			@Override
			public String run() {
				Guest guest = new Guest();
				guest.setLanguage(Language.ENGLISH);
				return guest.getId();
			}
		});
		when(httpSession.getAttribute(UserSession.CLIENT_ID)).thenReturn(guestName);

		Map<String, Object> dummyMap = new HashMap<String, Object>();
		dummyMap.put(UserSession.MESSAGE, "dummy Message");
		when(httpSession.getAttribute(UserSession.SAVED_ARGS)).thenReturn(dummyMap);

		UserSession userSession = new UserSession(USER_SESSION_NAME, "", httpSession, "en");
		SessionManager.setThreadLocalSession(userSession);
	}

	@Override
	protected void after() {
		SessionManager.setThreadLocalSession(null);
	}

}
