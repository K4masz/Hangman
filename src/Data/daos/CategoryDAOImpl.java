package Data.daos;

import Data.intefaces.CategoryDAO;
import Data.model.Category;
import core.DBConnection;
import core.Executer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO{

    private Executer exec = new Executer(DBConnection.getInstance());

    @Override
    public List<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        ResultSet results = exec.executeQuery("SELECT * FROM Categories");

        //populating the List
        try {
            while (results.next()) {
                String name = results.getString("name");
                int id = results.getInt("categoryId");
                Category temp = new Category(id, name);
                categories.add(temp);
                System.out.println(id + " " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public Category getCategory(int id) {
        ResultSet result = exec.executeQuery("SELECT * FROM Categories WHERE categoryId = '" + id + "'");
        try {
            result.next();
            String resultName = result.getString("name");
            int resultId = result.getInt("categoryId");
            Category temp = new Category(resultId, resultName);
            return temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category getCategoryByName(String name) {
        ResultSet result = exec.executeQuery("SELECT * FROM Categories WHERE name = '" + name + "'");
        try {
            result.next();
            String resultName = result.getString("name");
            int resultId = result.getInt("categoryId");
            Category temp = new Category(resultId, resultName);
            return temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addCategory(Category category) {
        exec.executeUpdate("INSERT INTO Categories VALUES (1,'" + category.getName() + "')");
        System.out.println(category.getName() + " Added to Database");
    }

    @Override
    public void updateCategory(Category category) {
        exec.executeUpdate("UPDATE Categories SET name = '" + category.getName() + "' WHERE id= " + category.getCategoryId());
        this.getCategory(category.getCategoryId());
    }

    @Override
    public void deleteCategory(Category category) {
        exec.executeUpdate("DELETE FROM Categories WHERE id='" + category.getCategoryId() + "'");
    }
}
