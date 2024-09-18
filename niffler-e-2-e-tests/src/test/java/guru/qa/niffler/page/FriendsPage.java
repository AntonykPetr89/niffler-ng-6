package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final SelenideElement emptyFriendTable = $x("//p[text()='There are no users yet']");
    private final SelenideElement headerForFriendTable = $x("//h2[text()='My friends']");
    private final SelenideElement incomeRequestHeader = $x("//h2[text()='Friend requests']");
    private final ElementsCollection friendsTable= $$x("//tbody[@id='friends']/tr");
    private final ElementsCollection requestsTable = $$x("//tbody[@id='requests']/tr");
    private final ElementsCollection allPeopleTable = $$("tbody#all tr");


    public FriendsPage checkMessageInEmptyTable() {
        emptyFriendTable.shouldBe(visible);
        return this;
    }

    public FriendsPage shouldBeHeaderInFriendTable(String text) {
        headerForFriendTable.shouldHave(text(text)).shouldBe(visible);
        return this;
    }

    public FriendsPage shouldBeFriendInTable(String friendName) {
        friendsTable.findBy(text(friendName)).shouldBe(visible);
        return this;
    }

    public FriendsPage shouldBeHeaderForIncomeFriendRequest(String text) {
        incomeRequestHeader.shouldHave(text(text)).shouldBe(visible);
        return this;
    }

    public void shouldBeFriendRequestInTable(String friendName) {
        requestsTable.findBy(text(friendName)).shouldBe(visible);
    }

    public FriendsPage shouldBePresentOutcomeRequestInAllPeopleTable(String name, String status) {
        allPeopleTable.filter(text(name)).first().$("span").shouldHave(text(status)).shouldBe(visible);
        return this;
    }
}

