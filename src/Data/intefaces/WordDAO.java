package Data.intefaces;

import Data.model.Word;

import java.util.List;

public interface WordDAO {
    List<Word> getAllWords();
    Word getWord(int id);
    void addWord(Word word);
    void updateWord(Word word);
    void deleteWord(Word word);
}
