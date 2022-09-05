import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Description;
import pages.AgentDeskPage;
import pages.LoginPage;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.AllureLogger;


public class ChatsPerformanceTest extends BaseTest {

    @Description("Assert that time of chat transferring is less than 20 seconds")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal", "urlPlatform"})
    public void transferChatTimeTest(String tenant, String agent, String urlPortal, String urlPlatform) throws InterruptedException {
        SoftAssertions assertions = new SoftAssertions();
        AgentDeskPage supervisorAgentDeskPage = openAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(supervisorAgentDeskPage.getPage());
        Page agentPage = createNewPage();
        LoginPage agentLoginPage = new LoginPage(agentPage);
        agentLoginPage.navigate(urlPlatform + "/internal/static/auth-tool", new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
        agentLoginPage.loginTenant(tenant, "Agent1");
        waitWilePageFullyLoaded(agentLoginPage.getPage());
        AgentDeskPage agentDeskPage = dashboardPage.launchAgentDesk(agentPage, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        Thread.sleep(1000);
        long currentTimeBeforeTransferring = System.currentTimeMillis();
        supervisorAgentDeskPage.transferChat(1, 0);
        agentDeskPage.acceptTransferChat();
        waitWilePageFullyLoaded(supervisorAgentDeskPage.getPage());
        long currentTimeAfterTransferring = System.currentTimeMillis();
        long transferringTime = currentTimeAfterTransferring - currentTimeBeforeTransferring;
        AllureLogger.logToAllure("Transferring time = " + transferringTime + " milliseconds");
        saveScreenshot(agentDeskPage.getPage().screenshot());
        logger.info("Transferring time = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat is transferred longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
        agentDeskPage.getPage().close();
    }

    @Description("Assert the time of closing the live chat and appearing it in the 'Closed' tab")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void closeChatTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        SoftAssertions assertions = new SoftAssertions();
        AgentDeskPage agentDeskPage = openAgentPage(tenant, agent, urlPortal);

        String issuedDataTestId = agentDeskPage.getDataTestId(0);
        agentDeskPage.clickChatItem(0);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        long currentTimeBeforeClosing = System.currentTimeMillis();
        agentDeskPage.closeChat(0);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.waitWhileChatDisappearFromPage(issuedDataTestId);
        agentDeskPage.openClosedChatsTab();
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        long currentTimeAfterClosing = System.currentTimeMillis();
        long transferringTime = currentTimeAfterClosing - currentTimeBeforeClosing;
        AllureLogger.logToAllure("Time of closing chat = " + transferringTime + " milliseconds");
        saveScreenshot(agentDeskPage.getPage().screenshot());
        logger.info("Time of closing chat = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat was closed longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }

    @Description("Assert the time of moving chat to 'Pending' tab")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void moveChatToPendingTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        SoftAssertions assertions = new SoftAssertions();
        AgentDeskPage agentDeskPage = openAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        String issuedDataTestId = agentDeskPage.getDataTestId(0);
        agentDeskPage.clickChatItem(0);
        long currentTimeBeforeMoving = System.currentTimeMillis();
        agentDeskPage.moveChatToPending(0);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.openPendingTab();
        waitWilePageFullyLoaded(agentDeskPage.getPage());

        agentDeskPage.clickChatItem(0);
        long currentTimeAfterMoving = System.currentTimeMillis();

        long transferringTime = currentTimeAfterMoving - currentTimeBeforeMoving;

        AllureLogger.logToAllure("Time of moving chat to pending is = " + transferringTime + " milliseconds");
        saveScreenshot(agentDeskPage.getPage().screenshot());
        logger.info("Time of moving chat to pending is = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat was moved do pending in time longer than 20 seconds")
                .isLessThan(20);
        assertions.assertThat(agentDeskPage.isChatPresentOnPage(issuedDataTestId))
                .as("pending chat is not appeared in the pending tab")
                .isTrue();
        assertions.assertAll();
    }


    private AgentDeskPage openAgentPage(String tenant, String agent, String urlPortal) {
        loginPage.loginTenant(tenant, agent);
        waitWilePageFullyLoaded(loginPage.getPage());
        loginPage.navigateDashboard(urlPortal);
        waitWilePageFullyLoaded(dashboardPage.getPage());
        return dashboardPage.launchAgentDesk(dashboardPage.getPage(), urlPortal);
    }


    private Page createNewPage() {
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


}
