import org.testng.annotations.*;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jorphan.collections.HashTree;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseAgentTest extends BaseTest{

    @BeforeMethod
    @Parameters({ "testPlaneName", "loopCount", "noOfChats", "setRampupNo", "urlChannels", "orcaWAId", "ChatsCreationJsonData"})
    public void setup( String testPlanName, String loopCount, int noOfThreads, int setRampupNo, String urlChannels, String orcaWAId, String postBodyPath) throws Exception {
        Path fileName = Path.of(postBodyPath);
        String postBody = String.format(Files.readString(fileName), orcaWAId);

        HTTPSamplerProxy httpSampler = setHttpSampler(orcaWAId, urlChannels, "orca/message",  postBody, setHeader());
        HashTree testPlanTree = configureTestPlan(testPlanName, noOfThreads, setRampupNo, loopCount,  httpSampler);
        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
        logger.info("Jmeter engine run");
        while (jMeterEngine.isActive()) {
            Thread.sleep(1000);
            logger.info(jMeterEngine.isActive());
        }
        logger.info("jmeter is stopped");

    }

}







