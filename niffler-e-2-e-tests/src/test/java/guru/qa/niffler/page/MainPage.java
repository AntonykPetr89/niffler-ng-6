package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsMessage = $("#stat");
    private final SelenideElement historyOfSpendingMessage = $("#spendings");
    private final SelenideElement userIcon = $(".MuiAvatar-circular");
    private final SelenideElement userLink = $("a.nav-link[href='/profile']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public ProfilePage navigateToProfile() {
        userIcon.click();
        userLink.click();
        return new ProfilePage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkStatisticsMessageIsVisible() {
        statisticsMessage.shouldBe(visible);
    }

    public void checkHistoryOfSpendingMessageIsVisible() {
        historyOfSpendingMessage.shouldBe(visible);
    }
}
