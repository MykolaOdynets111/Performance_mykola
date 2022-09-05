import io.qameta.allure.Attachment;
import io.qameta.allure.Description;


import junit.framework.TestListener;
import org.assertj.core.api.SoftAssertions;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.SettingsPage;
import pages.setingsTabs.*;
import utils.AllureLogger;

public class SettingsPagePerformanceTest extends BaseTest {
    SoftAssertions assertions = new SoftAssertions();

    @Description("Assert the time of open tabs in Settings page")
    @Test(enabled = true)
    @Parameters({"tenant", "agent", "urlPortal"})
    public void openSettingsTabsTest(String tenant, String agent, String urlPortal) {

        loginPage.loginTenant(tenant, agent);
        waitWilePageFullyLoaded(loginPage.getPage());
        loginPage.navigateDashboard(urlPortal);
        SettingsPage settingsPage = dashboardPage.openSettingsPage(dashboardPage.getPage());
        waitWilePageFullyLoaded(settingsPage.getPage());
        checkTimeTabOpen(settingsPage, "Chat Tags");
        checkTimeTabOpen(settingsPage, "Auto Responders");
        checkTimeTabOpen(settingsPage, "Preferences");
        checkTimeTabOpen(settingsPage, "Surveys");
        checkTimeTabOpen(settingsPage, "Business Profile");
        assertions.assertAll();

    }

    private void checkTimeTabOpen(SettingsPage settingsPage, String tab) {
        long currentTimeBeforeOpening = System.currentTimeMillis();
        switch (tab) {
            case ("Chat Tags"):
                ChatTagsTab chatTagsTab = settingsPage.openChatsTagTab(settingsPage.getPage());
                waitWilePageFullyLoaded(chatTagsTab.getPage());
                break;
            case ("Auto Responders"):
                AutoRespondersTab autoRespondersTab = settingsPage.openAutoRespondersTab();
                waitWilePageFullyLoaded(autoRespondersTab.getPage());
                break;
            case ("Preferences"):
                PreferencesTab preferencesTab = settingsPage.openPreferencesTab();
                waitWilePageFullyLoaded(preferencesTab.getPage());
                break;
            case ("Surveys"):
                SurveysTab surveysTab = settingsPage.openSurveysTab();
                waitWilePageFullyLoaded(surveysTab.getPage());
                break;
            case ("Business Profile"):
                BusinessProfileTab businessProfileTab = settingsPage.openBusinessProfileTab();
                waitWilePageFullyLoaded(businessProfileTab.getPage());
                break;
        }
        long currentTimeAfterOpening = System.currentTimeMillis();
        long tabsOpenTime = currentTimeAfterOpening - currentTimeBeforeOpening;
        saveScreenshot(settingsPage.getPage().screenshot());
        AllureLogger.logToAllure(tab + " table open time = " + tabsOpenTime + " milliseconds");
        logger.info(tab + " table open time = " + tabsOpenTime + " milliseconds");

//        saveTextLog(tab + " open time = " + tabsOpenTime + " milliseconds");
        assertions.assertThat(tabsOpenTime / 1000l)
                .as(tab + " open time is longer than 20 seconds")
                .isLessThan(20);
    }


}
