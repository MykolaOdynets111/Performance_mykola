package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DashboardPage extends BasePage {

    private final Locator settingsBtn;
    private final Locator departmentManagementBtn;

    public DashboardPage(Page page) {
        super(page);
        this.settingsBtn = page.locator("a:has-text('Settings')");
        this.departmentManagementBtn = page.locator("a:has-text(' Departments Management')");
    }

    public AgentDeskPage launchAgentDesk(Page page, String urlPortal) {
        page.navigate(urlPortal + "/live");
        return new AgentDeskPage(page);
    }

    public SupervisorDeskPage launchSupervisorDesk(Page page, String urlPortal) {
        page.navigate(urlPortal + "/supervisor/live");
        return new SupervisorDeskPage(page);
    }

    public SettingsPage openSettingsPage(Page page) {
        settingsBtn.click();
        return new SettingsPage(page);
    }

    public DepartmentManagementPage openDepartmentManagementPage(Page page) {
        departmentManagementBtn.click();
        return new DepartmentManagementPage(page);
    }

}
