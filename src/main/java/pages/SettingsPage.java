package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.setingsTabs.*;


public class SettingsPage extends BasePage {

    private final Locator chatTagsTabTitle;
    private final Locator autoRespondersTabTitle;
    private final Locator preferencesTabTitle;
    private final Locator surveysTabTitle;
    private final Locator businessProfileTabTitle;

    public SettingsPage(Page page) {
        super(page);
        this.chatTagsTabTitle = page.locator("[data-testid='tab-navigation-panel-chat-tags']");
        this.autoRespondersTabTitle = page.locator("[data-testid='tab-navigation-panel-auto-responders']");
        this.preferencesTabTitle = page.locator("[data-testid='tab-navigation-panel-preferences']");
        this.surveysTabTitle = page.locator("[data-testid='tab-navigation-panel-surveys']");
        this.businessProfileTabTitle = page.locator("[data-testid='tab-navigation-panel-business-profile']");
    }

    public BusinessProfileTab openBusinessProfileTab() {
        businessProfileTabTitle.click();
        return new BusinessProfileTab(page);
    }

    public ChatTagsTab openChatsTagTab() {
        chatTagsTabTitle.click();
        return new ChatTagsTab(page);
    }

    public AutoRespondersTab openAutoRespondersTab() {
        autoRespondersTabTitle.click();
        return new AutoRespondersTab(page);
    }

    public PreferencesTab openPreferencesTab() {
        preferencesTabTitle.click();
        return new PreferencesTab(page);
    }

    public SurveysTab openSurveysTab() {
        surveysTabTitle.click();
        return new SurveysTab(page);
    }






}
