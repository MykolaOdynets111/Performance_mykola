package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import pages.setingsTabs.*;


public class DepartmentManagementPage extends BasePage {

    private final Locator createNewDepartmentBtn;
    private final Locator departmentNameInput;
    private final Locator agentCheckboxes;
    private final Locator submitDepartmentCreationBtn;


    public DepartmentManagementPage(Page page) {
        super(page);
        this.createNewDepartmentBtn = page.locator("button:has-text('Create new department')");
        this.departmentNameInput = page.locator("#department-name");
        this.agentCheckboxes = page.locator("span.cl-checkbox__label");
        this.submitDepartmentCreationBtn = page.locator("//button[@type='submit']");


    }

    public void fillInDataInDepartmentCreationTab(int agents) {
        createNewDepartmentBtn.click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        departmentNameInput.type("test1");
        for (int i = 0; i < agents; i++) {
            agentCheckboxes.nth(i).check();
        }
    }

    public void createNewDepartment() {
        submitDepartmentCreationBtn.click();
    }


}
