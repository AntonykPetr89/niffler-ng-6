package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)

public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();


    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UsersQueueExtension.UserType(WITH_FRIEND) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("Ivan", "A97f092a")
                .navigateToFriends()
                .shouldBeHeaderInFriendTable("My friends")
                .shouldBeFriendInTable("Nikolai");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    public void friendsTableShouldBeEmptyForNewUser(@UsersQueueExtension.UserType(EMPTY) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("Petr", "A97f092a")
                .navigateToFriends()
                .checkMessageInEmptyTable();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    public void incomeInvitationBePresentInFriendsTable(@UsersQueueExtension.UserType(WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("Vasya", "A97f092a")
                .navigateToFriends()
                .shouldBeHeaderForIncomeFriendRequest("Friend requests")
                .shouldBeFriendRequestInTable("Max");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    public void outcomeInvitationBePresentInAllPeoplesTable(@UsersQueueExtension.UserType(WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("Max", "A97f092a")
                .navigateToAllPeople()
                .shouldBePresentOutcomeRequestInAllPeopleTable("Vasya", "Waiting...");
    }

}
