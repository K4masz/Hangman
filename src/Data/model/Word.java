package Data.model;

public class Word {

    private int wordId;
    private String content;

    public Word(int wordId, String content) {
        this.wordId = wordId;
        this.content = content;
    }

    public Word(String content) {
        this.content = content;
    }

    public Word() {
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int id) {
        this.wordId = wordId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
