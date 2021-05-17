package org.wahlzeit.agent;

import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.BaseModelTest;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.Tags;
import org.wahlzeit.model.User;
import org.wahlzeit.service.mailing.NullEmailService;

public class NotifyAboutPraiseAgentIT extends BaseModelTest {

    private NotifyAboutPraiseAgent agent;

    @Before
    public void setupDependencies() {
        agent = new NotifyAboutPraiseAgent();
        agent.emailService = new NullEmailService();
        agent.userRepository = userRepository;
    }

    @Test
    public void test_notifyUser() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        Photo photo = photoFactory.createPhoto(user, buildMockImageBytes(), new Tags());
        photo = photoRepository.insert(photo);
        agent.addForNotify(photo);

        // act
        agent.notifyOwners();
    }

}
