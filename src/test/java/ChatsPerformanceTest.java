import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Description;
import org.aspectj.lang.annotation.Before;
import org.testng.annotations.*;
import pages.AgentDeskPage;
import pages.LoginPage;
import org.assertj.core.api.SoftAssertions;
import utils.AllureLogger;
import utils.ApachePOIExcelWrite;


public class ChatsPerformanceTest extends BaseTest {

    @BeforeMethod
    public void setTestNameInExcel() {
        ApachePOIExcelWrite.testresultdata.put("Chats performance  time test ", "");
        ApachePOIExcelWrite.testresultdata.put("------------------", "");
    }

    @Description("Assert that time of chat transferring is less than 20 seconds")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal", "urlPlatform"})
    public void transferChatTimeTest(String tenant, String agent, String urlPortal, String urlPlatform) throws InterruptedException {

        AgentDeskPage supervisorAgentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(supervisorAgentDeskPage.getPage());
        Page agentPage = createNewPage();
        LoginPage agentLoginPage = new LoginPage(agentPage);
        agentLoginPage.navigate(urlPlatform + "/internal/static/auth-tool", new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
        agentLoginPage.loginTenant(tenant, "Agent1");
        waitWilePageFullyLoaded(agentLoginPage.getPage());
        AgentDeskPage agentDeskPage = dashboardPage.launchAgentDesk(agentPage, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
//        Thread.sleep(1000);

        supervisorAgentDeskPage.checkChatForTransferring(1, 0);
        long currentTimeBeforeTransferring = System.currentTimeMillis();
        supervisorAgentDeskPage.confirmTransfer();
        agentDeskPage.acceptTransferChat();
        waitWilePageFullyLoaded(supervisorAgentDeskPage.getPage());
        long currentTimeAfterTransferring = System.currentTimeMillis();
        long transferringTime = currentTimeAfterTransferring - currentTimeBeforeTransferring;
        AllureLogger.logToAllure("Transferring time = " + transferringTime + " milliseconds");
        ApachePOIExcelWrite.testresultdata.put("Transferring time, milliseconds ", transferringTime);
        saveScreenshot(agentDeskPage.getPage().screenshot());
        logger.info("Transferring time = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat is transferred longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
        agentDeskPage.getPage().close();
    }

    @Description("Assert the time of closing the live chat and appearing it in the 'Closed' tab")
    @Test(enabled = false)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void closeChatTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);

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
        ApachePOIExcelWrite.testresultdata.put("Time of closing chat, milliseconds ", transferringTime);
        saveScreenshot(agentDeskPage.getPage().screenshot());
        logger.info("Time of closing chat = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat was closed longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }

    @Description("Assert the time of moving chat to 'Pending' tab")
    @Test(enabled = false)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void moveChatToPendingTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        SoftAssertions assertions = new SoftAssertions();
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
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
        ApachePOIExcelWrite.testresultdata.put("Time of moving chat to pending, milliseconds ", transferringTime);
        saveScreenshot(agentDeskPage.getPage().screenshot());
        logger.info("Time of moving chat to pending is = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat was moved do pending in time longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }


}
