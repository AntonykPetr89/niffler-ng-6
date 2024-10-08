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
    private final SelenideElement friendsLink = $("a.nav-link[href='/people/friends']");
    private final SelenideElement allPeopleLink = $("a.nav-link[href='/people/all']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public ProfilePage navigateToProfile() {
        userIcon.click();
        userLink.click();
        return new ProfilePage();
    }

    public FriendsPage navigateToFriends() {
        userIcon.click();
        friendsLink.click();
        return new FriendsPage();
    }

    public FriendsPage navigateToAllPeople() {
        userIcon.click();
        allPeopleLink.click();
        return new FriendsPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkIsLoaded(){
        statisticsMessage.shouldBe(visible);
        historyOfSpendingMessage.shouldBe(visible);
    }

}
