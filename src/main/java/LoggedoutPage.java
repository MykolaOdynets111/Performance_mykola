
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;


public class LoggedoutPage extends BasePage {


    private final Locator LoginButton;

    public LoggedoutPage(Page page) {
        super(page);
        this.LoginButton = page.locator("[id='tenants']");
    }


    public LoginPage pressLogIn (){
        LoginButton.click();
        return new LoginPage(page);
    }

}
