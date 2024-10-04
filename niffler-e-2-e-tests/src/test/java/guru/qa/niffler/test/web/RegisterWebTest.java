package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegisterWebTest {
    RegisterPage registerPage = new RegisterPage();
    private static final Config CFG = Config.getInstance();
    private static final String REGISTERED_USERNAME = "Petr";
    final String successMessage = "Congratulations! You've registered!";
    final String userExistMessage = "Username `Petr` already exists";
    final String passwordAndConfirmPasswordNotEqualMessage = "Passwords should be equal";
    final String validPassword = randomPassword();

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateNewAccountButton();
        registerPage
                .setUsername(randomUsername())
                .setPassword(validPassword)
                .setPasswordSubmit(validPassword)
                .submitRegistration()
                .checkRegistrationNewUser(successMessage);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateNewAccountButton();
        registerPage
                .setUsername(REGISTERED_USERNAME)
                .setPassword(validPassword)
                .setPasswordSubmit(validPassword)
                .submitRegistration()
                .checkErrorForm(userExistMessage);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateNewAccountButton();
        registerPage
                .setUsername(randomUsername())
                .setPassword(randomPassword())
                .setPasswordSubmit(randomPassword())
                .submitRegistration()
                .checkErrorForm(passwordAndConfirmPasswordNotEqualMessage);
    }
}
