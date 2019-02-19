package xyz.bolitao.exp01;

/**
 * @author Boli Tao
 * 未能识别的字符
 */
public class UnRecognized {
    private int row;
    private String word;

    public UnRecognized(int row, String word) {
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
