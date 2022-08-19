
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;

import org.testng.annotations.Parameters;


public class LoginPage extends BasePage {

    //    private final Page page;
    private final Locator selectTenantDropdown;
    private final Locator selectAgentDropdown;
    private final Locator authenticateButton;

    public LoginPage(Page page) {
        super(page);
        this.selectTenantDropdown = page.locator("[id='tenants']");
        this.selectAgentDropdown = page.locator("[id='agents']");
        this.authenticateButton = page.locator("[id='auth-button']");
    }

    public DashboardPage navigateDashboard(String urlPortal) {
        page.navigate(urlPortal + "/dashboard/overview/activity");
        return new DashboardPage(page);
    }

    public void navigate(String uRL) {
        page.navigate(uRL);
    }

    public LoginPage setTenant(String tenantName) {
        selectTenantDropdown.selectOption(new SelectOption().setLabel(tenantName));
        return this;
    }

    public LoginPage setAgent(String agentName) {
        selectAgentDropdown.selectOption(new SelectOption().setLabel(agentName));
        return this;
    }


    public LoginPage pressAuthenticateButton() {
        authenticateButton.click();
        return this;

    }

    public LoginPage loginTenant (String tenant, String agent){
        return this.setTenant(tenant)
                .setAgent(agent)
                .pressAuthenticateButton()
                .pressAuthenticateButton();
    }

}
