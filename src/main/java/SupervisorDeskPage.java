import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class SupervisorDeskPage extends TenantDeskPage{


    public SupervisorDeskPage(Page page) {
        super(page);
        this.supervisorPopup = page.locator(".cl-agent-view-trigger-btn");
        this.launchAgentBtn = page.locator("text=Launch Agent");
        this.confirmLaunchAgentBtn = page.locator("//div[@class='cl-modal__footer-buttons']/*[2]");

    }
    private Locator supervisorPopup;
    private Locator launchAgentBtn;
    private Locator confirmLaunchAgentBtn;


    public AgentDeskPage launchAgent (Page page){
        supervisorPopup.click();
        launchAgentBtn.click();
        confirmLaunchAgentBtn.click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return new AgentDeskPage(page);

    }






}
