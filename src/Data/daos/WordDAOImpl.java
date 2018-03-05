package Data.daos;

import Data.intefaces.WordDAO;
import Data.model.Category;
import Data.model.Word;
import core.DBConnection;
import core.Executer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDAOImpl implements WordDAO {

    private Executer exec = new Executer(DBConnection.getInstance());

    @Override
    public List<Word> getAllWords() {
        ArrayList<Word> words = new ArrayList<>();

        ResultSet results = exec.executeQuery("SELECT * FROM Words");

        //populating the List
        try {
            while (results.next()) {
                //Own
                String content = results.getString("content");
                int id = results.getInt("wordId");
                int categoryId = results.getInt("categoryId");
                //Foreign

                Word temp = new Word(id, content, categoryId);
                words.add(temp);
                System.out.println(id + " " + content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return words;
    }

    @Override
    public Word getWord(int id) {
        ResultSet result = exec.executeQuery("SELECT * FROM Words WHERE wordId = " + id );
        try {
            result.next();

            int resultId = result.getInt("wordId");
            String resultName = result.getString("content");
            int categoryId = result.getInt("categoryId");
            //Foreign
            //TODO Add foreign category table

            Word temp = new Word(resultId, resultName, categoryId);
            return temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addWord(Word word) {
        exec.executeUpdate("INSERT INTO Words VALUES (1,'" + word.getWordId() + "')");
        System.out.println(word.getWordId() + " Added to Database");
    }

    @Override
    public void updateWord(Word word) {
        exec.executeUpdate("UPDATE Words SET content = '" + word.getContent() + "' WHERE id= " + word.getWordId());
        this.getWord(word.getWordId());
    }

    @Override
    public void deleteWord(Word word) {
        exec.executeUpdate("DELETE FROM Words WHERE id='" + word.getWordId() + "'");
    }
}
