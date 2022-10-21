import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import jdk.jfr.Description;
import org.testng.annotations.*;
import pages.AgentDeskPage;
import pages.LoginPage;
import utils.ApachePOIExcelWrite;


public class ChatsPerformanceTest extends BaseAgentTest {

    @BeforeMethod
    public void setTestNameInExcel() {
        ApachePOIExcelWrite.testresultdata.put("Chats performance  time test ", "");
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
        supervisorAgentDeskPage.checkChatForTransferring(1, 0);
//        String issuedDataTestId = supervisorAgentDeskPage.getDataTestId(0);
        long currentTimeBeforeTransferring = System.currentTimeMillis();
        supervisorAgentDeskPage.confirmTransfer();
//        supervisorAgentDeskPage.waitWhileChatDisappearFromPage(issuedDataTestId);
        try {
            agentDeskPage.acceptTransferChat();
            waitWilePageFullyLoaded(supervisorAgentDeskPage.getPage());
            long currentTimeAfterTransferring = System.currentTimeMillis();
            long transferringTime = currentTimeAfterTransferring - currentTimeBeforeTransferring;
            ApachePOIExcelWrite.testresultdata.put("Transferring time, milliseconds ", transferringTime);
            logger.info("Transferring time = " + transferringTime + " milliseconds");
            assertions.assertThat(transferringTime / 1000l)
                    .as("chat is transferred longer than 20 seconds")
                    .isLessThan(20);
        } catch (PlaywrightException exception) {
            logger.info("Can't accept transferring");
            assertions.fail("Can't accept transferring");
        } finally {
            agentDeskPage.getPage().close();
            agentPage.close();
        }
        assertions.assertAll();
    }

    @Description("Assert the time of closing the live chat and appearing it in the 'Closed' tab")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void closeChatTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        String issuedDataTestId = agentDeskPage.getDataTestId(0);
        long currentTimeBeforeClosing = System.currentTimeMillis();
        agentDeskPage.closeChat(0);
        agentDeskPage.waitWhileChatDisappearFromPage(issuedDataTestId);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        long currentTimeAfterClosing = System.currentTimeMillis();
        agentDeskPage.openClosedChatsTab();
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        assertions.assertThat(agentDeskPage.getDataTestId(0))
                .as("chat was not appeared in the 'Closed' tab ")
                .isEqualTo(issuedDataTestId);
        long transferringTime = currentTimeAfterClosing - currentTimeBeforeClosing;
        ApachePOIExcelWrite.testresultdata.put("Time of closing chat, milliseconds ", transferringTime);
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
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        String issuedDataTestId = agentDeskPage.getDataTestId(0);
        long currentTimeBeforeMoving = System.currentTimeMillis();
        agentDeskPage.moveChatToPending(0);
        agentDeskPage.waitWhileChatDisappearFromPage(issuedDataTestId);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.openPendingTab();
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        long currentTimeAfterMoving = System.currentTimeMillis();
        assertions.assertThat(agentDeskPage.getDataTestId(0))
                .as("chat was not appeared in the 'Pending' tab ")
                .isEqualTo(issuedDataTestId);
        long transferringTime = currentTimeAfterMoving - currentTimeBeforeMoving;
        ApachePOIExcelWrite.testresultdata.put("Time of moving chat to pending, milliseconds ", transferringTime);
        logger.info("Time of moving chat to pending is = " + transferringTime + " milliseconds");
        assertions.assertThat(transferringTime / 1000l)
                .as("chat was moved do pending in time longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }

    @Description("Assert the time of adding attachment to chat")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void addAttachmentToChatTimeTest(String tenant, String agent, String urlPortal) {
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        long currentTimeBeforeAdding = System.currentTimeMillis();
        agentDeskPage.uploadExampleAttachment(agentDeskPage.getPage());
        long currentTimeAfterAdding = System.currentTimeMillis();
        long uploadingTime = currentTimeAfterAdding - currentTimeBeforeAdding;
        ApachePOIExcelWrite.testresultdata.put("Time of uploading attachment, milliseconds ", uploadingTime);
        logger.info("Time of uploading attachment is = " + uploadingTime + " milliseconds");
        agentDeskPage.clickChatItem(0);
        assertions.assertThat(uploadingTime / 1000l)
                .as("Time of uploading attachment, longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }

    @Description("Assert the time of adding attachment to chat")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void sendLocationToChatTimeTest(String tenant, String agent, String urlPortal) {
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        agentDeskPage.clickChatItem(0);
        long currentTimeBeforeSendLocation = System.currentTimeMillis();
        int locationTypingDelay = agentDeskPage.sendLocation(agentDeskPage.getPage(), "Ukraine");
        long currentTimeAfterSendLocation = System.currentTimeMillis();
        long sendLocationTime = currentTimeAfterSendLocation - currentTimeBeforeSendLocation - locationTypingDelay;
        ApachePOIExcelWrite.testresultdata.put("Time of sending location, milliseconds ", sendLocationTime);
        logger.info("Time of sending location is = " + sendLocationTime + " milliseconds");
        assertions.assertThat(sendLocationTime / 1000l)
                .as("Time of sending location, longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }

}
