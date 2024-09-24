package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "Petr",
            archived = false
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

    @Category(
            username = "Petr",
            archived = true
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
