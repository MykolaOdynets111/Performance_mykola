import pages.AgentDeskPage;
import pages.LoggedoutPage;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;


public class PagePerformanceTest extends BaseWebTest {


    @Title("Test Dashboard and Agent pages uploading time")
    @Description("Test Dashboard and Agent pages uploading time")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void pagesDownloadingTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        SoftAssertions assertions = new SoftAssertions();
        long timeBeforeLoadingLoginPage = System.currentTimeMillis();
        loginPage.loginTenant(tenant, agent);
        waitWilePageFullyLoaded(loginPage.getPage());
        long timeAfterLoadingLoginPage = System.currentTimeMillis();
        long timeOfTenantLogin = timeAfterLoadingLoginPage - timeBeforeLoadingLoginPage;
        System.out.println("Tenant was logged in " + (timeOfTenantLogin) + " milliseconds");
        assertions.assertThat(timeOfTenantLogin / 1000l)
                .as("Time of Tenant Log-in is longer than 20 seconds")
                .isLessThan(20);

        long timeBeforeLoadingDashboardPage = System.currentTimeMillis();
        loginPage.navigateDashboard(urlPortal);
        waitWilePageFullyLoaded(loginPage.getPage());
        long timeAfterLoadingDashboardPage = System.currentTimeMillis();
        long timeOfLoadingDashboardPage = timeAfterLoadingDashboardPage - timeBeforeLoadingDashboardPage;
        System.out.println("Dashboard page was uploaded in " + (timeOfLoadingDashboardPage) + " milliseconds");
        assertions.assertThat(timeOfLoadingDashboardPage / 1000l)
                .as("Dashboard page was opened longer than 20 seconds")
                .isLessThan(20);

        long timeBeforeLoadingSupervisor = System.currentTimeMillis();
        supervisorDeskPage = dashboardPage.launchSupervisorDesk(dashboardPage.getPage(), urlPortal);
        waitWilePageFullyLoaded(supervisorDeskPage.getPage());
        long timeAfterLoadingSupervisor = System.currentTimeMillis();
        long timeOfLoadingSupervisorPage = timeAfterLoadingSupervisor - timeBeforeLoadingSupervisor;
        System.out.println("Supervisor page was uploaded in " + (timeOfLoadingSupervisorPage) + " milliseconds");
        assertions.assertThat(timeOfLoadingSupervisorPage / 1000l)
                .as("Supervisor page was opened longer than 20 seconds")
                .isLessThan(20);

        long timeBeforeScrollingSupervisor = System.currentTimeMillis();
        int countScrollingIterations = supervisorDeskPage.scrollToLastChat(supervisorDeskPage.getPage());
        waitWilePageFullyLoaded(supervisorDeskPage.getPage());
        long timeAfterScrollingSupervisor = System.currentTimeMillis();
        long timeOfScrollingSupervisorPage = timeAfterScrollingSupervisor - timeBeforeScrollingSupervisor;
        System.out.println("Time of scrolling to the end of the page is " + timeOfScrollingSupervisorPage + " milliseconds");
        long averageOfScrolling = timeOfScrollingSupervisorPage / countScrollingIterations;
        System.out.println("Average value for scrolling chats is " + averageOfScrolling + " milliseconds");
        assertions.assertThat(averageOfScrolling)
                .as("Time of scrolling screen, takes more than 5 seconds")
                .isLessThan(5000);

        long TimeBeforeSwitching = System.currentTimeMillis();
        AgentDeskPage agentDeskPage = supervisorDeskPage.launchAgent(supervisorDeskPage.getPage());
        waitWilePageFullyLoaded(supervisorDeskPage.getPage());
        long TimeAfterSwitching = System.currentTimeMillis();
        long timeOfSwitchToAgentPage = TimeAfterSwitching - TimeBeforeSwitching;
        System.out.println("Switching from Supervisor to Agent page takes " + timeOfSwitchToAgentPage + " milliseconds");
        assertions.assertThat(timeOfSwitchToAgentPage / 1000l)
                .as("Switching from Supervisor to Agent page was longer than 20 seconds")
                .isLessThan(20);
        long TimeBeforeLogout = System.currentTimeMillis();
        LoggedoutPage loggedoutPage = agentDeskPage.logOut(agentDeskPage.getPage());
        waitWilePageFullyLoaded(loggedoutPage.getPage());
        long TimeAfterLogout = System.currentTimeMillis();
        long timeOfLogout = TimeAfterLogout - TimeBeforeLogout;
        System.out.println("Tenant was logged out in  " + timeOfLogout + " milliseconds");
        assertions.assertThat(timeOfSwitchToAgentPage / 1000l)
                .as("Tenant was logged out in time longer than 20 seconds")
                .isLessThan(20);

        assertions.assertAll();
    }

}
