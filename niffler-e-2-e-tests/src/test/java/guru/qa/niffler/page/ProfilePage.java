package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement confirmCategoryArchivedButton = $x("//button[text()='Archive']");
    private final SelenideElement confirmCategoryUnArchivedButton = $x("//button[text()='Unarchive']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement archiveCategoryCheckbox = $("input[type='checkbox']");
    private final SelenideElement successArchiveMessage = $x("//div[contains(text(), 'is archived')]");
    private final SelenideElement successUnArchiveMessage = $x("//div[contains(text(), 'is unarchived')]");

    public ProfilePage clickArchivedCategoryButton(String categoryName) {
        categoryList.filter(text(categoryName)).first().parent().$(".MuiIconButton-sizeMedium[aria-label='Archive category']").click();
        return this;
    }

    public ProfilePage clickConfirmCategoryArchivedButton() {
        confirmCategoryArchivedButton.click();
        return new ProfilePage();
    }

    public ProfilePage clickUnArchivedCategoryButton(String categoryName) {
        categoryList.filter(text(categoryName)).first().parent().$("button[aria-label='Unarchive category']").click();
        return this;
    }

    public ProfilePage clickConfirmCategoryUnArchivedButton() {
        confirmCategoryUnArchivedButton.click();
        return this;
    }

    public ProfilePage showArchiveCategory() {
        Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", archiveCategoryCheckbox);
        Selenide.executeJavaScript("arguments[0].click();", archiveCategoryCheckbox);
        return this;
    }

    public ProfilePage shouldBeVisibleSuccessUnArchiveMessage(String value) {
        successUnArchiveMessage.shouldHave(text("Category " + value + " is unarchived")).shouldBe(visible);
        return this;
    }

    public ProfilePage shouldBeVisibleSuccessArchiveMessage(String value) {
        successArchiveMessage.shouldHave(text("Category " + value + " is archived")).shouldBe(visible);
        return this;
    }

    public ProfilePage shouldNotBeVisibleArchivedCategory(String value) {
        categoryList.findBy(text(value)).shouldNotBe(visible);
        return this;
    }

    public ProfilePage shouldBeVisibleActiveCategory(String value) {
        categoryList.findBy(text(value)).shouldBe(visible);
        return this;
    }

}
