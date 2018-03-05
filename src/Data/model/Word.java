package Data.model;

public class Word {

    private int wordId;
    private String content;
    private int categoryId;

    public Word(int wordId, String content, int categoryId) {
        this.wordId = wordId;
        this.content = content;
        this.categoryId = categoryId;
    }

    public Word(String content, int categoryId) {
        this.content = content;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
