
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import pages.DepartmentManagementPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;


public class DepartmentsPerformanceTest extends BaseWebTest {
    Assertion assertions = new Assertion();


    @Title("Assert the time of department creation is less than 20 seconds")
    @Description("Assert the time of department creation is less than 20 seconds")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal", "departmentAgents"})
    public void departmentCreationTimeTest(String tenant, String agent, String urlPortal, int countOfAgents) {

        loginPage.loginTenant(tenant, agent);
        waitWilePageFullyLoaded(loginPage.getPage());
        loginPage.navigateDashboard(urlPortal);
        DepartmentManagementPage departmentManagementPage = dashboardPage.openDepartmentManagementPage(dashboardPage.getPage());
        waitWilePageFullyLoaded(departmentManagementPage.getPage());
        departmentManagementPage.fillInDataInDepartmentCreationTab(countOfAgents);
        long currentTimeBeforeCreation = System.currentTimeMillis();
        departmentManagementPage.createNewDepartment();
        long currentTimeAfterCreation = System.currentTimeMillis();
        long creationTime = currentTimeAfterCreation - currentTimeBeforeCreation;
        System.out.println("New department with " + countOfAgents + " agents" + " was created in " + creationTime + " milliseconds");
        assertions.assertTrue(creationTime / 1000l < 20, "New department was crated longer than 20 seconds");
    }


}
