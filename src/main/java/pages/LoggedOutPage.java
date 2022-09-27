package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoggedOutPage extends BasePage {

    private final Locator loginButton;

    public LoggedOutPage(Page page) {
        super(page);
        this.loginButton = page.locator("[id='tenants']");
    }

    public LoginPage pressLogIn (){
        loginButton.click();
        return new LoginPage(page);
    }

}
