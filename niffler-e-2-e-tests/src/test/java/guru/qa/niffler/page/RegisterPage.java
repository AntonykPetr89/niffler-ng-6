package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successRegistrationMessage = $(".form__paragraph_success");
    private final SelenideElement errorMessage = $(".form__error");


    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return new RegisterPage();
    }

    public void checkRegistrationNewUser(String value) {
        successRegistrationMessage.shouldHave(text(value)).shouldBe(visible);
    }

    public void checkErrorForm(String message) {
        errorMessage.shouldHave(text(message)).shouldBe(visible);
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return new RegisterPage();
    }
}
