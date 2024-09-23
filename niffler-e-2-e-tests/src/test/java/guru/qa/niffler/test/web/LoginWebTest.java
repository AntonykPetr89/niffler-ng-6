package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class LoginWebTest {
    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();
    private static final Config CFG = Config.getInstance();
    private static final String REGISTERED_USER = "Petr";
    private static final String PASSWORD_USER = "A97f092a";
    final String wrongCredentials = "Неверные учетные данные пользователя";

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(REGISTERED_USER, PASSWORD_USER);
        mainPage.checkHistoryOfSpendingMessageIsVisible();
        mainPage.checkStatisticsMessageIsVisible();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(randomUsername(), randomPassword());
        loginPage
                .checkErrorForm(wrongCredentials);
    }
}
