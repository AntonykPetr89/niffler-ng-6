package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.createSpend(spendEntity)
                    );
                }
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return jdbcTxTemplate.execute(() -> spendDao.findById(id));
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return jdbcTxTemplate.execute(() -> spendDao.findAllByUsername(username));
    }

    public void deleteSpend(SpendEntity spend) {
        jdbcTxTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(SpendJson.fromEntity(spend));
            spendDao.deleteSpend(spendEntity);
            return null;
        });
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = categoryDao.createCategory(CategoryEntity.fromJson(category));
                    return CategoryJson.fromEntity(categoryEntity);
                }
        );
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return jdbcTxTemplate.execute(() -> categoryDao.findById(id));
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return jdbcTxTemplate.execute(() -> categoryDao.findAllByUsername(username));
    }

    public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> categoryDao.findByUsernameAndCategoryName(username, categoryName));
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            categoryDao.deleteCategory(categoryEntity);
            return null;
        });
    }
}