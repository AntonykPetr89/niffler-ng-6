package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

public class RegisterWebTest {
    RegisterPage registerPage = new RegisterPage();
    private static final Config CFG = Config.getInstance();
    Faker faker = new Faker();
    String password = faker.internet().password(3, 10);
    private static final String REGISTERED_USER = "Petr";
    final String successMessage = "Congratulations! You've registered!";
    final String userExistMessage = "Username `Petr` already exists";
    final String passwordAndConfirmPasswordNotEqualMessage = "Passwords should be equal";

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountClick();
        registerPage
                .setUsername(faker.name().username())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkRegistrationNewUser(successMessage);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountClick();
        registerPage
                .setUsername(REGISTERED_USER)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkErrorForm(userExistMessage);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountClick();
        registerPage
                .setUsername(REGISTERED_USER)
                .setPassword(faker.internet().password(3, 10))
                .setPasswordSubmit(faker.internet().password(3, 10))
                .submitRegistration()
                .checkErrorForm(passwordAndConfirmPasswordNotEqualMessage);
    }
}
