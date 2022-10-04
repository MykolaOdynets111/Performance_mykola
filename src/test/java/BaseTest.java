import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
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


public abstract class BaseTest {
    protected static Browser browser;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    protected SupervisorDeskPage supervisorDeskPage;
    protected BrowserContext context;
    protected Playwright playwright;
    protected static Logger logger = Logger.getLogger(BaseTest.class);
    protected SoftAssertions assertions;
    protected StandardJMeterEngine jMeterEngine;

    @BeforeTest
    @Parameters({"noOfChats"})
    public void setupTest(int noOfThreads){
        ApachePOIExcelWrite.testresultdata.put("chat count ",  noOfThreads);
        ApachePOIExcelWrite.testresultdata.put("","");
    }


    @BeforeMethod
    @Parameters({"urlPlatform"})
    public void setup(String urlPlatform) throws Exception {
        logger.setLevel(Level.INFO);
        //Load the application using jmeter:
        jMeterEngine = new StandardJMeterEngine();
        JMeterUtils.loadJMeterProperties("/Users/modynets/Documents/Roku/apache-jmeter-5.4.3/bin/jmeter.properties");
        JMeterUtils.setJMeterHome("/Users/modynets/Documents/Roku/apache-jmeter-5.4.3");
        JMeterUtils.initLocale();
        SaveService.loadProperties();
        assertions = new SoftAssertions();


        //running jmeter tests from /jmx file:
        //HashTree testPlanTree = SaveService.loadTree(new File("src/main/resources/jmxFile.jmx"));
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
        loginPage.navigate(urlPlatform + "/internal/static/auth-tool", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.NETWORKIDLE));
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
        logger.info("set Test Plan = " + testPlanName);
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
        logger.info("Initialized Thread Group");
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
        return loopController;
    }

    protected static HeaderManager setHeader() {
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("Content-Type", "application/json; charset=UTF-8"));
        headerManager.add(new Header("Accept", "*/*"));
        headerManager.setEnabled(true);
        headerManager.setName("HTTP Header Manager");
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        return headerManager;
    }

    protected static HTTPSamplerProxy setHttpSampler(String orcaWAId, String setDomainName, String setPath, String postBody, HeaderManager headerManager) throws IOException {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setHeaderManager(headerManager);
        httpSampler.setDomain(setDomainName);
        httpSampler.setPath(setPath);
        httpSampler.setMethod("POST");
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
        httpArgument.setValue(postBody);
        httpArgument.setAlwaysEncoded(false);
        httpArgument.setMetaData("=");
        arguments.addArgument(httpArgument);
        httpSampler.setArguments(arguments);
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        return httpSampler;
    }

    protected HashTree configureTestPlan(String testPlanName, int noOfThreads, int setRampupNo, String loopCount, HTTPSamplerProxy httpSampler) throws IOException {

        TestPlan testPlan = initializeTestPlan(testPlanName);
        LoopController loopController = setLoopController(loopCount);
        ThreadGroup threadGroup = setThreadGroup(noOfThreads, setRampupNo, loopController);
        HeaderManager headerManager = setHeader();
        HashTree testPlanTree = new HashTree();
        testPlanTree.add(testPlan, threadGroup);
        HashTree httpSamplerTree = testPlanTree.add(testPlan, threadGroup);
        httpSamplerTree.add(httpSampler, headerManager);

        //Save this tes plan as a .jmx for future reference
        SaveService.saveTree(testPlanTree, new FileOutputStream("src/main/resources/jmxFile.jmx"));

        return testPlanTree;
    }

    protected long waitWilePageFullyLoaded(Page page) {
        long timeBeforeLoading = System.currentTimeMillis() / 1000l;
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        long timeAfterLoading = System.currentTimeMillis() / 1000l;

        return (timeAfterLoading - timeBeforeLoading);
    }






    protected Page createNewPage() {
        Playwright playwright1 = Playwright.create();
        Browser browser1 = playwright1.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setChannel("chrome")
                        .setHeadless(false)
                        .setDevtools(false)
                );
        BrowserContext context1 = browser1.newContext();
        return context1.newPage();
    }

    protected AgentDeskPage openNewAgentPage(String tenant, String agent, String urlPortal) {
        return openDashboardPage(tenant, agent, urlPortal).launchAgentDesk(dashboardPage.getPage(), urlPortal);
    }

    protected DashboardPage openDashboardPage(String tenant, String agent, String urlPortal) {
        loginPage.loginTenant(tenant, agent);
        waitWilePageFullyLoaded(loginPage.getPage());
        DashboardPage dashboardPage = loginPage.navigateDashboard(urlPortal);
        waitWilePageFullyLoaded(dashboardPage.getPage());
        return dashboardPage;
    }

    @AfterMethod
    @Parameters({"tenantId", "agentId", "urlPlatform", "domain"})
    public void clearTestData(String tenantId, String agentId, String urlPlatform, String domain) {
        ClosingChats closingChats = new ClosingChats();
//        closingChats.main(tenantId, agentId, urlPlatform, domain);
//        RemovingDepartments removingDepartments = new RemovingDepartments();
//        removingDepartments.main(tenantId, agentId, urlPlatform, domain);
        browser.close();
    }

    @AfterTest
    @Parameters({"noOfChats"})
    public void closeSuite(int noOfThreads){
        ApachePOIExcelWrite.setupAfterSuite(noOfThreads);
    }

}







