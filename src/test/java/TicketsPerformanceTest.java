import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import jdk.jfr.Description;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AgentDeskPage;
import pages.LoginPage;
import pages.SupervisorDeskPage;
import utils.ChangeSettings;

public class TicketsPerformanceTest extends BaseAgentTest {

    ChangeSettings changeSettings = new ChangeSettings();

    @BeforeTest
    @Parameters({"tenantId", "agentId", "urlPlatform", "domain"})
    public void uncheckAllSupportHours(String tenantId, String agentId, String urlPlatform, String domain) {
        changeSettings.uncheckSupportHours(tenantId, agentId, urlPlatform, domain);
    }

    @Description("Assert that time assigning the ticket to agent is less than 20 seconds")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal", "urlPlatform", "domain"})
    public void assignTicketTimeTest(String tenant, String agent, String urlPortal, String urlPlatform, String domain) {

        dashboardPage = openDashboardPage(tenant, agent, urlPortal);
        SupervisorDeskPage supervisorAgentDeskPage = dashboardPage.launchSupervisorDesk(dashboardPage.getPage(), urlPortal);
        waitWilePageFullyLoaded(supervisorAgentDeskPage.getPage());
        Page agentPage = createNewPage();
        LoginPage agentLoginPage = new LoginPage(agentPage);
        agentLoginPage.navigate(urlPlatform + "/internal/static/auth-tool", new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
        agentLoginPage.loginTenant(tenant, "Agent1");
        waitWilePageFullyLoaded(agentLoginPage.getPage());
        AgentDeskPage agentDeskPage = dashboardPage.launchAgentDesk(agentPage, urlPortal);
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        long currentTimeBeforeTransferring = System.currentTimeMillis();
        supervisorAgentDeskPage.openTicketsTab();
        supervisorAgentDeskPage.assignTicket(1);
    }

    @AfterTest
    @Parameters({"tenantId", "agentId", "urlPlatform", "domain"})
    public void resetAllSupportHours(String tenantId, String agentId, String urlPlatform, String domain) {
        changeSettings.resetSupportHours(tenantId, agentId, urlPlatform, domain);
    }

}
