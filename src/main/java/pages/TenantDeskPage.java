package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Path;

public abstract class TenantDeskPage extends BasePage {

    private static final String DATA_TEST_ID = "data-testId";
    private final String attachmentInputSelector;
    private final Locator closedChatsTab;
    private final Locator ticketsTab;
    private final Locator pendingTab;
    private final Locator taggedTab;
    private final Locator liveTab;
    private final Locator profileTab;
    private final Locator notesTab;
    private final Locator historyTab;
    private final Locator logOutLink;
    private final Locator demoLink;
    private final Locator unavailableCheckbox;
    private final Locator chatItem;
    private final Locator messageItem;
    private final Locator addAttachmentIcon;
    private final Locator sendFileBtn;
    private final Locator messageWithAttachment;
    private final Locator addLocationBtn;
    private final Locator locationInput;
    private final Locator sendLocationBtn;
    private final Locator messageWithLocation;





    protected TenantDeskPage(Page page) {
        super(page);
        this.closedChatsTab = page.locator("[data-testid=\"tab-navigation-panel-closed\"]");
        this.ticketsTab = page.locator("[data-testid=\"tab-navigation-panel-tickets\"]");
        this.pendingTab = page.locator("[data-testid=\"tab-navigation-panel-pending\"]");
        this.taggedTab = page.locator("[data-testid=\"tab-navigation-panel-tagged\"]");
        this.liveTab = page.locator("[data-testid=\"tab-navigation-panel-live\"]");
        this.profileTab = page.locator("[data-testid=\"tab-right-panel-0\"]");
        this.notesTab = page.locator("[data-testid=\"tab-right-panel-1\"]");
        this.historyTab = page.locator("[data-testid=\"tab-right-panel-2\"]");
        this.logOutLink = page.locator("[data-testid=\"logout-button\"]");
        this.demoLink = page.locator(".cl-profile-info__agent-name");
        this.unavailableCheckbox = page.locator("text=Unavailable");
        this.chatItem = page.locator(".cl-chat-item");
        this.messageItem = page.locator("//div[@data-testid='chat-message']");
        this.addAttachmentIcon = page.locator("[name='attachment']");
        this.attachmentInputSelector = ".cl-drag-zone>:first-child";
        this.sendFileBtn = page.locator("//button[contains(text(),'Send file')]");
        this.messageWithAttachment = page.locator(".media-message__preview");
        this.addLocationBtn = page.locator("[id='Map Pin']");
        this.locationInput = page.locator("[placeholder='Search location']");
        this.sendLocationBtn = page.locator("//button[contains(text(),'Send location')]");
        this.messageWithLocation = page.locator(".media-message__preview");

    }


    public void openLiveTab() {
        liveTab.click();
    }

    public void openPendingTab() {
        pendingTab.click();
    }

    public void openTaggedTab() {
        taggedTab.click();
    }

    public void openClosedChatsTab() {
        closedChatsTab.click();
    }

    public void openTicketsTab() {
        ticketsTab.click();
    }

    public void openProfileTab() {
        profileTab.click();
    }

    public void openNotesTab() {
        notesTab.click();
    }

    public void openHistoryTab() {
        historyTab.click();
    }

    public LoggedOutPage logOut(Page page) {
        demoLink.click();
        logOutLink.click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return new LoggedOutPage(page);
    }

    public void setUnavailableCheckbox() {
        demoLink.click();
        unavailableCheckbox.click();
    }

    public int scrollToLastChat(Page page) throws InterruptedException {
        String firstName;
        int i = 0;
        do {
            Locator first = chatItem.nth(chatItem.count() - 1);
            firstName = first.getAttribute(DATA_TEST_ID);
            first.scrollIntoViewIfNeeded();

            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.waitForLoadState(LoadState.LOAD);
            Thread.sleep(1000);
            i++;

        }
        while (!firstName.equals(chatItem.nth(chatItem.count() - 1).getAttribute(DATA_TEST_ID)));
        return i;

    }

    public String getDataTestId(int chatNum) {
        return chatItem.nth(chatNum).getAttribute(DATA_TEST_ID);
    }

    public void clickChatItem(int chatNum) {
        chatItem.nth(chatNum).click();
    }

    public boolean isChatPresentOnPage(String dataTestId) {
        for (int i = 0; i <= (chatItem.count() - 1); i++) {
            chatItem.nth(i).waitFor();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            if (chatItem.nth(i).getAttribute(DATA_TEST_ID).equals(dataTestId)) {
                return true;
            }
        }
        return false;
    }

    public void waitWhileChatDisappearFromPage(String issuedDataTestId) throws InterruptedException {
        chatItem.nth(0).waitFor();
        do {
            Thread.sleep(1000);
        }
        while (chatItem.nth(0).getAttribute("data-testid").equals(issuedDataTestId));
    }

    public int scrollToFirstMessage(Page page) throws InterruptedException {
        String firstID;
        int i = 0;
        do {
            Locator first = messageItem.nth(messageItem.count() - 1);
            firstID = first.getAttribute("id");
            first.scrollIntoViewIfNeeded();

            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.waitForLoadState(LoadState.LOAD);
            Thread.sleep(1000);
            i++;
        }
        while (!firstID.equals(messageItem.nth(messageItem.count() - 1).getAttribute("id")));
        return i;

    }
    public void uploadExampleAttachment(Page page){
        addAttachmentIcon.click();
        page.setInputFiles(attachmentInputSelector, Path.of("src/main/resources/example_attachment.png"));
        sendFileBtn.click();
        messageWithAttachment.waitFor();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.LOAD);
    }

    public void sendLocation(Page page, String location){
        addLocationBtn.click();
        page.keyboard().insertText(location);;
        locationInput.press("Enter");
        sendLocationBtn.click();
        messageWithLocation.waitFor();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.LOAD);
    }

}