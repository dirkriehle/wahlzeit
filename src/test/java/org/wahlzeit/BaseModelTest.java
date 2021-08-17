package org.wahlzeit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.wahlzeit.agent.AgentManager;
import org.wahlzeit.database.repository.CaseRepository;
import org.wahlzeit.database.repository.PhotoRepository;
import org.wahlzeit.database.repository.UserRepository;
import org.wahlzeit.main.DatabaseMain;
import org.wahlzeit.model.CaseFactory;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.UserFactory;
import org.wahlzeit.service.*;
import org.wahlzeit.utils.SysConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;

/**
 * Setups the database connections for tests
 */
public class BaseModelTest {

    private static DatabaseMain databaseMain;

    /**
     * Mock services/repositories here
     */
    protected static UserService userService;
    protected static UserRepository userRepository;
    protected static UserFactory userFactory;
    protected static PhotoService photoService;
    protected static PhotoRepository photoRepository;
    protected static PhotoFactory photoFactory;
    protected static PhotoFlagService photoFlagService;
    protected static CaseService caseService;
    protected static CaseRepository caseRepository;
    protected static CaseFactory caseFactory;


    @BeforeClass
    public static void setup() throws Exception {
        // connect db
        databaseMain = new DatabaseMain(new SysConfig());
        databaseMain.startUp();

        // mock @inject calls
        Transformer transformer = new Transformer();

        // user
        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = userFactory;
        userService = new UserService();
        userService.transformer = transformer;
        userService.factory = userFactory;
        userService.repository = userRepository;

        // photo
        photoFactory = new PhotoFactory();
        photoRepository = new PhotoRepository();
        photoRepository.factory = photoFactory;
        photoService = new PhotoService();
        photoService.transformer = transformer;
        photoService.factory = photoFactory;
        photoService.repository = photoRepository;
        photoService.userRepository = userRepository;
        photoService.agentManager = new AgentManager();
        photoService.config = new SysConfig();


        // case
        caseFactory = new CaseFactory();
        caseRepository = new CaseRepository();
        caseRepository.factory = caseFactory;
        caseService = new CaseService();
        caseService.transformer = transformer;
        caseService.caseRepository = caseRepository;
        caseService.caseFactory = caseFactory;
        caseService.photoRepository = photoRepository;

        // photoFlag
        photoFlagService = new PhotoFlagService();
        photoFlagService.repository = photoRepository;
        photoFlagService.transformer = transformer;
    }

    @AfterClass
    public static void tearDown() {
        databaseMain.shutDown();
    }

    /**
     * Builds an unique email, so tests can get repeated without resetting the database
     *
     * @param name eg the test name
     * @return an unique username, prefix with the current timestamp
     */
    protected String buildUniqueName(String name) {
        return "unique" + Long.toHexString(Instant.now().toEpochMilli()) + name;
    }

    /**
     * Builds an unique email, so tests can get repeated without resetting the database
     *
     * @param identifier eg the test name
     * @return an unique email, prefix with the current timestamp
     */
    protected String buildUniqueEmail(String identifier) {
        return "unique" + Long.toHexString(Instant.now().toEpochMilli()) + identifier + "@fau.de";
    }

    /**
     * Builds an all black image with default height
     *
     * @return generated image bytes
     */
    protected byte[] buildMockImageBytes() {
        return buildMockImageBytes(100, 100);
    }

    /**
     * Builds an all black picture
     *
     * @param width  picture width
     * @param height picture height
     * @return generated image bytes
     */
    protected byte[] buildMockImageBytes(int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (Exception e) {
            throw new RuntimeException(e); // will never fail
        }
        return baos.toByteArray();
    }


}
