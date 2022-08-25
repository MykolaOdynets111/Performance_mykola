package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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
    private Locator agentName;
    private final Locator agentNotesArea;
    private final Locator transferBtn;
    private final Locator acceptBtn;
    private final Locator pendingButton;




    public void transferChat(String agent, int chatNum) throws InterruptedException {
        clickChatItem(chatNum);
        transferIcon.click();
        selectTransferAgent(agent);
        agentNotesArea.fill("Transfer");
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


    private void selectTransferAgent(String agent) throws InterruptedException {
        agentName = page.locator("text=" + agent);
        transferAgentPopUp.click();
        Thread.sleep(1000);
        agentName.nth(1).waitFor();
        agentName.nth(1).click();
    }

}
