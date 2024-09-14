package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

public class LoginWebTest {
    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();
    private static final Config CFG = Config.getInstance();
    Faker faker = new Faker();
    String password = faker.internet().password(3, 10);
    String username = faker.internet().password(3, 10);
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
                .login(username, password);
        loginPage
                .checkErrorForm(wrongCredentials);
    }
}
