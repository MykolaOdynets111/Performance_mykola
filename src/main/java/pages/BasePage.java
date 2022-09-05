package pages;

import com.microsoft.playwright.Page;

public abstract class BasePage {

    protected Page page;

    BasePage(Page page) {
        this.page = page;
    }

    public String getTitle() {
        return page.title();
    }

    public Page getPage() {
        return page;
    }


}

