package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public SpendJson findSpendById(SpendJson spendJson) {
        Optional<SpendEntity> entity = spendDao.findById(spendJson.id());
        return entity.map(SpendJson::fromEntity).orElseThrow();
    }

    public List<SpendJson> findAllSpendsByUsername(String username) {
        List<SpendEntity> spendEntityList = spendDao.findAllByUsername(username);
        return spendEntityList.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    public void deleteSpend(SpendEntity spend) {
        spendDao.delete(spend);
    }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity entity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.create(entity));
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findById(id);
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findByUsernameAndCategoryName(username, categoryName);
        return categoryEntity.map(CategoryJson::fromEntity).orElseThrow();
    }

    public void deleteCategory(CategoryEntity category) {
        categoryDao.delete(category);
    }
}