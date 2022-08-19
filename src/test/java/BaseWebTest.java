import com.microsoft.playwright.options.LoadState;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;

import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.testelement.TestPlan;

import org.apache.jorphan.collections.HashTree;
import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import com.microsoft.playwright.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseWebTest {
    protected static Browser browser;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    protected SupervisorDeskPage supervisorDeskPage;
    protected BrowserContext context;
    protected Playwright playwright;

    @BeforeMethod
    @Parameters({"urlPlatform", "testPlaneName", "loopCount", "noOfThreads", "setRampupNo", "urlChannels", "orcaWAId"})
    public void setup(String urlPlatform, String testPlanName, String loopCount, int noOfThreads, int setRampupNo, String urlChannels, String orcaWAId) throws Exception {
        //Load the application using jmeter:
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();
        JMeterUtils.loadJMeterProperties("/Users/modynets/Documents/Roku/apache-jmeter-5.4.3/bin/jmeter.properties");
        JMeterUtils.setJMeterHome("/Users/modynets/Documents/Roku/apache-jmeter-5.4.3");
        JMeterUtils.initLocale();
        SaveService.loadProperties();

        //running jmeter tests from /jmx file:
        //HashTree testPlanTree = SaveService.loadTree(new File("src/main/resources/jmxFile.jmx"));
        HashTree testPlanTree = configureTestPlan(testPlanName, noOfThreads, setRampupNo, urlChannels, loopCount, orcaWAId);
        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
        System.out.println("Jmeter engine run");
        while (jMeterEngine.isActive()) {
            Thread.sleep(1000);
            System.out.println(jMeterEngine.isActive());
        }
        System.out.println("jmeter is stopped");
        //running playwright for measure pages interaction:
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setChannel("chrome")
                        .setHeadless(false)
                        .setDevtools(false)
                );
        context = browser.newContext();
        Page page = context.newPage();
        loginPage = new LoginPage(page);
        dashboardPage = new DashboardPage(page);
        loginPage.navigate(urlPlatform + "/internal/static/auth-tool");
    }


    private TestPlan initializeTestPlan(String testPlanName) {
        TestPlan testPlan = new TestPlan(testPlanName);
        testPlan.setEnabled(true);
        testPlan.setTearDownOnShutdown(true);
        testPlan.setSerialized(false);
        testPlan.setFunctionalMode(false);
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        System.out.println("set Test Plan = " + testPlanName);
        return testPlan;

    }

    private static ThreadGroup setThreadGroup(int noOfThreads, int setRampupNo, LoopController loopController) {
        ThreadGroup setupThreadGroup = new ThreadGroup();
        setupThreadGroup.setName("Orca WhatsApp Chat");
        setupThreadGroup.setNumThreads(noOfThreads);
        setupThreadGroup.setRampUp(setRampupNo);
        setupThreadGroup.setProperty("num_threads", noOfThreads);
        setupThreadGroup.setProperty("ramp_time", setRampupNo);
        setupThreadGroup.setEnabled(true);
        setupThreadGroup.setSamplerController(loopController);
        setupThreadGroup.setIsSameUserOnNextIteration(true);
        System.out.println("Initialized Thread Group");
        setupThreadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        setupThreadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        return setupThreadGroup;
    }

    private static LoopController setLoopController(String loopCount) {
        LoopController loopController = new LoopController();
        loopController.setLoops(loopCount);
        loopController.setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.initialize();
        loopController.setEnabled(true);
        loopController.setName("Loop Controller");
        System.out.println("Initalized Loop Controller");
        return loopController;
    }

    private static HeaderManager setHeader() {
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("Content-Type", "application/json; charset=UTF-8"));
        headerManager.add(new Header("Accept", "*/*"));
        headerManager.setEnabled(true);
        headerManager.setName("HTTP Header Manager");
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        return headerManager;
    }

    private static HTTPSamplerProxy setHttpSampler(String orcaWAId, String setDomainName, String setPath, String requestType, HeaderManager headerManager) throws IOException {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setHeaderManager(headerManager);
        httpSampler.setDomain(setDomainName);
        httpSampler.setPath(setPath);
        httpSampler.setMethod(requestType);
        httpSampler.setProtocol("https");
        httpSampler.setName("OrcaWhatsappChat");
        httpSampler.setUseKeepAlive(true);
        httpSampler.setFollowRedirects(true);
        httpSampler.setAutoRedirects(false);
        httpSampler.setDoMultipart(false);
        httpSampler.setEnabled(true);
        httpSampler.setPostBodyRaw(true);
        Arguments arguments = new Arguments();
        HTTPArgument httpArgument = new HTTPArgument();
        httpArgument.setAlwaysEncoded(false);
        Path fileName = Path.of("src/main/resources/jsonData.txt");
        String str = String.format(Files.readString(fileName), orcaWAId);
        httpArgument.setValue(str);
        httpArgument.setAlwaysEncoded(false);
        httpArgument.setMetaData("=");
        arguments.addArgument(httpArgument);
        httpSampler.setArguments(arguments);
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        return httpSampler;
    }

    private HashTree configureTestPlan(String testPlanName, int noOfThreads, int setRampupNo, String URL, String loopCount, String orcaWAId) throws IOException {

        TestPlan testPlan = initializeTestPlan(testPlanName);
        LoopController loopController = setLoopController(loopCount);
        ThreadGroup threadGroup = setThreadGroup(noOfThreads, setRampupNo, loopController);
        HeaderManager headerManager = setHeader();
        HTTPSamplerProxy httpSampler = setHttpSampler(orcaWAId, URL, "orca/message", "POST", headerManager);
        HashTree testPlanTree = new HashTree();
        testPlanTree.add(testPlan, threadGroup);
        HashTree httpSamplerTree = testPlanTree.add(testPlan, threadGroup);
        httpSamplerTree.add(httpSampler, headerManager);

        //Added summarizer for logging meta info
        Summariser summariser = new Summariser("summaryOfResults");
        //Collect results
        ResultCollector resultCollector = new ResultCollector(summariser);
        resultCollector.setFilename("src/main/resources/Results.jtl");
        // add result collector to test plan
        testPlanTree.add(testPlanTree.getArray()[0], resultCollector);
        //Save this tes plan as a .jmx for future reference
        SaveService.saveTree(testPlanTree, new FileOutputStream("src/main/resources/jmxFile.jmx"));

        return testPlanTree;
    }

    protected long waitWilePageFullyLoaded(Page page) {
        long timeBeforeLoading = System.currentTimeMillis() / 1000l;
        page.waitForLoadState(LoadState.NETWORKIDLE);
        long timeAfterLoading = System.currentTimeMillis() / 1000l;

        return (timeAfterLoading - timeBeforeLoading);
    }


    @AfterMethod
    public void killDriverInstance() {
        ClosingChats.main();
        browser.close();


    }


}
