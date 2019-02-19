package xyz.bolitao.exp01;

import java.util.ArrayList;

/**
 * @author Boli Tao
 * 封装的词法分析方法
 */
public class Analyse {
    /**
     * 关键字
     */
    private String[] keyword = {"int", "long", "short", "float", "double", "char", "unsigned",
            "signed", "const", "void", "volatile", "enum", "struct", "union", "if", "else", "goto",
            "switch", "case", "do", "while", "for", "continue", "break", "return", "default",
            "typedef", "auto", "register", "extern", "static", "sizeof"};

    /**
     * 运算符
     */
    private String[] operator = {"+", "-", "*", "/", "%", "=", ">", "<", "!", "==", "!=",
            ">=", "<=", "++", "--", "&", "&&", "||", "[", "]"};

    /**
     * 分隔符
     */
    private String[] delimiter = {",", ";", "(", ")", "{", "}", "\'", "\"", ":", "#"};

    public Analyse() {
    }

    /**
     * 判断数字
     *
     * @param ch 需要判断的字符
     * @return whether ch is digit
     */
    public boolean isDigit(char ch) {
        if (ch > 0 && ch < 9) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字母
     *
     * @param ch 需要判断的字符
     * @return 是否为字母
     */
    public boolean isLetter(char ch) {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * TODO: important
     * 判断是否为两个运算符
     *
     * @param str 要检测的串
     * @param ch
     * @return str 是否是两个运算符
     */
    public boolean isTwoOperator(String str, char ch) {
        char lc;
        int flag = 0;
        if (str.length() > 2 || str.length() == 0) {
            return false;
        } else {
            lc = str.charAt(str.length() - 1);
            if (ch == '=' && (lc == '>' || lc == '<' || lc == '=' || lc == '!')) {

            } else if (ch == '+' && lc == '+') {

            } else if (ch == '-' && lc == '-') {

            } else if (ch == '|' && lc == '|') {

            } else if (ch == '&' && lc == '&') {

            } else {
                return false;
                //否就返回false
            }
            return true;
            //其它符号的情况都返回true
        }
    }

    /**
     * 判断是否为关键字
     *
     * @param str 需要判断的串
     * @return 1：是关键字
     */
    public int Keyword(String str) {
        int i;
        for (i = 0; i < keyword.length; i++) {
            if (str.equals(keyword[i])) {
                break;
            }
        }
        if (i < keyword.length) {
            return 1;
        } else {
            return -1;
        }
//        return 1;
    }

    /**
     * 判断是否为运算符
     *
     * @param str
     * @return
     */
    public int Operator(String str) {
        int i;
        for (i = 0; i < operator.length; i++) {
            if (str.equals(operator[i])) {
                break;
            }
        }
        if (i < operator.length) {
            return 4;
        } else {
            return -1;
        }
//        return 4;
    }

    /**
     * 判断分隔符
     *
     * @param str
     * @return
     */
    public int Delimiter(String str) {
        int i;
        for (i = 0; i < delimiter.length; i++) {
            if (str.equals(delimiter[i])) {
                break;
            }
        }
        if (i < delimiter.length) {
            return 5;
        } else {
            return -1;
        }
    }

    /**
     * 判断字符串能否被识别
     *
     * @param str
     * @return
     */
    public boolean isIdentified(String str) {
        int i;
        char ch;
        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ((i == 0 && !isLetter(ch)) || (!isDigit(ch) && !isLetter(ch))) {
                break;
            }
        }
        if (i < str.length()) {
            return false;
        } else {
            return true;
        }
//        return (i < str.length());
        // TODO : 上面一行代码和 if else 作用一样
    }

    public String pretreat(String str) {
        // \t = tab
        // \r = carriage  return
        // \n = newline
        String finalString = "";
        int i;
        char ch, nc;
        for (i = 0; i < str.length() - 1; i++) {
            ch = str.charAt(i);
            nc = str.charAt(i + 1);
            if (ch == '\n') {
                ch = '$';
                finalString += ch;
            } else if (ch == ' ' || ch == '\r' || ch == '\t') {
                if (nc == ' ' || nc == '\r' || nc == '\t') {
                    continue;
                } else {
                    ch = ' ';
                    finalString += ch;
                }
            } else {
                finalString += ch;
            }
        }
        ch = str.charAt(str.length() - 1);
        if (ch != ' ' && ch != '\r' && ch != '\t' && ch != '\n') {
            finalString += ch;
        }
        return finalString;
    }

    public ArrayList<Word> divide(String str) {
        ArrayList<Word> list = new ArrayList<>();
        String s = "";
        char ch;
        int i;
        int row = 1;
        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (i == 0 && ch == ' ') {
                continue;
            }
            if (ch == ' ') {
                if (s != "") {
                    list.add(new Word(row, s));
                    s = "";
                } else {
                    continue;
                }
            } else if (isDigit(ch) || isLetter(ch)) {
                if (s == "" || isDigit(s.charAt(s.length() - 1)) || isLetter(s.charAt(s.length() - 1))) {
                    s += ch;
                } else {
                    list.add(new Word(row, s));
                    s = "";
                    s += ch;
                }
            } else {
                if (isTwoOperator(s, ch)) {
                    s += ch;
                } else {
                    if (s == "" && ch != '$') {
                        s += ch;
                    } else if (s == "" && ch == '$') {
                        row++;
                    } else {
                        list.add(new Word(row, s));
                        s = "";
                        if (ch != '$') {
                            s += ch;
                        } else {
                            row++;
                        }
                    }
                }
            }
        }
        if (s != "") {
            list.add(new Word(row, s));
        }
        return list;
    }

    /**
     * 判断字符串是数字串还是单字符/字符串
     *
     * @param str
     * @return
     */
    public int check(String str) {
        char ch;
        ch = str.charAt(0);
        if (ch >= '0' && ch <= '9') {
            return 1;
            // 数字串
        }
        if (str.length() == 1) {
            return 2;
            // 单字符
        } else {
            return 3;
            // 串
        }
    }

    /**
     * 判断是否为数字串
     *
     * @param str
     * @return
     */
    public int checkDigit(String str) {
        int i;
        char ch;
        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (ch > '9' || ch < '0') {
                break;
            }
        }
        if (i < str.length()) {
            return 0;
            // 无法识别
        } else {
            return 3;
            // 10 表示常数
        }
    }

    /**
     * 判断是否是单字符
     *
     * @param str
     * @return
     */
    public int checkChar(String str) {
        if (Operator(str) != -1) {
            // 返回操作符
            return Operator(str);
        } else if (Delimiter(str) != -1) {
            // 返回分解符
            return Delimiter(str);
        } else if (isIdentified(str)) {
            return 2;
            // 标识符
        } else {
            return 0;
            // 不能被识别
        }
    }

    /**
     * 判断是否为字符串
     *
     * @param str
     * @return
     */
    public int checkString(String str) {
        if (Operator(str) != -1) {
            // 返回操作符
            return Operator(str);
        } else if (Keyword(str) != -1) {
            // 返回关键字
            return Keyword(str);
        } else if (isIdentified(str)) {
            // 返回标识符
            return 2;
        } else {
            // 无法被识别
            return 0;
        }
    }
}