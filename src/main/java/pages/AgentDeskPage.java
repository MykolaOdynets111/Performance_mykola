package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;

public class AgentDeskPage extends TenantDeskPage {


    public AgentDeskPage(Page page) {

        super(page);
        this.transferIcon = page.locator("text=Transfer");
        this.closeChatIcon = page.locator("text=Close Chat");
        this.transferAgentPopUp = page.locator("//div[contains(text(), 'Select an agent')]/..");
        this.agentNotesArea = page.locator("//textarea[@name='agentNote']");

        this.transferBtn = page.locator("//button[contains(text(), 'Transfer')]");
        this.acceptBtn = page.locator("//button[contains(text(), 'Accept')]");
        this.pendingButton = page.locator("//button[@data-testid='header-toggle-pending']");


    }

    private final Locator transferIcon;
    private final Locator closeChatIcon;
    private final Locator transferAgentPopUp;
    private final Locator agentNotesArea;
    private final Locator transferBtn;
    private final Locator acceptBtn;
    private final Locator pendingButton;


    public void checkChatForTransferring(int agent, int chatNum) throws InterruptedException {
        clickChatItem(chatNum);
        transferIcon.click();
        selectTransferAgent(agent);
        page.isEnabled("//textarea[@name='agentNote']");
        agentNotesArea.fill("Transfer");
    }
    public void confirmTransfer(){
        transferBtn.click();
    }

    public void acceptTransferChat() {
        acceptBtn.click();
    }

    public void closeChat(int chatNum) {
        clickChatItem(chatNum);
        closeChatIcon.click();
    }

    public void moveChatToPending(int chatNum) {
        clickChatItem(chatNum);
        pendingButton.click();
    }


    private void selectTransferAgent(int agent) throws InterruptedException {
        String agentNameSelector = String.format("//*[@id='react-select-4-option-%d']", (agent - 1));
        Locator agentName = page.locator(agentNameSelector);
        page.isVisible(agentNameSelector);
        transferAgentPopUp.click();
        while (agentName.getAttribute("aria-disabled").equals("true")) {
            Thread.sleep(100);
        }
        page.isEnabled(agentNameSelector);
        agentName.click();
    }


}
