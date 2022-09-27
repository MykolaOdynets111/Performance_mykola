import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jorphan.collections.HashTree;
import org.testng.annotations.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public abstract class BaseChatsTest extends BaseTest {
    protected int noOfMessages;

    @BeforeMethod
    @Parameters({"testPlaneName", "loopCount", "noOfMessages", "setRampupNo", "urlChannels", "orcaWAId", "messagesCreationJsonData"})
    public void setup(String testPlanName, String loopCount, int noOfThreads, int setRampupNo, String urlChannels, String orcaWAId, String postBodyPath) throws Exception {
        noOfMessages = noOfThreads;
        HTTPSamplerProxy httpSampler = setHttpSampler(orcaWAId, urlChannels, "orca/message", prepareMessagePostBody(postBodyPath, orcaWAId), setHeader());
        HashTree testPlanTree = configureTestPlan(testPlanName, noOfThreads, setRampupNo, loopCount, httpSampler);
        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
        logger.info("Jmeter engine run");
        while (jMeterEngine.isActive()) {
            Thread.sleep(1000);
            logger.info(jMeterEngine.isActive());
        }

        logger.info("jmeter is stopped");
    }

    private String prepareMessagePostBody(String postBodyPath, String orcaWAId) throws IOException {
        Path fileName = Path.of(postBodyPath);
        int chatName = new Random().nextInt();
        return String.format(Files.readString(fileName), chatName, orcaWAId);
    }


}







