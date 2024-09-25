package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @User(
            username = "Petr",
            categories = @Category(
                archived = false
        )
    )
    @Test
    void archivedCategoryShouldNotVisibleInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("Petr", "A97f092a")
                .navigateToProfile()
                .clickArchivedCategoryButton(category.name())
                .clickConfirmCategoryArchivedButton()
                .shouldBeVisibleSuccessArchiveMessage(category.name())
                .shouldNotBeVisibleArchivedCategory(category.name());
    }

    @User(
            username = "Petr",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void activeCategoryShouldBeVisibleInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("Petr", "A97f092a")
                .navigateToProfile()
                .showArchiveCategory()
                .clickUnArchivedCategoryButton(category.name())
                .clickConfirmCategoryUnArchivedButton()
                .shouldBeVisibleSuccessUnArchiveMessage(category.name())
                .showArchiveCategory()
                .shouldBeVisibleActiveCategory(category.name());
    }
}
