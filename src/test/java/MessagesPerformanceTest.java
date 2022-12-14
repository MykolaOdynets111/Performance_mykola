
import jdk.jfr.Description;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AgentDeskPage;
import utils.ApachePOIExcelWrite;

public class MessagesPerformanceTest extends BaseChatsTest {


    @Description("Test Dashboard and Agent pages uploading time")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void messagesScrollingTimeTest(String tenant, String agent, String urlPortal) throws InterruptedException {
        ApachePOIExcelWrite.testresultdata.put("Messages scrolling time test ", "");
        ApachePOIExcelWrite.testresultdata.put("------------------", "");
        AgentDeskPage agentDeskPage = openNewAgentPage(tenant, agent, urlPortal);
        agentDeskPage.clickChatItem(0);
        long timeBeforeScrollingSupervisor = System.currentTimeMillis();
        int countScrollingIterations = agentDeskPage.scrollToFirstMessage(agentDeskPage.getPage());
        waitWilePageFullyLoaded(agentDeskPage.getPage());
        long timeAfterScrollingSupervisor = System.currentTimeMillis();
        long timeOfScrollingSupervisorPage = timeAfterScrollingSupervisor - timeBeforeScrollingSupervisor -
                (countScrollingIterations* 1000L);
        ApachePOIExcelWrite.testresultdata.put("Time of scrolling to the first message for " + noOfMessages + " messages is ", timeOfScrollingSupervisorPage);
        long averageOfScrolling = timeOfScrollingSupervisorPage / countScrollingIterations;
        ApachePOIExcelWrite.testresultdata.put("Average value for scrolling " + noOfMessages + " messages is ", averageOfScrolling);
        assertions.assertThat(averageOfScrolling)
                .as("Time of scrolling messages, takes more than 5 seconds")
                .isLessThan(5000);
    }

}
