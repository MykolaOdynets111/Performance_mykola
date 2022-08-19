import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DashboardPage extends BasePage {

    private final Locator launchAgentDeskBtn;
    private final Locator launchSupervisorDeskBtn;

    public DashboardPage(Page page) {
        super(page);
        this.launchAgentDeskBtn = page.locator("text=Launch Agent Desk");
        this.launchSupervisorDeskBtn = page.locator("text=Launch Supervisor Desk");
    }

    public AgentDeskPage launchAgentDesk(Page page, String urlPortal) {
//         page.waitForPopup(launchAgentDeskBtn::click);
        page.navigate(urlPortal+"/live");
        return new AgentDeskPage(page);
    }


    public SupervisorDeskPage launchSupervisorDesk(Page page, String urlPortal) {
        page.navigate(urlPortal+"/supervisor/live");
//        launchSupervisorDeskBtn.click();
//       page.waitForPopup(launchSupervisorDeskBtn::click);
        return new SupervisorDeskPage(page);
    }

}
