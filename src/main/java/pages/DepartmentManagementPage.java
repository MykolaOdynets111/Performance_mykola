package pages;

import com.github.javafaker.Faker;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;


public class DepartmentManagementPage extends BasePage {

    private final Locator createNewDepartmentBtn;
    private final Locator departmentNameInput;
    private final Locator agentCheckboxes;
    private final Locator submitDepartmentCreationBtn;


    public DepartmentManagementPage(Page page) {
        super(page);
        this.createNewDepartmentBtn = page.locator("//button[contains(text(),' new department')]");
        this.departmentNameInput = page.locator("#department-name");
        this.agentCheckboxes = page.locator("span.cl-checkbox__label");
        this.submitDepartmentCreationBtn = page.locator("//button[@type='submit']");
    }

    @Step("Filling data for creation department with {0} agents")
    public void fillInDataInDepartmentCreationTab(int agents) {
        createNewDepartmentBtn.click();
        Faker faker = new Faker();

        departmentNameInput.type(faker.beer().name());
        for (int i = 0; i < agents; i++) {
            agentCheckboxes.nth(i).check();
        }
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void createNewDepartment() {
        submitDepartmentCreationBtn.click();
    }


}
