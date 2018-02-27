package Data.daos;

import Data.intefaces.WordDAO;
import Data.model.Word;
import core.Executer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDAOImpl implements WordDAO {

    private Executer exec;

    @Override
    public List<Word> getAllWords() {
        ArrayList<Word> words = new ArrayList<>();

        ResultSet results = exec.executeQuery("SELECT * FROM Words");

        //populating the List
        try {
            while (results.next()) {
                String content = results.getString("content");
                int id = results.getInt("wordId");

                Word temp = new Word(id, content);
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
        ResultSet result = exec.executeQuery("SELECT * FROM Words WHERE id = '" + id + "';");
        try {
            result.next();
            String resultName = result.getString("WordId");
            int resultId = result.getInt("content");
            //TODO Add foreign keys;
            Word temp = new Word(resultId, resultName);
            return temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addWord(Word word) {
        exec.executeUpdate("INSERT INTO Words VALUES ('" + word.getWordId() + "');");
        System.out.println(word.getWordId() + " Added to Database");
    }

    @Override
    public void updateWord(Word word) {
        exec.executeUpdate("UPDATE Words SET content = '" + word.getContent() + "' WHERE id= " + word.getWordId() + ";");
        this.getWord(word.getWordId());
    }

    @Override
    public void deleteWord(Word word) {
        exec.executeUpdate("DELETE FROM Words WHERE id='" + word.getWordId() + "';");
    }
}
