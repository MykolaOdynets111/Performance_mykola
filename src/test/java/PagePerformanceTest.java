import jdk.jfr.Description;
import pages.AgentDeskPage;
import pages.LoggedOutPage;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.ApachePOIExcelWrite;

public class PagePerformanceTest extends BaseAgentTest {


    @Description("Test Dashboard and Agent pages uploading time")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void pagesDownloadingTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        ApachePOIExcelWrite.testresultdata.put("Pages Download time test ", "");
        long timeBeforeLoadingLoginPage = System.currentTimeMillis();
        loginPage.loginTenant(tenant, agent);
        waitWilePageFullyLoaded(loginPage.getPage());
        long timeAfterLoadingLoginPage = System.currentTimeMillis();
        long timeOfTenantLogin = timeAfterLoadingLoginPage - timeBeforeLoadingLoginPage;
        ApachePOIExcelWrite.testresultdata.put("Tenant was logged in ", timeOfTenantLogin);
        logger.info("Tenant was logged in " + (timeOfTenantLogin) + " milliseconds");
        assertions.assertThat(timeOfTenantLogin / 1000l)
                .as("Time of Tenant Log-in is longer than 20 seconds")
                .isLessThan(20);
        long timeBeforeLoadingDashboardPage = System.currentTimeMillis();
        dashboardPage = loginPage.navigateDashboard(urlPortal);
        waitWilePageFullyLoaded(dashboardPage.getPage());
        long timeAfterLoadingDashboardPage = System.currentTimeMillis();
        long timeOfLoadingDashboardPage = timeAfterLoadingDashboardPage - timeBeforeLoadingDashboardPage;
        ApachePOIExcelWrite.testresultdata.put("Dashboard page was uploaded in ", timeOfLoadingDashboardPage);
        logger.info("Dashboard page was uploaded in " + (timeOfLoadingDashboardPage) + " milliseconds");
        assertions.assertThat(timeOfLoadingDashboardPage / 1000l)
                .as("Dashboard page was opened longer than 20 seconds")
                .isLessThan(20);
        long timeBeforeLoadingSupervisor = System.currentTimeMillis();
        supervisorDeskPage = dashboardPage.launchSupervisorDesk(dashboardPage.getPage(), urlPortal);
        waitWilePageFullyLoaded(supervisorDeskPage.getPage());
        long timeAfterLoadingSupervisor = System.currentTimeMillis();
        long timeOfLoadingSupervisorPage = timeAfterLoadingSupervisor - timeBeforeLoadingSupervisor;
        ApachePOIExcelWrite.testresultdata.put("Supervisor page was uploaded in ", timeOfLoadingSupervisorPage);
        logger.info("Supervisor page was uploaded in " + (timeOfLoadingSupervisorPage) + " milliseconds");
        assertions.assertThat(timeOfLoadingSupervisorPage / 1000l)
                .as("Supervisor page was opened longer than 20 seconds")
                .isLessThan(20);
        long timeBeforeScrollingSupervisor = System.currentTimeMillis();
        int countScrollingIterations = supervisorDeskPage.scrollToLastChat(supervisorDeskPage.getPage());
        waitWilePageFullyLoaded(supervisorDeskPage.getPage());
        long timeAfterScrollingSupervisor = System.currentTimeMillis();
        long timeOfScrollingSupervisorPage = timeAfterScrollingSupervisor - timeBeforeScrollingSupervisor -
                (countScrollingIterations* 1000L);
        ApachePOIExcelWrite.testresultdata.put("Time of scrolling to the end of the page is ", timeOfScrollingSupervisorPage);
        logger.info("Time of scrolling to the end of the page is " + timeOfScrollingSupervisorPage + " milliseconds");
        long averageOfScrolling = timeOfScrollingSupervisorPage / countScrollingIterations;
        ApachePOIExcelWrite.testresultdata.put("Average value for scrolling chats is ", averageOfScrolling);
        logger.info("Average value for scrolling chats is " + averageOfScrolling + " milliseconds");
        assertions.assertThat(averageOfScrolling)
                .as("Time of scrolling screen, takes more than 5 seconds")
                .isLessThan(5000);
        long TimeBeforeSwitching = System.currentTimeMillis();
        AgentDeskPage agentDeskPage = supervisorDeskPage.launchAgent(supervisorDeskPage.getPage());
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        long TimeAfterSwitching = System.currentTimeMillis();
        long timeOfSwitchToAgentPage = TimeAfterSwitching - TimeBeforeSwitching;
        ApachePOIExcelWrite.testresultdata.put("Switching from Supervisor to Agent page takes ", timeOfSwitchToAgentPage );
        logger.info("Switching from Supervisor to Agent page takes " + timeOfSwitchToAgentPage + " milliseconds");
        assertions.assertThat(timeOfSwitchToAgentPage / 1000l)
                .as("Switching from Supervisor to Agent page was longer than 20 seconds")
                .isLessThan(20);
        long TimeBeforeLogout = System.currentTimeMillis();
        LoggedOutPage loggedoutPage = agentDeskPage.logOut(agentDeskPage.getPage());
        waitWilePageFullyLoaded(loggedoutPage.getPage());
        long TimeAfterLogout = System.currentTimeMillis();
        long timeOfLogout = TimeAfterLogout - TimeBeforeLogout;
        ApachePOIExcelWrite.testresultdata.put("Tenant was logged out in  ", timeOfLogout);
        logger.info("Tenant was logged out in  " + timeOfLogout + " milliseconds");
        assertions.assertThat(timeOfSwitchToAgentPage / 1000l)
                .as("Tenant was logged out in time longer than 20 seconds")
                .isLessThan(20);
        assertions.assertAll();
    }

}
