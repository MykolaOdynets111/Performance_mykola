import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.SettingsPage;
import pages.setingsTabs.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;
import org.testng.log4testng.Logger;



public class SettingsPagePerformanceTest extends BaseWebTest {
    SoftAssertions assertions = new SoftAssertions();


    @Title("Assert the time of open tabs in Settings page")
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

    }

    private void checkTimeTabOpen(SettingsPage settingsPage, String tab) {
        long currentTimeBeforeOpening = System.currentTimeMillis();
        switch (tab) {
            case ("Chat Tags"):
                ChatTagsTab chatTagsTab = settingsPage.openChatsTagTab();
                waitWilePageFullyLoaded(chatTagsTab.getPage());
            case ("Auto Responders"):
                AutoRespondersTab autoRespondersTab = settingsPage.openAutoRespondersTab();
                waitWilePageFullyLoaded(autoRespondersTab.getPage());
            case ("Preferences"):
                PreferencesTab preferencesTab = settingsPage.openPreferencesTab();
                waitWilePageFullyLoaded(preferencesTab.getPage());
            case ("Surveys"):
                SurveysTab surveysTab = settingsPage.openSurveysTab();
                waitWilePageFullyLoaded(surveysTab.getPage());
            case ("Business Profile"):
                BusinessProfileTab businessProfileTab = settingsPage.openBusinessProfileTab();
                waitWilePageFullyLoaded(businessProfileTab.getPage());
        }
        long currentTimeAfterOpening = System.currentTimeMillis();
        long tabsOpenTime = currentTimeAfterOpening - currentTimeBeforeOpening;
        logger.debug(tab + " open time = " + tabsOpenTime + " milliseconds");
//        System.out.println(tab + " open time = " + tabsOpenTime + " milliseconds");
        assertions.assertThat(tabsOpenTime / 1000l)
                .as(tab + " open time is longer than 20 seconds")
                .isLessThan(20);

    }


}
