package Data.intefaces;

import Data.model.Category;

import java.util.List;

public interface CategoryDAO {
    List<Category> getAllCategories();
    Category getCategory(int id);
    void addCategory(Category categories);
    void updateCategory(Category categories);
    void deleteCategory(Category categories);
}
