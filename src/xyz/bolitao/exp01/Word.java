package xyz.bolitao.exp01;

/**
 * @author Boli Tao
 * 识别到的字符
 */
public class Word {
    private int row;
    private String word;

    public Word(int row, String word) {
        this.row = row;
        this.word = word;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
