
import io.qameta.allure.Step;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.*;
import pages.AgentDeskPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SupervisorDeskPage;
import utils.ApachePOIExcelWrite;
import utils.ClosingChats;
import utils.RemovingDepartments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public abstract class BaseChatsTest extends BaseTest {

    @BeforeTest
    @Parameters({"noOfChats"})
    public void setupTest(int noOfThreads) {
        ApachePOIExcelWrite.testresultdata.put("chat count ", noOfThreads);
        ApachePOIExcelWrite.testresultdata.put("", "");
    }


    @BeforeMethod
    @Parameters({"testPlaneName", "loopCount", "noOfMessages", "setRampupNo", "urlChannels", "orcaWAId", "messagesCreationJsonData"})
    @Step("Setup")
    public void setup(String testPlanName, String loopCount, int noOfThreads, int setRampupNo, String urlChannels, String orcaWAId, String postBodyPath) throws Exception {
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







