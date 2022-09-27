
import jdk.jfr.Description;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import pages.DepartmentManagementPage;
import utils.ApachePOIExcelWrite;


public class DepartmentsPerformanceTest extends BaseAgentTest {
    Assertion assertions = new Assertion();


    @Description("Assert the time of department creation is less than 20 seconds")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal", "departmentAgents"})
    public void departmentCreationTimeTest(String tenant, String agent, String urlPortal, int countOfAgents) {
        ApachePOIExcelWrite.testresultdata.put("Department section test ", "" );
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
        ApachePOIExcelWrite.testresultdata.put("New department with " + countOfAgents + " agents" + " was created in, milliseconds ", creationTime );
        logger.info("New department with " + countOfAgents + " agents" + " was created in " + creationTime + " milliseconds");
        assertions.assertTrue(creationTime / 1000l < 20, "New department was crated longer than 20 seconds");
    }

}
